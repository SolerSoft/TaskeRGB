package com.solersoft.taskergb.binding

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import com.solersoft.taskergb.BR
import kotlin.reflect.KProperty

typealias NotifyPropertyDelegate = (fieldId: Int) -> Unit
typealias ValueChangeDelegate<T> = (oldValue: T, newValue: T) -> Unit

class DelegatedBindable<T>(private var value: T,
                           private val notifyPropertyChanged : NotifyPropertyDelegate,
                           private val expression: ValueChangeDelegate<T>? = null) {

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
        notifyPropertyChanged(bindingTarget)
        expression?.invoke(oldValue, value)
    }
}

fun <T> BaseObservable.bindDelegate(value: T, expression: ((oldValue: T, newValue: T) -> Unit)? = null):
        DelegatedBindable<T> =
        DelegatedBindable(value, {fieldId -> this.notifyPropertyChanged(fieldId)})

fun <T> ObservableViewModel.bindDelegate(value: T, expression: ((oldValue: T, newValue: T) -> Unit)? = null):
        DelegatedBindable<T> =
        DelegatedBindable(value, {fieldId -> this.notifyPropertyChanged(fieldId)})