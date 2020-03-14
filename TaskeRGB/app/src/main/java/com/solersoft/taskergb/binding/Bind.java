package com.solersoft.taskergb.binding;

import androidx.lifecycle.MutableLiveData;

import kotlin.reflect.KMutableProperty;
import kotlin.reflect.KMutableProperty0;

public class Bind {
    public static <R> MutableLiveData<R> live(KMutableProperty0<R> prop) {
        return ((LiveProperty<R>)prop.getDelegate()).getData();
    }
}
