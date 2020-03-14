package com.solersoft.taskergb.binding

import androidx.databinding.BaseObservable
import com.solersoft.taskergb.BR
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

typealias NotifyPropertyDelegate = (fieldId: Int) -> Unit
typealias ValueChangeDelegate<T> = (oldValue: T, newValue: T) -> Unit

/**
 * Simplifies implementing Observable through delegates.
 * @see {@Link https://stablekernel.com/reducing-data-binding-boilerplate-with-kotlin}
 */
class ObservableProperty<T>(private var value: T,
                            private val notifyPropertyChanged : NotifyPropertyDelegate,
                            private val valueChanged: ValueChangeDelegate<T>? = null) : ReadWriteProperty<Any?, T> {

    private var bindingTarget: Int = -1

    override operator fun getValue(thisRef: Any?, p: KProperty<*>) = value

    override operator fun setValue(thisRef: Any?, p: KProperty<*>, v: T) {

        val oldValue = value
        value = v
        if (bindingTarget == -1) {
            bindingTarget = BR::class.java.fields.filter {
                it.name == p.name
            }[0].getInt(null)
        }
        notifyPropertyChanged(bindingTarget)
        valueChanged?.invoke(oldValue, value)
    }
}

fun <T> BaseObservable.bindDelegate(value: T, valueChanged: ValueChangeDelegate<T>? = null):
        ObservableProperty<T> =
        ObservableProperty(value = value, notifyPropertyChanged = { fieldId -> this.notifyPropertyChanged(fieldId)}, valueChanged = valueChanged)

fun <T> ObservableViewModel.bindDelegate(value: T, valueChanged: ValueChangeDelegate<T>? = null):
        ObservableProperty<T> =
        ObservableProperty(value = value, notifyPropertyChanged = { fieldId -> this.notifyPropertyChanged(fieldId)}, valueChanged = valueChanged)


/*

class LiveField<T> : MutableLiveData<T> {

    constructor(value: T) {
        this.value = value
    }

    operator fun getValue(thisRef: Any?, p: KProperty<*>) = this.value

    operator fun setValue(thisRef: Any?, p: KProperty<*>, v: T) {
        this.value = v
    }
}

fun <T> BaseObservable.bindLive(value: T):
        LiveField<T> =
        LiveField(value)

fun <T> ObservableViewModel.bindLive(value: T):
        LiveField<T> =
        LiveField(value)

 */