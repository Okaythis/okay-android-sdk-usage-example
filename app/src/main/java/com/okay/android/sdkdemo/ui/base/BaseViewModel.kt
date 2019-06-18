package com.okay.android.sdkdemo.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    val disposables = CompositeDisposable()
    protected val message = MutableLiveData<String>()
    fun getMessage(): LiveData<String> = message

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}