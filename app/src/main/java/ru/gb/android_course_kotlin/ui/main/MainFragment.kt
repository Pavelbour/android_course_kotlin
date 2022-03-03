package ru.gb.android_course_kotlin.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.android_course_kotlin.Controller
import ru.gb.android_course_kotlin.DataState
import ru.gb.android_course_kotlin.R
import java.lang.IllegalStateException

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var controller: Controller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Controller) {
            this.controller = context
        } else {
            throw IllegalStateException("Activity must implement Controller interface.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cities_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val adapter : Adapter = Adapter(controller)

        val recyclerView = view.findViewById<RecyclerView>(R.id.citiesList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setAdapter(adapter)

        val observer = Observer<DataState> {
            if (it is DataState.Success) {
                adapter.setData(it.weather)
            }
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getListFromLocalSource()
    }
}