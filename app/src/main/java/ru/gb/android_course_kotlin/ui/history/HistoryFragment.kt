package ru.gb.android_course_kotlin.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_history.*
import ru.gb.android_course_kotlin.DataState
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.databinding.FragmentHistoryBinding
import ru.gb.android_course_kotlin.showSnackBar

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_history__recycler_view.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.getAllHistory()
    }

    private fun renderData(dataState: DataState) {
        when (dataState) {
            is DataState.Success -> {
                binding.fragmentHistoryRecyclerView.visibility = View.VISIBLE
                binding.includeLoadingLayout.loadingLayout.visibility = View.GONE
                adapter.setData(dataState.weather)
            }
            is DataState.Loading -> {
                binding.fragmentHistoryRecyclerView.visibility = View.GONE
                binding.includeLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is DataState.Error -> {
                binding.fragmentHistoryRecyclerView.visibility = View.VISIBLE
                binding.includeLoadingLayout.loadingLayout.visibility = View.GONE
                binding.fragmentHistoryRecyclerView.showSnackBar(
                    R.string.error,
                    R.string.reload,
                    {
                        viewModel.getAllHistory()
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}