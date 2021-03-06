package com.allysonjeronimo.doit.extensions

import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

/*
    Adicionar no root do layout da fragment:
    android:focusable="true"
    android:focusableInTouchMode="true"
 */
fun AppCompatActivity.hideKeyboard(){
    val view = this.currentFocus
    if(view != null){
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}