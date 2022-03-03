package ru.gb.android_course_kotlin.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.android_course_kotlin.Controller
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.domain.Weather

class Adapter(private val controller: Controller, private var data: ArrayList<Weather> = arrayListOf()) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityLabel: TextView = view.findViewById(R.id.cityLabel)
        val cityTemperature: TextView = view.findViewById(R.id.cityTemperature)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_city, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val weather: Weather = data[position]

        viewHolder.cityLabel.text = weather.city.city
        viewHolder.cityTemperature.text = weather.temperature.toString()
        viewHolder.itemView.setOnClickListener { controller.showDetails(weather) }
    }

    override fun getItemCount() = data.size

    fun setData(data: ArrayList<Weather>) {
        this.data = data
        notifyDataSetChanged()
    }
}