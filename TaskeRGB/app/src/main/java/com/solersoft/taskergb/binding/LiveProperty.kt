package com.solersoft.taskergb.binding

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible

/**
 * Enables bindable LiveData fields through delegates.
 * Examples:
 * <pre>
 * var name : String by KLiveData("not given")
 *
 * or
 *
 * &#64;JvmField val nameData = KLiveData("not given")
 * var name : String by nameData
 * </pre>
 * @see {@link https://stackoverflow.com/questions/44844933/more-fun-with-kotlin-delegates}
 */
class LiveProperty<T>(private val default: T, private val liveData : MutableLiveData<T>? = null) : ReadWriteProperty<Any?,T> {
    val data = liveData ?: MutableLiveData<T>()

    override operator fun getValue(thisRef: Any?, property: KProperty<*>):T {
        return data.value ?: default
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value:T) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            data.value = value
        } else {
            data.postValue(value)
        }
    }
}

/**
 * Gets the {@link MutableLiveData} which backs the delegated field.
 * Example: viewModel::name.liveData
 */
inline fun <T> KMutableProperty0<*>.live(): MutableLiveData<T> {
    isAccessible = true
    return (getDelegate() as LiveProperty<T>).data
}

/**
 * Observes the {@link MutableLiveData} which backs the delegated field.
 * Example: viewModel::name.observe(owner, observer<R>)
 */
inline fun <reified T> KMutableProperty0<*>.observe(owner: LifecycleOwner, obs : Observer<T>) {
    isAccessible = true
    (getDelegate() as LiveProperty<T>).data.observe(owner,obs)
}