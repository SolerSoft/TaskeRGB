package com.solersoft.taskergb

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.solersoft.taskergb.tasker.palette.TargetResult
import kotlin.reflect.KClass

/**
 * The prefix used for all tasker variables
 */
const val VAR_PREFIX = "tr"

/**
 * Ensures the item is only added to the list once.
 */
fun <E> ArrayList<E>.addUnique(item: E){
    if (!this.contains(item)) {
        this.add(item)
    }
}

/**
 * Returns the contents of the string prepended with prefix.
 */
fun String.withPrefix() : String {
    return VAR_PREFIX.plus(this)
}

/**
 * Converts a ColorInt integer to a Tasker color string.
 */
fun Int.toTaskerColor() : String {
    return "#".plus(Integer.toHexString(this))
}

/**
 * Converts a nullable {@link TargetResult} to a tasker color.
 * @param defaultColor The default color to use if the target result is null.
 */
fun TargetResult?.toTaskerColor(defaultColor: String) : String {
    return this?.primary?.rgb?.toTaskerColor() ?: defaultColor
}

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
 * Throws an [IllegalArgumentException] with the result of calling [lazyMessage] if [value] isn't
 * formatted as a tasker color string.
 *
 * @sample samples.misc.Preconditions.failRequireWithLazyMessage
 */
inline fun requireTaskerColor(value: String, lazyMessage: () -> Any): Unit {
    require(value.startsWith('#'), lazyMessage)
    require(value.length >= 7, lazyMessage)
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

/**
 * Returns the dynamic value of the specified key if found; otherwise returns a default value.
 * @param key The dynamic key to return.
 * @param default The default value if {@link key} is not found.
 */
fun <T> TaskerInput<*>.deDynamic(key: String?, default: T) : T {
    // If key is null, return default
    if (key == null) { return default }

    // Attempt to get the info by key
    val info = this.dynamic.getByKey(key)

    // If we have the info, return its value. Otherwise default.
    return info?.valueAs<T>() ?: default
}