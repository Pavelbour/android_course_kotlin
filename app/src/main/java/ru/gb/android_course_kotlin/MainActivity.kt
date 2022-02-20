package ru.gb.android_course_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import ru.gb.android_course_kotlin.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val weatherRepository : WeatherRepository = WeatherRepository()
        weatherRepository.addWeather(Weather("New-York", 5, 2.0f, "N", false))
        weatherRepository.addWeather(Weather("Paris", 8, 1.5f, "W", true))
        weatherRepository.addWeather(Weather("London", 2, 5.0f, "S", false))

        weatherRepository.updateTemperature(6, 1)

        val weatherList : ArrayList<Weather> = weatherRepository.getWeatherList()
        val textView = findViewById<TextView>(R.id.mainactivit__text_wiev)


        for (weather in weatherList) {
            val text : String = textView.text.toString()
            textView.text = text + "The temperature in " + weather.city + " is " + weather.temperature + " degrees\n"
        }
        val btn = findViewById<Button>(R.id.main_activity__click_me_button)
        btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "click", Toast.LENGTH_SHORT).show()
            }
        })
    }
}