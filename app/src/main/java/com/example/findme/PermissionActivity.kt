package com.example.findme

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class PermissionActivity : AppCompatActivity(),
    ActivityCompat.OnRequestPermissionsResultCallback {

        private val requiredPermissions: Array<String> by lazy {
            try {
                this.packageManager.getPackageInfo(
                    this.packageName,
                    PackageManager.GET_PERMISSIONS
                ).requestedPermissions ?: arrayOf()
            } catch (e: PackageManager.NameNotFoundException) {
                arrayOf<String>()
            }
        }

        private fun allPermissionsGranted() = requiredPermissions.none { !isPermissionGranted(it) }

        private fun requestRuntimePermissions() {
            val allNeededPermissions = requiredPermissions.filter { !isPermissionGranted(it) }

            if (allNeededPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    this,
                    allNeededPermissions.toTypedArray(),
                    PERMISSION_REQUESTS
                )
            }
        }

        private fun isPermissionGranted(permission: String): Boolean {
            when (ContextCompat.checkSelfPermission(this, permission)) {
                PackageManager.PERMISSION_GRANTED -> {
                    Log.i(TAG, "Permission granted: $permission")
                    return true
                }
                else -> {
                    Log.i(TAG, "Permission NOT granted: $permission")
                    return false
                }
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            if (!allPermissionsGranted()) {
                requestRuntimePermissions()
            }
        }

        companion object {

            /** Tag for the [Log].  */
            private const val TAG = "PermissionActivity"

            private const val PERMISSION_REQUESTS = 1

        }
}