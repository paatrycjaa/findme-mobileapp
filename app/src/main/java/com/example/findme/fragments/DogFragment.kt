package com.example.findme.fragments

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.findme.DogsViewModel
import com.example.findme.MainActivity
import com.example.findme.R
import com.example.findme.adapters.DogsAdapter
import com.example.findme.databinding.FragmentDogBinding
import com.example.findme.databinding.FragmentHomeBinding
import com.example.findme.models.Dog

class DogFragment : BaseFragment() {

    private var _binding: FragmentDogBinding? = null
    private val binding get() = _binding!!
    override var bottomNavigationViewVisibility = View.GONE

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDogBinding.inflate(inflater, container, false)

        val choosenDog = arguments?.getParcelable<Dog>("dog")
        val url = choosenDog?.image_url
        val imagePath = binding.dogImage
        Glide.with(this).load(url).into(imagePath)

        binding.petHome.text = choosenDog?.pet_home
        binding.date.text = choosenDog?.date
        binding.description.text = choosenDog?.description
        binding.gender.text = choosenDog?.gender
        binding.address.text = choosenDog?.address
        binding.city.text = choosenDog?.city
        // activity?.setTitle("Aktualności")
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.supportFragmentManager?.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

}

