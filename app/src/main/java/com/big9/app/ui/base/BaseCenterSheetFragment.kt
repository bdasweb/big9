package com.big9.app.ui.base

import android.R
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.utils.helpers.Constants
import java.io.File
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


open class BaseCenterSheetFragment: DialogFragment() {


    override fun getTheme(): Int {
        return R.style.Theme_NoTitleBar_Fullscreen
    }
    fun ImageView.back(){

        this.setOnClickListener{
            dismiss()
        }
    }

    fun ImageView.backToHome(){
        this.setOnClickListener{
            (activity as? DashboardActivity)?.navigate()

            //findNavController().popBackStack(R.id.homeFragment2,false)
        }
    }
    fun View.takeScreenshot(): Bitmap {
        // Create a Bitmap with the same dimensions as the View
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)

        // Create a Canvas to draw the View onto the Bitmap
        val canvas = Canvas(bitmap)

        // Draw the View onto the Canvas
        this.draw(canvas)

        return bitmap
    }

    // Function to save the screenshot to a file
    fun saveScreenshot(bitmap: Bitmap, file: File): Boolean {
        try {
            // Create a FileOutputStream to write the Bitmap to the file
            val outputStream = FileOutputStream(file)

            // Compress the Bitmap and write it to the file
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            // Close the FileOutputStream
            outputStream.close()

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun String.encrypt(): String {
        val fixedIV = if (Constants.AES_IV.length < 16) Constants.AES_IV + " ".repeat(16 - Constants.AES_IV.length) else Constants.AES_IV.substring(0, 16)

        val keySpec = SecretKeySpec(
            Constants.AES_KEY.toByteArray(Charsets.UTF_8),
            Constants.AES_ALGORITHM
        )
        val ivParameterSpec = IvParameterSpec(fixedIV.toByteArray(Charsets.UTF_8))

        val cipher = Cipher.getInstance(Constants.AES_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)

        val encryptedBytes = cipher.doFinal(this.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun String.decrypt(): String {
        val fixedIV = if (Constants.AES_IV.length < 13) Constants.AES_IV + " ".repeat(13 - Constants.AES_IV.length) else Constants.AES_IV.substring(0, 13)

        val keySpec = SecretKeySpec(
            Constants.AES_KEY.toByteArray(Charsets.UTF_8),
            Constants.AES_ALGORITHM
        )
        val ivParameterSpec = IvParameterSpec(fixedIV.toByteArray(Charsets.UTF_8))

        val cipher = Cipher.getInstance(Constants.AES_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)

        val encryptedBytes = Base64.decode(this, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }

}