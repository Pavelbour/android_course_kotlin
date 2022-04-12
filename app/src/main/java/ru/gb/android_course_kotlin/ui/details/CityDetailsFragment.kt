package ru.gb.android_course_kotlin.ui.details


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.gb.android_course_kotlin.App
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.data.WeatherDTO
import ru.gb.android_course_kotlin.data.WeatherLoader
import ru.gb.android_course_kotlin.data.localData.db.DbRepository
import ru.gb.android_course_kotlin.data.localData.db.IDbRepository
import ru.gb.android_course_kotlin.databinding.FragmentMainBinding
import ru.gb.android_course_kotlin.domain.Weather

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

class CityDetailsFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val dbRepository: IDbRepository = DbRepository(App.getHistoryDao())

    private val loaderListener =
        object : WeatherLoader.WeatherLoaderListener {
            override fun onLoaded(weatherDTO: WeatherDTO) {
                weatherBundle.temperature = weatherDTO.fact.temp
                weatherBundle.feelsLike = weatherDTO.fact.feelsLike

                saveCityToDb(weatherBundle)
                setData(weatherBundle)
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
        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let { weather ->
            weatherBundle = weather
        }
        loadWeather(weatherBundle)
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

            context?.let { ctx->
                Glide.with(ctx)
                    .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                    .into(binding.headerIcon)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather(weatherData: Weather) {
        val weatherLoader =
            WeatherLoader(loaderListener, weatherData.city.lat, weatherData.city.lon)
        weatherLoader.loadWeather()
    }

    private fun saveCityToDb(weather: Weather) {
        Thread {
            dbRepository.saveEntity(weather)
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
    }
}