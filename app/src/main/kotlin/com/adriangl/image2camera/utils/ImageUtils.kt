package com.adriangl.image2camera.utils

import java.io.File

/**
 * Check if the image inputStream a JPEG. JPEG image files begin with FF D8 and end
 * with FF D9
 */
@UseExperimental(ExperimentalUnsignedTypes::class)
fun isValidJpeg(file: File): Boolean {
    return try {
        val fileSize = file.length()

        val fileHeader = ByteArray(2)
        val fileFooter = ByteArray(2)

        file.inputStream().use { inputStream ->
            with(inputStream) {
                read(fileHeader, 0, 2)
                skip(fileSize - 4)
                read(fileFooter, 0, 2)
            }
        }

        val fileHeaderMatches = (fileHeader[0].toUInt() == 0xFF.toUInt()) && (fileHeader[1].toUInt() == 0xD8.toUInt())
        val fileFooterMatches = (fileFooter[0].toUInt() == 0xFF.toUInt()) && (fileFooter[1].toUInt() == 0xD9.toUInt())

        fileHeaderMatches && fileFooterMatches

    } catch (e: Exception) {
        false
    }
}