package com.example.findme.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findme.DogsViewModel
import com.example.findme.R
import com.example.findme.adapters.DogsAdapter
import com.example.findme.adapters.DogsHomePetsAdapter
import com.example.findme.databinding.FragmentHomeBinding
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
class HomePetsDogsFragment : Fragment() {

    private var _binding: FragmentHomePetsDogsBinding? = null
    private val binding get() = _binding!!
    private val adapter = DogsHomePetsAdapter()
    private lateinit var viewModel: DogsViewModel
    private var choosenHomePet = String()

    override fun onCreate(savedInstanceState: Bundle?) {
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("HomeFragment", "On view created")
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.reverseLayout = true
        binding.recyclerViewHomeDogs.layoutManager = gridLayoutManager

        binding.recyclerViewHomeDogs.adapter = adapter
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
        binding.recyclerViewHomeDogs.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        viewModel.dog.observe(viewLifecycleOwner, {adapter.addHomeDog(it,choosenHomePet)})
        viewModel.getRealtimeUpdate()
        //viewModel.getRealtimeUpdateLimited(5)
    }
}