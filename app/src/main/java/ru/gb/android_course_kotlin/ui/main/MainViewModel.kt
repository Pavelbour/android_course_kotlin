package ru.gb.android_course_kotlin.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.android_course_kotlin.DataState
import ru.gb.android_course_kotlin.data.IWeatherRepository
import ru.gb.android_course_kotlin.data.WeatherRepository
import ru.gb.android_course_kotlin.domain.Weather
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserve: MutableLiveData<DataState> = MutableLiveData(),
                    private val repository : IWeatherRepository = WeatherRepository()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getListFromLocalSource() = getDataFromLocalSource()

    fun getListFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.postValue(DataState.Loading)
        Thread {
            sleep(5000)
            liveDataToObserve.postValue(DataState.Success(repository.getWeatherListFromLocalStorage()))
        }.start()
    }

    fun addNewItem(weather: Weather) {
        repository.addNewItem(weather)
        liveDataToObserve.postValue(DataState.Success(repository.getWeatherListFromLocalStorage()))
    }
}