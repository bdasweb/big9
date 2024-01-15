package com.big9.app.ui.fragment.regandkyc

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R


import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.FragmentCameraBinding
import com.big9.app.databinding.FragmentPdfBinding


import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.helpers.Constants.contentValues
import com.big9.app.utils.helpers.Constants.isBackCamera
import com.big9.app.utils.helpers.Constants.isGallary
import com.big9.app.utils.helpers.Constants.isPdf
import com.big9.app.utils.helpers.Constants.isVideo
import com.big9.app.utils.helpers.PermissionUtils
import com.big9.app.utils.helpers.PermissionUtils.createAlertDialog
import com.big9.app.utils.`interface`.PermissionsCallback
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


class PdfCaptureFragment : BaseFragment() {
    val TAG = "camera"
    @RequiresApi(Build.VERSION_CODES.P)



    lateinit var binding: FragmentPdfBinding
    private val authViewModel: AuthViewModel by activityViewModels()


    private lateinit var pickPdfLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pdf, container, false)
        binding.viewModel = authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        viewOnClick()
    }

    private fun viewOnClick() {


    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun init() {

            //pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))



                    pickPdfLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { result: Uri? ->
                        if (result != null) {
                            authViewModel?.filePath?.value = result
                            findNavController().popBackStack()
                        }
                    }



            val mimeTypes = arrayOf("application/pdf")
            pickPdfLauncher.launch(mimeTypes)




    }

    fun onViewClick() {


    }


}