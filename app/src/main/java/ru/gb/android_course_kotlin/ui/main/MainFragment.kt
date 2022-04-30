package ru.gb.android_course_kotlin.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.gb.android_course_kotlin.DataState
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.databinding.FragmentCitiesListBinding
import ru.gb.android_course_kotlin.domain.City
import ru.gb.android_course_kotlin.domain.Weather
import ru.gb.android_course_kotlin.showSnackBar
import ru.gb.android_course_kotlin.ui.details.CityDetailsFragment
import ru.gb.android_course_kotlin.ui.newCity.NewCity
import java.io.IOException

const val REQUEST_CODE = 1812
const val REFRESH_PERIOD = 60000L
const val MINIMAL_DISTANCE = 100f

class MainFragment() : Fragment() {

    private var _binding: FragmentCitiesListBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()
    private val adapter = Adapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCitiesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.citiesList.layoutManager = LinearLayoutManager(context)
        binding.citiesList.adapter = adapter
        binding.fragmentCitiesListBuild.text = getString(R.string.build)

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

        binding.addNewCityButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, NewCity())
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.fabLocation.setOnClickListener {
            checkPermission()
        }

    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ->
                {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResult: IntArray
    ) {
        checkPermissionsResult(requestCode, grantResult)
    }

    private fun checkPermissionsResult(requestCode: Int, grantResult: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions  = 0
                if (grantResult.isNotEmpty()) {
                    for (i in grantResult) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }
                    if (grantResult.size == grantedPermissions) {
                        getLocation()
                    } else {
                        showDialog(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }
                return
            }
        }
    }

    private fun showDialog(title: String, message:String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_message))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access))
                { _, _ ->
                    requestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline))
                { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_of),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_of),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it, location)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                binding.fabLocation.post {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _->
                    openDetailsFragment(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun openDetailsFragment(weather: Weather) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.container,
                CityDetailsFragment().also { fragment ->
                    fragment.arguments =
                        Bundle().also { bundle ->
                            bundle.putParcelable(
                                CityDetailsFragment.BUNDLE_EXTRA,
                                weather
                            )
                        }
                }
            )?.addToBackStack("")
            ?.commit()
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }
}