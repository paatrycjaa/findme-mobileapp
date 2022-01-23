package com.example.findme.fragments

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.findme.R
import com.example.findme.databinding.FragmentDialogBinding
import com.example.findme.databinding.FragmentSearchBinding
import com.example.findme.models.Dog
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        activity?.setTitle("Szukaj pupila")
        val selectedImageUri = arguments?.getParcelable<Uri>("image")
        if (selectedImageUri != null) {
            FirebaseVisionImage.fromFilePath(requireContext(), selectedImageUri).also {
                binding.image.setImageBitmap(it.bitmap)
            }

        }
        return binding.root
    }

    private fun classifyImage (bitmap : Bitmap){

    }
}