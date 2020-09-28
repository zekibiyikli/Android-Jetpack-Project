package com.example.kotlincountries.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlincountries.model.Country

@Dao
interface CountryDAO {
    // Data Access object

    //Inseert -> Insert Into
    // suspend -> coroutine, pause & resume
    // vararg -> multiple country objects - sayısını bilmediğimiz bir tekil objeyi oluşturmak için kullanılır
    // List<Long> -> primary keys

    @Insert
    suspend fun insertAll(vararg countries: Country):List<Long>

    @Query("SELECT * FROM Country")
    suspend fun getAllCountries(): List<Country>

    @Query("SELECT * FROM Country WHERE id=:id")
    suspend fun getCountry(id:Int):Country

    @Query("DELETE FROM Country")
    suspend fun deleteAllCountries()

}