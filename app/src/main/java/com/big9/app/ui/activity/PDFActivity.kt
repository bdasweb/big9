package com.big9.app.ui.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.big9.app.R
import com.big9.app.databinding.ActivityPdfactivityBinding

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument

import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


import java.io.File
import java.io.FileOutputStream
@AndroidEntryPoint
class PDFActivity : AppCompatActivity() {
    lateinit var binding: ActivityPdfactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_pdfactivity)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pdfactivity)
        //val pdfGenerator = PdfGenerator()

        binding.test.setOnClickListener {
            generatePdfWithBackground(binding.test.context,"test52.pdf","hello")
            //PdfGenerator.generatePdfWithBackground()
           /* val fileName = "example.pdf"
            val content = "Hello, this is the content of the PDF file."

            pdfGenerator.generatePdf(fileName, content)*/
        }
    }

    fun generatePdfWithBackground(context: Context, fileName: String, content: String) {
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(filePath, fileName)

        try {

            val pdfWriter = PdfWriter(FileOutputStream(file))
            val pdfDocument = PdfDocument(pdfWriter)
            //val pdfDocument = PdfDocument(pdfWriter,PageSize(customPageSize))
            val document = Document(pdfDocument)

            // Load the drawable resource as a Bitmap
            val drawableId = R.drawable.ep_certificate // Replace with your drawable resource ID
            //val bitmap = (ContextCompat.getDrawable(context, drawableId) as BitmapDrawable).bitmap
            val bitmap = addTextToDrawable(context,drawableId,"Biswajit")

            // Convert the Bitmap to a byte array
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            // Create an Image object from the byte array
            val backgroundImage = Image(ImageDataFactory.create(byteArray))

            backgroundImage.scaleToFit(500F, 800f)
            document.add(backgroundImage)

            // Add content on top of the background
            document.add(Paragraph(content))

            document.close()

        } catch (e: java.io.IOException) {
            e.printStackTrace()
        }
    }

    fun addTextToDrawable(context: Context, drawableId: Int, text: String): Bitmap {
        // Load the drawable resource as a Bitmap
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        // Create a canvas to draw on the bitmap
        val canvas = Canvas(bitmap)

        // Create a paint object for drawing text
        val paint = Paint().apply {
            color = getColor(R.color.title_header2)  // Set text color
            textSize = 60f // Set text size
            textAlign = Paint.Align.CENTER
        }
        val paint2 = Paint().apply {
            color = getColor(R.color.black) // Set text color
            textSize = 40f // Set text size
            textAlign = Paint.Align.CENTER

        }


        // Calculate text position (centered)
        //val x = (bitmap.width - paint.measureText(text)) / 2
        val x = bitmap.width   / 2
        val y = bitmap.height / 2

        // Draw text on the canvas
        canvas.drawText(text, x.toFloat(), y.toFloat()+70, paint)
        canvas.drawText("Deb", x.toFloat(), y.toFloat()+130, paint2)

        return bitmap
    }
}




/*
class PdfGenerator {
companion object{
    fun generatePdf(fileName: String, content: String) {

        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(filePath, fileName)

        try {
            val pdfWriter = PdfWriter(FileOutputStream(file))
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            document.add(Paragraph(content))

            document.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

}*/
