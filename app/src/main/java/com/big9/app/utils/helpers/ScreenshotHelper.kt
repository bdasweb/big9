package com.big9.app.utils.helpers

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScreenshotHelper(private val context: Context) {

    // Function to take a screenshot of a view
    fun takeScreenshot(view: View): File? {
        val bitmap = createBitmapFromView(view)

        // Save the bitmap to a file
        val screenshotFile = saveBitmapToFile(bitmap)

        return screenshotFile
    }

    // Function to create a bitmap from a view
    private fun createBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    // Function to save a bitmap to a file
    private fun saveBitmapToFile(bitmap: Bitmap): File? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "screenshot_$timestamp.png"
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        try {
            val file = File.createTempFile(fileName, ".png", storageDir)

            val outputStream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Add the screenshot to the gallery
            addToGallery(file)

            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    // Function to add the screenshot to the gallery
    private fun addToGallery(file: File) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, file.name)
            put(MediaStore.Images.Media.DESCRIPTION, "Screenshot")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.DATA, file.absolutePath)
        }

        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    // Function to share the screenshot file
    fun shareScreenshot(screenshotFile: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "com.big9.com.big9.app.fileprovider",
            screenshotFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooserIntent = Intent.createChooser(shareIntent, "Share Screenshot")
        context.startActivity(chooserIntent)
    }
}