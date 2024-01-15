package com.big9.app.ui.fragment.regandkyc



import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.FragmentDocuploadBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.fragment.CameraDialog
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.isAfterReg
import com.big9.app.utils.helpers.Constants.isBackCamera
import com.big9.app.utils.helpers.Constants.isGallary
import com.big9.app.utils.helpers.Constants.isPdf
import com.big9.app.utils.helpers.Constants.isVideo
import com.big9.app.utils.helpers.Constants.videoUri
import com.big9.app.utils.helpers.SharedPreff
import com.big9.app.utils.`interface`.CallBack

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class DocuploadFragment : BaseFragment() {
    lateinit var binding: FragmentDocuploadBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private var loader: Dialog? = null
    private val VIDEO_CAPTURE = 101
    var textView: TextView?=null
    var type=""
    @Inject
    override lateinit var sharedPreff: SharedPreff
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docupload, container, false)
        binding.viewModel = authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    override fun onResume() {
        super.onResume()
        if (isVideo){
            loader?.let {
                it.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (videoUri!=null){
                        authViewModel.videoFilePath.value=videoUri
                    }
                    authViewModel.videokycBase64.value=authViewModel.videoFilePath.value?.getVideoPathFromContentUri(binding.root.context)?.videoToBase64()
                    authViewModel.videokyc.value=authViewModel.videoFilePath.value?.getFileNameFromUri()
                    it.dismiss()
                    //authViewModel.videokycBase64.value.toString().testDataFile()
                    //authViewModel?.videokycBase64?.value?.decodeBase64ToVideo(binding.root.context)
                    isVideo=false
                },15000)
            }
        }
    }
    private fun onViewClick() {
        activity?.let {act->
            binding.apply {
                rootView.setOnClickListener {
                    activity?.let { act -> rootView.hideSoftKeyBoard(act) }
                }
                llVideoKyc.setOnClickListener{
                    isGallary=false
                    isVideo=true
                    isBackCamera=false
                    /*val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

                    // Set the maximum video duration in seconds (30 minutes = 30 * 60 seconds)
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5000)

                    // Start the video capture activity
                    startActivityForResult(intent, VIDEO_CAPTURE)*/
                    findNavController().navigate(R.id.action_docuploadFragment_to_cameraFragment)
                }



                llPartnerPanId.setOnClickListener{
                    isBackCamera=true
                    type="pan"
                    isPdf=false
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llCpan.setOnClickListener{
                    isBackCamera=true
                    isPdf=false
                    type="cpan"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llPaadhar.setOnClickListener{
                    isBackCamera=true
                    isPdf=false
                    type="paadhar"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }
                llPartnerAadharBack.setOnClickListener{
                    isBackCamera=true
                    isPdf=false
                    type="PartnerAadharBack"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }
                llGst.setOnClickListener{
                    isBackCamera=true
                    isPdf=true
                    type="llGst"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llCertificateOfIncorporation.setOnClickListener{
                    isBackCamera=true
                    isPdf=true
                    type="llCertificateOfIncorporation"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }
                llBoardResolution.setOnClickListener{
                    isBackCamera=true
                    isPdf=true
                    type="llBoardResolution"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }
                llTrade.setOnClickListener{
                    isBackCamera=true
                    isPdf=true
                    type="llTrade"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llUserSelfi.setOnClickListener{
                    isBackCamera=false
                    isPdf=false
                    type="llUserSelfi"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },true)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llCselfi.setOnClickListener{
                    isBackCamera=false
                    isPdf=false
                    type="llCselfi"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    },true)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                btnNext.setOnClickListener {
                    authViewModel.apply {
                        try {
                            if (videoUri!=null){
                                authViewModel.videoFilePath.value=videoUri
                            }
                            authViewModel.videokycBase64.value=authViewModel.videoFilePath.value?.getVideoPathFromContentUri(binding.root.context)?.videoToBase64()
                            authViewModel.videokyc.value=authViewModel.videoFilePath.value?.getFileNameFromUri()



                            //binding.tvPancardVideoKycImage.setText(authViewModel.filePath.toString())
                            /*videoFile.value?.file =
                                filePath.value?.toString()
                                    ?.videoToBase64(binding.root.context)
                                    .toString()*/
                            /*  authViewModel.videokycBase64.value=filePath.value?.getVideoPathFromContentUri(binding.root.context)?.
                                  videoToBase64()
                                  .toString()*/


                            if (docValidation()) {
                                //  sharedPreff?.setLoginData()

                                /*val documentUploadModel = DocumentUploadModel(
                                    panPathBase64= panPathBase64.value*//*?.encrypt()*//*,
                                cpanPathBase64=cpanPathBase64.value*//*?.encrypt()*//*,
                                paadharBase64=paadharBase64.value*//*?.encrypt()*//*,
                                partnerAadharBackBase64=PartnerAadharBackBase64.value?.encrypt(),
                                llGstBase64=llGstBase64.value*//*?.encrypt()*//*,
                                llCertificateOfIncorporationBase64=llCertificateOfIncorporationBase64.value*//*?.encrypt()*//*,
                                llBoardResolutionBase64=llBoardResolutionBase64.value*//*?.encrypt()*//*,
                                llTradeBase64=llTradeBase64 . value*//*?.encrypt()*//*,
                                llUserSelfiBase64=llUserSelfiBase64.value*//*?.encrypt()*//*,
                                llCselfiBase64=llCselfiBase64 . value*//*?.encrypt()*//*,
                                videokycBase64=videokycBase64.value*//*?.encrypt()*//*,
                            )*/

                                val (isLogin, loginResponse) =sharedPreff.getLoginData()
                                authViewModel?.apply {
                                    val data = mapOf(
                                        "userid" to loginResponse?.userid?.toString(),

                                        )
                                    /* var panimagedata=
                                         authViewModel.panPathBase64.value?.let { createImagePart("panimagedata", it) }
                                     var aadharfrontimagedata=
                                         authViewModel.paadharBase64.value?.let { createImagePart("aadharfrontimagedata", it) }

                                     var aadharbackimagedata=
                                         authViewModel.PartnerAadharBackBase64.value?.let { createImagePart("aadharbackimagedata", it) }
     */

                                    var partnerPanCard=
                                        authViewModel.panPathBase64.value?.let { createImagePart("partnerPanCard", it) }
                                    var companyPanCard=
                                        authViewModel.cpanPathBase64.value?.let { createImagePart("companyPanCard", it) }
                                    var partnerAadhaarFront=
                                        authViewModel.paadharBase64.value?.let { createImagePart("partnerAadhaarFront", it) }
                                    var partnerAadhaarBack=
                                        authViewModel.PartnerAadharBackBase64.value?.let { createImagePart("partnerAadhaarBack", it) }
                                    var gstin=
                                        authViewModel.llGstBase64.value?.let { createPdfPart("gstin", it) }
                                    var coi=
                                        authViewModel.llCertificateOfIncorporationBase64.value?.let { createPdfPart("coi", it) }
                                    var boardResolution=
                                        authViewModel.llBoardResolutionBase64.value?.let { createPdfPart("boardResolution", it) }
                                    var tradeLicense=
                                        authViewModel.llTradeBase64.value?.let { createPdfPart("tradeLicense", it) }
                                    var userSelfi=
                                        authViewModel.llUserSelfiBase64.value?.let { createImagePart("userSelfi", it) }
                                    var userScp=
                                        authViewModel.llCselfiBase64.value?.let { createImagePart("userScp", it) }
                                    /* var videoKyc=
                                         authViewModel.videokycBase64.value?.let { createVideoPart("videoKyc", it) }*/
                                    var videoKyc=
                                        authViewModel.videokycBase64.value?.let { it.toMultipartRequestBody() }
                                    /*

                                                                    try {
                                                                        val file = File(context?.filesDir, "testingabc.txt")
                                                                        val fileOutputStream = FileOutputStream(file)
                                                                        fileOutputStream.write(authViewModel.videokycBase64.value?.toByteArray())
                                                                        fileOutputStream.close()
                                                                        println("Data has been written to ${file.absolutePath}")
                                                                    } catch (e: IOException) {
                                                                        println("An error occurred: $e")
                                                                    }

                                                                    try {
                                                                        val decodedBytes = Base64.decode(authViewModel.videokycBase64.value, Base64.DEFAULT)
                                                                        val file = File(context?.filesDir, "fileName.mp4")
                                                                        val fileOutputStream = FileOutputStream(file)
                                                                        fileOutputStream.write(decodedBytes)
                                                                        fileOutputStream.close()
                                                                        println("Video file saved as ${file.absolutePath}")
                                                                    } catch (e: IOException) {
                                                                        println("An error occurred: $e")
                                                                    }
                                    */


                                    val gson= Gson()
                                    var jsonString = gson.toJson(data)
                                    loginResponse?.AuthToken?.let {
                                        var encriptData=jsonString.encrypt()
                                        //authViewModel?.onboardingBasicinfo(it,jsonString.encrypt())
                                        //if (panimagedata != null) {
                                        authViewModel?.documentUpload(it,encriptData,partnerPanCard,companyPanCard,partnerAadhaarFront,partnerAadhaarBack,gstin,
                                            coi,boardResolution,userSelfi,tradeLicense,userScp,videoKyc)
                                        //}
                                    }
                                    /*
                                                            val regModel = RegForm(
                                                                name = authViewModel.name.value,
                                                                mobile = authViewModel.mobile.value,
                                                                alternativeMobile = authViewModel.alternativeMobile.value,
                                                                email = authViewModel.email.value,
                                                                address = authViewModel.address.value,
                                                                pinCode = authViewModel.pinCode.value,
                                                                dateOfBirth = authViewModel.dateOfBirth.value,
                                                                state = authViewModel.state.value,
                                                                city = authViewModel.city.value,
                                                                area = authViewModel.area.value,
                                                                aadhar = authViewModel.aadhar.value,
                                                                panCardNo = authViewModel.panCardNo.value,
                                                                llPanBase64 = authViewModel.llPanBase64.value,
                                                                llCpanBase64 = authViewModel.llCpanBase64.value,
                                                                llBpanBase64 = authViewModel.llBpanBase64.value,
                                                                gender=authViewModel.genderReg.value
                                                            )*/
                                }



                                //json.toString().testDataFile()
                                /*val regModel = DocumentUploadModel(
                                    panPathBase64= panPathBase64.value,
                                    cpanPathBase64=cpanPathBase64.value,
                                    paadharBase64=paadharBase64.value,
                                    partnerAadharBackBase64=PartnerAadharBackBase64.value,
                                    llGstBase64=llGstBase64.value,
                                    llCertificateOfIncorporationBase64=llCertificateOfIncorporationBase64.value,
                                    llBoardResolutionBase64=llBoardResolutionBase64.value,
                                    llTradeBase64=llTradeBase64 . value,
                                    llUserSelfiBase64=llUserSelfiBase64.value,
                                    llCselfiBase64=llCselfiBase64 . value,
                                    videokycBase64=videokycBase64.value,
                                )*/
                                /*cpanPathBase64
                                llCselfiBase64
                                llUserSelfiBase64
                                paadharBase64
                                panPathBase64
                                partnerAadharBackBase64
                                 */

                                //startActivity(Intent(requireActivity(), DashboardActivity::class.java))
                                //***
                                /* val intent = Intent(requireActivity(), DashboardActivity::class.java)
                                 intent.putExtra(isAfterReg,true)
                                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                 requireActivity().startActivity(intent)*/

                                //****
                                //authViewModel.documentRegistration(documentUploadModel)

                                //Toast.makeText(binding.root.context, "Ok", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(binding.root.context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }


    // Function to convert a video file URI to Base64
    fun uriToBase64(context: Context, videoUri: Uri): String {
        val contentResolver: ContentResolver = context.contentResolver

        // Open an input stream from the content resolver
        val inputStream: InputStream? = contentResolver.openInputStream(videoUri)

        // Use a ByteArrayOutputStream to read the data from the input stream
        val buffer = ByteArray(8192) // You can adjust the buffer size as needed
        val outputStream = ByteArrayOutputStream()

        try {
            var bytesRead: Int
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        } finally {
            inputStream?.close()
        }

        // Convert the bytes to a Base64 encoded string
        val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

        return base64String
    }

    private fun getImage(s:String) {
        when(s){
            "g"->{
                isVideo=false
                isGallary=true
                if (!isPdf) {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                else{
                    //findNavController().navigate(R.id.action_docuploadFragment_to_cameraFragment)
                    findNavController().navigate(R.id.action_docuploadFragment_to_pdfCaptureFragment)
                }
                //
            }
            "t"->{
                isVideo=false
                isGallary=false
                findNavController().navigate(R.id.action_docuploadFragment_to_cameraFragment)
            }

        }
    }

    fun initView() {
        binding.root.context?.let {
            sharedPreff = SharedPreff(context)
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }

    }

    fun setObserver() {

        authViewModel?.apply {
            filePath.observe(viewLifecycleOwner){

                when(type){
                    "pan"->{
                        panPathBase64.value=it?.uriToBase64(binding.root.context.contentResolver)
                        panPath.value=it?.getFileNameFromUri()
                    }
                    "cpan"->{
                        cpanPathBase64.value=it?.uriToBase64(binding.root.context.contentResolver)
                        cpanPath.value=it?.getFileNameFromUri()
                    }
                    "paadhar"->{
                        paadharBase64.value=it?.uriToBase64(binding.root.context.contentResolver)
                        paadhar.value=it?.getFileNameFromUri()
                    }
                    "PartnerAadharBack"->{
                        PartnerAadharBackBase64.value=it?.uriToBase64(binding.root.context.contentResolver)
                        PartnerAadharBack.value=it?.getFileNameFromUri()
                    }
                    "llGst"->{
                        llGstBase64.value=it?.toString()?.pdfToBase64(binding.root.context)
                        llGst.value=it?.getFileNameFromUri()
                    }
                    "llCertificateOfIncorporation"->{
                        llCertificateOfIncorporationBase64.value=it?.toString()?.pdfToBase64(binding.root.context)
                        llCertificateOfIncorporation.value=it?.getFileNameFromUri()
                    }
                    "llBoardResolution"->{
                        llBoardResolutionBase64.value=it?.toString()?.pdfToBase64(binding.root.context)
                        llBoardResolution.value=it?.getFileNameFromUri()
                    }
                    "llTrade"->{
                        llTradeBase64.value=it?.toString()?.pdfToBase64(binding.root.context)
                        llTrade.value=it?.getFileNameFromUri()
                    }
                    "llUserSelfi"->{
                        llUserSelfiBase64.value=it?.uriToBase64(binding.root.context.contentResolver)
                        llUserSelfi.value=it?.getFileNameFromUri()
                    }
                    "llCselfi"->{
                        llCselfiBase64.value=it?.uriToBase64(binding.root.context.contentResolver)
                        llCselfi.value=it?.getFileNameFromUri()
                    }


                }

                //Log.d("TAG_file", "true setObserver: "+it.uriToBase64(binding.root.context.contentResolver))
            }
            documentUploadResponseLiveData?.observe(viewLifecycleOwner){
                when (it) {
                    is ResponseState.Loading -> {
                        loader?.show()
                    }

                    is ResponseState.Success -> {
                        loader?.dismiss()
                        val intent = Intent(requireActivity(), DashboardActivity::class.java)
                        //intent.putExtra(isAfterReg,true)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        requireActivity().startActivity(intent)

                    }

                    is ResponseState.Error -> {
                        loader?.dismiss()
                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val videoUri = data?.data

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "Video saved to:\n"
                        + videoUri, Toast.LENGTH_LONG).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "Video recording cancelled.",
                    Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Failed to record video",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri != null) {
            authViewModel.filePath.value = uri

            //findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        } else {
            authViewModel.filePath.value =Uri.parse("/")

            Log.d("PhotoPicker", "No media selected")
        }

    }

    fun createImagePart(name: String, base64String: String): MultipartBody.Part {
        val bytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, "image.jpg", requestBody)
    }
    fun createPdfPart(name: String, base64String: String): MultipartBody.Part {
        val bytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        val requestBody = bytes.toRequestBody("application/pdf".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, "document.pdf", requestBody)
    }

    fun createVideoPart(name: String, base64String: String): MultipartBody.Part {
        val bytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        val requestBody = bytes.toRequestBody("video/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, "video.mp4", requestBody)
    }


    fun String.sendMultipartRequest(): MultipartBody {


        // Convert Base64 string to bytes
        val decodedBytes = android.util.Base64.decode(this, android.util.Base64.DEFAULT)

        // Create a request body from the decoded bytes
        val requestBody = decodedBytes.toRequestBody("video/mp4".toMediaTypeOrNull())

        // Create a multipart body with the video part
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("videoKyc", "video.mp4", requestBody)
            .build()

        return multipartBody


    }

    fun String.toMultipartRequestBody(): MultipartBody.Part {
        // Convert Base64 string to bytes
        val decodedBytes = android.util.Base64.decode(this, android.util.Base64.DEFAULT)

        // Create a request body from the decoded bytes
        val requestBody = decodedBytes.toRequestBody("video/mp4".toMediaTypeOrNull())

        // Create a multipart body part with the video data
        return MultipartBody.Part.createFormData("videoKyc", "video.mp4", requestBody)
    }

}