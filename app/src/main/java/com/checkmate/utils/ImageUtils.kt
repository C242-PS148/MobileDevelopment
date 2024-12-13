package com.checkmate.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.core.content.ContentProviderCompat.requireContext
import java.io.File
import java.io.IOException

class ImageUtils {
    companion object{
        fun imageProxyToBitmap(image: ImageProxy): Bitmap {
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
        fun saveBitmapToFile(ctx: Context, bitmap: Bitmap): File {
            val cacheDir = ctx.cacheDir
            val imageFile = File(cacheDir, "captured_image_${System.currentTimeMillis()}.jpg")

            val outputStream = imageFile.outputStream()
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
            } finally {
                outputStream.close()
            }

            return imageFile
        }

        fun getFileFromUri(ctx: Context, uri: Uri): File? {
            val contentResolver = ctx.contentResolver
            val file = File(ctx.cacheDir, "selected_image_${System.currentTimeMillis()}.jpg")

            try {
                val inputStream = contentResolver.openInputStream(uri) ?: return null
                val outputStream = file.outputStream()

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

            return file
        }
    }
}