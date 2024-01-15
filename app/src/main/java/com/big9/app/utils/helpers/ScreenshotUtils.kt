package com.big9.app.utils.helpers

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScreenshotUtils {

    companion object {

        fun takeScreenshot(context: Context, screenshotBitmap: Bitmap) {
            // Save the captured image to a file
            val imagePath = saveBitmapToFile(context, screenshotBitmap)
            //Toast.makeText(context, ""+imagePath, Toast.LENGTH_SHORT).show()
            // Share the captured image using an Intent
            shareScreenshot(context, imagePath)
        }

        private fun saveBitmapToFile(context: Context, bitmap: Bitmap): String {
            /*val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "screenshot_$timeStamp.png"

            val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Screenshots")

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val filePath = File(directory, fileName).absolutePath

            try {
                FileOutputStream(filePath).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                Log.d("ScreenshotUtils", "Screenshot saved: $filePath")
            } catch (e: IOException) {
                Log.e("ScreenshotUtils", "Error saving screenshot: ${e.message}", e)
            }

            // Add the image to the system's MediaStore to make it accessible by other apps
            MediaStore.Files.Media.insertImage(
                context.contentResolver,
                filePath,
                fileName,
                "Screenshot"
            )

            return filePath*/

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "screenshot_$timeStamp.png"

            val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Screenshots")

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val filePath = File(directory, fileName).absolutePath

            if (saveBitmapToFile2(bitmap, filePath)) {
                Log.d("ScreenshotUtils", "Screenshot saved: $filePath")
                return filePath
            } else {
                Log.e("ScreenshotUtils", "Error saving screenshot")
                // You might want to handle this case accordingly in your app
                return ""
            }

        }

        fun saveBitmapToFile2(bitmap: Bitmap, filePath: String): Boolean {
            return try {
                val file = File(filePath)
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        private fun shareScreenshot(context: Context, imagePath: String) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            val imageUri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                File(imagePath)
            )
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(Intent.createChooser(shareIntent, "Share Screenshot"))
        }
    }
}