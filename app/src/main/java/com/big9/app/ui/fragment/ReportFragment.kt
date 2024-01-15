package com.big9.app.ui.fragment


import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.R
import com.big9.app.adapter.reportAdapter.ReportAdapter
import com.big9.app.data.model.ReportModel
import com.big9.app.data.model.ReportPropertyModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentReportBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.newReportList
import com.big9.app.utils.helpers.Constants.reportAdapter
import com.big9.app.utils.helpers.Constants.reportDetailsAdapter
import com.big9.app.utils.helpers.Constants.reportDetailsPropertyList
import com.big9.app.utils.helpers.Constants.reportList
import com.big9.app.utils.`interface`.CallBack
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ReportFragment : BaseFragment()  {
    lateinit var binding: FragmentReportBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var lastClickTime1: Long = 0
    private var lastClickTime2: Long = 0
    private var lastClickTime3: Long = 0
    private val LINES_PER_PAGE = 30
    var isAsintask=true
    var printList = ArrayList<String>()
    private val myViewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    var startDate=""
    var endDate=""
    var startIndex = 20
    var endIndex = 30
    var isTopAsink=true
    private lateinit var recyclerView: RecyclerView
    var isScrollingLoaderShowing=false
    var isConfirmCall=true
    //private lateinit var tableViewModel: TableViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)




       // tableViewModel = ViewModelProvider(this)[TableViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observer()
        onViewClick()
    }

    override fun onResume() {
        super.onResume()
        //reportList?.clear()

    }

    fun clearAllData(){
        //reportList?.clear()
        reportAdapter?.let {
           // binding.bottomLoader.visibility=View.GONE
            //reportList.clear()
            //newReportList.clear()
            it.items=ArrayList()
            it.notifyDataSetChanged()
        }
    }




    private fun generateAndSaveImagesInBatches(printList: String) {
      /*  val totalImages = printList.size
        var startIndex = 0*/

        // Create a directory to save the images in the Downloads directory
       /* val imagesDir = File(binding.root.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "big9")
        imagesDir.mkdirs()*/


        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, "big9")
        file.mkdirs()

        generateAndSaveImages(printList, file)

      /*  while (startIndex < totalImages) {
            val endIndex = (startIndex + batchCount).coerceAtMost(totalImages)
            val batchList = printList.subList(startIndex, endIndex)

            // Generate and save images for the current batch
            generateAndSaveImages(batchList, file)

            startIndex += batchCount
        }*/

        Toast.makeText(binding.root.context, "Report save to download folder", Toast.LENGTH_SHORT).show()
    }

    private fun generateAndSaveImages(data: String, outputDir: File) {
        val fileName = "report_${System.currentTimeMillis()}.pdf" // Create a unique file name
        val file = File(outputDir, fileName)
        saveToPdf(data, file)
        //createPdfWithCanvas(data, file)
        Log.d("TAG_pdf_file", "generateAndSaveImages: "+file)
       /* for ((index, item) in data.withIndex()) {
            // Generate a bitmap image for each item in the list
            val bitmap = generateBitmapFromText(data)

            // Save the bitmap to a file
            val fileName = "report_$index.jpg"
            val file = File(outputDir, fileName)
            saveBitmapToFile(data, file)
        }*/



       /* val data = """
            $data
        """.trimIndent()*/



       // saveToPdf(data, file)



    }

    private fun generateBitmapFromText(text: String): Bitmap {
        // Create a bitmap with a white background
        val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // Draw the text on the bitmap
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        canvas.drawText(text, 10f, 100f, paint)

        return bitmap
    }
