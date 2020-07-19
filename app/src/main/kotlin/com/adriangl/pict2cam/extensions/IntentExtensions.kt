package com.adriangl.pict2cam.extensions

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

/**
 * Check if the intent is an image capture one, so the app can get it.
 */
fun Intent.isImageCaptureAction(): Boolean {
    return action == MediaStore.ACTION_IMAGE_CAPTURE
}

/**
 * Returns the camera Uri to write to in an API-level agnostic way.
 */
fun Intent.getImageCaptureOutputUri(): Uri? {
    if (!isImageCaptureAction()) return null

    var outputUri: Uri? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        clipData?.let { clipData ->
            outputUri = clipData.getItemAt(0).uri
        }
    }

    // Compatibility for system below Lollipop, try to get the Uri by other means
    if (outputUri == null) {
        extras?.let { extras ->
            outputUri = extras.getParcelable(MediaStore.EXTRA_OUTPUT)
        }
    }

    return outputUri
}