package com.allysonjeronimo.doit.ui.validator

import com.allysonjeronimo.doit.data.db.entity.Task

class TaskValidator {

    fun validate(description:String) = with(description){
        checkDescription(description)
    }

    private fun checkDescription(description:String) =
        description.isNotEmpty() && description.trim().length > 2
}