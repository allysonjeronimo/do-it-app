package com.allysonjeronimo.doit.ui.tasklist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.allysonjeronimo.doit.data.db.entity.Task
import com.allysonjeronimo.doit.repository.TaskRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskListViewModelTest {

    /**
     * Forçar ao viewModel para executar na thread atual e não na Main Thread do Android,
     * o que causaria erro já que o teste executa na JVM
     */
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val repository = mockk<TaskRepository>()
    // relaxed = true, para que não seja chamado o every
    private val allTasksEventObserver = mockk<Observer<List<Task>>>(relaxed = true)
    private val isLoadingObserver = mockk<Observer<Boolean>>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when view model fetches data then it should call the repository`(){
        val viewModel = instantiateViewModel()
        val mockedList = listOf(
            Task(1, "Estudar"),
            Task(2, "Ler"),
            Task(3, "Programar")
        )

        coEvery{repository.findAll()} returns mockedList

        viewModel.tasks()

        coVerify { repository.findAll() }
        verify { allTasksEventObserver.onChanged(mockedList) }
    }

    @Test
    fun `when view model fetches data is completed then loading should be set to false`(){
        val viewModel = instantiateViewModel()
        val mocketList = listOf(
            Task(1, "Estudar"),
            Task(2, "Ler"),
            Task(3, "Programar")
        )

        coEvery { repository.findAll() } returns mocketList

        viewModel.tasks()

        coVerifyOrder {
            isLoadingObserver.onChanged(true)
            repository.findAll()
            allTasksEventObserver.onChanged(mocketList)
            isLoadingObserver.onChanged(false)
        }
    }

    private fun instantiateViewModel() : TaskListViewModel {
        val viewModel = TaskListViewModel(repository)
        // registrar o observer utilizando observeForever
        // já que estamos na JVM e não temos o lifeCycle
        viewModel.allTasksEvent.observeForever(allTasksEventObserver)
        viewModel.isLoadingEvent.observeForever(isLoadingObserver)

        return viewModel
    }
}