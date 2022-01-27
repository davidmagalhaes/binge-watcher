package br.com.davidmag.bingewatcher.presentation.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService

object UiUtils {

    fun closeKeyboard(context: Context, rootView: View){
        val imm: InputMethodManager? =
            getSystemService(context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(rootView.windowToken, 0)
    }
}