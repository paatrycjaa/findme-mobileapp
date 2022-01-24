package com.example.findme.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.findme.MainActivity

abstract class BaseFragment : Fragment() {

    protected open var bottomNavigationViewVisibility = View.VISIBLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get the reference of the parent activity and call the setBottomNavigationVisibility method.
        if (activity is MainActivity) {
            var  mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }
    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            var  mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }
}