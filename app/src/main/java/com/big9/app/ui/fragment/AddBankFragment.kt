package com.big9.app.ui.fragment


import android.app.Dialog
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.model.BankListModel

import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentAddBankBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment

import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.isIsCheck
import com.big9.app.utils.`interface`.CallBack
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddBankFragment : BaseFragment() {
    lateinit var binding: FragmentAddBankBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var authViewModel: AuthViewModel?=null
    private var loader: Dialog? = null
    var bankList = ArrayList<BankListModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_bank, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.bank_check_ErrorVisible.value=false
        if (isIsCheck){
            authViewModel?.filePath?.observe(viewLifecycleOwner){
                binding.imgCheck.setImage(it)
            }
            isIsCheck =false
        }
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
            cRootView.setOnClickListener {
                activity?.let { act -> cRootView.hideSoftKeyBoard(act) }
            }


            etAmt.oem(btnSubmit)
            btnSubmit.setOnClickListener{
                activity?.let { act ->
                    viewModel?.apply {
                    if (beneficiaryValidation()) {
                        if (authViewModel?.filePath?.value != null && authViewModel?.filePath?.value.toString() != "/") {
                            bank_check_ErrorVisible.value = false

                            lifecycleScope.launch {
                                var imageBase64=""
                                //val job = CoroutineScope(Dispatchers.IO).launch {
                                    imageBase64= filePath?.value?.uriToBase64(binding.root.context.contentResolver)
                                        .toString()
                                    bankSlipDocumentImageBase64?.value =imageBase64

                               // }

                                addBank()


                            }
                        } else {
                            hideKeyBoard(etAmt)
                            bank_check_ErrorVisible.value = true
                        }
                    }
                }
                }

            }


            etBeneficiaryBankName.setOnClickListener{
                activity?.let {act->
                    val bankListBottomSheetDialog = BankListBottomSheetDialog(object : CallBack {
                        override fun getValue(s: String) {
                           // Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                        }
                    },bankList)
                    bankListBottomSheetDialog.show(
                        act.supportFragmentManager,
                        bankListBottomSheetDialog.tag
                    )
                }

            }

            imgCheck.setOnClickListener{tvUploadPayslip.performClick()}
            tvUploadPayslip.setOnClickListener{
                activity?.let {act->
                    Constants.isBackCamera =true

                    Constants.isPdf =false
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {
                            getImage(s)
                        }
                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)

                }
            }
        }



    }
    fun addBank(){
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        if (isLogin){
            loginResponse?.let {loginData->
                viewModel?.apply {
                    val  data = mapOf(
                        "userid" to loginData.userid,

                        "bname" to  beneficiary_bank_name.value?.toString(),
                        "ifsccode" to beneficiary_ifsc.value?.toString(),
                        "accnumber" to beneficiary_acc.value?.toString(),
                        "accholder" to beneficiary_name.value?.toString()
                    )

                    /*userid
                    bname
                    accholder
                    accnumber
                    ifsccode*/

                    var panimagedata=
                        bankSlipDocumentImageBase64.value?.let { createImagePart("imagefile", it) }
                    val gson= Gson()
                    var jsonString = gson.toJson(data)
                    loginData.AuthToken?.let {

                        addToBank(it,jsonString.encrypt(),panimagedata)
                    }
                }
            }
        }
    }
    fun initView() {
        activity?.let {act->
            loader = MethodClass.custom_loader(act, getString(R.string.please_wait))

            binding.apply {
                authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
            }

            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            loginResponse?.let { loginData ->
                loginData.userid?.let {
                    val data = mapOf(
                        "userid" to it,
                    )
                    val gson =  Gson();
                    var jsonString = gson.toJson(data);
                    loginData.AuthToken?.let {
                        viewModel?.addBankBankList(it,jsonString.encrypt())
                    }
                }
            }



        }
    }

    fun setObserver() {
        viewModel.addToBankReceptLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    findNavController().popBackStack()

                    viewModel.addToBankReceptLiveData?.value=null
                }

                is ResponseState.Error -> {

                    loader?.dismiss()

                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel.addToBankReceptLiveData?.value=null

                }
            }
        }

        viewModel?.addBankBankListResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    bankList.clear()
                    it.data?.data?.let {
                        it.forEach{
                            bankList.add(BankListModel(R.drawable.bank_imps,it.name.toString(),"",it.ifsc.toString()))
                        }

                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }



    }


    private fun getImage(s:String) {
        when(s){
            "g"->{
                Constants.isVideo =false
                Constants.isGallary =true
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                //findNavController().navigate(R.id.action_regFragment_to_cameraFragment)
            }
            "t"->{
                Constants.isIsCheck =true
                Constants.isVideo =false
                Constants.isGallary =false
                findNavController().navigate(R.id.action_addBankFragment_to_cameraFragment2)
            }

        }
    }
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri != null) {
            authViewModel?.filePath?.value = uri
            binding.imgCheck.setImage(uri)
            /*Glide.with(binding.tvUploadPayslip.context)
                .load(uri)
                .into(binding.imgPlaySlip)*/
            //findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        } else {
            viewModel?.filePath?.value = Uri.parse("/")

            Log.d("PhotoPicker", "No media selected")
        }

    }
    fun createImagePart(name: String, base64String: String): MultipartBody.Part {
        val bytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, "image.jpg", requestBody)
    }
}