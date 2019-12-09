package com.adriangl.image2camera.extensions

import android.graphics.Bitmap

/**
 * Uses the [Bitmap] to do operations over it and then recycles it.
 */
fun <T> Bitmap.use(fn: (Bitmap) -> T): T {
    try {
        return fn(this)
    } finally {
        this.recycle()
    }
}