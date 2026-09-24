package com.example.cargrasp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min

object BitmapUtils {

    private const val MAX_IMAGE_DIMENSION = 1024

    /**
     * Loads a downsampled, correctly-oriented Bitmap from an image Uri.
     */
    suspend fun decodeBitmapFromUri(context: Context, uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        try {
            // First decode bounds only to compute inSampleSize
            var input: InputStream? = context.contentResolver.openInputStream(uri) ?: return@withContext null
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(input, null, options)
            input.close()

            // Calculate sample size
            options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION)
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.ARGB_8888

            // Decode actual bitmap
            input = context.contentResolver.openInputStream(uri) ?: return@withContext null
            var decodedBitmap = BitmapFactory.decodeStream(input, null, options)
            input.close()

            if (decodedBitmap == null) return@withContext null

            // Correct EXIF orientation
            val orientation = getExifOrientation(context, uri)
            if (orientation != 0) {
                val matrix = Matrix().apply { postRotate(orientation.toFloat()) }
                val rotatedBitmap = Bitmap.createBitmap(
                    decodedBitmap, 0, 0, decodedBitmap.width, decodedBitmap.height, matrix, true
                )
                if (rotatedBitmap != decodedBitmap) {
                    decodedBitmap.recycle()
                    decodedBitmap = rotatedBitmap
                }
            }

            // If still larger than MAX_IMAGE_DIMENSION, scale smoothly
            val maxDim = max(decodedBitmap.width, decodedBitmap.height)
            if (maxDim > MAX_IMAGE_DIMENSION) {
                val scale = MAX_IMAGE_DIMENSION.toFloat() / maxDim
                val targetW = (decodedBitmap.width * scale).toInt()
                val targetH = (decodedBitmap.height * scale).toInt()
                val scaledBitmap = Bitmap.createScaledBitmap(decodedBitmap, targetW, targetH, true)
                if (scaledBitmap != decodedBitmap) {
                    decodedBitmap.recycle()
                    decodedBitmap = scaledBitmap
                }
            }

            decodedBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Scales and fixes orientation for an in-memory Bitmap.
     */
    fun optimizeBitmap(bitmap: Bitmap, maxDim: Int = MAX_IMAGE_DIMENSION): Bitmap {
        val currentMax = max(bitmap.width, bitmap.height)
        if (currentMax <= maxDim) return bitmap

        val scale = maxDim.toFloat() / currentMax
        val targetW = (bitmap.width * scale).toInt()
        val targetH = (bitmap.height * scale).toInt()
        return Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
    }

    /**
     * Reads EXIF orientation degrees from Uri.
     */
    private fun getExifOrientation(context: Context, uri: Uri): Int {
        return try {
            val input = context.contentResolver.openInputStream(uri) ?: return 0
            val exif = ExifInterface(input)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            input.close()

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Calculates power-of-two inSampleSize.
     */
    private fun calculateInSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * Saves a Bitmap to the app cache directory and returns the local file Uri.
     */
    fun saveBitmapToCache(context: Context, bitmap: Bitmap, fileName: String = "captured_car_${System.currentTimeMillis()}.jpg"): Uri? {
        return try {
            val cacheDir = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
            val file = File(cacheDir, fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
