package com.big9.app.ui.fragment.regandkyc


import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.adapter.spinnerAdapter.CustomSpinnerAdapter

import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.BankDetailsFragmentBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.fragment.CameraDialog
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.CallBack
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class BankDetailsFragment : BaseFragment() {
    lateinit var binding: BankDetailsFragmentBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bank_details_fragment, container, false)
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

    private fun onViewClick() {
        binding.rootView.setOnClickListener {
            activity?.let { act -> binding.rootView.hideSoftKeyBoard(act) }
        }
        binding.btnSaveContinue.setOnClickListener {
            if (authViewModel.bankDetailsValidation()) {
                /*val bankDetails = BankDetails(
                    beneficiaryName= authViewModel?.beneficiaryName?.value,
                    accountNumber= authViewModel?.accountNumber?.value,
                    confirmAccountNumber= authViewModel?.confirmAccountNumber?.value,

                    ifscCode= authViewModel?.ifscCode?.value,
                    employeeCode= authViewModel?.employeeCode?.value,
                    cancleCheckBase64= authViewModel?.cancleCheckBase64?.value,
                    bankName= authViewModel?.bankName?.value
                )*/



                val (isLogin, loginResponse) = sharedPreff.getLoginData()
                loginResponse?.let { loginData ->
                    loginData.userid?.let {



                        val data2 = mapOf(
                         "userid" to it,
                        "baccountname" to authViewModel?.beneficiaryName?.value,
                        "baccountno" to authViewModel?.accountNumber?.value,
                        "confirm_acno" to authViewModel?.confirmAccountNumber?.value,
                        "bank_name" to authViewModel?.bankName?.value,
                        "ifsc_code" to authViewModel?.ifscCode?.value,
                        "sm_mobile_no" to authViewModel?.beneficiaryName?.value,
                       /* "cancelled_cheque" to authViewModel?.cancleCheckBase64?.value,*/
                        )
                        var cancelled_cheque=
                            authViewModel.cancleCheckBase64.value?.let { createImagePart("chequeimagedata", it) }
                        val gson = Gson()
                        var jsonString2 = gson.toJson(data2);

                        /*try {
                            val file = File(context?.filesDir, "bank.txt")
                            val fileOutputStream = FileOutputStream(file)
                            fileOutputStream.write(authViewModel.cancleCheckBase64.value?.toByteArray())
                            fileOutputStream.close()
                            showLogDcriptData("bankCheck","${file.absolutePath}")

                        } catch (e: IOException) {
                           showLogDcriptData("bankCheck","Error "+e.message)
                        }*/


                        loginData.AuthToken?.let {
                            authViewModel?.bankDetails(it, jsonString2.encrypt(),cancelled_cheque)

                        }

                    }
                }

            }
            }

        binding.llCheck.setOnClickListener{
            Constants.isBackCamera =true

            Constants.isPdf =false
            val cameraDialog = CameraDialog(object : CallBack {
                override fun getValue(s: String) {

                    getImage(s)
                }

            },false)
           activity?.let { act->  cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)}
        }
    }

    fun initView() {

        activity?.let {act->
                    loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }

        defaultApiCall()

    }

    private fun defaultApiCall() {
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        loginResponse?.let { loginData ->
            loginData.userid?.let {
                val data = mapOf(
                    "userid" to it
                )
                val gson =  Gson()
                var jsonString = gson.toJson(data);
                loginData.AuthToken?.let {
                    authViewModel?.businesstype(it,jsonString.encrypt())
                }



                val data2 = mapOf(
                    "userid" to it
                )

                var jsonString2 = gson.toJson(data2);
                loginData.AuthToken?.let {
                    authViewModel?.bankname(it,jsonString2.encrypt())

                }

            }
        }
    }

    fun setObserver() {
        authViewModel?.filePath?.observe(viewLifecycleOwner){
            it?.let {uti->
               //authViewModel?.cancleCheck?.value = uti.toString()
                authViewModel.cancleCheckBase64.value= Uri.parse(uti.toString()).uriToBase64(binding.root.context.contentResolver)
                //authViewModel.pancardImage3.value=it.getFileNameFromUri()
                //Log.d("TAG_file", "true setObserver: "+it.uriToBase64(binding.root.context.contentResolver))

                val (fileName, fileType) = it.getFileNameAndTypeFromUri(binding.root.context)
                authViewModel?.cancleCheck?.value = fileName
                 }


        }
        authViewModel?.bankDetailsResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    findNavController().navigate(R.id.action_bankDetailsFragment_to_docuploadFragment)
                    it.data?.data?.let {

                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
        authViewModel?.banknameResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    it.data?.data?.let {


                        var dataArray = Array<String>(it.size) { "" }

                        for (index in it.indices){
                            dataArray[index]= it[index].title.toString()

                        }
                        setBusnessTypeSpinner(dataArray)
                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }



    }

    fun Spinner.setSpinner(callBack: CallBack, genderArray: Array<String>){
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                callBack.getValue(genderArray[position])
                // val selectedItem = items[position]
                // Handle the selected item
                //Toast.makeText(this@MainActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected
            }
        }
    }

    private fun getImage(s:String) {
        when(s){
            "g"->{
                Constants.isVideo =false
                Constants.isGallary =true
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                //findNavController().navigate(R.id.action_bankDetailsFragment_to_cameraFragment)
            }
            "t"->{
                Constants.isVideo =false
                Constants.isGallary =false
                findNavController().navigate(R.id.action_bankDetailsFragment_to_cameraFragment)
            }

        }
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri != null) {


            authViewModel.cancleCheckBase64.value= Uri.parse(uri.toString()).uriToBase64(binding.root.context.contentResolver)

            val (fileName, fileType) = uri.getFileNameAndTypeFromUri(binding.root.context)
            authViewModel?.cancleCheck?.value = fileName


            //authViewModel?.cancleCheck?.value = uri.toString()

            //findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        } else {
            authViewModel?.cancleCheck?.value = "/"

            Log.d("PhotoPicker", "No media selected")
        }

    }

    private fun setBusnessTypeSpinner(businesstypeData: Array<String>) {
        binding.apply {
            spinnerBank.apply {
                adapter= CustomSpinnerAdapter(binding.root.context, businesstypeData)
                //adapter = ArrayAdapter<String>(this.context, R.layout.custom_spinner_item, businesstypeData)
                setSpinner(object : CallBack {
                    override fun getValue(s: String) {
                        authViewModel.bankName.value=s
                        viewModel?.bankNameErrorVisible?.value = s.equals("Select Bank")

                        // Toast.makeText(binding.root.context, "$s", Toast.LENGTH_SHORT).show()
                    }
                },businesstypeData)
            }
        }


    }

    fun createImagePart(name: String, base64String: String): MultipartBody.Part {
        val bytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, "image.jpg", requestBody)
    }
}