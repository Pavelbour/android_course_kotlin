package ru.gb.android_course_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gb.android_course_kotlin.databinding.ActivityMainBinding
import ru.gb.android_course_kotlin.domain.Weather
import ru.gb.android_course_kotlin.ui.main.MainFragment
import ru.gb.android_course_kotlin.ui.cityDetails.CityDetails

class MainActivity : AppCompatActivity(), Controller {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, MainFragment.newInstance())
                .replace(R.id.container, MainFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun showDetails(weather: Weather) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CityDetails(weather))
            .addToBackStack(null)
            .commit()
    }
}