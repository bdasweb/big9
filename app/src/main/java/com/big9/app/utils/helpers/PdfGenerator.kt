package com.big9.app.utils.helpers

class PdfGenerator {

    /*fun generatePdfWithBackground(context: Context, fileName: String, content: String) {
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(filePath, fileName)

        try {
            val pdfWriter = PdfWriter(FileOutputStream(file))
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            // Load the drawable resource as a Bitmap
            val drawableId = R.drawable.ep_certificate // Replace with your drawable resource ID
            val bitmap = (ContextCompat.getDrawable(context, drawableId) as BitmapDrawable).bitmap

            // Create an Image object from the Bitmap
            val backgroundImage = Image(ImageDataFactory.create(bitmap, ImageDataFactory.getImageBytesForRawBytes(bitmapToByteArray(bitmap))))

            backgroundImage.scaleToFit(pdfDocument.defaultPageSize.width, pdfDocument.defaultPageSize.height)
            document.add(backgroundImage)

            // Add content on top of the background
            document.add(Paragraph(content))

            document.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }*/



}