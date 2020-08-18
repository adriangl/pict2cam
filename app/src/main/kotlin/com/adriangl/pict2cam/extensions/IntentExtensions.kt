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

package com.adriangl.pict2cam.extensions

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

/**
 * Check if the intent is an image capture one, so the app can get it.
 */
fun Intent.isImageCaptureAction(): Boolean = action == MediaStore.ACTION_IMAGE_CAPTURE

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