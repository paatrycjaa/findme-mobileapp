package com.example.findme.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.findme.DogsViewModel
import com.example.findme.HomePetsViewModel
import com.example.findme.R
import com.example.findme.adapters.DogsAdapter
import com.example.findme.adapters.HomePetsAdapter
import com.example.findme.databinding.FragmentHomePetsBinding
import com.example.findme.models.Dog
import com.example.findme.models.HomePet

class HomePetsFragment : BaseFragment() {

    private var _binding: FragmentHomePetsBinding? = null
    private val binding get() = _binding!!
    private val adapter = HomePetsAdapter()
    private lateinit var viewModel: HomePetsViewModel
    override var bottomNavigationViewVisibility = View.VISIBLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.setTitle("Schroniska")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _binding = FragmentHomePetsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomePetsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("HomePetFragment", "On view created")
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewPetshome.adapter = adapter

        adapter.setOnItemClickListener(object : HomePetsAdapter.onHomePetItemClickListener {
            override fun onHomePetItemClicked(position: Int, homePet: HomePet) {
                val homepetfragment = HomePetsDetail()
                val args = Bundle()
                args.putParcelable("homepet", homePet)
                homepetfragment.arguments = args
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fl_wrapper, homepetfragment)
                    addToBackStack(null)
                    commit()
                }
            }
        })
        viewModel.homepet.observe(viewLifecycleOwner, {adapter.addHomePet(it)})
        viewModel.getRealtimeUpdate()

    }
}