package ru.gb.android_course_kotlin.ui.cityDetails

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import ru.gb.android_course_kotlin.data.FactDTO
import ru.gb.android_course_kotlin.data.WeatherDTO
import ru.gb.android_course_kotlin.data.WeatherLoader
import ru.gb.android_course_kotlin.databinding.FragmentMainBinding
import ru.gb.android_course_kotlin.domain.Weather
import ru.gb.android_course_kotlin.services.LATITUDE_EXTRA
import ru.gb.android_course_kotlin.services.LONGITUDE_EXTRA
import ru.gb.android_course_kotlin.services.WeatherApiRequestService

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_CONDITION_EXTRA = "CONDITION"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"

class CityDetails(val weather: Weather) : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    private val loadResultReceiver: BroadcastReceiver = object :
        BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                    DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                    DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                    DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                    DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                    DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                    DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                    DETAILS_RESPONSE_SUCCESS_EXTRA -> setWeatherData(
                                intent.getIntExtra(DETAILS_TEMP_EXTRA, TEMP_INVALID),
                                intent.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, FEELS_LIKE_INVALID),
                            )
                    else -> TODO(PROCESS_ERROR)
                }
            }
        }

    private fun setWeatherData(temperature: Int, feelsLike: Int) {
        weather.temperature = temperature
        weather.feelsLike = feelsLike
        setData(weather)
    }

//    private val loaderListener =
//        object : WeatherLoader.WeatherLoaderListener {
//            override fun onLoaded(weatherDTO: WeatherDTO) {
//                weather.temperature = weatherDTO.fact.temp
//                weather.feelsLike = weatherDTO.fact.feelsLike
//
//                setData(weather)
//            }
//
//            override fun onFailed(throwable: Throwable) {
//                Log.e("", "Network error")
//                throwable.printStackTrace()
//            }
//        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.registerReceiver(
            loadResultReceiver,
            IntentFilter(DETAILS_INTENT_FILTER)
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        loadWeather(weather)
        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let { weather ->
            weatherBundle = weather
        }
        getWeather()
    }

    private fun getWeather() {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        context?.let {
            it.startService(Intent(it, WeatherApiRequestService::class.java).apply {
                putExtra(
                    LATITUDE_EXTRA,
                    weather.city.lat
                )
                putExtra(
                    LONGITUDE_EXTRA,
                    weather.city.lon
                )
            })
        }
    }


    private fun setData(weatherData: Weather) {
        binding.mainView.visibility = View.VISIBLE
        binding.loadingLayout.visibility = View.GONE
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


//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun loadWeather(weatherData: Weather) {
//        val weatherLoader =
//            WeatherLoader(loaderListener, weatherData.city.lat, weatherData.city.lon)
//        weatherLoader.loadWeather()
//    }
//
//    private fun showLoading(isShow: Boolean) {
//        with(binding) {
//            loadingLayout.isVisible = isShow
//            mainView.isVisible = !isShow
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        context?.unregisterReceiver(loadResultReceiver)
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
    }
}