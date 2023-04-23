package com.ikbo0621.anitree.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


/**
 * Operations with progress bar
 * */
fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.disable() {
    isEnabled = false
}

fun View.enabled() {
    isEnabled = true
}

/**
 * Short toast function
 * */
fun Fragment.toast(msg: String?) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}

/**
 * Email validation with matcher
 * */
fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Transform anime title to request url format
 * */
fun String.fitToExactRequest() =
    this.replace(" - ", "-")
        .replace(" ", "-")
        .replace("\'", "")
        .replace(":", "")
        .replace("(", "")
        .replace(")", "")
        .lowercase()

fun String.fitToGuessRequest() =
    this.trim()
        .replace(" ", "%20")
        .replace("\'", "")
        .replace(":", "")
        .replace("(", "")
        .replace(")", "")
        .lowercase()