package com.example.currencyconverter.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyconverter.helper.Resource
import com.example.currencyconverter.helper.SingleLiveEvent
import com.example.currencyconverter.model.ApiResponse
import com.example.currencyconverter.model.Rates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepo: MainRepo) : ViewModel() {

    private val _data = SingleLiveEvent<Resource<ApiResponse>>()

    val data = _data
    val convertedRate = MutableLiveData<Double>()

    fun getConvertedData(access_key: String, from: String, to: String, amount: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            mainRepo.getConvertedData(access_key, from, to, amount).collect {
                data.postValue(it)
            }
        }
    }
}