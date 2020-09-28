package com.example.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlincountries.model.Country
import com.example.kotlincountries.service.CountryAPIService
import com.example.kotlincountries.service.CountryDatabase
import com.example.kotlincountries.util.CustomSharedPreferences
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryApiService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refrestTime = 10*60*1000*1000*1000L // 10 dakikayı nanosaniyeyi verir

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData(){
        val updateTime = customPreferences.getTime()
        if (updateTime!=null && updateTime !=0L && System.nanoTime()-updateTime<refrestTime){
            getDataFromSQLite()
        }else{
            getDataFromAPI()
        }
    }

    private fun getDataFromSQLite(){
        launch {
            val countries=CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries From SQLite",Toast.LENGTH_LONG).show()
        }
    }

    private fun getDataFromAPI(){
        countryLoading.value=true

        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.io())//Çalıştırma
                .observeOn(AndroidSchedulers.mainThread())//Gösterme
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries From API",Toast.LENGTH_LONG).show()
                    }
                    override fun onError(e: Throwable) {
                        countryLoading.value=false
                        countryError.value=true
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun showCountries(countryList: List<Country>){
        countries.value=countryList
        countryError.value=false
        countryLoading.value=false
    }

    private fun storeInSQLite(list: List<Country>){
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray()) // verileri tek tek hale getirecektir
            var i=0
            while (i<list.size){
                list[i].id=listLong[i].toInt()
                i++
            }

            showCountries(list)
        }

        customPreferences.saveTime(System.nanoTime())
    }
}