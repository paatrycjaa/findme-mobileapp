package com.example.findme.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findme.DogsViewModel
import com.example.findme.R
import com.example.findme.adapters.DogsAdapter
import com.example.findme.adapters.DogsAdapter.onDogItemClickListener
import com.example.findme.databinding.FragmentHomeBinding
import com.example.findme.models.Dog

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = DogsAdapter()
    private lateinit var viewModel: DogsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        activity?.setTitle("Aktualności")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        viewModel = ViewModelProvider(this).get(DogsViewModel::class.java)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("HomeFragment", "On view created")
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recyclerViewHome.layoutManager = linearLayoutManager

        binding.recyclerViewHome.adapter = adapter
        adapter.setOnItemClickListener(object : onDogItemClickListener{
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
        binding.recyclerViewHome.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        viewModel.dog.observe(viewLifecycleOwner, {adapter.addDog(it)})
        //viewModel.getRealtimeUpdate()
        viewModel.getRealtimeUpdateLimited(5)
    }

}