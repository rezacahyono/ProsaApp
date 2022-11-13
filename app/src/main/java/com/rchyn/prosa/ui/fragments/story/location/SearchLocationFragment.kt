package com.rchyn.prosa.ui.fragments.story.location

import android.Manifest
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.FragmentSearchLocationBinding
import com.rchyn.prosa.ui.activities.MainActivity
import com.rchyn.prosa.ui.fragments.story.StoryViewModel
import com.rchyn.prosa.utils.showSoftKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.io.IOException


class SearchLocationFragment : Fragment() {

    private var _binding: FragmentSearchLocationBinding? = null
    private val binding get() = _binding as FragmentSearchLocationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var act: MainActivity

    private val storyViewModel: StoryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchLocationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        act.askLocationPermissionGranted()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupSearchLocation()

        binding.btnMyLocation.setOnClickListener {
            getMyLocation()
        }
    }

    private fun setupSearchLocation() {
        val searchView = binding.searchLocation
        searchView.requestFocus()
        searchView.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                showSoftKeyboard(requireContext(), view.findFocus())
            }
        }

        KeyboardVisibilityEvent.setEventListener(requireActivity()) { hasOpen ->
            if (!hasOpen) {
                searchView.clearFocus()
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchLocation(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    internal fun searchLocation(name: String) {
        lifecycleScope.launch {
            try {
                val deferred = this.async(Dispatchers.IO) {
                    val geocoder = Geocoder(requireContext())
                    return@async geocoder.getFromLocationName(name, 10)
                }
                withContext(Dispatchers.Main) {
                    val data = deferred.await()
                    Log.d("TAG", "searchLocation: ${data?.size}")
                }
            } catch (e: IOException) {
                Log.e("TAG", "searchLocation: ${e.message}")
            }
        }
    }

    private fun getMyLocation() {
        if (!act.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            !act.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    storyViewModel.setMyLocation(location)
                    navigateToBack()
                } else {
                    act.showSnackBar(getString(R.string.text_message_location_not_found))
                }
            }
        }
    }

    private fun navigateToBack() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}