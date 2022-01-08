package com.example.findme.fragments

import android.os.Bundle
import android.text.Html
import android.text.Html.fromHtml
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.findme.R
import com.example.findme.databinding.FragmentDogBinding
import com.example.findme.databinding.FragmentHomePetsDetailBinding
import com.example.findme.models.Dog
import com.example.findme.models.HomePet
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomePetsDetail : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomePetsDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        binding.googleMap.onCreate(savedInstanceState)
//        binding.googleMap.onResume()
//        binding.googleMap.getMapAsync(this)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomePetsDetailBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager
            ?.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val choosenHomePet = arguments?.getParcelable<HomePet>("homepet")
        // activity?.setTitle("Aktualności")
        binding.address.text = choosenHomePet?.address
        binding.city.text = choosenHomePet?.city
        binding.name.text = choosenHomePet?.name
        binding.contact.text = choosenHomePet?.contact
        binding.web.text = choosenHomePet?.web

//        binding.googleMap.onCreate(savedInstanceState)
//        binding.googleMap.onResume()
//        binding.googleMap.getMapAsync(this)

        //binding.web.text = choosenHomePet?.web?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY ) }
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.supportFragmentManager?.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val sydney = LatLng(-34.0, 151.0)
        mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


}