package com.solersoft.taskergb.util

import android.util.Log
import com.solersoft.taskergb.BR
import androidx.databinding.BaseObservable
import java.lang.Exception
import kotlin.reflect.KProperty

class DelegatedBindable<T>(private var value: T,
                           private val observer: BaseObservable,
                           private val expression: ((oldValue: T, newValue: T) -> Unit)? = null) {

    private var bindingTarget: Int = -1

    operator fun getValue(thisRef: Any?, p: KProperty<*>) = value

    operator fun setValue(thisRef: Any?, p: KProperty<*>, v: T) {

        val oldValue = value
        value = v
        if (bindingTarget == -1) {
            bindingTarget = BR::class.java.fields.filter {
                it.name == p.name
            }[0].getInt(null)
        }
        observer.notifyPropertyChanged(bindingTarget)
        expression?.invoke(oldValue, value)
    }
}

fun <T> BaseObservable.bindDelegate(value: T, expression: ((oldValue: T, newValue: T) -> Unit)? = null):
        DelegatedBindable<T> =
        DelegatedBindable(value, this)