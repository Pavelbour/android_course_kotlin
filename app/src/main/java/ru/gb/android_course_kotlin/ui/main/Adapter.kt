package ru.gb.android_course_kotlin.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.ui.details.CityDetailsFragment
import ru.gb.android_course_kotlin.domain.Weather

class Adapter(private val activity: Fragment) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private var data: List<Weather> = arrayListOf()

    val fragment: Fragment = activity

    var listener: ((Weather)->Unit)? = fun (weather: Weather) {
        fragment.activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.container, CityDetailsFragment().also { fragment ->
                fragment.arguments = Bundle().also { bundle ->
                    bundle.putParcelable(
                        CityDetailsFragment.BUNDLE_EXTRA,
                        weather
                    )
                }
            })
            ?.addToBackStack(null)
            ?.commit()
    }

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

        with(viewHolder) {
            cityLabel.text = weather.city.city
            cityTemperature.text = weather.temperature.toString()
            itemView.setOnClickListener { listener?.let { it1 -> it1(weather) } }
        }
    }

    fun removeListener() {
        listener = null
    }

    override fun getItemCount() = data.size

    fun setData(data: List<Weather>) {
        this.data = data
        notifyDataSetChanged()
    }
}