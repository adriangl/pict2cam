package com.adriangl.image2camera.extensions

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun Intent.isImageCaptureAction(): Boolean {
    return action == MediaStore.ACTION_IMAGE_CAPTURE
}

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