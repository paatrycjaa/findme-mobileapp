package com.example.findme.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findme.DogsViewModel
import com.example.findme.MainActivity
import com.example.findme.R
import com.example.findme.adapters.DogsAdapter
import com.example.findme.adapters.DogsHomePetsAdapter2
import com.example.findme.databinding.FragmentHomePetsDogsBinding
import com.example.findme.models.Dog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePetsDogsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePetsDogsFragment : BaseFragment() {

    private var _binding: FragmentHomePetsDogsBinding? = null
    private val binding get() = _binding!!
    private val adapter = DogsHomePetsAdapter2()
    private lateinit var viewModel: DogsViewModel
    private var choosenHomePet = String()
    private var name = String()
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
        // Inflate the layout for this fragment
        _binding = FragmentHomePetsDogsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DogsViewModel::class.java]
        choosenHomePet = arguments?.getString("pethome").toString()
        name = arguments?.getString("name").toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("HomeFragment", "On view created")
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recyclerViewHomeDogs.layoutManager = linearLayoutManager

        binding.name.text= name
        binding.recyclerViewHomeDogs.adapter = adapter
        adapter.setOnItemClickListener(object : DogsHomePetsAdapter2.onDogItemClickListener {
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
        binding.recyclerViewHomeDogs.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        viewModel.dog.observe(viewLifecycleOwner, {adapter.addHomeDog(it,choosenHomePet)})
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