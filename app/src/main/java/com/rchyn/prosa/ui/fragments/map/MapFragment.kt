package com.rchyn.prosa.ui.fragments.map


import android.Manifest
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.rchyn.prosa.R
import com.rchyn.prosa.components.LoadingDialog
import com.rchyn.prosa.databinding.FragmentMapBinding
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.ui.activities.MainActivity
import com.rchyn.prosa.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding as FragmentMapBinding

    private val mapViewModel: MapViewModel by viewModels()

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var act: MainActivity
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as MainActivity
        loadingDialog = LoadingDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = false
            isMapToolbarEnabled = true
            isTiltGesturesEnabled = true
        }

        setMapStyle(mMap)

        mapViewModel.storiesWithLocationState.observe(viewLifecycleOwner) { state ->
            when {
                state.isError -> {
                    loadingDialog.dismissLoading()
                    val msg = state.messageError?.let { uiText ->
                        when (uiText) {
                            is UiText.DynamicString ->
                                uiText.value
                            is UiText.StringResource ->
                                getString(uiText.id)
                        }
                    } ?: ""
                    act.showSnackBar(msg)
                }
                state.isLoading -> {
                    loadingDialog.startLoading()
                }
                else -> {
                    addStoryMarker(state.listStory, googleMap)
                    loadingDialog.dismissLoading()
                }
            }
        }

        setCameraMyLocation(mMap)
        mMap.setOnMarkerClickListener {
            val story = it.tag as Story
            navigateToStoryBottomDialog(story)
            true
        }
    }

    private fun addStoryMarker(
        stories: List<Story>,
        googleMap: GoogleMap
    ) {
        stories.forEach { story ->
            if (story.lat != null && story.lon != null) {

                val latLng = LatLng(story.lat, story.lon)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .icon(
                            vectorToBitmap(
                                R.drawable.ic_location_on,
                                requireContext().resolveColorAttr(android.R.attr.colorAccent),
                                resources
                            )
                        )
                )?.tag = story
            }
        }
    }

    private fun setCameraMyLocation(googleMap: GoogleMap) {
        if (!act.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            !act.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            googleMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude), 10f
                        )
                    )
                } else {
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(-2.3932797, 108.8507139), 5f
                        )
                    )
                }
            }
        }
    }

    private fun navigateToStoryBottomDialog(story: Story) {
        val directions = MapFragmentDirections.actionMapNavToStoryBottomSheetNav(story)
        findNavController().navigate(directions)
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        try {
            val success = act.theme?.let { value ->
                if (act.isDarkThemeOn() || (value == getString(R.string.dark_theme))) {
                    googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            requireContext(),
                            R.raw.map_style_dark
                        )
                    )
                } else {
                    googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            requireContext(),
                            R.raw.map_style_light
                        )
                    )
                }
            } ?: googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style_light
                )
            )
            if (!success) {
                act.showSnackBar(getString(R.string.text_message_error))
            }
        } catch (exception: Resources.NotFoundException) {
            act.showSnackBar(getString(R.string.text_message_error))
        }
    }
}

