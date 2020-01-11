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
import kotlin.reflect.KClass


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
 * Throws an [IllegalArgumentException] with the result of calling [lazyMessage] if [name] is
 * not a valid member of the enum.
 */
inline fun <reified T: Enum<T>> requireName(name: String, crossinline lazyMessage: () -> Any): Unit {

    // Attempt to get named value. This automatically results in an IllegalArgumentException
    // If the string is not valid
    enumValueOf<T>(name)
}

/**
 * Throws an [IllegalArgumentException] with the result of calling [lazyMessage] if [value] doesn't
 * fall within the expected range.
 *
 * @sample samples.misc.Preconditions.failRequireWithLazyMessage
 */
inline fun requireRange(value: Double, max: Double, min: Double=0.0, lazyMessage: () -> Any): Unit {
    require((value >= min) && (value <= max), lazyMessage)
}

/**
 * Throws an [IllegalArgumentException] with the result of calling [lazyMessage] if [value] doesn't
 * fall within the expected range.
 */
inline fun requireRange(value: Int, max: Int, min: Int=0, lazyMessage: () -> Any): Unit {
    require((value >= min) && (value <= max), lazyMessage)
}

/**
 * Throws an [IllegalArgumentException] with the result of calling [lazyMessage] if [value] doesn't
 * fall within the ordinal range of the enum type.
 */
inline fun <reified T: Enum<T>> requireRange(value: Int, crossinline lazyMessage: () -> Any): Unit {

    // Get the values
    val values = enumValues<T>()

    // New message function to append valid range information.
    val newLazyMessage  = { ->
        var msg = lazyMessage().toString()
        if (msg.isNotEmpty()) msg = msg.plus(" ")
        msg = msg.plus("Value must fall between 0 and ${values.lastIndex}")
        msg.toString()
    }

    // Use range requirement with updated message function
    require((value >= 0) && (value <= values.lastIndex), newLazyMessage)
}

/**
 * Returns the enum value at the specified index of throws an error if the index is invalid.
 * @param index The index of the enum value to return.
 */
fun <T : Enum<*>> KClass<T>.byIndex(index: Int): T {

    // Get values
    val values = this.java.enumConstants

    // Ensure sure we're in range
    requireRange(index, values.lastIndex) { "$index is not a valid index for ${this.simpleName}" }

    // Return converted type
    return values[index]
}

inline fun <reified T: Enum<T>> T.byNewIndex(index: Int) : T {
    return enumValues<T>()[index]
}