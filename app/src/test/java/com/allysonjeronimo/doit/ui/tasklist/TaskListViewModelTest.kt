package com.allysonjeronimo.doit.ui.tasklist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.allysonjeronimo.doit.R
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
    private val messageEventObserver = mockk<Observer<Int>>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel:TaskListViewModel
    private lateinit var mockedList:List<Task>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        instantiateViewModel()
        mockList()
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    private fun instantiateViewModel(){
        viewModel = TaskListViewModel(repository)
        // registrar o observer utilizando observeForever
        // já que estamos na JVM e não temos o lifeCycle
        viewModel.allTasksEventData.observeForever(allTasksEventObserver)
        viewModel.isLoadingEventData.observeForever(isLoadingObserver)
        viewModel.messageEventData.observeForever(messageEventObserver)
    }

    private fun mockList(){
        mockedList = listOf(
            Task(1, "Estudar"),
            Task(2, "Ler"),
            Task(3, "Programar")
        )
    }

    @Test
    fun `when view model fetches data then it should call the repository`(){
        coEvery{repository.findAll()} returns mockedList

        viewModel.tasks()

        coVerify { repository.findAll() }
        verify { allTasksEventObserver.onChanged(mockedList) }
    }

    @Test
    fun `when view model fetches data is completed then loading should be set to false`(){
        coEvery { repository.findAll() } returns mockedList

        viewModel.tasks()

        coVerifyOrder {
            isLoadingObserver.onChanged(true)
            repository.findAll()
            allTasksEventObserver.onChanged(mockedList)
            isLoadingObserver.onChanged(false)
        }
    }

    @Test
    fun `when view model tasks get exception then messageEventData should be set to error`(){

        coEvery { repository.findAll() } throws Exception()

        viewModel.tasks()

        coVerify {
            repository.findAll()
        }

        coVerify {
            messageEventObserver.onChanged(R.string.task_list_error_on_loading)
        }
    }

}