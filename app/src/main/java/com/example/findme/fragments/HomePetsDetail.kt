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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findme.DogsViewModel
import com.example.findme.HomePetsViewModel
import com.example.findme.R
import com.example.findme.adapters.DogsAdapter
import com.example.findme.adapters.DogsHomePetsAdapter
import com.example.findme.adapters.HomePetsAdapter
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
    private val adapter = DogsHomePetsAdapter()
    private lateinit var viewModel: DogsViewModel
    private var homepetname = String()

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
        viewModel = ViewModelProvider(this)[DogsViewModel::class.java]

        val choosenHomePet = arguments?.getParcelable<HomePet>("homepet")
        // activity?.setTitle("Aktualności")
        binding.address.text = choosenHomePet?.address
        binding.city.text = choosenHomePet?.city
        binding.name.text = choosenHomePet?.name
        binding.contact.text = choosenHomePet?.contact
        binding.web.text = choosenHomePet?.web
        homepetname = choosenHomePet?.name_link.toString()
        binding.fab.hide()

        binding.recyclerViewPetshome.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy:Int){
                super.onScrolled(recyclerView, dx, dy)

                if (dx > 10 && !binding.fab.isShown) {
                    binding.fab.show()
                }

                if (dx < -10 && binding.fab.isShown) {
                    binding.fab.hide()
                }
            }
        })

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.supportFragmentManager?.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerViewPetshome.adapter = adapter
        adapter.setOnItemClickListener(object : DogsHomePetsAdapter.onDogItemClickListener {
            override fun onDogItemClicked(position: Int, dog: Dog) {
                val dogfragment = DogFragment()
                val args = Bundle()
                args.putParcelable("dog", dog)
                dogfragment.arguments = args
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fl_wrapper, dogfragment)
                    addToBackStack(null)
                    commit()
                }
            }
        })
        binding.fab.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val dogsfragment = HomePetsDogsFragment()
                val args = Bundle()
                args.putString("pethome", homepetname)
                dogsfragment.arguments = args
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fl_wrapper, dogsfragment)
                    addToBackStack(null)
                    commit()
                }
            }
        })
        viewModel.dog.observe(viewLifecycleOwner, {adapter.addHomeDog(it, homepetname)})
        viewModel.getRealtimeUpdate()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val sydney = LatLng(-34.0, 151.0)
        mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


}

