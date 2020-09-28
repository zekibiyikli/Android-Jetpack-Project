package com.example.kotlincountries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlincountries.model.Country

class CountryViewModel: ViewModel() {
    val countryLiveData= MutableLiveData<Country>()

    fun getDataFromRoom(){
        val country=Country("1","2","3","4","5","6")
        countryLiveData.value=country
    }
}