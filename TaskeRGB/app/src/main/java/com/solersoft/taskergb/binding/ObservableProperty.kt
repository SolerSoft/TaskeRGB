package com.solersoft.taskergb.binding

import androidx.databinding.BaseObservable
import com.solersoft.taskergb.BR
import com.solersoft.taskergb.requireRange
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

        // If we don't know our binding ID yet, need to find it
        if (bindingTarget == -1) {

            // Get binding fields that match our field name
            val bindFields = BR::class.java.fields.filter {
                it.name == p.name
            }

            // Make sure we have exactly one result
            requireRange(bindFields.size, min = 1, max = 1) { "Could not determine binding for field '${p.name}'. Did you annotate the field with @get:Bindable?" }

            // Get the binding target
            bindingTarget = bindFields[0].getInt(null)
        }
        notifyPropertyChanged(bindingTarget)
        valueChanged?.invoke(oldValue, value)
    }
}

fun <T> BaseObservable.observableProperty(value: T, valueChanged: ValueChangeDelegate<T>? = null):
        ObservableProperty<T> =
        ObservableProperty(value = value, notifyPropertyChanged = { fieldId -> this.notifyPropertyChanged(fieldId)}, valueChanged = valueChanged)

fun <T> ObservableViewModel.observableProperty(value: T, valueChanged: ValueChangeDelegate<T>? = null):
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