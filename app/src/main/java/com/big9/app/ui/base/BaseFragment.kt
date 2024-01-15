package com.big9.app.ui.base

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.InputFilter
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.utils.*
import com.big9.app.utils.helpers.Constants.AES_ALGORITHM
import com.big9.app.utils.helpers.Constants.AES_IV
import com.big9.app.utils.helpers.Constants.AES_KEY
import com.big9.app.utils.helpers.Constants.AES_TRANSFORMATION
import com.big9.app.utils.helpers.Constants.INPUT_FILTER_MAX_VALUE
import com.big9.app.utils.helpers.Constants.INPUT_FILTER_POINTER_LENGTH
import com.big9.app.utils.helpers.DecimalDigitsInputFilter
import com.big9.app.utils.helpers.PermissionUtils
import com.big9.app.utils.helpers.SharedPreff
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.PermissionsCallback
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
open class BaseFragment: Fragment(){
    @Inject
    open lateinit var sharedPreff: SharedPreff



    fun Uri.uriToBase64(contentResolver: ContentResolver): String? {
        var inputStream: InputStream? = null
        try {
            inputStream = contentResolver.openInputStream(this)
            if (inputStream != null) {
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                return Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }
    fun String.convertImageToBase64_2(): String? {
        try {
            val file = File(this)
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(file.length().toInt())
            inputStream.read(buffer)
            inputStream.close()

            val encodedString = Base64.encodeToString(buffer, Base64.DEFAULT)
            return encodedString
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
    fun String.videoToBase64(): String? {

        try {
            val file = File(this)
            val length = file.length().toInt()
            val bytes = ByteArray(length)

            val input = FileInputStream(file)
            input.read(bytes)
            input.close()

            val base64String = Base64.encodeToString(bytes, Base64.DEFAULT)

            return base64String
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null


        /*val contentResolver: ContentResolver = context.contentResolver
        Log.d("kol", "videoToBase64:file4 "+this)
        try {
            // Open an input stream to the content URI
            val inputStream: InputStream? = contentResolver.openInputStream(Uri.parse(this))

            if (inputStream != null) {
                // Read the video data into a byte array
                val buffer = ByteArray(1024)
                var bytesRead: Int
                val output = ByteArrayOutputStream()

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                }

                // Convert the byte array to Base64
                val videoData = output.toByteArray()
                val base64Video = Base64.encodeToString(videoData, Base64.DEFAULT)

                inputStream.close()
                output.close()
                Log.d("kol", "videoToBase64:1 "+base64Video)
                return base64Video
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("kol", "videoToBase64:2 "+e.message)
        }catch (e: Exception) {
            e.printStackTrace()
            Log.d("kol", "videoToBase642:3 "+e.message)
        }

        return null*/
    }

    fun Uri.getVideoPathFromContentUri(context: Context): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        var path: String? = null

        try {
            val cursor: Cursor? = context.contentResolver.query(this, projection, null, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex: Int = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                    path = it.getString(columnIndex)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return path
    }
    fun String.decodeBase64ToVideo(context: Context) {

        try {
            // Get the Downloads folder path
            val folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            // Ensure the folder exists
            /*val folder = File(folderPath)
            if (!folder.exists()) {
                folder.mkdirs()  // Create the folder if it doesn't exist
            }*/

            // Define the file name
            val fileName = "video_afterbase64.mp4"

            // Decode the Base64 string
            val decodedBytes = Base64.decode(this, Base64.DEFAULT)

            // Create the file and write the decoded bytes to it
            val file = File(folderPath, fileName)
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(decodedBytes)
            fileOutputStream.close()


        } catch (e: IOException) {
            e.printStackTrace()

        }
    }

    fun String.pdfToBase64(context: Context): String? {
        try {
            val contentResolver: ContentResolver = context.contentResolver
            val uri: Uri = Uri.parse(this)

            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val buffer = ByteArrayOutputStream()
                val bufferSize = 1024
                val data = ByteArray(bufferSize)
                var len: Int
                while (inputStream.read(data).also { len = it } != -1) {
                    buffer.write(data, 0, len)
                }
                inputStream.close()

                val bytes = buffer.toByteArray()
                return Base64.encodeToString(bytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun View.back(){
        this.setOnClickListener{
            findNavController().popBackStack()
        }

    }


    fun EditText.setupAmount() {
        /*val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            // Define your filter logic here
            val newText = dest.toString().substring(0, dstart) + source.subSequence(start, end) + dest.toString().substring(dend)
            if (newText.matches(Regex("^\\d{0,7}(\\.\\d{0,2})?\$"))) {
                null // Accept the input
            } else {
                "" // Reject the input
            }
        }*/

        //this.filters = arrayOf(inputFilter)
        this.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(INPUT_FILTER_MAX_VALUE, INPUT_FILTER_POINTER_LENGTH))

    }
    fun View.showDatePickerDialog(callBack: CallBack) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.context,
            R.style.MyDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                //val selectedDate = "$dayOfMonth-${month + 1}-$year" // +1 because months are zero-based
                val selectedDate = "$year-${month + 1}-$dayOfMonth" // +1 because months are zero-based
                callBack.getValue(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun String.getCurrentDate(callBack: CallBack) {
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        callBack.getValue(currentDate)
    }
    fun String.currentdate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendar.time)
        return currentDate
    }


    fun ImageView.setImage(uri: Uri) {
        Glide.with(this.context)
            .load(uri)
            .into(this)
    }

    fun rotateView(view: View, degrees: Float) {
        val rotation = ObjectAnimator.ofFloat(view, "rotation", degrees)
        rotation.duration = 500 // Adjust the duration as needed
        rotation.start()
    }

    fun WebView.set(url:String,postData:String){
        this.webViewClient = MyWebViewClient()
        //val url = "https://www.gibl.in/wallet/validate2/"
       // val postData = "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
        //val postData = "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
        this.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
        }
        Log.d("TAG_urlabcd", "set:A "+url)
        Log.d("TAG_urlabcd", "set:B *"+postData+"*")
        this.postUrl(url, postData.toByteArray())
    }

    fun WebView.set(url:String){
        this.webViewClient = MyWebViewClient()
        //val url = "https://www.gibl.in/wallet/validate2/"
        //val postData = "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
        this.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
        }
        this.postUrl(url, "".toByteArray())
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return false
        }
    }


    fun Uri.getFileNameFromUri(): String? {
        var fileName: String? = null
        context?.let { ctx ->
            when {
                "content".equals(this.scheme, ignoreCase = true) -> {
                    // If the URI scheme is "content"
                    val cursor: Cursor? = ctx.contentResolver.query(this, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val displayNameIndex: Int =
                                it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                            if (displayNameIndex != -1) {
                                fileName = it.getString(displayNameIndex)
                            }
                        }
                    }
                }

                "file".equals(this.scheme, ignoreCase = true) -> {
                    // If the URI scheme is "file"
                    fileName = this.lastPathSegment
                }

                else -> {

                        // For other URI schemes
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                            DocumentsContract.isDocumentUri(ctx, this)
                        ) {
                            // If it's a DocumentsProvider URI
                            val docId: String = DocumentsContract.getDocumentId(this)
                            val split: List<String> = docId.split(":")
                            if (split.size > 1) {
                                val contentUri: Uri = when (split[0]) {
                                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                    else -> throw IllegalArgumentException("Unsupported URI scheme")
                                }
                                val selection: String = "_id=?"
                                val selectionArgs: Array<String> = arrayOf(split[1])
                                val projection: Array<String> =
                                    arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
                                val cursor: Cursor? =
                                    ctx.contentResolver.query(
                                        contentUri,
                                        projection,
                                        selection,
                                        selectionArgs,
                                        null
                                    )
                                cursor?.use {
                                    if (it.moveToFirst()) {
                                        val displayNameIndex: Int =
                                            it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                                        if (displayNameIndex != -1) {
                                            fileName = it.getString(displayNameIndex)
                                        }
                                    }
                                }
                            }
                            else{
                                fileName=this.toString()
                            }
                        }
                    else{
                            fileName=this.toString()
                        }


                }
            }
        }
        return fileName
    }

    fun Uri.getFileNameAndTypeFromUri(context: Context): Pair<String?, String?> {
        var fileName: String? = null
        var fileType: String? = null

        context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                val mimeTypeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)

                fileName = cursor.getString(displayNameIndex)
                fileType = cursor.getString(mimeTypeIndex)
            }
        }

