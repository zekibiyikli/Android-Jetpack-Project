package com.example.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kotlincountries.R
import com.example.kotlincountries.adapter.CountryAdapter
import com.example.kotlincountries.util.downloadFromUrl
import com.example.kotlincountries.util.placeHolderProgressBar
import com.example.kotlincountries.viewmodel.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country.*


class CountryFragment : Fragment() {

    private lateinit var viewModel : CountryViewModel
    var countryId:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryId=CountryFragmentArgs.fromBundle(it).countryId
        }

        viewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryId)

        observeLivedata()
    }

    fun observeLivedata(){
        viewModel.countryLiveData.observe(viewLifecycleOwner,Observer{country->
            country?.let {
                countryName.text=country.countryName
                countryCapital.text=country.countryCapital
                countryCurrency.text=country.countryCurrency
                countryLanguage.text=country.countryLanguage
                countryRegion.text=country.countryRegion
                context?.let {
                    countryImage.downloadFromUrl(country.imageUrl, placeHolderProgressBar(it))
                }
            }
        })
    }
}