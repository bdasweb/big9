package com.big9.app.ui.fragment.regandkyc


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.model.businessCategoryData
import com.big9.app.data.model.businesstypeData

import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.KycDetailsFragmentBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack2
import com.google.gson.Gson

class KycDetailsFragment : BaseFragment() {
    lateinit var binding: KycDetailsFragmentBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private var loader: Dialog? = null
    var businesstypeDataList: ArrayList<businesstypeData> = ArrayList()
    var businessCategoryDataList: ArrayList<businessCategoryData> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.kyc_details_fragment, container, false)
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

            if (authViewModel.kycValidation()) {
                authViewModel.apply {
                    /*val regModel = KycDetails(
                        businessType=businessType.value,
                        businessCategory=businessCategory.value,
                        partnerNameName=partnerNameName.value,
                        businessName=businessName.value,
                        businessAddress=businessAddress.value,
                        companyPanCardNumber=companyPanCardNumber.value,
                        kycAadharNumber=kycAadharNumber.value,
                        kycGSTNumber=kycGSTNumber.value,
                        partnerPanCardNumber=partnerPanCardNumber.value
                    )*/

                    /*  val gson = Gson()
                      val json = gson.toJson(regModel)*/
                    //json.toString().testDataFile()
                    //findNavController().navigate(R.id.action_kycDetailsFragment_to_bankDetailsFragment)

                    var businesstypeId = ""
                    var businessCId = ""
                    val (isLogin, loginResponse) = sharedPreff.getLoginData()
                    loginResponse?.let { loginData ->
                        try {
                            businessId.value?.toInt()?.let {
                                businesstypeId = businesstypeDataList.get(it).id.toString()
                            }

                            businessCategoryId.value?.toInt()?.let {
                                businessCId = businessCategoryDataList.get(it).id.toString()
                            }


                        } catch (e: Exception) {
                        }
                        loginData.userid?.let {
                            val data = mapOf(
                                "userid" to it,


                                "btype" to businesstypeId,
                                "bcategory" to businessCId,
                                "partnername" to partnerNameName.value,
                                "bname" to businessName.value,
                                "baddress" to businessAddress.value,
                                "cmpanno" to companyPanCardNumber.value,
                                "ppanno" to partnerPanCardNumber.value,
                                "paadharno" to kycAadharNumber.value,
                                "gstno" to kycGSTNumber.value,
                            )
                            val gson = Gson()
                            var jsonString = gson.toJson(data);
                            loginData.AuthToken?.let {
                                authViewModel?.companyDetailsMethod(it, jsonString.encrypt())
                            }


                        }
                    }

                }


            }
        }

    }

    fun initView() {

        activity?.let { act ->
            loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        Handler(Looper.getMainLooper()).postDelayed({
            defaultAPICall()
        }, 100)

        binding.apply {


        }
    }

    private fun defaultAPICall() {

        val (isLogin, loginResponse) = sharedPreff.getLoginData()
        loginResponse?.let { loginData ->
            loginData.userid?.let {
                val data = mapOf(
                    "userid" to it
                )
                val gson = Gson()
                var jsonString = gson.toJson(data);
                loginData.AuthToken?.let {
                    authViewModel?.businesstype(it, jsonString.encrypt())
                }


                val data2 = mapOf(
                    "userid" to it
                )

                var jsonString2 = gson.toJson(data2);
                loginData.AuthToken?.let {
                    authViewModel?.businesscategoryMethod(it, jsonString2.encrypt())

                }

            }
        }


    }

    fun setObserver() {
        authViewModel?.businesstypeResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    it.data?.data?.let {


                        businesstypeDataList.clear()
                        businesstypeDataList = it
                        var dataArray = Array<String>(it.size) { "" }

                        for (index in it.indices) {
                            if (index == 0) {
                                authViewModel.businessId.value = it[index].id.toString()
                            }
                            dataArray[index] = it[index].title.toString()

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
        authViewModel?.businesscategoryMethodResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    it.data?.data?.let {

                        businessCategoryDataList.clear()
                        businessCategoryDataList = it
                        var dataArray = Array<String>(it.size) { "" }

                        for (index in it.indices) {
                            if (index == 0) {
                                authViewModel.businessCategoryId.value = it[index].id.toString()
                            }
                            dataArray[index] = it[index].title.toString()

                        }
                        setBusnessCategorySpinner(dataArray)
                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
        authViewModel?.companyDetailsMethodResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    authViewModel?.filePath?.value=null
                    findNavController().navigate(R.id.action_kycDetailsFragment_to_bankDetailsFragment)
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


    }

    private fun setBusnessTypeSpinner(businesstypeData: Array<String>) {
        binding.apply {
            spinnerBusinessType.apply {
                /*val busnessType = arrayOf(
                    "Select Business Type",
                    "Limited",
                    "LLP",
                    "Others",
                    "Partnership",
                    "Private Limited",
                    "Proprietor")*/
                adapter = ArrayAdapter<String>(
                    this.context,
                    R.layout.custom_spinner_item,
                    businesstypeData
                )
                setSpinner(object : CallBack2 {
                    override fun getValue2(s: String, id: String) {
                        authViewModel.businessType.value = s
                        authViewModel.businessId.value = id
                        // Toast.makeText(binding.root.context, "$s", Toast.LENGTH_SHORT).show()
                    }
                }, businesstypeData)
            }
        }

    }

    private fun setBusnessCategorySpinner(businessCategoryData: Array<String>) {


        binding.apply {
            spinnerBusinessCategory.apply {

                adapter = ArrayAdapter<String>(
                    this.context,
                    R.layout.custom_spinner_item,
                    businessCategoryData
                )
                setSpinner(object : CallBack2 {
                    override fun getValue2(s: String, id: String) {
                        authViewModel.businessType.value = s
                        authViewModel.businessCategoryId.value = id

                        // Toast.makeText(binding.root.context, "$s", Toast.LENGTH_SHORT).show()
                    }
                }, businessCategoryData)
            }
        }

    }


    fun Spinner.setSpinner(callBack2: CallBack2, genderArray: Array<String>) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                callBack2.getValue2(genderArray[position], position.toString())
                // val selectedItem = items[position]
                // Handle the selected item
                //Toast.makeText(this@MainActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected
            }
        }
    }
}