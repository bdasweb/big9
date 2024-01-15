package com.big9.app.ui.fragment.paymentRequest


import android.app.Dialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentPaymentRequestImformationBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.fragment.CameraDialog
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.isDenomination
import com.big9.app.utils.helpers.Constants.isGallary
import com.big9.app.utils.helpers.Constants.isIsPaySlip
import com.big9.app.utils.helpers.Constants.isVideo
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PaymentRequestImformationFragment : BaseFragment() {
    lateinit var binding: FragmentPaymentRequestImformationBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var authViewModel: AuthViewModel?=null
    private var loader: Dialog? = null
    var formType=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_request_imformation, container, false)
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

    override fun onResume() {
        super.onResume()
        authViewModel?.filePath?.observe(viewLifecycleOwner) {
            if (isIsPaySlip) {

                viewModel.paySleeyUriUri=it
                viewModel.paySleeyUriUri?.let {iUri->
                    viewModel.paySleeyUri = iUri.uriToBase64(binding.root.context.contentResolver)
                }
                /*viewModel.paySleeyUriUri = it
                viewModel.paySleeyUri = it.uriToBase64(binding.root.context.contentResolver)


                //denomSlipUri=it
                binding.imgPlaySlip.setImage(it)*/


                isIsPaySlip = false
                isDenomination = false
            }

            if (isDenomination) {

                viewModel.denomSlipUriUri=it
                viewModel.denomSlipUriUri?.let {iUri->
                    viewModel.denomSlipUri = iUri.uriToBase64(binding.root.context.contentResolver)
                }


                isIsPaySlip = false
                isDenomination = false
            }
            viewModel.paySleeyUriUri?.let { iUri ->
                binding.imgPlaySlip.setImage(iUri)
            }
            viewModel.denomSlipUriUri?.let { iUri ->
                binding.imgDoc.setImage(iUri)
            }

        }

    }
    private fun onViewClick() {
        binding.apply {
            imgBack.back()
            imgPlaySlip.setOnClickListener{tvUploadPayslip.performClick()}
            tvUploadPayslip.setOnClickListener{
                activity?.let {act->
                    Constants.isBackCamera =true
                    isIsPaySlip=true
                    isDenomination=false
                    Constants.isPdf =false
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {
                            getImage(s,true)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)

                }
                }
            tvDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        //tvDob.text = s
                        viewModel?.depositeDate?.value=s
                        viewModel?.depositeDateErrorVisible?.value =false
                        viewModel?.depositeDateError?.value=""
                    }

                })
            }
            btnSubmit.setOnClickListener{
                if (viewModel?.PaymentrequestValidation() == true){
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    loginResponse?.let { loginData ->
                        loginData.userid?.let {
                            val data = mapOf(
                                "userid" to loginData.userid,

                            "bankid" to viewModel?.selectedBankId?.value,
                            "ftype" to formType,
                            "prmode" to "",
                            "depositamount" to viewModel?.paymentAmt?.value,
                            "depositdate" to viewModel?.depositeDate?.value,
                            "bankreference" to viewModel?.transActionId?.value,
                            "remarks" to viewModel?.particular?.value,
                            "txtquantity10" to viewModel?.denomination_10?.value,
                            "txtquantity20" to viewModel?.denomination_20?.value,
                            "txtquantity50" to viewModel?.denomination_50?.value,
                            "txtquantity100" to viewModel?.denomination_100?.value,
                            "txtquantity200" to viewModel?.denomination_200?.value,
                            "txtquantity500" to viewModel?.denomination_500?.value,
                            "txtquantity2000" to viewModel?.denomination_2000?.value,

                            )

                            var PaymentSlip=
                                //paySleeyUri?.let { it.createImagePart("paymentSlip") }
                                viewModel?.paySleeyUri?.let { createImagePart("paymentSlip", it) }


                            var denomSlip=
                                //paySleeyUri?.let { it.createImagePart("paymentSlip") }
                                viewModel?.paySleeyUri?.let { createImagePart("denomSlip", it) }
                           /* var denomSlip=
                                denomSlipUri?.let { it.createImagePart("denomSlip")  }*/


                            val gson =  Gson()
                            var jsonString = gson.toJson(data);
                            loginData.AuthToken?.let {
                                val data=jsonString.encrypt()
                                showLog(data)
                                viewModel?.PaymentRequist(it,data,PaymentSlip,denomSlip)
                            }
                        }
                    }
                    //Toast.makeText(requireActivity(), "Ok", Toast.LENGTH_SHORT).show()
                }
            }

            //denomination
            imgDoc.setOnClickListener{tvUploadDoc.performClick()}
            tvUploadDoc.setOnClickListener{
                activity?.let {act->
                    Constants.isBackCamera =true
                    isIsPaySlip=false
                    isDenomination=true
                    Constants.isPdf =false
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {
                            getImage(s, false)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)

                }
            }
        }



    }

    private fun getImage(s: String, b: Boolean) {
        when(s){
            "g"->{
                isVideo =false
                isGallary =true
                if (b) {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                else{
                    pickMediaDenomination.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                //findNavController().navigate(R.id.action_regFragment_to_cameraFragment)
            }
            "t"->{

                isVideo =false
                isGallary =false
                findNavController().navigate(R.id.action_paymentRequestImformationFragment_to_cameraFragment2)
            }

        }
    }

    fun initView() {

        viewModel?.selectedBankMode?.observe(viewLifecycleOwner){
            binding.apply {
                if (it.trim()=="CASH CDM" || it.trim()=="CASH - COUNTER DEPOSIT" ){
                    formType="F2"
                    imgDoc.visibility=View.VISIBLE
                    tvUploadDoc.visibility=View.VISIBLE
                    llDnomination1.visibility=View.VISIBLE
                    llDnomination2.visibility=View.VISIBLE
                    llDnomination3.visibility=View.VISIBLE
                    llDnomination0.visibility=View.VISIBLE
                }
                else{
                    formType="F1"
                    imgDoc.visibility=View.GONE
                    tvUploadDoc.visibility=View.GONE
                    llDnomination1.visibility=View.GONE
                    llDnomination2.visibility=View.GONE
                    llDnomination3.visibility=View.GONE
                    llDnomination0.visibility=View.GONE
                }
            }

        }
        activity?.let {act->
                    loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.apply {
            etAmt.setupAmount()
        }
    }

    fun setObserver() {
        viewModel?.PaymentRequistResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    viewModel.popup_message.value=it?.data?.Description.toString()
                    val successPopupFragment = SuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                           // findNavController().popBackStack()
                            findNavController().navigate(R.id.action_paymentRequestImformationFragment_to_homeFragment2)
                        }
                    })

                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }

    }
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri != null) {
            authViewModel?.filePath?.value = uri
            if (isDenomination){
                binding.imgDoc.setImage(uri)
                viewModel.denomSlipUri = uri.uriToBase64(binding.root.context.contentResolver)
                isIsPaySlip=false
                isDenomination=false
            }
            else if (isIsPaySlip){
                binding.imgPlaySlip.setImage(uri)
                viewModel.paySleeyUri = uri.uriToBase64(binding.root.context.contentResolver)
                isIsPaySlip=false
                isDenomination=false
            }
            /*Glide.with(binding.tvUploadPayslip.context)
                .load(uri)
                .into(binding.imgPlaySlip)*/
            //findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        } else {
            viewModel?.filePath?.value = Uri.parse("/")

            Log.d("PhotoPicker", "No media selected")
        }

    }



    val pickMediaDenomination = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri != null) {
            //authViewModel?.filePath?.value = uri

                binding.imgDoc.setImage(uri)
                viewModel.denomSlipUri = uri.uriToBase64(binding.root.context.contentResolver)
                isIsPaySlip=false
                isDenomination=false


            /*Glide.with(binding.tvUploadPayslip.context)
                .load(uri)
                .into(binding.imgPlaySlip)*/
            //findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        } else {
            viewModel?.filePath?.value = Uri.parse("/")

            Log.d("PhotoPicker", "No media selected")
        }

    }

    fun Uri.createImagePart(partName: String): MultipartBody.Part? {
        val contentResolver: ContentResolver = binding.root.context.contentResolver
        val file: File = File(this.path) // Assuming the URI is a file URI

        // Create a request body from the file
        val requestBody = file.asRequestBody(contentResolver.getType(this)?.toMediaTypeOrNull())

        // Create a multipart body part from the request body
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    fun createImagePart(name: String, base64String: String): MultipartBody.Part {
        val bytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, "image.jpg", requestBody)
    }
}