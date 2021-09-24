package ru.geekbrains.materialdesignpractice.view

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_INDEFINITE).show()
}

fun View.showToastShort(text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun View.showToastLong(text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()

}