package ru.gb.android_course_kotlin.ui.newCity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.gb.android_course_kotlin.databinding.FragmentNewSityBinding
import ru.gb.android_course_kotlin.domain.City
import ru.gb.android_course_kotlin.domain.Weather
import ru.gb.android_course_kotlin.main.MainViewModel


class NewCity : Fragment() {
    private var _binding: FragmentNewSityBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewSityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNewCityButton.setOnClickListener{
            val cityName: String = binding.addNewCityName.text.toString()
            val lat : Double = binding.addNewCityLat.text.toString().toDouble()
            val lon : Double = binding.addNewCityLon.text.toString().toDouble()
            val city = City(cityName, lat, lon)
            val weather = Weather(city, 0, -3)
            viewModel.addNewItem(weather)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}