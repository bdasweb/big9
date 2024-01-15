package com.big9.app.ui.fragment

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.CameraBottomsheetLayoutBinding

import com.big9.app.ui.base.BaseBottomSheetFragment
import com.big9.app.utils.helpers.Constants.isPdf
import com.big9.app.utils.`interface`.CallBack

class CameraDialog(val callBack: CallBack,val isSelfi:Boolean) : BaseBottomSheetFragment() {
    lateinit var binding: CameraBottomsheetLayoutBinding
    private val authViewModel: AuthViewModel by activityViewModels()


    val TAG = "camera"

    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown: Long = 15000
    private val countDownInterval: Long = 1000


    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.camera_bottomsheet_layout, container, false)
        binding.viewModel = authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun getTheme(): Int {
        return R.style.SheetDialog
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {
        binding.apply {
            if (isSelfi){
                imgGallary.visibility=View.GONE
                tvGallery.visibility=View.GONE
            }
            else{
                imgGallary.visibility=View.VISIBLE
                tvGallery.visibility=View.VISIBLE
            }
            imgGallary.setOnClickListener{
                callBack.getValue("g")
                dismiss()
            }
            tvGallery.setOnClickListener{
                callBack.getValue("g")
                dismiss()
            }
            imgTakephoto.setOnClickListener{
                callBack.getValue("t")
                dismiss()
            }
            tvTakePhoto.setOnClickListener{
                callBack.getValue("t")
                dismiss()
            }

        }

    }

    private fun setObserver() {

    }

    private fun initView() {
        binding.apply {
            if (isPdf){
                tvGallery.setText("Choose a PDF file.")
                imgTakephoto.visibility=View.GONE
                tvTakePhoto.visibility=View.GONE
            }
            else{
                binding.tvGallery.setText("Choose from gallery")
                imgTakephoto.visibility=View.VISIBLE
                tvTakePhoto.visibility=View.VISIBLE
            }
        }

    }

}