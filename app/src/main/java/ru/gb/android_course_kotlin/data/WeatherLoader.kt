package ru.gb.android_course_kotlin.data

import android.os.Handler
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.android_course_kotlin.BuildConfig
import java.io.IOException

class WeatherLoader(private val listener: WeatherLoaderListener, private val lat: Double, private val lon: Double) {
    interface WeatherLoaderListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }

    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(WeatherApi::class.java)

    fun loadWeather() {
        val handler : Handler = Handler()

        weatherApi.getWeather(
            BuildConfig.WEATHER_API_KEY, lat,
            lon
        ).enqueue(object : Callback<WeatherDTO> {

                    @Throws(IOException::class)
                    override fun onResponse(call: Call<WeatherDTO>?, response: Response<WeatherDTO>) {
                        val serverResponse: WeatherDTO? = response.body()
                        if (response.isSuccessful && serverResponse != null) {
                            handler.post {
                                listener.onLoaded(serverResponse)
                            }
                        } else {
                            TODO("Error handler")
                        }
                    }

                    override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
    }
}