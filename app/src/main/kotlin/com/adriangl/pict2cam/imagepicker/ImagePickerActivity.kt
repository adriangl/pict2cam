/*
 * Copyright 2020 Adrián García
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adriangl.pict2cam.imagepicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adriangl.pict2cam.R
import com.adriangl.pict2cam.extensions.getImageCaptureOutputUri
import com.adriangl.pict2cam.extensions.isImageCaptureAction
import com.adriangl.pict2cam.utils.ImageConstants
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE

/**
 * Picker activity needed to receive the result of the media query to return it to the original
 * caller. This activity is translucent so the user doesn't perceive any artifacts.
 */
class ImagePickerActivity : AppCompatActivity() {

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 1234
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 4321

        private const val INTENT_IMAGE_TYPE = "image/*"
        private const val CONTENT_SCHEME = "content"
    }

    private val outputUri: Uri? by lazy { intent.getImageCaptureOutputUri() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImagePickerScreen()
        }

        // Remove animation from launcher, the default animation gives a weird effect in API 24+
        overridePendingTransition(0, 0)

        // Remove status tint; basically we're telling the activity's Window to expand above
        // the limits of the screen.
        // Check: http://stackoverflow.com/a/29311321
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setupImagePicker()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    PICK_IMAGE_REQUEST_CODE -> {
                        if (data != null) {
                            // Call cropping library
                            openImageCropper(data.data!!, outputUri!!)
                        } else {
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        }
                    }

                    CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                        val resultUri = CropImage.getActivityResult(data)?.uri

                        if (resultUri == null) {
                            setResult(Activity.RESULT_CANCELED)
                        } else {
                            setResult(Activity.RESULT_OK)
                        }
                        finish()
                    }
                }
            }
            CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                Toast.makeText(this, R.string.crop_image_activity_result_error, Toast.LENGTH_LONG)
                    .show()
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
            else -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE ->
                if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
                    showImagePickerChooser()
                }
        }
    }

    private fun setupImagePicker() {
        if (intent.isImageCaptureAction()) {
            if (outputUri != null) {
                if (requestWriteExternalStoragePermission()) {
                    showImagePickerChooser()
                }
            } else {
                // Output URI was not provided by the calling app, so exit.
                Toast.makeText(this, R.string.image_picker_invalid_uri_error, Toast.LENGTH_LONG).show()
                finish()
            }
        } else {
            // Not a valid intent, exit
            Toast.makeText(this, R.string.image_picker_invalid_intent_error, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun requestWriteExternalStoragePermission(): Boolean {
        // No need to request permissions if writing to a content:// Uri
        if (outputUri?.scheme == CONTENT_SCHEME) return true

        if (!isWriteExternalPermissionGranted()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) {
                AlertDialog.Builder(this)
                    .setMessage(R.string.image_picker_write_external_rationale_message)
                    .setPositiveButton(
                    R.string.image_picker_write_external_rationale_action_allow
                ) { _, _ ->
                        ActivityCompat.requestPermissions(
                            this@ImagePickerActivity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                        )
                    }
                    .show()

                return false
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                )

                return false
            }
        } else {
            return true
        }
    }

    private fun showImagePickerChooser() {
        val intent = Intent().apply {
            type = INTENT_IMAGE_TYPE
            action = Intent.ACTION_GET_CONTENT
        }

        @Suppress("DEPRECATION")
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.image_picker_chooser_title)),
            PICK_IMAGE_REQUEST_CODE
        )
    }

    private fun openImageCropper(imageUri: Uri, outputUri: Uri) {
        CropImage.activity(imageUri)
            // Set crop to full size by default
            .setInitialCropWindowPaddingRatio(0f)
            // Image quality
            .setOutputCompressFormat(ImageConstants.DEFAULT_COMPRESS_FORMAT)
            .setOutputCompressQuality(ImageConstants.DEFAULT_IMAGE_QUALITY)
            .setOutputUri(outputUri)
            .start(this)
    }

    private fun isWriteExternalPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
}