        if (fileName.isNullOrEmpty()) {
            fileName = this.lastPathSegment
        }

        return Pair(fileName, fileType)
    }

    fun Fragment.hideKeyBoard(view:View){
        activity?.let {act->
            val inputMethodManager = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            val focusedView: View? = view

            focusedView?.let {
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }

    }

    fun EditText.oem(view:View){
        this.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.performClick()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    fun String.testDataFile(): Boolean {
        val fileName="big9_file_test.txt"
        val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Check if external storage is available
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            try {
                val file = File(downloadFolder, fileName)
                val outputStream = FileOutputStream(file)
                outputStream.write(this.toByteArray())
                outputStream.close()
                return true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return false
    }
    fun View.takeScreenshot2(): Bitmap {
        // Create a Bitmap with the same dimensions as the View
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)

        // Create a Canvas to draw the View onto the Bitmap
        val canvas = Canvas(bitmap)

        // Draw the View onto the Canvas
        this.draw(canvas)

        return bitmap
    }

    fun String.encrypt(): String {
        val fixedIV = if (AES_IV.length < 16) AES_IV + " ".repeat(16 - AES_IV.length) else AES_IV.substring(0, 16)

        val keySpec = SecretKeySpec(AES_KEY.toByteArray(Charsets.UTF_8),
            AES_ALGORITHM
        )
        val ivParameterSpec = IvParameterSpec(fixedIV.toByteArray(Charsets.UTF_8))

        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)

        val encryptedBytes = cipher.doFinal(this.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun showLogDcriptData( s:String?){
        s?.let {
            Log.e("dcriptData",s.decrypt())
        }
    }
    fun showLogDcriptData( s:String?,tag:String){
        s?.let {
            Log.e(tag,s.decrypt())
        }
    }
    fun showLog( s:String?){
        s?.let {
            Log.e("dcriptData",s.decrypt())
        }
    }
    fun showLog( s:String?,tag:String){
        s?.let {
            Log.e(tag,s.decrypt())
        }
    }



    fun String.decrypt(): String {
        val fixedIV = if (AES_IV.length < 16) AES_IV + " ".repeat(16 - AES_IV.length) else AES_IV.substring(0, 16)

        val keySpec = SecretKeySpec(AES_KEY.toByteArray(Charsets.UTF_8),
            AES_ALGORITHM
        )
        val ivParameterSpec = IvParameterSpec(fixedIV.toByteArray(Charsets.UTF_8))

        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)

        val encryptedBytes = Base64.decode(this, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkBasePermission(context:Context):Boolean {
        var hasPermission=false
        if (!PermissionUtils.hasVideoRecordingPermissions(context)) {


            PermissionUtils.requestVideoRecordingPermission(context, object :
                PermissionsCallback {
                override fun onPermissionRequest(granted: Boolean) {
                    if (!granted) {
                        hasPermission= false

                    } else {

                        hasPermission= true
                    }

                }

            })

        }
        return hasPermission
    }

    fun View.setBottonLoader(isVisible:Boolean,view:View){
        //this.isVisible=isVisible
        //view.isVisible=!this.isVisible
    }

    fun Uri.getImageSizeInMb(contentResolver: ContentResolver): Double? {
        contentResolver.openInputStream(this)?.use { inputStream ->
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)
            val fileSizeInBytes = inputStream.available().toDouble()
            val fileSizeInMb = fileSizeInBytes / (1024.0 * 1024.0) // Convert bytes to megabytes
            return fileSizeInMb
        }
        return null
        /*
        val contentResolver: ContentResolver = // Your ContentResolver instance

        val imageSizeInMb = uri.getImageSizeInMb(contentResolver)
         */
    }


    fun Uri.getImageSize(context: Context): String {
        val contentResolver: ContentResolver = context.contentResolver

        // Get the file's display name and size
        contentResolver.query(this, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                val displayName = cursor.getString(displayNameIndex)
                val size = cursor.getLong(sizeIndex)

                // Convert the size to appropriate units
                val sizeString = when {
                    size >= 1e9 -> "${(size / 1e9).roundToInt()} GB"
                    size >= 1e6 -> "${(size / 1e6).roundToInt()} MB"
                    size >= 1e3 -> "${(size / 1e3).roundToInt()} KB"
                    else -> "$size bytes"
                }

                return "File: $displayName\nSize: $sizeString"
            }
        }

        return "File not found"
    }


    fun String.formatAsIndianCurrency(): String {
        if (this.isNullOrEmpty()) {
            return "0.00"
        }

        return try {
            val number = this.toDoubleOrNull() ?: 0.00
            val decimalFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale("en", "IN")))
            decimalFormat.format(number)
        } catch (e: NumberFormatException) {
            "0.00"
        }
    }
