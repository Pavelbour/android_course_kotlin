package ru.gb.android_course_kotlin.ui.cityDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.databinding.FragmentMainBinding
import ru.gb.android_course_kotlin.domain.Weather

class CityDetails(val weather: Weather) : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(weather)
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