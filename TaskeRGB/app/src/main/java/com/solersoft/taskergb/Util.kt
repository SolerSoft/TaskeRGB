package com.solersoft.taskergb

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.*
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.setPadding
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.solersoft.taskergb.tasker.palette.ColorTargetResult
import kotlin.reflect.KClass


/**
 * The prefix used for all tasker variables
 */
const val VAR_PREFIX = "tr"
private const val PERMISSION_REQUEST_COARSE_LOCATION = 1

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
fun ColorTargetResult?.toTaskerColor(defaultColor: String) : String {
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

fun Activity.alert(title: String, message: String?) {
    val alertDialog = AlertDialog.Builder(this).create()
    alertDialog.setTitle(title)
    alertDialog.setMessage(message)
    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, _ -> dialog.dismiss() }
    alertDialog.show()
}

/*
Instead using https://github.com/LouisCAD/Splitties/tree/master/modules/permissions

fun Activity.checkLocationPermission() : Boolean {
    when (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
        PackageManager.PERMISSION_GRANTED -> true
        PackageManager.PERMISSION_DENIED -> {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to user *asynchronously* -- don't block this thread waiting
                // for the user's response! After user sees the explanation, try again to request
                // the permission
                Toast.makeText(this, "Location access is required to show Bluetooth devices nearby.", Toast.LENGTH_LONG).show()
            } else {
                //No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_COARSE_LOCATION)
            }
            false
        }
    }
}
 */

fun Activity.getDp(px: Int) : Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px.toFloat(), resources.displayMetrics).toInt()
}

/**
 * Shows a help popup with the specified title and markup message.
 * @param title The title to display for the help dialog.
 * @param markup The markup text to display in the help dialog.
 */
fun Activity.showHelp(title: String, markup: String) {

    // Create the TextView that will display the markup
    val textView = TextView(this);

    // Give it some nice padding and formatting
    textView.setPadding(getDp(16))
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)

    // Parse the markup
    textView.text = HtmlCompat.fromHtml(markup, 0)

    // Create builder
    val ab = AlertDialog.Builder(this);

    // Set title
    ab.setTitle(title)

    // Set view to TextView
    ab.setView(textView)

    // OK button
    ab.setPositiveButton(R.string.ok) { dialog, _ ->
        dialog.dismiss()
    }

    // No cancel button
    ab.setCancelable(false)

    // Show it
    ab.show()
}

/**
 * Shows a help popup with the localized title 'Help' and the specified markup message.
 * @param markup The markup text to display in the help dialog.
 */
fun Activity.showHelp(markup: String) {

    // Get "Help" title
    val title = this.getString(R.string.help)

    // Call other override
    showHelp(title, markup)
}

/**
 * Shows a help popup with the localized title 'Help' and the specified markup resource.
 * @param markupId The ID of the markup string resource to display in the help dialog.
 */
fun Activity.showHelp(@StringRes markupId : Int) {
    showHelp(this.getString(markupId))
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