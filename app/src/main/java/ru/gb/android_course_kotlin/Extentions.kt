package ru.gb.android_course_kotlin

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    text: Int,
//    actionText: String,
    actionText: Int,
    action: (View)-> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    val actionTextString: String = resources.getString(actionText)
    Snackbar.make(this, text, length).setAction(actionTextString, action).show()
}
