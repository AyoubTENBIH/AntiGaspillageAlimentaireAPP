package com.example.anti_gaspillagealimentaireapp.utils;

import androidx.lifecycle.MutableLiveData;

/**
 * LiveData qui ne livre qu'une seule fois (pour navigation / toasts).
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {
}
