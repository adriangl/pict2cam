package com.adriangl.image2camera.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.adriangl.image2camera.extensions.getImageCaptureOutputUri
import com.adriangl.image2camera.extensions.isImageCaptureAction
import com.adriangl.image2camera.extensions.use
import com.adriangl.image2camera.utils.isValidJpeg
import com.adriangl.image2camera.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**
 * Picker activity neede to receive the result of the media query to return it to the original
 * caller. This activity is translucent so the user doesn't perceive any artifacts.
 */
class ImagePickerActivity : AppCompatActivity() {

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 1234
        private const val INTENT_IMAGE_TYPE = "image/*"
        private const val CONTENT_SCHEME = "content"

        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 4321
    }

    private val outputUri: Uri? by lazy { intent.getImageCaptureOutputUri() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_picker_activity)

        // Remove animation from launcher, the default animation gives a weird effect in API 24+
        overridePendingTransition(0, 0)

        // Remove status tint; basically we're telling the activity's Window to expand above
        // the limits of the screen.
        // Check: http://stackoverflow.com/a/29311321
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setupImagePicker()
    }


    private fun setupImagePicker() {
        if (intent.isImageCaptureAction()) {
            if (outputUri != null) {
                if (requestPermissionIfNeeded()) {
                    pickImages()
                }
            } else {
                Toast.makeText(this, R.string.not_support_message, Toast.LENGTH_LONG).show()
                finish()
            }
        } else {
            // Not a valid intent, exit
            Toast.makeText(this, R.string.not_support_message, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun requestPermissionIfNeeded(): Boolean {
        // No need to request permissions if writing to a content:// Uri
        if (outputUri?.scheme == CONTENT_SCHEME) {
            return true
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                val builder = AlertDialog.Builder(this)
                        .setMessage(R.string.permission_write_external_message)
                        .setPositiveButton(R.string.confirm) { _, _ ->
                            ActivityCompat.requestPermissions(
                                    this@ImagePickerActivity,
                                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                            )
                        }
                builder.create().show()
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            return true
        }

        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE ->
                if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
                    pickImages()
                }
        }
    }

    private fun pickImages() {
        val intent = Intent().apply {
            type = INTENT_IMAGE_TYPE
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), PICK_IMAGE_REQUEST_CODE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val dialogFragment = ImageReadDialogFragment.newInstance()
                dialogFragment.show(supportFragmentManager, null)

                val result = runBlocking {
                    try {
                        if (saveImageToOutput(data.data!!, outputUri!!)) {
                            Activity.RESULT_OK
                        } else {
                            Activity.RESULT_CANCELED
                        }
                    } catch (e: IOException) {
                        Activity.RESULT_CANCELED
                    }
                }

                dialogFragment.dismiss()

                setResult(result)

            } else {
                setResult(Activity.RESULT_CANCELED)
            }

            finish()
        }
    }

    private suspend fun saveImageToOutput(source: Uri, des: Uri): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (hasValidJpegData(this@ImagePickerActivity, source)) {
                    // Plain old copy-paste to destination Uri
                    copyFile(this@ImagePickerActivity, source, des)
                } else {
                    // Convert image to JPEG and then copy
                    saveImageAsJpeg(this@ImagePickerActivity, source, des)
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun saveImageAsJpeg(context: Context, source: Uri, des: Uri): Boolean {
        val contentResolver = context.contentResolver

        return try {
            contentResolver.openInputStream(source)?.use { inputStream ->
                contentResolver.openOutputStream(des)?.use { outputStream ->
                    BitmapFactory.decodeStream(inputStream).use { bitmap ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun copyFile(context: Context, source: Uri, destination: Uri): Boolean {
        val contentResolver = context.contentResolver

        return try {
            contentResolver.openInputStream(source)?.use { inputStream ->
                contentResolver.openOutputStream(destination)?.use { outputStream ->
                    inputStream.copyTo(outputStream, 1024)
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun hasValidJpegData(context: Context, source: Uri): Boolean {
        val destinationFile = File(context.cacheDir, "tmp.bin")

        try {
            // Copy file to internal cache dir so we can examine it
            copyFile(context, source, destinationFile.toUri())

            // Examine its data to get if it's a JPEG
            return isValidJpeg(destinationFile)
        } finally {
            destinationFile.delete()
        }
    }
}
