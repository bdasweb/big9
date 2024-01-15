package com.big9.app.ui.receipt.newRecept


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.adapter.ReceiptAdapter
import com.big9.app.data.model.PrepaidMoboleTranspherModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentReceptDialogBinding

import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.ui.base.BaseCenterSheetFragment
import com.big9.app.utils.*
import com.big9.app.utils.helpers.Constants.recycleViewReceiptList
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PrePaidMobileReceptDialogFragment() : BaseCenterSheetFragment() {
    lateinit var binding: FragmentReceptDialogBinding
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recept_dialog, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {

        binding.apply {
            imgBack.back()
            imgHome.backToHome()
            fabShare.setOnClickListener{
                shareImage()
            }
          }
        }

    private fun shareImage() {

        activity?.let {
            binding.apply {
               // val screenshotHelper = ScreenshotHelper(binding.root.context)

// Get the root view of the activity or the view you want to capture
               // val rootView = it.window.decorView.rootView

// Take a screenshot
               // val screenshotFile = screenshotHelper.takeScreenshot(rootView)

// Check if the screenshot file is not null
                /*screenshotFile?.let {
                    // Share the screenshot
                    screenshotHelper.shareScreenshot(it)
                }*/


                 var screenshotBitmap =cardView2.takeScreenshot()
                (activity as? DashboardActivity)?.shareImage(screenshotBitmap)
                // val screenshotFile = File(cardView2.context.getExternalFilesDir(null), "screenshot.png")
                // saveScreenshot(screenshotBitmap, screenshotFile)
                //val bitmap = Bitmap.createBitmap(cardView2.width, cardView2.height, Bitmap.Config.ARGB_8888)

                //val canvas = Canvas(bitmap)

                //cardView2.draw(canvas)
                //shareBitmapWithoutSaving(bitmap,context)
                // shareBitmap(bitmap,cardView2.context)
            }
        }


    }

    private fun shareBitmapWithoutSaving(bitmap: Bitmap, context: Context?) {
        try {
            // Create a temporary file to store the bitmap
            val cachePath = File(context?.applicationContext?.cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "shared_image.png")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            // Create an Intent to share the file URI
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))

            // Start the activity for sharing
            startActivity(Intent.createChooser(shareIntent, "Share Image"))
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the exception (e.g., show an error message)
        }
    }

    fun shareBitmap(bitmap: Bitmap, context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        val uri: Uri = getImageUriFromBitmap(bitmap,context)
        Log.d("TAG_bitmap", "shareBitmap: ${uri.toFile()} ")
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        //startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap, context: Context): Uri {
        val cachePath = File(context.applicationContext.cacheDir, "images")
        cachePath.mkdirs() // don't forget to make the directory
        val stream = FileOutputStream("$cachePath/image.png") // overwrites this image every time
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
        return FileProvider.getUriForFile(context, "com.big9.com.big9.app.fileprovider", cachePath)
    }


    fun initView() {
        setCrdViewMinHeight()
        binding.recycleViewReceiptDetails.apply {

            adapter= ReceiptAdapter(recycleViewReceiptList)
        }
    }

    private fun setCrdViewMinHeight() {

        binding.cardView2.apply {
            // Get the screen height
            val screenHeight = resources.displayMetrics.heightPixels

            // Calculate 50% of the screen height
            val minHeight = (screenHeight * 0.7).toInt()


            var layoutParams1 = layoutParams
            layoutParams1.height = minHeight
            //layoutParams = layoutParams
        }

    }

    fun setObserver() {
        binding.apply {

        }

    }

    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String): Bitmap? {
        val writer = QRCodeWriter()
        val bitMatrix: BitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, 400, 400)
        val w: Int = bitMatrix.getWidth()
        val h: Int = bitMatrix.getHeight()
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                pixels[y * w + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }
}