package com.solersoft.taskergb

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


fun String.toToast(context: Context) {
    Handler(Looper.getMainLooper()).post { Toast.makeText(context, this, Toast.LENGTH_LONG).show() }
}

fun Activity.selectOne(title: String, options: List<String>, callback: (String?) -> Unit) {
    AlertDialog.Builder(this).apply {
        setIcon(R.mipmap.ic_launcher)
        setTitle(title)
        val arrayAdapter = ArrayAdapter<String>(this@selectOne, android.R.layout.select_dialog_singlechoice).apply {
            addAll(options)
        }
        setAdapter(arrayAdapter) { _, which -> callback(arrayAdapter.getItem(which)) }
        setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss(); callback(null) }
    }.show()
}

fun Activity.alert(title: String, message: String) {
    val alertDialog = AlertDialog.Builder(this).create()
    alertDialog.setTitle(title)
    alertDialog.setMessage(message)
    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, _ -> dialog.dismiss() }
    alertDialog.show()
}

val RadioGroup.checkedRadioButton get() = this.findViewById<RadioButton>(checkedRadioButtonId)

/**
 * Throws an [IllegalArgumentException] with the result of calling [lazyMessage] if [value] doesn't
 * fall within the expected range.
 *
 * @sample samples.misc.Preconditions.failRequireWithLazyMessage
 */
public inline fun requireRange(value: Double, max: Double, min: Double=0.0, lazyMessage: () -> Any): Unit {
    require((value >= min) && (value <= max), lazyMessage)
}

/**
 * Throws an [IllegalArgumentException] with the result of calling [lazyMessage] if [value] doesn't
 * fall within the expected range.
 *
 * @sample samples.misc.Preconditions.failRequireWithLazyMessage
 */
public inline fun requireRange(value: Int, max: Int, min: Int=0, lazyMessage: () -> Any): Unit {
    require((value >= min) && (value <= max), lazyMessage)
}