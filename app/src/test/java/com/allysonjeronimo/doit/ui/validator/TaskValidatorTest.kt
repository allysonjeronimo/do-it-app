package com.allysonjeronimo.doit.ui.validator

import com.allysonjeronimo.doit.data.db.entity.Task
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class TaskValidatorTest{

    private val validator by lazy{
        TaskValidator()
    }

    private val validTask = Task(
        1,
        "Task 1",
        false
    )

    @Test
    fun `should validate task1 for a valid Task`(){
        assertTrue(validator.validate(validTask.description))
    }

    @Test
    fun `should not validate task without a description`(){
        val task = validTask.copy(description = "")
        assertFalse(validator.validate(task.description))
    }

}