//    fun View.hideKeyboard( ) {
//
//            val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(windowToken, 0)
//
//
//    }


//     fun View.setupUI() {
//
//        // Set up touch listener for non-text box views to hide keyboard.
//        if (view !is EditText) {
//            this.setOnTouchListener { v, event ->
//                hideSoftKeyBoard(this)
//                false
//            }
//        }
//
//        //If a layout container, iterate over children and seed recursion.
//        if (this is ViewGroup) {
//            for (i in 0 until this.childCount) {
//                val innerView = this.getChildAt(i)
//                innerView.setupUI()
//            }
//        }
//    }

    fun View.hideSoftKeyBoard(activity: Activity?){
        //val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.hideSoftInputFromWindow(v.windowToken, 0)

            hideKeyboardByClickingOutside(this,activity)


    }

    fun hideKeyboardByClickingOutside(view: View, activity: Activity?) {
        //First time hide keyboard
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideSoftKeyboard(activity)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView: View = view.getChildAt(i)
                hideKeyboardByClickingOutside(innerView, activity)
            }
        }
    }
    private fun hideSoftKeyboard(activity: Activity?) {
        activity?.let {
            val inputMethodManager: InputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus?.windowToken,
                    0
                )
            }
        }
    }


}






data class TempData(val pathName:String,val methodName:String,val modelName:String,)
data class TempRepository(var fileName:String?)
fun main() {
    val filen="HomeFragment"
    val f= TempRepository("AuthRepositoryRepository.kt")
    var a=  TempData("v1/services/aeps/check_merchant","checkMerchant","checkMerchantModel")
    temp(a,f,filen)
}
fun temp(tempData: TempData,tempRepository:TempRepository?=null,fragmentFileName:String) {
    //val baseFilePath = "E:/android_project/big9/project/big9/"
    val baseFilePath = "D:/big9/big9"
    val middlePath = "app/src/main/java/com/big9/app/"
    var endPath = "network/"
    var endPathFileName = "RetroApi.kt"

    var filePath = "$baseFilePath$middlePath$endPath$endPathFileName"
    val templateText = "@POST(\"${tempData.pathName}\")\n" +
            "    suspend fun ${tempData.methodName}(\n" +
            "        @Header(\"Authtoken\") token: String,\n" +
            "        @Body data: String\n" +
            "    ): Response<${tempData.modelName}>\n"

    var file = File(filePath)
    if (file.exists()) {
        var existingContent = file.readText()
        var lastIndex = existingContent.lastIndexOf('}')
        if (lastIndex != -1) {
            val updatedContent = StringBuilder(existingContent).apply {
                insert(lastIndex, "\n$templateText")
            }.toString()
            file.writeText(updatedContent)
            println("File '$filePath' has been updated with the template text.")


            endPath = "data/model/newModel/"
            endPathFileName = "${tempData.modelName}.kt"
            filePath = "$baseFilePath$middlePath$endPath$endPathFileName"

            val templateText = """
        import com.google.gson.annotations.SerializedName

        class ${tempData.modelName} {
            @SerializedName("userid")
            var userid: String? = null
            @SerializedName("status")
            var status: String? = null
        }
    """.trimIndent()
            //writeFileData(templateText,filePath)
            var file = File(filePath)
            if (!file.exists()) {
                file.createNewFile()
            }

            file.writeText(templateText)


            println("File '$filePath' has been created with the template text.")
        } else {
            println("File '$filePath' does not contain a closing brace '}'.")
        }
    } else {
        println("File '$filePath' does not exist.")
    }

    // repository
    endPath = "repository/"

    endPathFileName = "AuthRepositoryRepository.kt"
    filePath = "$baseFilePath$middlePath$endPath$endPathFileName"
    var file2 = File(filePath)
    if (file2.exists()) {
        var existingContent = file2.readText()
        var lastIndex = existingContent.lastIndexOf('}')
        if (lastIndex != -1) {



            val templateText2 = """
        	//${tempData.methodName}
    private val _${tempData.methodName}ResponseLiveData =
        MutableLiveData<ResponseState<${tempData.modelName}>>()
    val ${tempData.methodName}ResponseLiveData: LiveData<ResponseState<${tempData.modelName}>>
        get() = _${tempData.methodName}ResponseLiveData


    suspend fun ${tempData.methodName}(token: String, loginModel: String) {
        _${tempData.methodName}ResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.${tempData.methodName}(token, loginModel.replace("\\n", "").replace("\\r", ""))
            _${tempData.methodName}ResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _${tempData.methodName}ResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    """.trimIndent()
            // writeFileData(templateText2,filePath)
            val updatedContent = StringBuilder(existingContent).apply {
                insert(lastIndex, "\n$templateText2")
            }.toString()

            println("File '$filePath' has been updated with the template text.")
            file2.writeText(updatedContent)


            println("File '$filePath' has been created with the template text.")
        } else {
            println("File '$filePath' does not contain a closing brace '}'.")
        }
    } else {
        println("File '$filePath' does not exist.")
    }



    //viewmodel

    endPath = "data/viewMovel/"

    endPathFileName = "MyViewModel.kt"
    filePath = "$baseFilePath$middlePath$endPath$endPathFileName"
    var file3 = File(filePath)
    if (file3.exists()) {
        var existingContent = file3.readText()
        var lastIndex = existingContent.lastIndexOf('}')
        if (lastIndex != -1) {



            val templateText2 = """
      //${tempData.methodName}
    val ${tempData.methodName}ResponseLiveData: LiveData<ResponseState<${tempData.modelName}>>
        get() = repository.${tempData.methodName}ResponseLiveData
    fun ${tempData.methodName}(token: String, data: String) {
        viewModelScope.launch {
            repository.${tempData.methodName}(token,data)
        }
    }


    """.trimIndent()
            // writeFileData(templateText2,filePath)
            val updatedContent = StringBuilder(existingContent).apply {
                insert(lastIndex, "\n$templateText2")
            }.toString()

            println("File '$filePath' has been updated with the template text.")
            file3.writeText(updatedContent)


            println("File '$filePath' has been created with the template text.")
        } else {
            println("File '$filePath' does not contain a closing brace '}'.")
        }
    } else {
        println("File '$filePath' does not exist.")
    }


    //replace
    // Replace placeholders in a file in the ui/fragment directory
    endPath = "ui/fragment/"
    endPathFileName = "$fragmentFileName.kt"
    filePath = "$baseFilePath$middlePath$endPath$endPathFileName"
    val fragmentTemplateText1 = """
        
        myViewModel?.${tempData.methodName}ResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }
                is ResponseState.Success -> {
                    loader?.dismiss()
                }
                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
    """.trimIndent()
    val fragmentTemplateText2 = """
        
        val (isLogin, loginResponse) = sharedPreff.getLoginData()
        loginResponse?.let { loginData ->
            loginData.userid?.let {
                val data = mapOf(
                    "userid" to loginData.userid,
                    "startdate" to "01-12-2023",
                    "enddate" to "15-12-2023",
                )
                val gson = Gson()
                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {
                    myViewModel?.${tempData.methodName}(it, jsonString.encrypt())
                }
            }
        }
    """.trimIndent()
    replaceInFile(filePath, "temp1", fragmentTemplateText2)
    replaceInFile(filePath, "temp2", fragmentTemplateText1)

}
fun replaceInFile(filePath: String, target: String, replacement: String) {
    val file = File(filePath)
    if (file.exists()) {
        val content = file.readText()
        val updatedContent = content.replace(target, replacement)
        file.writeText(updatedContent)
        println("File '$filePath' has been updated with the replacements.")
    } else {
        println("File '$filePath' does not exist.")
    }
}





