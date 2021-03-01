package com.google.codelab.reminderwithgps.utils

import android.app.AlertDialog
import android.content.Context
import com.google.codelab.reminderwithgps.R

fun Context.showAlertDialog(title: Int, message: Int) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.ok, null)
        .show()
}
