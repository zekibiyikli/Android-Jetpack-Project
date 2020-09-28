package com.example.kotlincountries.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application):AndroidViewModel(application), CoroutineScope{

    private val job= Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main  // önce işi yap sonra main threadi yap

    override fun onCleared() {
        super.onCleared()
        job.cancel()// eğer app contexti giderse iş iptal et
    }
}