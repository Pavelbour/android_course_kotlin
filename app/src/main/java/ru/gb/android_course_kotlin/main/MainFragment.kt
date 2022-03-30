package ru.gb.android_course_kotlin.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.android_course_kotlin.DataState
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.showSnackBar
import ru.gb.android_course_kotlin.ui.newCity.NewCity

class MainFragment() : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()
    private val adapter = Adapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cities_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.citiesList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setAdapter(adapter)

        val observer = Observer<DataState> {
            when(it) {
                is DataState.Success -> {
                    adapter.setData(it.weather)
                }
                is DataState.Error-> {
                    view.showSnackBar(
                        text = R.string.error,
                        actionText = R.string.reload,
                        action = { viewModel.getListFromLocalSource()}
                    )
                }
            }
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getListFromLocalSource()

        val newCityButton : Button = view.findViewById(R.id.add_new_city__button)
        newCityButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, NewCity())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }
}