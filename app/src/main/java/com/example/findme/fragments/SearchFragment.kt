package com.example.findme.fragments

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findme.DogsViewModel
import com.example.findme.MainActivity
import com.example.findme.R
import com.example.findme.adapters.DogsAdapter
import com.example.findme.adapters.DogsHomePetsAdapter
import com.example.findme.databinding.FragmentDialogBinding
import com.example.findme.databinding.FragmentSearchBinding
import com.example.findme.models.Dog
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.lang.Exception

class SearchFragment : BaseFragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val adapter = DogsAdapter()
    private lateinit var viewModel: DogsViewModel
    private val dogstype = mutableListOf<String>()
    override var bottomNavigationViewVisibility = View.GONE

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        (activity as MainActivity).setBottomNavigationVisibility(View.INVISIBLE)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.textResult.isVisible = false
        activity?.setTitle("Szukaj pupila")
        val selectedImageUri = arguments?.getParcelable<Uri>("image")
        if (selectedImageUri != null) {
            FirebaseVisionImage.fromFilePath(requireContext(), selectedImageUri).also {
                binding.image.setImageBitmap(it.bitmap)
                classifyImage(it.bitmap)
            }
        }
        viewModel = ViewModelProvider(this).get(DogsViewModel::class.java)
        return binding.root
    }

    private fun classifyImage (bitmap : Bitmap){
        if ((activity as MainActivity).classifier == null) {
            binding.textResult.isVisible = true
            binding.textResult.text = "Uninitialized Classifier or invalid context."
            return
        }

        (activity as MainActivity).classifier?.classifyFrame(bitmap)?.
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val types = task.result.split("\n") as MutableList<String>
                for (type in types){
                    if(type.equals("brak")){
                        binding.textResult.isVisible = true
                        binding.textResult.text = "Przykro nam, ale żadno ze zwierząt nie jest wystarczająco podobne do Twojego pupila :( "
                    }
                    dogstype.add(type)
                }
                try {
                    viewModel.dog.observe(viewLifecycleOwner, { adapter.addDogType(it, dogstype) })
                    viewModel.getRealtimeUpdate()
                }
                catch (e: Exception){}
            } else {
                val e = task.exception
                Log.e("Search Fragment", "Error classifying frame", e)
                binding.textResult.isVisible = true
                binding.textResult.text = e?.message
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("HomeFragment", "On view created")
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        //linearLayoutManager.reverseLayout = true
        //linearLayoutManager.stackFromEnd = true
        binding.recyclerViewDog.layoutManager = linearLayoutManager

        binding.recyclerViewDog.adapter = adapter
        adapter.setOnItemClickListener(object : DogsAdapter.onDogItemClickListener {
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
        binding.recyclerViewDog.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        Log.i("Search Fragment",dogstype.toString())
        viewModel.dog.observe(viewLifecycleOwner, {adapter.addDogType(it,dogstype)})
        //viewModel.dog.observe(viewLifecycleOwner, {adapter.addDog(it)})
        viewModel.getRealtimeUpdate()
        //viewModel.getRealtimeUpdateLimited(5)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.supportFragmentManager?.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }
}