//
//    private fun saveToPdf(data: String, file: File) {
//        val outputStream: OutputStream = FileOutputStream(file)
//        val pdfDocument = PdfDocument()
//        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
//        val page = pdfDocument.startPage(pageInfo)
//        val canvas = page.canvas
//        val paint = Paint()
//        paint.color = Color.BLACK
//        paint.textSize = 12f
//
//        val textLines = data.split("\n")
//        var yPos = 50f
//        val lineHeight = 20f // Adjust this value based on your requirements for line spacing
//
//        for (line in textLines) {
//            val textBounds = Rect()
//            paint.getTextBounds(line, 0, line.length, textBounds)
//            val textHeight = textBounds.height().toFloat()
//
//            if (yPos + textHeight < pageInfo.pageHeight) {
//                canvas.drawText(line, 80f, yPos + textHeight, paint)
//                yPos += lineHeight + textHeight
//            } else {
//                // Create a new page if the current page is full
//                pdfDocument.finishPage(page)
//                val newPage = pdfDocument.startPage(pageInfo)
//                val newCanvas = newPage.canvas
//                newCanvas.drawText(line, 80f, yPos, paint)
//                yPos = lineHeight + textHeight
//            }
//        }
//
//        pdfDocument.finishPage(page)
//        pdfDocument.writeTo(outputStream)
//        pdfDocument.close()
//        outputStream.close()
//        loader?.dismiss()
//    }
fun createPdfWithCanvas(data: String, file: File,) {
    // Create a new PdfDocument
    val pdfDocument = PdfDocument()

    // Create a PageInfo object with the desired page attributes
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

    // Start a new page
    val page = pdfDocument.startPage(pageInfo)

    // Get the Canvas object from the page
    val canvas = page.canvas

    // Create a Paint object for drawing text
    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 12f
    }

    // Draw text on the Canvas from the printList
    var yPos = 50f
    for (print in printList) {
        canvas.drawText(print, 80f, yPos, paint)
        yPos += 20f // Adjust the vertical spacing between lines
    }

    // Finish the page
    pdfDocument.finishPage(page)

    // Create a file to save the PDF
    //val outputStream: OutputStream = FileOutputStream(file)

    // Write the PDF content to the file
    FileOutputStream(file).use { outputStream ->
        pdfDocument.writeTo(outputStream)
    }

    // Close the PdfDocument
    pdfDocument.close()
}
    /*private fun saveToPdf(data: String, file: File) {
        val outputStream: OutputStream = FileOutputStream(file)
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val lines = data.lines()
        var pageIndex = 0

        while (pageIndex * LINES_PER_PAGE < lines.size) {
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()
            paint.color = Color.BLACK
            paint.textSize = 12f

            // Calculate the visible portion of data for this page
            val startIndex = pageIndex * LINES_PER_PAGE
            val endIndex = minOf((pageIndex + 1) * LINES_PER_PAGE, lines.size)
            val visibleData = lines.subList(startIndex, endIndex).joinToString("\n")

            // Draw the visible data on the page
            var yPos = 50f // Adjust the starting position for the content
            val lineHeight = 20f // Adjust this value based on your requirements for line spacing
            val textLines = visibleData.split("\n")
            for (line in textLines) {
                canvas.drawText(line, 80f, yPos, paint)
                yPos += lineHeight
            }

            pdfDocument.finishPage(page)
            pageIndex++
        }

        // Write the PDF document to the output stream
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()
        loader?.dismiss()
    }*/


    private fun saveToPdf(data: String, file: File) {
        val outputStream: OutputStream = FileOutputStream(file)
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val lines = data.lines()
        var pageIndex = 0

        while (pageIndex * LINES_PER_PAGE < lines.size) {
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()
            paint.color = Color.BLACK
            paint.textSize = 12f

            // Calculate the visible portion of data for this page
            val startIndex = pageIndex * LINES_PER_PAGE
            val endIndex = minOf((pageIndex + 1) * LINES_PER_PAGE, lines.size)
            val visibleData = lines.subList(startIndex, endIndex).joinToString("\n")

            // Draw the visible data on the page
            var yPos = 50f // Adjust the starting position for the content
            val lineHeight = 20f // Adjust this value based on your requirements for line spacing
            val textLines = visibleData.split("\n")
            for (line in textLines) {
                canvas.drawText(line, 80f, yPos, paint)
                yPos += lineHeight
            }

            pdfDocument.finishPage(page)
            pageIndex++
        }

        // Write the PDF document to the output stream
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()
    }

    /*private fun saveToPdf(data: String, file: File) {
        val outputStream: OutputStream = FileOutputStream(file)
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 12f





        // Draw the title at the top of the page
        val titleBounds = Rect()
        paint.getTextBounds("Sample", 0, "Sample".length, titleBounds)
        val titleHeight = titleBounds.height().toFloat()
        canvas.drawText("Sample", 80f, 50f, paint)


        val textLines = data.split("\n")
        var yPos = 10f
        for (line in textLines) {
            canvas.drawText(line, 10f, yPos, paint)
            yPos += 20f
        }
        pdfDocument.finishPage(page)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()
        loader?.dismiss()
    }*/

   /* private fun saveToPdf(title: String, content: String, file: File) {
        val outputStream: OutputStream = FileOutputStream(file)
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points (72 points = 1 inch)
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 12f

        // Draw the title at the top of the page
        val titleBounds = Rect()
        paint.getTextBounds(title, 0, title.length, titleBounds)
        val titleHeight = titleBounds.height().toFloat()
        canvas.drawText(title, 80f, 50f, paint)

        // Draw the content below the title
        val contentLines = content.split("\n")
        var yPos = 100f // Adjust the starting position for the content
        val lineHeight = 20f // Adjust this value based on your requirements for line spacing

        for (line in contentLines) {
            val textBounds = Rect()
            paint.getTextBounds(line, 0, line.length, textBounds)
            val textHeight = textBounds.height().toFloat()

            if (yPos + textHeight < pageInfo.pageHeight) {
                canvas.drawText(line, 80f, yPos + textHeight, paint)
                yPos += lineHeight + textHeight
            } else {
                // Create a new page if the current page is full
                pdfDocument.finishPage(page)
                val newPage = pdfDocument.startPage(pageInfo)
                val newCanvas = newPage.canvas
                newCanvas.drawText(line, 80f, yPos, paint)
                yPos = lineHeight + textHeight
            }
        }

        // Finish the page and write the PDF content to the file
        pdfDocument.finishPage(page)
        pdfDocument.writeTo(outputStream)

        // Close the document and the output stream
        pdfDocument.close()
        outputStream.close()
        loader?.dismiss()
    }*/
    /*private fun saveToPdf(data: String, file: File) {
        val outputStream: OutputStream = FileOutputStream(file)
       // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
       // outputStream.write(data.toByteArray())
       // outputStream.close()
        loader?.dismiss()
        Log.d("TAG_pdf_data", "saveBitmapToFile: \n"+data)
        // Create a new PdfDocument
        val pdfDocument = PdfDocument()

        // Create a PageInfo object with the desired page attributes
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()

        // Start a new page
        val page = pdfDocument.startPage(pageInfo)

        // Create a Canvas object from the page
        val canvas = page.canvas

        // Add text to the page
        val text = data
        val paint = Paint()
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawPaint(paint)

        paint.color = Color.BLACK
        paint.textSize = 20f

        canvas.drawText(text, 80f, 50f,paint )

        // Finish the page
        pdfDocument.finishPage(page)

        // Create a file to save the PDF
        //val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "sample.pdf")

        // Write the PDF content to the file
        FileOutputStream(file).use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }

        // Close the PdfDocument
        pdfDocument.close()


    }*/

    private fun onViewClick() {

        binding.apply {
            tvDownload.setOnClickListener {

                if(printList!=null){
                    loader?.show()
                    var data=""
                    printList.forEach{
                        data="\n"+data+"\n"+  it+"\n\n"
                    }
                    Log.d("TAG_rawData", "onViewClick: "+data)
                    generateAndSaveImagesInBatches(data)
                    //writeArrayToFile(tvDownload.context,"report.ixt",printList)
                }

            }



          imgBack.setOnClickListener{
              reportList?.clear()
              printList.clear()
              reportAdapter?.let {
                 // binding.bottomLoader.visibility=View.GONE
                  reportList.clear()
                  newReportList.clear()
                  it.items=ArrayList()
                  it.notifyDataSetChanged()
              }
              findNavController().popBackStack()
          }
          //imgBack.back()

          tvStartDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.startDate?.value=s
                    }

                })
            }

            tvEndDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.enddate?.value=s
                    }

                })
            }

            tvConfirm.setOnClickListener{
                /*if(isConfirmCall){
                    isConfirmCall=false*/
                tvConfirm.setBottonLoader(false,llLoader)

                    startIndex = 0
                    endIndex = 20
                    binding.loaderBottom.visibility = View.GONE
                    //binding.btnHasdata.visibility = View.GONE
                    reportAdapter?.let {
                        reportList.clear()
                        newReportList.clear()
                        it.items=ArrayList()
                        it.notifyDataSetChanged()
                    }
                    getAllData()
                //}

            }


        }
    }

  /*  private fun generateAndSaveImagesInBatches(printList: List<String>) {
        val totalImages = printList.size
        var startIndex = 0

        // Create a directory to save the images
        val imagesDir = File(binding.root.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "big9")
        imagesDir.mkdirs()

        while (startIndex < totalImages) {
            val endIndex = (startIndex + batchCount).coerceAtMost(totalImages)
            val batchList = printList.subList(startIndex, endIndex)

            // Generate and save images for the current batch
            generateAndSaveImages(batchList, imagesDir)

            startIndex += batchCount
        }

        Toast.makeText(binding.root.context, "Images generated and saved successfully", Toast.LENGTH_SHORT).show()
    }


    private fun generateAndSaveImages(data: List<String>, outputDir: File) {
        for ((index, item) in data.withIndex()) {
            // Generate a bitmap image for each item in the list
            val bitmap = generateBitmapFromText(item)

            // Save the bitmap to a file
            val fileName = "image_$index.jpg"
            val file = File(outputDir, fileName)
            saveBitmapToFile(bitmap, file)
        }
    }

    private fun generateBitmapFromText(text: String): Bitmap {
        // Create a bitmap with a white background
        val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // Draw the text on the bitmap
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        canvas.drawText(text, 10f, 100f, paint)

        return bitmap
    }

     fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        val outputStream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
    }*/
    fun initView() {

        reportDetailsPropertyList = ArrayList()
        reportDetailsAdapter?.items= ArrayList()
        reportDetailsAdapter?.notifyDataSetChanged()

        startIndex = 0
        endIndex = 20
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

        }
        viewModel?.apply {
            startDate.value ="".currentdate()
            enddate.value="".currentdate()
        }

        initRecycleView()
        Handler(Looper.getMainLooper()).postDelayed({
            reportAdapter?.let {
                reportList.clear()
                newReportList.clear()
                it.items=ArrayList()
                it.notifyDataSetChanged()
            }
            getAllData()
        },100)

        backPressed()
    }

    private fun initRecycleView() {
        binding.recycleViewReport.apply {
            recyclerView = this
            reportAdapter = ReportAdapter(ReportPropertyModel(""),ArrayList(),  object : CallBack {
                override fun getValue(s: String) {
                    viewModel.reportTypeIDRecept.value=s
                    /*val bundle = Bundle()
                    bundle.putString("jsonData", s)*/
                    findNavController().navigate(
                        R.id.action_reportFragment_to_reportDetailsFragment
                    )
                }

            })
            adapter=reportAdapter

        }
    }

    private fun getAllData() {

        reportList.clear()
        printList.clear()
        reportAdapter?.let {
            reportList.clear()
            newReportList.clear()
            it.items=ArrayList()
            it.notifyDataSetChanged()
        }
        viewModel?.reportType?.value?.let { type ->
            isConfirmCall=true
            when (type) {


                getString(R.string.payment) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                    loginResponse?.let {loginData->
                        val data = mapOf(
                            "userid" to loginData.userid,
                            "startdate" to startDate,
                            "enddate" to endDate,
                        )
                        val gson= Gson()
                        var jsonString = gson.toJson(data)
                        /*val requestBody = """
                        {
                        "userid": ${loginData.userid},
                        "startdate": "07-12-2022",
                        "enddate": "07-12-2023"
                        }
                        """.trimIndent()*/
                        /*val requestBody = """${jsonString.encrypt()}"""*/


                        loginData.AuthToken?.let {
                            myViewModel?.paymentReport(it,jsonString.encrypt())
                           // loader?.show()

                        }
                    }
                }
                }


                getString(R.string.transactions) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.transcationReport(it,jsonString.encrypt())
                               // loader?.show()
                            }
                        }
                    }

                }

                getString(R.string.dmt) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.dmtReport(it,jsonString.encrypt())
                              //  loader?.show()
                            }
                        }
                    }

                }

                getString(R.string.load_Requests) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.loadRequestReport(it,jsonString.encrypt())
                           //     loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.wallet_ledger) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.walletLedgerReport(it,jsonString.encrypt())
                            //    loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.cashout_ledger) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.cashout_ledger_report(it,jsonString.encrypt())
                            //    loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.aeps) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.aepsReport(it,jsonString.encrypt())
                             //   loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.micro_atm) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.microatmReport(it,jsonString.encrypt())
                            //    loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.commissions) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.commissionReport(it,jsonString.encrypt())
                           //     loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.bank_settle) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.bank_settle_report(it,jsonString.encrypt())
                             //   loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.wallet_settle) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.walletSettleReport(it,jsonString.encrypt())
                            //    loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.complaints) -> {
                    reportList?.clear()
                    printList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.complaints_report(it,jsonString.encrypt())
                                    loader?.show()
                            }
                        }
                    }
                }


                /*getString(R.string.complaints) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.complaints_report(it,jsonString.encrypt())
                             //   loader?.show()
                            }
                        }
                    }
                }*/

                else -> {}

            }
        }
    }



    private fun observer() {

        viewModel?.startDate?.observe(viewLifecycleOwner){
            startDate=it
        }
        viewModel?.enddate?.observe(viewLifecycleOwner){
           endDate=it
        }

        myViewModel?.paymentReportResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()

                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                    //reportList.add(ReportModel("001","778.00","10-10-2023","Payment send",0, desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.send_logo))
                    //reportList.add(ReportModel("002","778.00","10-10-2023","Payment received",1 ,desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.receive_logo))
                    if(!it.data?.data.isNullOrEmpty()){
                        it.data?.data?.let {responseData->
                            for (index in responseData.indices){
                                val items=responseData[index]
                                items.apply {
                                    var desc="Receiver Name:$receiverName\nSender Current Balance : $curBalSender\nSender Mobile No.:$senderMobileNo"+

                                            "\nReceiver Mobile No.: $receiverMobileNo"

                                    reportList.add(ReportModel(PaymentBYId,LastTransactionAmount,LastTransactionTime,AmountMode,0,desc,imageInt = R.drawable.send_logo))
                                    val print=
                                        "Payment Id : $PaymentBYId\n"+
                                            "Last Transaction Amount : $LastTransactionAmount\n"+
                                            "Last Transaction Time : $LastTransactionTime\n"+
                                            "Amount Mode : $AmountMode\n"+
                                            "Amount Mode : $AmountMode\n"+
                                            ""+desc



                                    printList.add(print)

                                }

                            }
                            showrecycleView(1)
                        }


                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


        myViewModel?.ranscationReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            reportList.clear()
                            /*reportList.add(
                                ReportModel(
                                    "001",
                                    "778.00",
                                    "10-10-2023",
                                    "Failed",
                                    0,
                                    desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                                    imageInt = R.drawable.close_icon,
                                    isClickAble = true
                                )
                            )
                            reportList.add(
                                ReportModel(
                                    "002",
                                    "778.00",
                                    "10-10-2023",
                                    getString(R.string.success),
                                    1,
                                    desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                                    imageInt = R.drawable.right_tick
                                )
                            )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){
                                        var status =""
                                        var statusCode =0
                                        var imageInt =0
                                        items.apply {
                                            if (Status?.lowercase()=="success") {
                                                status=getString(R.string.success)
                                                statusCode=1
                                                imageInt=R.drawable.right_tick
                                            }
                                            else {
                                                status = Status.toString()
                                                statusCode=0
                                                imageInt=R.drawable.close_icon
                                            }
                                            var desc="Operator :$Operator \nReferance id - $referenceID\nCustomer Mobile No.:$CustNo"
                                            reportList.add(ReportModel(TransactionID,Amount,tDate,status,statusCode,desc,imageInt,isClickAble=true, IDP = ID))

                                            val print="Transaction ID : $TransactionID"+
                                                    "Amount : $Amount"+
                                                    "Date : $tDate"+
                                                    "Status: $status"+

                                                    ""+desc

                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(9)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.dmtReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            /*reportList.add(
                                ReportModel(
                                    "001",
                                    "778.00",
                                    "10-10-2023",
                                    "Refunded",
                                    0,
                                    desc = "Rajiv\nA/c No.:111111111111\nSender: 5555555555",
                                    imageInt = R.drawable.imps_logo,
                                    image1 = 2,
                                    isClickAble = true
                                )
                            )
                            reportList.add(
                                ReportModel(
                                    "002",
                                    "778.00",
                                    "10-10-2023",
                                    getString(R.string.success),
                                    1,
                                    desc = "Jhuma Chowdhary\nA/c No.:000000000000\nSender :8888888888",
                                    imageInt = R.drawable.imps_logo,
                                    image1 = 2
                                )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {
                                            var status =""
                                            var statusCode =0
                                            var imageInt =0
                                            if (tranStatus?.lowercase()=="success") {
                                                getString(R.string.success)
                                                statusCode=1
                                                imageInt=R.drawable.right_tick
                                            }
                                            else {
                                                status = tranStatus.toString()
                                                statusCode=0
                                                imageInt=R.drawable.close_icon
                                            }

                                            var desc="$recName \nA/c No.:$recAcno\nCustomer Mobile No.:$custMobno\nUTR: $utr\nReceipt Id: $receiptid\nType: $type"

                                            reportList.add(ReportModel(tranId,tranAmt,transDt,status,statusCode,desc,imageInt,image1 = 2,isClickAble=true, IDP = receiptid))


                                            val print="Transaction ID : $tranId"+
                                                    "Amount : $tranAmt"+
                                                    "Date : $transDt"+
                                                    "Status: $status"+

                                                    ""+desc

                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(8)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.loadRequestReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            /*reportList.add(
                            ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Credit/Sales Supports",
                            2,
                            desc = "Axis Bank-Online\nPayment Ref id- 5376254\nApproved on 2023-10-30",
                            imageInt = R.drawable.right_tick
                            )
                            )
                            reportList.add(
                            ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Credit/Sales Supports",
                            2,
                            desc = "Axis Bank-Online\nSame Bank\nPayment Ref Id: ASEESSS",
                            imageInt = R.drawable.rounded_i
                            )
                            )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {
                                            var donedate=""
                                            donedate="\nApproved on: $isdonedate"
                                            if (isdonedate.isNullOrEmpty()){
                                                donedate=""
                                            }
                                            var image=if (isdone=="rejected"){
                                                R.drawable.close_icon
                                            }
                                            else{
                                                R.drawable.rounded_i
                                            }
                                            var desc="Bankname $bankname\n$donedate Completed: $isdone"
                                            reportList.add(ReportModel(purchaseid,Amount,insdate,"Credit/Sales Supports",2,desc,imageInt = image))

                                            val print="Purchase ID : $purchaseid"+
                                                    "Amount : $Amount"+
                                                    "Date : $insdate"+
                                                     ""+desc

                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(7)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.walletLedgerReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            //binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            /*reportList.add(
                                ReportModel(
                                    "001",
                                    "-778.00",
                                    "10-10-2023\n" +
                                            "05:49:11",
                                    "ePotlyNB Money\nForward",
                                    3,
                                    desc = "",
                                    image1 = 2,
                                    imageInt=R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )
                            reportList.add(
                                ReportModel(
                                    "001",
                                    "-778.00",
                                    "10-10-2023\n" +
                                            "05:49:11",
                                    "ePotlyNB Money\nForward",
                                    3,
                                    desc = "",
                                    image1 = 2,
                                    imageInt=R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )*/

                            if(!it.data?.data.isNullOrEmpty()){
                               /* val size=if (it.data?.data?.size?:0 >=60){
                                    60
                                }
                                else{
                                    it.data?.data?.size?.minus(1)?:0
                                }*/

                               // Log.d("TAG_size", "observer: "+it.data?.data?.size)
                                it.data?.data?.let {responseData->
                                    for (index in responseData.indices){
                                    //for (index in 0 until minOf(responseData.size, size)) {
                                        val items=responseData[index]
                                        items.apply {

                                            reportList.add(ReportModel(refillid,amount,insdate,type,3,desc = "Status: $status",image1 = 2,imageInt=R.drawable.rupee_rounded,price2 = "Closing ₹$curramt",proce1TextColor = 2,isMiniStatement = false))

                                            val print="Transaction ID : $refillid"+
                                                    "Amount : $amount"+
                                                    "Date : $insdate"+
                                                    "Type : $type"+
                                                    "Closing ₹$curramt"


                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(reportList.size)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.aepsReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                         /*   reportList.add(
                            ReportModel(
                                "001",
                                "778.00",
                                "10-10-2023",

                                desc = "AAdhar No.:xxxx-xxxx-1458\nRRN: Balance 0\nSettltment Transaction id: 300000312",
                                imageInt = R.drawable.close_icon,
                                isMiniStatement = true,
                                miniStatementValue = "Mini Statement",
                                isClickAble = true
                            )
                        )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {
                                            val desc="$responseDescription " +

                                                    "\nBank Reference Number : $BankReferenceNumber"+
                                                    "\nTransaction Status  : $tranStatus"+
                                                    "\nAadhar No  : $aadharno"+
                                                    "\nBank Reference Number  : $BankReferenceNumber"+
                                                    "\nAvailable Balance  : $avbalance"
                                            reportList.add(ReportModel(tranId,tranAmt,transDt,desc,imageInt = R.drawable.close_icon,isMiniStatement = true,miniStatementValue = "${type?.replace("_"," ")}",isClickAble = true, IDP = tranId))

                                            val print="Transaction ID : $tranId"+
                                                    "Amount : $tranAmt"+
                                                    "Date : $transDt"+
                                                    "Type : $type"+
                                                    desc


                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(5)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.microatmReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {
                                            val desc="$responseDescription   " +
                                                    "\nBank Reference Number : $BankReferenceNumber" +
                                                    "\ntype : $type" +
                                                    "\nTransaction Status: $tranStatus"+
                                                    "\nPancard: $maskedPan"
                                            reportList.add(ReportModel(tranId,tranAmt,transDt,desc,imageInt = R.drawable.rounded_i, IDP = tranId, isClickAble = true))

                                            val print="Transaction ID : $tranId"+
                                                    "Amount : $tranAmt"+
                                                    "Date : $transDt"+
                                                    desc


                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(4)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.commissionReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let { responseData ->
                                    for (items in responseData){

                                        items.apply {
                                            var comm_data:String=""

                                            comm_data=if (type=="rs"){
                                                "₹$comm"
                                            }
                                            else if (type=="%"){
                                                "$comm%"
                                            }
                                            else{
                                                comm.toString()
                                            }
                                            reportList.add(ReportModel(comm_data, reporyStatus = opname,imageInt = R.drawable.rounded_i))

                                            val print=""+
                                                    "Status : $opname"
                                                    comm_data


                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(12)
                                    /*CoroutineScope(Main).launch {

                                    for (index in responseData.indices) {

                                               // paging++

                                                 if (responseData.isNotEmpty()) {
                                                     responseData[index].apply {
                                                         val desc = "$opname   "
                                                         if (index<=20) {
                                                         reportList.add(
                                                             ReportModel(
                                                                 "",
                                                                 desc = desc,
                                                                 price = comm,
                                                                 imageInt = R.drawable.rounded_i
                                                             )
                                                         )
                                                     }
                                                         *//*Log.d("TAG_table", "observer: "+tableViewModel.insertData(DataEntity(responseId="",
                                                             desc = desc,
                                                             price = comm,
                                                             imageInt = R.drawable.rounded_i)))*//*
                                                             *//*reportList2.add(
                                                                 ReportModel(
                                                                     "",
                                                                     desc = desc,
                                                                     price = comm,
                                                                     imageInt = R.drawable.rounded_i
                                                                 )
                                                             )*//*

                                                     }
                                                    // showPagingRecycleView()
                                                     //showrecycleView()
                                                 }

                                            }

                                            *//*for (index in responseData.indices) {
                                               // paging++
                                             if (index<=20) {
                                                 if (responseData.isNotEmpty()) {
                                                     responseData[index].apply {
                                                         val desc = "$opname   "
                                                         reportList.add(
                                                             ReportModel(
                                                                 "",
                                                                 desc = desc,
                                                                 price = comm,
                                                                 imageInt = R.drawable.rounded_i
                                                             )
                                                         )
                                                     }
                                                     showrecycleView()
                                                 }
                                             }
                                            }*//*
                                        *//*if (commissionReportList.size>10){
                                            for(index in commissionReportList.indices){
                                                i
                                            }
                                            reportList
                                        }*//*



                                    }*/
                                }
                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        /*myViewModel?.complaints_reportReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            Log.d("TAG_complain", "observer: "+it.data?.Description)
                            loader?.dismiss()
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        *//*items.apply {
                                            val desc="$complaintTypeName   \nTransactionId : $txtTransactionId"
                                            reportList.add(ReportModel(ticketID,"",ticketDate,desc,imageInt = R.drawable.rounded_i))
                                        }*//*

                                    }
                                    showrecycleView()
                                }


                            }

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }*/

        myViewModel?.complaints_reportReportResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                     loader?.show()

                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    //Log.d("TAG_complain", "observeraaa: "+it.data?.Description)
                    if(!it.data?.data.isNullOrEmpty()){
                        it.data?.data?.let {responseData->
                            for (items in responseData){

                                items.apply {
                                /*
                                {
                                      "ddlComplaintCategory": "1",
                                      "ddlComplaintType": "1",
                                      "ticketID": "28",
                                      "ticketDate": "2023-10-10 11:21:44",
                                      "txtTransactionId": "300000029",
                                      "ticketFlag": "0",
                                      "status": "0",
                                      "complaintCategoryName": "BDNR",
                                      "complaintTypeName": "Recharges and Bills"
                                    }
                                 */

                                    var desc="Complaint Type $complaintTypeName " +
                                            "\nTransaction Id :$txtTransactionId"+
                                            "\nTicket ID :$ticketID"+
                                            "\nComplaint Category Name :$complaintCategoryName"
                                    reportList.add(ReportModel(txtTransactionId,"","Complaint Date $ticketDate",desc=desc))

                                    val print="Transaction ID : $txtTransactionId"+

                                            "Complaint Date $ticketDate"+
                                            desc


                                    printList.add(print)
                                }

                            }

                        }


                    }
                    showrecycleView(33)
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }



        myViewModel?.walletSettleReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                          loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {


                                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                                            var desc="$transDt \nUTR :$utr"
                                            reportList.add(ReportModel(tranId,tranAmt,"",reporyStatus=desc))
                                            val print="Transaction ID : $tranId"+
                                            "Amount : $tranAmt"+
                                            desc


                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(10)
                                }


                            }


                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.bank_settle_reportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {


                                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                                            var desc="$transDt \nUTR :$utr"
                                            reportList.add(ReportModel(tranId,tranAmt,"",desc=desc))
                                            val print="Transaction ID : $tranId"+
                                                    "Amount : $tranAmt"+
                                                    desc


                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(11)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.cashout_ledger_reportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                           loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                           /* reportList.add(
                                ReportModel(
                                    "001",
                                    "-778.00",
                                    "10-10-2023\n" +
                                            "05:49:11",
                                    "ePotlyNB Money\nForward",
                                    3,
                                    desc = "",
                                    image1 = 2,
                                    imageInt=R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {

                                           reportList.add(ReportModel(refillid,amount,insdate,"",3,desc = "Type: $type\n Status: $status",image1 = 2,imageInt=R.drawable.rupee_rounded,price2 = "Closing ₹$curramt",proce1TextColor = 2,isMiniStatement = false))

                                            val print="Transaction ID : $refillid"+
                                                    "Amount : $amount"+
                                                    "Date : $insdate"+
                                                    "Type: $type\n Status: $status"


                                            printList.add(print)
                                        }

                                    }
                                    showrecycleView(2)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        /*myViewModel?.transcation_report_receiptResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()
                        }

                        is ResponseState.Success -> {
                            Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }*/
        /*lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                CoroutineScope(Dispatchers.Main).launch {
                    launch(Dispatchers.Main) {
                        // Perform UI-related operation here, e.g., update UI elements
                        binding.nsvTop.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                            // Check if the scroll position has changed
                            if (scrollY != oldScrollY) {
                                // Check if the NestedScrollView has reached the bottom
                                val maxScrollRange =
                                    binding.nsvTop.getChildAt(0).height - binding.nsvTop.height
                                val isAtBottom = scrollY >= maxScrollRange

                                if (isAtBottom) {

                                    // NestedScrollView is at the bottom, perform your actions here
                                    *//*if (!isDataLoadingFromLocal) {
                               // if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 10000) ){

                                    CoroutineScope(Dispatchers.Main).launch{
                                        binding.loaderBottom.visibility=View.VISIBLE
                                    }
                                CoroutineScope(Dispatchers.IO).launch {
                                    getAllData3()
                                }
                                    //isDataLoadingFromLocal=true
                                //}
                            }*//*
                                    CoroutineScope(Dispatchers.IO).launch {
                                        //getAllData3()
                                        if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                            if (!(endIndex >= (Constants.reportList.size - 1))) {
                                                if (isAsintask) {
                                                    Log.d("TAG_s2", "observer:334 ")
                                                    //commissionReportAdapter?.setLoading(true)
                                                    CoroutineScope(Dispatchers.Main).launch {
                                                        binding.loaderBottom.visibility =
                                                            View.VISIBLE
                                                    }
                                                    lifecycleScope.launch {
                                                        withContext(Dispatchers.IO) {
                                                            CoroutineScope(Dispatchers.IO).launch {
                                                                val loadMoreDataTask =
                                                                    MyAsyncTask2()
                                                                loadMoreDataTask.execute()
                                                                isAsintask = false
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                    Log.d("TAG_s2", "observer:111 ")

                                } else {
                                    // NestedScrollView is not at the bottom
                                    Log.d("TAG_s2", "observer:222 ")

                                }
                            }
                        }
                    }

                    // Other code in the main coroutine

                }
            }
        }*/

        /*binding.nsvTop.setOnScrollChangeListener { _, _, _, _, _ ->
            val maxScrollRange = binding.nsvTop.getChildAt(0).height - binding.nsvTop.height
            val isAtBottom = binding.nsvTop.scrollY >= maxScrollRange

            if (isAtBottom) {
                // Get the last visible item position in the RecyclerView
                val layoutManager = (recyclerView.layoutManager as? LinearLayoutManager)
                val lastVisibleItemPosition = layoutManager?.findLastVisibleItemPosition()

                // Check if the last visible item position is within the last two items
                if (lastVisibleItemPosition != null && lastVisibleItemPosition >= reportAdapter?.items?.size?.minus(
                        2
                    ) ?: 0) {

                        CoroutineScope(Dispatchers.Main).launch {
                            binding.loaderBottom.visibility = View.VISIBLE
                        }

                        if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                            if (!(endIndex >= (Constants.reportList.size - 1)) && isAsintask) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val loadMoreDataTask = MyAsyncTask2()
                                    loadMoreDataTask.execute()
                                    isAsintask = false
                                }
                            }
                        }

                }
            }
        }*/
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                // Get the first visible item position
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Get the last visible item position
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // You can use these positions as needed
                // For example, log them
               /* Log.d("RecyclerView", "First visible position: $firstVisibleItemPosition")
                Log.d("RecyclerView", "Last visible position: $lastVisibleItemPosition")
                Log.d("RecyclerView", "adapter position: ${reportAdapter?.items?.size}")
*/
                if (lastVisibleItemPosition>=(reportAdapter?.items?.size?.minus(1)?:0)) {


                    if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                        if (!(endIndex >= (Constants.reportList.size - 1)) && isAsintask) {
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.VISIBLE
                            }
                            CoroutineScope(Dispatchers.IO).launch {

                                val loadMoreDataTask = MyAsyncTask2()
                                loadMoreDataTask.execute()
                                isAsintask = false
                            }
                        }
                        else{
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.GONE
                            }
                        }
                    }
                }
                else if (firstVisibleItemPosition==0){
                    if (startIndex>0) {
                        if (!(SystemClock.elapsedRealtime() - lastClickTime2 < 3000)) {
                            if (isTopAsink) {
                               // val myAsyncTask2ScrollTop = MyAsyncTask2ScrollTop()
                              //  myAsyncTask2ScrollTop.execute()
                                isTopAsink=false
                            }
                        }
                    }
                }

                else {
                    // NestedScrollView is not at the bottom
                }


            }
        })
      /*  binding.nsvTop.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY != oldScrollY) {
                val maxScrollRange = binding.nsvTop.getChildAt(0).height - binding.nsvTop.height
                val isAtBottom = scrollY >= maxScrollRange

                Log.d("TAG_scrollY", "observer:scrollY "+scrollY)
                Log.d("TAG_scrollY", "observer:oldScrollY "+oldScrollY)
                val isAtTop = scrollY == 0
                if (isAtBottom) {


                    if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                        if (!(endIndex >= (Constants.reportList.size - 1)) && isAsintask) {
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.VISIBLE
                            }
                            CoroutineScope(Dispatchers.IO).launch {

                                val loadMoreDataTask = MyAsyncTask2()
                                loadMoreDataTask.execute()
                                isAsintask = false
                            }
                        }
                        else{
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.GONE
                            }
                        }
                    }
                }
                else if (isAtTop){
                    if (startIndex>0) {
                        if (!(SystemClock.elapsedRealtime() - lastClickTime2 < 3000)) {
                            if (isTopAsink) {
                                val myAsyncTask2ScrollTop = MyAsyncTask2ScrollTop()
                                myAsyncTask2ScrollTop.execute()
                                isTopAsink=false
                            }
                        }
                    }
                }

                else {
                    // NestedScrollView is not at the bottom
                }
            }
        }*/
    }

    fun showrecycleView(a:Int) {
       // Log.d("TAG_data", "showrecycleView: "+a)
        clearAllData()
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            reportAdapter?.apply {
                //reportList.clear()

                viewModel?.reportType?.value?.let { type ->
                    when (type) {

                        getString(R.string.payment) -> {
                            // reportList.add(ReportModel("001","778.00","10-10-2023","Payment send",0, desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.send_logo))
                            //  reportList.add(ReportModel("002","778.00","10-10-2023","Payment received",1 ,desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.receive_logo))


                        }


                        getString(R.string.transactions) -> {
                            /* reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Failed",
                            0,
                            desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                            imageInt = R.drawable.close_icon,
                            isClickAble = true
                        )
                    )
                    reportList.add(
                        ReportModel(
                            "002",
                            "778.00",
                            "10-10-2023",
                            getString(R.string.success),
                            1,
                            desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                            imageInt = R.drawable.right_tick
                        )
                    )*/

                        }

                        getString(R.string.dmt) -> {
                            /* reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Refunded",
                            0,
                            desc = "Rajiv\nA/c No.:111111111111\nSender: 5555555555",
                            imageInt = R.drawable.imps_logo,
                            image1 = 2,
                            isClickAble = true
                        )
                    )
                    reportList.add(
                        ReportModel(
                            "002",
                            "778.00",
                            "10-10-2023",
                            getString(R.string.success),
                            1,
                            desc = "Jhuma Chowdhary\nA/c No.:000000000000\nSender :8888888888",
                            imageInt = R.drawable.imps_logo,
                            image1 = 2
                        )
                    )*/

                        }

                        getString(R.string.load_Requests) -> {
                            /* reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Credit/Sales Supports",
                            2,
                            desc = "Axis Bank-Online\nPayment Ref id- 5376254\nApproved on 2023-10-30",
                            imageInt = R.drawable.right_tick
                        )
                    )
                    reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Credit/Sales Supports",
                            2,
                            desc = "Axis Bank-Online\nSame Bank\nPayment Ref Id: ASEESSS",
                            imageInt = R.drawable.rounded_i
                        )
                    )*/
                        }

                        getString(R.string.wallet_ledger) -> {
                            /* reportList.add(
                        ReportModel(
                            "001",
                            "-778.00",
                            "10-10-2023\n" +
                                    "05:49:11",
                            "ePotlyNB Money\nForward",
                            3,
                            desc = "",
                            image1 = 2,
                            imageInt=R.drawable.rupee_rounded,
                            price2 = "Closing ₹1021.00",
                            proce1TextColor = 2,
                            isMiniStatement = false
                        )
                    )
                    reportList.add(
                        ReportModel(
                            "001",
                            "-778.00",
                            "10-10-2023\n" +
                                    "05:49:11",
                            "ePotlyNB Money\nForward",
                            3,
                            desc = "",
                            image1 = 2,
                            imageInt=R.drawable.rupee_rounded,
                            price2 = "Closing ₹1021.00",
                            proce1TextColor = 2,
                            isMiniStatement = false
                        )
                    )*/

                        }

                        getString(R.string.cashout_ledger) -> {
                            /*reportList.add(
                                ReportModel(
                                    "001",
                                    "-778.00",
                                    "10-10-2023\n" +
                                            "05:49:11",
                                    "ePotlyNB Money\nForward",
                                    3,
                                    desc = "",
                                    image1 = 2,
                                    imageInt = R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )*/
                        }

                        getString(R.string.aeps) -> {
                            /*reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",

                            desc = "AAdhar No.:xxxx-xxxx-1458\nRRN: Balance 0\nSettltment Transaction id: 300000312",
                            imageInt = R.drawable.close_icon,
                            isMiniStatement = true,
                            miniStatementValue = "Mini Statement",
                            isClickAble = true
                        )
                    )*/
                        }

                        getString(R.string.micro_atm) -> {
                            // ReportPropertyModel("Transaction id")
                            //isClickAble = true
                        }

                        getString(R.string.commissions) -> {
                            //  ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.bank_settle) -> {
                            /*reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Failed",
                            0,
                            desc = "Type: Settle to bank",
                            isClickAble = true,
                            image1 = 3
                        )
                    )*/
                        }

                        getString(R.string.wallet_settle) -> {
                            /*reportList.add(
                        ReportModel(
                            "001",
                            "10.00",
                            "10-10-2023",
                            "Failed",
                            0,
                            desc = "Type: Settle to wallet\nstatus - processed\ndetails-wallet",

                            image1 = 3
                        )
                    )*/
                        }

                        getString(R.string.complaints) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        else -> {}
                    }
                }




                viewModel?.reportType?.value?.let { type ->

                    val setReportPropertyModel = when (type) {

                        getString(R.string.payment) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.transactions) -> {
                            ReportPropertyModel("Transaction id", "")
                        }

                        getString(R.string.dmt) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.load_Requests) -> {
                            ReportPropertyModel("Purchased Id")
                        }

                        getString(R.string.wallet_ledger) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.cashout_ledger) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.aeps) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.micro_atm) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.commissions) -> {
                            ReportPropertyModel("Commissions")
                        }

                        getString(R.string.bank_settle) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.wallet_settle) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.complaints) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        else -> {
                            ReportPropertyModel("Transaction id")
                        }
                    }
                    //Log.d("TAG_complain", "observeraaa:c "+reportList.size)
                    if (reportList.size > 0) {
                        binding.tvNoDataFound.visibility = View.GONE
                    } else {
                        binding.tvNoDataFound.visibility = View.VISIBLE

                    }
                    //binding.nsv.isVisible=!binding.tvNoDataFound.isVisible
                    reportPropertyModel=setReportPropertyModel
                    //items=reportList
                    lifecycleScope.launch {
                     //   binding.btnHasdata.visibility=View.GONE
                        Log.d("TAG_size", "showrecycleView: "+reportList.size)
                        reportList?.let {
                            if(it.size>20 && !(it.size<=30)) {
                                getAllData2()
                            }
                            else{
                                //binding.btnHasdata.visibility=View.GONE
                                reportAdapter?.items = reportList
                                reportAdapter?.notifyDataSetChanged()
                            }
                        }
                    }
                  /*  binding.btnHasdata.setOnClickListener {
                      //  binding.btnHasdata.visibility=View.GONE
                        getAllData2()
                    }*/
                    //loadAllData()
                    /*handler.postDelayed({
                        reportAdapter.items=reportList2
                        reportAdapter.notifyDataSetChanged()
                    }, 2000)*/



                }

            }

            /*lifecycleScope.launchWhenStarted {
                tableViewModel.data.collectLatest { pagingData ->
                    reportAdapter.submitData(pagingData)
                }
            }*/
           /* lifecycleScope.launchWhenStarted {
                tableViewModel.data.observe(viewLifecycleOwner) { pagingData ->
                    reportAdapter.submitData(lifecycle, pagingData)
                }
            }
            tableViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.bottomLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
            }*/
        }
    }
    private fun getAllData2() {
        loader?.show()
        if (!(endIndex >= (reportList.size - 1))) {
            for (index in reportList.indices) {
                if (index >= startIndex && index <= endIndex) {
                    var items = reportList[index]
                    items.apply {
                        newReportList.add(this)
                    }

                }
            }

            reportAdapter?.items = newReportList
            Log.d("TAG_adapterSize", "getAllData2: "+reportAdapter?.items?.size)
            reportAdapter?.notifyDataSetChanged()
           // binding.btnHasdata.visibility=View.VISIBLE
        } /*else {
            binding.btnHasdata.visibility = View.GONE
        }*/

        //delay(2000)
        loader?.dismiss()
        startIndex += 10
        endIndex += 10
        loader?.dismiss()
        //showrecycleView()
    }

    /*fun showPagingRecycleView() {
        val startIndex = 1
        val endIndex = 20
        val dataInRange = tableViewModel.getDataInRange(startIndex, endIndex)
        dataInRange?.forEach(){
            Log.d("TAG_id", "showPagingRecycleView: "+it?.id)
        }
       *//* val handler = Handler(Looper.getMainLooper())
        handler.post {
            binding.recycleViewReport.apply {
                //reportList.clear()

                viewModel?.reportType?.value?.let { type ->
                    when (type) {

                        getString(R.string.payment) -> {
                            // reportList.add(ReportModel("001","778.00","10-10-2023","Payment send",0, desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.send_logo))
                            //  reportList.add(ReportModel("002","778.00","10-10-2023","Payment received",1 ,desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.receive_logo))


                        }


                        getString(R.string.transactions) -> {
                            *//**//* reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Failed",
                            0,
                            desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                            imageInt = R.drawable.close_icon,
                            isClickAble = true
                        )
                    )
                    reportList.add(
                        ReportModel(
                            "002",
                            "778.00",
                            "10-10-2023",
                            getString(R.string.success),
                            1,
                            desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                            imageInt = R.drawable.right_tick
                        )
                    )*//**//*

                        }

                        getString(R.string.dmt) -> {
                            *//**//* reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Refunded",
                            0,
                            desc = "Rajiv\nA/c No.:111111111111\nSender: 5555555555",
                            imageInt = R.drawable.imps_logo,
                            image1 = 2,
                            isClickAble = true
                        )
                    )
                    reportList.add(
                        ReportModel(
                            "002",
                            "778.00",
                            "10-10-2023",
                            getString(R.string.success),
                            1,
                            desc = "Jhuma Chowdhary\nA/c No.:000000000000\nSender :8888888888",
                            imageInt = R.drawable.imps_logo,
                            image1 = 2
                        )
                    )*//**//*

                        }

                        getString(R.string.load_Requests) -> {
                            *//**//* reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Credit/Sales Supports",
                            2,
                            desc = "Axis Bank-Online\nPayment Ref id- 5376254\nApproved on 2023-10-30",
                            imageInt = R.drawable.right_tick
                        )
                    )
                    reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Credit/Sales Supports",
                            2,
                            desc = "Axis Bank-Online\nSame Bank\nPayment Ref Id: ASEESSS",
                            imageInt = R.drawable.rounded_i
                        )
                    )*//**//*
                        }

                        getString(R.string.wallet_ledger) -> {
                            *//**//* reportList.add(
                        ReportModel(
                            "001",
                            "-778.00",
                            "10-10-2023\n" +
                                    "05:49:11",
                            "ePotlyNB Money\nForward",
                            3,
                            desc = "",
                            image1 = 2,
                            imageInt=R.drawable.rupee_rounded,
                            price2 = "Closing ₹1021.00",
                            proce1TextColor = 2,
                            isMiniStatement = false
                        )
                    )
                    reportList.add(
                        ReportModel(
                            "001",
                            "-778.00",
                            "10-10-2023\n" +
                                    "05:49:11",
                            "ePotlyNB Money\nForward",
                            3,
                            desc = "",
                            image1 = 2,
                            imageInt=R.drawable.rupee_rounded,
                            price2 = "Closing ₹1021.00",
                            proce1TextColor = 2,
                            isMiniStatement = false
                        )
                    )*//**//*

                        }

                        getString(R.string.cashout_ledger) -> {
                            reportList.add(
                                ReportModel(
                                    "001",
                                    "-778.00",
                                    "10-10-2023\n" +
                                            "05:49:11",
                                    "ePotlyNB Money\nForward",
                                    3,
                                    desc = "",
                                    image1 = 2,
                                    imageInt = R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )
                        }

                        getString(R.string.aeps) -> {
                            *//**//*reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",

                            desc = "AAdhar No.:xxxx-xxxx-1458\nRRN: Balance 0\nSettltment Transaction id: 300000312",
                            imageInt = R.drawable.close_icon,
                            isMiniStatement = true,
                            miniStatementValue = "Mini Statement",
                            isClickAble = true
                        )
                    )*//**//*
                        }

                        getString(R.string.micro_atm) -> {
                            // ReportPropertyModel("Transaction id")
                            //isClickAble = true
                        }

                        getString(R.string.commissions) -> {
                            //  ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.bank_settle) -> {
                            *//**//*reportList.add(
                        ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Failed",
                            0,
                            desc = "Type: Settle to bank",
                            isClickAble = true,
                            image1 = 3
                        )
                    )*//**//*
                        }

                        getString(R.string.wallet_settle) -> {
                            *//**//*reportList.add(
                        ReportModel(
                            "001",
                            "10.00",
                            "10-10-2023",
                            "Failed",
                            0,
                            desc = "Type: Settle to wallet\nstatus - processed\ndetails-wallet",

                            image1 = 3
                        )
                    )*//**//*
                        }

                        getString(R.string.complaints) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        else -> {}
                    }
                }




                viewModel?.reportType?.value?.let { type ->

                    val reportPropertyModel = when (type) {

                        getString(R.string.payment) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.transactions) -> {
                            ReportPropertyModel("Transaction id", "")
                        }

                        getString(R.string.dmt) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.load_Requests) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.wallet_ledger) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.cashout_ledger) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.aeps) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.micro_atm) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.commissions) -> {
                            ReportPropertyModel("Commissions")
                        }

                        getString(R.string.bank_settle) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.wallet_settle) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.complaints) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        else -> {
                            ReportPropertyModel("Transaction id")
                        }
                    }
                    recyclerView=this
                    if (reportList.size > 0) {
                        binding.tvNoDataFound.visibility = View.GONE
                    } else {
                        binding.tvNoDataFound.visibility = View.VISIBLE

                    }
                    // binding.nsv.isVisible=!binding.tvNoDataFound.isVisible
                    pagingreportAdapter = PagingReportAdapter(reportPropertyModel,  object : CallBack {
                        override fun getValue(s: String) {
                            val bundle = Bundle()
                            bundle.putString("jsonData", s)
                            findNavController().navigate(
                                R.id.action_reportFragment_to_reportDetailsFragment,
                                bundle
                            )
                        }

                    })
                    adapter=reportAdapter
                    //loadAllData()
                    *//**//*handler.postDelayed({
                        reportAdapter.items=reportList2
                        reportAdapter.notifyDataSetChanged()
                    }, 2000)*//**//*



                }

            }

            *//**//*lifecycleScope.launchWhenStarted {
                tableViewModel.data.collectLatest { pagingData ->
                    reportAdapter.submitData(pagingData)
                }
            }*//**//*
            *//**//*lifecycleScope.launchWhenStarted {
                tableViewModel.data.observe(viewLifecycleOwner) { pagingData ->
                    pagingreportAdapter.submitData(lifecycle, pagingData)
                }
            }
            tableViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.bottomLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
            }*//**//*

            tableViewModel.data.observe(viewLifecycleOwner) { pagingData ->

                Log.d("TAGpagingData", "showPagingRecycleView: "+pagingData)
                pagingreportAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }

            // Observe the loading state
            tableViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                // Update UI based on the loading state
                binding.bottomLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }*//*

        // Example: Get data in a specific range

    }*/

    /*override fun onPause() {
        super.onPause()
        reportAdapter?.let {
            it.items=ArrayList()
            it.notifyDataSetChanged()
        }
    }*/
    fun backPressed(){
        activity?.let {
            it.onBackPressedDispatcher
            .addCallback(it, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (isEnabled) {
                        isEnabled = false
                        reportAdapter?.let {
                          //  binding.bottomLoader.visibility=View.GONE
                            reportList.clear()
                            newReportList.clear()
                            it.items=ArrayList()
                            it.notifyDataSetChanged()
                        }
                        it.onBackPressedDispatcher.onBackPressed()

                    }
                }
            }
            )
        }

    }


    inner class MyAsyncTask2 : AsyncTask<Void, Void, Unit>() {

        override fun doInBackground(vararg params: Void?) {
            // Background work (in a background thread)

            /*for (index in reportList.indices) {
                if (index >= startIndex && index <= endIndex) {
                    var items = reportList[index]
                    items.apply {
                        newReportList.add(this)
                    }

                }
            }

            reportAdapter?.items = newReportList*/


            /*if (!(endIndex >= (reportList.size - 1))) {
               // Log.d("TAG_s2", "observer:444 ")
                for (index in startIndex until minOf(endIndex, reportList.size)) {
                   // Log.d("TAG_s2", "observer:555 ")
                    if (index >= startIndex && index <= endIndex) {
                      //  Log.d("TAG_s2", "observer:666 ")
                        val items = Constants.reportList[index]
                        items.apply {
                            //reportList2.add(items)
                            newReportList?.add(items)
                        }
                    }
                }
            }*/
            val batchSize = 10
            startIndex= reportAdapter?.itemCount?:0
            endIndex =startIndex+10
            isScrollingLoaderShowing=false
            // Ensure that the indices are within bounds
            if (startIndex < reportList.size && startIndex < endIndex) {
                val newData = reportList.subList(startIndex, minOf(endIndex, reportList.size))
                /*if (newReportList.size>39) {
                    isScrollingLoaderShowing=true
                    newReportList.clear()
                }*/
                newReportList.addAll(newData)

                // Update indices for the next batch
                startIndex = endIndex
                endIndex = minOf(endIndex + batchSize, reportList.size)
            }


        }

        override fun onPostExecute(result: Unit?) {
            // UI-related operations (in the main thread)
            /*activity?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!(endIndex >= (Constants.commissionReportList.size - 1))) {
                        reportAdapter?.notifyDataSetChanged()
                    }

                startIndex += 10
                endIndex += 10
                loader?.dismiss()
            }


        }*/

            activity?.let {
                 it.runOnUiThread(){
                CoroutineScope(Dispatchers.IO).launch {
                    CoroutineScope(Dispatchers.Main).launch {
                      //  if (!(endIndex >= (reportList.size - 1))) {

                           /* withContext(Dispatchers.Default) {
                                reportAdapter?.items=reportList
                            }*/
                            withContext(Dispatchers.Main) {
                                // Update the adapter on the main thread
                                if (isScrollingLoaderShowing){
                                    loader?.show()
                                }
                                delay(500)
                                reportAdapter?.notifyDataSetChanged()
                                if (isScrollingLoaderShowing){
                                    binding.nsvTop.scrollTo(0, 0)
                                }
                                isScrollingLoaderShowing=false
                                /*val itemCount = recyclerView.adapter?.itemCount ?: 0

                                if (itemCount >= 1) {

                                    binding.nsvTop.scrollTo(0, 0)
                                }*/
                            }

                           // reportAdapter?.notifyDataSetChanged()
                      //  }
                        loader?.dismiss()



                        CoroutineScope(Dispatchers.IO).launch {
                           /* startIndex = endIndex + 1
                            endIndex += 10*/




                            isAsintask = true
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.loaderBottom.visibility = View.GONE
                        }
                    }
                    }
                }
            }

    }

}

    inner class MyAsyncTask2ScrollTop : AsyncTask<Void, Void, Unit>() {

        override fun doInBackground(vararg params: Void?) {
            // Background work (in a background thread)

            /*for (index in reportList.indices) {
                if (index >= startIndex && index <= endIndex) {
                    var items = reportList[index]
                    items.apply {
                        newReportList.add(this)
                    }

                }
            }

            reportAdapter?.items = newReportList*/


            /*if (!(endIndex >= (reportList.size - 1))) {
               // Log.d("TAG_s2", "observer:444 ")
                for (index in startIndex until minOf(endIndex, reportList.size)) {
                   // Log.d("TAG_s2", "observer:555 ")
                    if (index >= startIndex && index <= endIndex) {
                      //  Log.d("TAG_s2", "observer:666 ")
                        val items = Constants.reportList[index]
                        items.apply {
                            //reportList2.add(items)
                            newReportList?.add(items)
                        }
                    }
                }
            }*/
            val batchSize = 10

            isScrollingLoaderShowing=false
            if (startIndex >= batchSize) {
                // Move back to the previous batch
                startIndex -= batchSize
                endIndex = minOf(startIndex + batchSize, reportList.size)
                if (newReportList.size>39) {
                    newReportList.clear()
                }
                var arrTemp=newReportList
                newReportList.clear()


                val newData = reportList.subList(startIndex, endIndex)
                newReportList.addAll(newData)
                if (newReportList.size<=39) {
                    newReportList.addAll(arrTemp)
                }
                // Update UI or perform other actions as needed
            }


        }

        override fun onPostExecute(result: Unit?) {
            // UI-related operations (in the main thread)
            /*activity?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!(endIndex >= (Constants.commissionReportList.size - 1))) {
                        reportAdapter?.notifyDataSetChanged()
                    }

                startIndex += 10
                endIndex += 10
                loader?.dismiss()
            }


        }*/

            activity?.let {
                it.runOnUiThread(){
                    CoroutineScope(Dispatchers.IO).launch {
                        CoroutineScope(Dispatchers.Main).launch {
                            //  if (!(endIndex >= (reportList.size - 1))) {

                            /* withContext(Dispatchers.Default) {
                                 reportAdapter?.items=reportList
                             }*/
                            withContext(Dispatchers.Main) {
                                // Update the adapter on the main thread
                                //if (isScrollingLoaderShowing){
                                    loader?.show()
                                //}

                                reportAdapter?.notifyDataSetChanged()
                                //if (isScrollingLoaderShowing){

                                //}
                                isScrollingLoaderShowing=false
                                /*val itemCount = recyclerView.adapter?.itemCount ?: 0

                                if (itemCount >= 1) {

                                    binding.nsvTop.scrollTo(0, 0)
                                }*/
                            }

                            // reportAdapter?.notifyDataSetChanged()
                            //  }
                            delay(1000)
                            loader?.dismiss()
                            binding.nsvTop.scrollTo(0, 5)
                            isTopAsink=true


                            CoroutineScope(Dispatchers.IO).launch {
                                /* startIndex = endIndex + 1
                                 endIndex += 10*/




                                isAsintask = true
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.GONE
                            }
                        }
                    }
                }
            }

        }

    }
    fun writeArrayToFile(context: Context, fileName: String, lines: List<String>) {
        val file = File(context.filesDir, fileName)
        file.bufferedWriter().use { writer ->
            lines.forEach { line ->
                writer.write(line)
                writer.newLine()
            }
        }
    }


    fun downloadImage(context: Context, imageName: String, imageData: ByteArray) {
        // Create a Bitmap from the byte array
        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)

        // Save the Bitmap to a file
        val imagesDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "DownloadedImages")
        imagesDir.mkdirs() // Create the directory if it doesn't exist
        val imageFile = File(imagesDir, "$imageName.jpg")
       // saveBitmapToFile(bitmap, imageFile)

        // Use DownloadManager to download the file
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.fromFile(imageFile)
        val request = DownloadManager.Request(downloadUri)
            .setTitle("Image Download")
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, imageName + ".jpg")

        downloadManager.enqueue(request)
    }

    /*private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        val outputStream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
    }*/
}

