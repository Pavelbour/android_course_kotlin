package ru.gb.android_course_kotlin.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ru.gb.android_course_kotlin.BuildConfig
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.databinding.FragmentSearchBinding
import java.io.IOException

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val initialPlace = LatLng(52.52000659999999, 13.404953999999975)
        googleMap.addMarker(
            MarkerOptions().position(initialPlace).title(getString(R.string.search_start))
        )
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                initialPlace,
                15f
            )
        )

        activateMyLocation(googleMap)
    }

    private fun activateMyLocation(googleMap: GoogleMap) {
        context?.let {
            val isPermissionGranted =
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                        PackageManager.PERMISSION_GRANTED

            googleMap.isMyLocationEnabled = isPermissionGranted
        }
    }

    private fun intiSearchByAddress() {
        if (BuildConfig.FLAVOR == "premium") {
            binding.searchButton.setOnClickListener {
                val geocoder = Geocoder(it.context)
                val searchText = binding.searchEditText.text.toString()
                Thread {
                    try {
                        val addresses = geocoder.getFromLocationName(searchText, 1)
                        if (addresses.size > 0) {
                            goToAddress(addresses, it, searchText)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }.start()
            }
        } else {
            binding.searchButton.isClickable = false
        }
    }

    private fun goToAddress(
        addresses: MutableList<Address>,
        view: View,
        searchText: String
    ) {
        val location = LatLng(
            addresses[0].latitude,
            addresses[0].longitude
        )
        view.post {
            setMarker(location, searchText)
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    15f
                )
            )
        }
    }

    private fun setMarker(
        location: LatLng,
        searchText: String
    ): Marker? = map.addMarker(
        MarkerOptions()
            .position(location)
            .title(searchText)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        intiSearchByAddress()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}