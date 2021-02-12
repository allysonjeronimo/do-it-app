package com.allysonjeronimo.doit.ui.task

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.allysonjeronimo.doit.R
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
class TaskViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel:TaskViewModel
    private val repository = mockk<TaskRepository>()
    private val messageEventObserver = mockk<Observer<Int>>(relaxed = true)

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        instantiateViewModel()
    }

    @After
    fun cleanUp(){
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    private fun instantiateViewModel() {
        viewModel = TaskViewModel(repository)
        viewModel.messageEventData.observeForever(messageEventObserver)
    }

    @Test
    fun `when viewModel addOrUpdateTask with id equal to 0 get success then messageEventLiveData should be set to inserted`(){
        coEvery {
            repository.insert(TASK_DESCRIPTION)
        } returns TASK_ID_ONE

        viewModel.addOrUpdateTask(TASK_ID_ZERO, TASK_DESCRIPTION)

        coVerify {
            repository.insert(TASK_DESCRIPTION)
        }

        coVerify {
            messageEventObserver.onChanged(R.string.task_inserted_successfully)
        }
    }

    @Test
    fun `when viewModel addOrUpdateTask with empty description then messageEventLiveData should be set to error`(){

        viewModel.addOrUpdateTask(TASK_ID_ZERO, TASK_DESCRIPTION_EMPTY)

        coVerify {
            messageEventObserver.onChanged(R.string.task_error_invalid_description)
        }
    }

    @Test
    fun `when viewModel addOrUpdateTask with id greater than 0 get success then messageEventLiveData should be set to updated`(){
        coJustRun {
            repository.update(TASK_ID_ONE, TASK_DESCRIPTION, TASK_DONE)
        }

        viewModel.addOrUpdateTask(TASK_ID_ONE, TASK_DESCRIPTION)

        coVerify {
            repository.update(TASK_ID_ONE, TASK_DESCRIPTION, TASK_DONE)
        }

        coVerify {
            messageEventObserver.onChanged(R.string.task_updated_successfully)
        }
    }

    @Test
    fun `when viewModel addOrUpdateTask get exception then messageEventLiveData should be set to error`(){
        coEvery {
            repository.insert(TASK_DESCRIPTION)
        } throws Exception()

        viewModel.addOrUpdateTask(TASK_ID_ZERO, TASK_DESCRIPTION)

        coVerify {
            repository.insert(TASK_DESCRIPTION)
        }

        coVerify {
            messageEventObserver.onChanged(R.string.task_error_to_insert)
        }
    }

    @Test
    fun `when viewModel deleteTask with id greater than 0 get success then messageEventLiveData should be set to deleted`(){
        coJustRun {
            repository.delete(TASK_ID_ONE)
        }

        viewModel.deleteTask(TASK_ID_ONE)

        coVerify {
            repository.delete(TASK_ID_ONE)
        }

        coVerify {
            messageEventObserver.onChanged(R.string.task_deleted_successfully)
        }
    }

    @Test
    fun `when viewModel deleteTask with id equal to 0 then messageEventLiveData should not be called`(){

        viewModel.deleteTask(TASK_ID_ZERO)

        coVerify(exactly = 0) {
            messageEventObserver.onChanged(R.string.task_deleted_successfully)
        }
    }

    @Test
    fun `when viewModel deleteTask get exception then messageEventLiveData should be set to error`(){
        coEvery {
            repository.delete(TASK_ID_ONE)
        } throws Exception()

        viewModel.deleteTask(TASK_ID_ONE)

        coVerify {
            repository.delete(TASK_ID_ONE)
        }

        coVerify {
            messageEventObserver.onChanged(R.string.task_error_to_delete)
        }
    }


    companion object{
        const val TASK_ID_ZERO = 0L
        const val TASK_ID_ONE = 1L
        const val TASK_DESCRIPTION = "New Task"
        const val TASK_DESCRIPTION_EMPTY = ""
        const val TASK_DONE = false
    }
}