package com.example.findme.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.findme.R
import com.example.findme.databinding.FragmentDialogBinding
import com.example.findme.databinding.FragmentDogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentDialogBinding? = null
    private val binding get() = _binding!!
    private var currentPhotoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.galleryIcon.setOnClickListener { chooseFromLibrary() }
        binding.aparatIcon.setOnClickListener { takePhoto() }
    }

    var launchCameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri = Uri.fromFile(currentPhotoFile)
            val searchFragment = SearchFragment()
            val args = Bundle()
            args.putParcelable("image", selectedImageUri)
            searchFragment.arguments = args
            makeCurrentFragment(searchFragment)
            dismiss()
        }
    }

    var launchGalleryActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri = data?.data ?: return@registerForActivityResult
            val searchFragment = SearchFragment()
            val args = Bundle()
            args.putParcelable("image", selectedImageUri)
            searchFragment.arguments = args
            makeCurrentFragment(searchFragment)
            dismiss()
        }
    }


    private fun chooseFromLibrary() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        // Only accept JPEG or PNG format.
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        //startActivityForResult(intent, REQUEST_PHOTO_LIBRARY)
        launchGalleryActivity.launch(intent)
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = requireActivity().cacheDir
        return createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents.
            currentPhotoFile = this
        }
    }

    private fun takePhoto() {
        Log.i(TAG,"Take photo function")
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFile()
            } catch (e: IOException) {
                    // Error occurred while creating the File.
                Log.e(TAG, "Unable to save image to run classification.", e)
                null
            }
            // Continue only if the File was successfully created.
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.findme.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                try {
                    //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    launchCameraActivity.launch(takePictureIntent)
                } catch ( e: ActivityNotFoundException){
                    Log.e(TAG, "Activity not found", e)
                }
            }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }


    companion object {

        /** Tag for the [Log].  */
        private const val TAG = "DialogFragment"

        /** Request code for starting photo capture activity  */
        private const val REQUEST_IMAGE_CAPTURE = 1

        /** Request code for starting photo library activity  */
        private const val REQUEST_PHOTO_LIBRARY = 2

    }

}