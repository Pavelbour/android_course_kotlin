package ru.gb.android_course_kotlin.ui.cityDetails

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.data.WeatherDTO
import ru.gb.android_course_kotlin.data.WeatherLoader
import ru.gb.android_course_kotlin.databinding.FragmentMainBinding
import ru.gb.android_course_kotlin.domain.Weather

class CityDetails(val weather: Weather) : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    private val loaderListener =
        object : WeatherLoader.WeatherLoaderListener {
            override fun onLoaded(weatherDTO: WeatherDTO) {
                weather.temperature = weatherDTO.fact.temp
                weather.feelsLike = weatherDTO.fact.feelsLike

                setData(weather)
            }

            override fun onFailed(throwable: Throwable) {
                Log.e("", "Network error")
                throwable.printStackTrace()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWeather(weather)
    }


    private fun setData(weatherData: Weather) {
        with(binding) {
            cityName.text = weatherData.city.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weatherData.city.lat.toString(),
                weatherData.city.lon.toString()
            )

            temperatureValue.text = weatherData.temperature.toString()
            feelsLikeValue.text = weatherData.feelsLike.toString()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather(weatherData: Weather) {
        val weatherLoader =
            WeatherLoader(loaderListener, weatherData.city.lat, weatherData.city.lon)
        weatherLoader.loadWeather()
    }

    private fun showLoading(isShow: Boolean) {
        with(binding) {
            loadingLayout.isVisible = isShow
            mainView.isVisible = !isShow
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}