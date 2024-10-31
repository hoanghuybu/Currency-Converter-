package com.example.currencyconverter.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.ColorInt
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.helper.EndPoints
import com.example.currencyconverter.helper.Resource
import com.example.currencyconverter.helper.Utility
import com.example.currencyconverter.model.Rates
import com.google.android.material.snackbar.Snackbar
import java.util.Currency
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var selectedItem1: String? = "AFN"
    private var selectedItem2: String? = "AFN"

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initSpinner()

        setUpClickListener()
    }

    private fun initSpinner(){


        val spinner1 = binding.spnFirstCountry


        spinner1.setItems( getAllCountries() )

        spinner1.setOnClickListener {
            Utility.hideKeyboard(this)
        }


        spinner1.setOnItemSelectedListener { view, position, id, item ->

            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getSymbol(countryCode)
            selectedItem1 = currencySymbol
            binding.txtFirstCurrencyName.setText(selectedItem1)
        }



        val spinner2 = binding.spnSecondCountry


        spinner1.setOnClickListener {
            Utility.hideKeyboard(this)
        }


        spinner2.setItems( getAllCountries() )



        spinner2.setOnItemSelectedListener { view, position, id, item ->
            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getSymbol(countryCode)
            selectedItem2 = currencySymbol
            binding.txtSecondCurrencyName.setText(selectedItem2)
        }

    }

    private fun getSymbol(countryCode: String?): String? {
        val availableLocales = Locale.getAvailableLocales()
        for (i in availableLocales.indices) {
            if (availableLocales[i].country == countryCode
            ) return Currency.getInstance(availableLocales[i]).currencyCode
        }
        return ""
    }

    private fun getCountryCode(countryName: String) = Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }

    private fun getAllCountries(): ArrayList<String> {

        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                countries.add(country)
            }
        }
        countries.sort()

        return countries
    }

    private fun setUpClickListener(){


        binding.btnConvert.setOnClickListener {


            val numberToConvert = binding.etFirstCurrency.text.toString()

            if(numberToConvert.isEmpty() || numberToConvert == "0"){
                Snackbar.make(binding.mainLayout,"Input a value in the first text field, result will be shown in the second text field", Snackbar.LENGTH_LONG)
                    .withColor(ContextCompat.getColor(this, R.color.dark_red))
                    .setTextColor(ContextCompat.getColor(this, R.color.white))
                    .show()
            }
            else if (!Utility.isNetworkAvailable(this)) {
                Snackbar.make(
                    binding.mainLayout,
                    "You are not connected to the internet",
                    Snackbar.LENGTH_LONG
                )
                    .withColor(ContextCompat.getColor(this, R.color.dark_red))
                    .setTextColor(ContextCompat.getColor(this, R.color.white))
                    .show()
            }
            else{
                doConversion()
            }
        }

        binding.txtContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val data: Uri = Uri.parse("mailto:ibrajix@gmail.com?subject=Hello")
            intent.data = data
            startActivity(intent)
        }

    }

    private fun doConversion(){

        Utility.hideKeyboard(this)

        binding.prgLoading.visibility = View.VISIBLE

        binding.btnConvert.visibility = View.GONE

        val apiKey = EndPoints.API_KEY
        val from = selectedItem1.toString()
        val to = selectedItem2.toString()
        val amount = binding.etFirstCurrency.text.toString().toDouble()

        mainViewModel.getConvertedData(apiKey, from, to, amount)

        observeUi()

    }

    @SuppressLint("SetTextI18n")
    private fun observeUi() {


        mainViewModel.data.observe(this, androidx.lifecycle.Observer {result ->

            when(result.status){
                Resource.Status.SUCCESS -> {
                    if (result.data?.status == "success"){

                        val map: Map<String, Rates>

                        map = result.data.rates

                        map.keys.forEach {

                            val rateForAmount = map[it]?.rate_for_amount

                            mainViewModel.convertedRate.value = rateForAmount

                            //format the result obtained e.g 1000 = 1,000
                            val formattedString = String.format("%,.2f", mainViewModel.convertedRate.value)

                            //set the value in the second edit text field
                            binding.etSecondCurrency.setText(formattedString)

                        }


                        //stop progress bar
                        binding.prgLoading.visibility = View.GONE
                        //show button
                        binding.btnConvert.visibility = View.VISIBLE
                    }
                    else if(result.data?.status == "fail"){
                        val layout = binding.mainLayout
                        Snackbar.make(layout,"Ooops! something went wrong, Try again", Snackbar.LENGTH_LONG)
                            .withColor(ContextCompat.getColor(this, R.color.dark_red))
                            .setTextColor(ContextCompat.getColor(this, R.color.white))
                            .show()

                        //stop progress bar
                        binding.prgLoading.visibility = View.GONE
                        //show button
                        binding.btnConvert.visibility = View.VISIBLE
                    }
                }
                Resource.Status.ERROR -> {

                    val layout = binding.mainLayout
                    Snackbar.make(layout,  "Oopps! Something went wrong, Try again", Snackbar.LENGTH_LONG)
                        .withColor(ContextCompat.getColor(this, R.color.dark_red))
                        .setTextColor(ContextCompat.getColor(this, R.color.white))
                        .show()
                    //stop progress bar
                    binding.prgLoading.visibility = View.GONE
                    //show button
                    binding.btnConvert.visibility = View.VISIBLE
                }

                Resource.Status.LOADING -> {
                    binding.prgLoading.visibility = View.VISIBLE
                    binding.btnConvert.visibility = View.GONE
                }
            }
        })
    }

    private fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar {
        this.view.setBackgroundColor(colorInt)
        return this
    }

}