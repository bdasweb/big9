package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.model.BottomSheetStateListModel
import com.big9.app.data.model.BottomSheetStateListModelWithId
import com.big9.app.data.model.ReceiptModel
import com.big9.app.data.viewMovel.DTHViewModel

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentAddRetailerBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.RetailerSuccessPopupFragment
import com.big9.app.ui.popup.SuccessPopupFragment2
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.common.MethodClass.userLogout
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack2
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson
import kotlinx.coroutines.launch

class AddRetailerFragment : BaseFragment() {
    lateinit var binding: FragmentAddRetailerBinding
    private val viewModel: MyViewModel by activityViewModels()
    private val dthViewModel: DTHViewModel by viewModels()
    private var loader: Dialog? = null
    var isFirstView=true
    var retailerid=""
    var stateList = ArrayList<BottomSheetStateListModelWithId>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_retailer, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initView()
            setObserver()
            onViewClick()
        }
    }

    private fun onViewClick() {
        binding.apply {

            imgBack.back()
            rootView.setOnClickListener {
                activity?.let { act -> rootView.hideSoftKeyBoard(act) }
            }


            btnSubmit.setOnClickListener {
                if (viewModel?.mobileforRetailerValidation() == true) {
                    submit()
                }
            }


            btnSubmit2.setOnClickListener {
                if (viewModel?.addRetailerValidation() == true) {
                    submit2()
                }
            }





        }
    }


    fun initView() {
        if (isFirstView){
            binding.croot1.visibility=View.VISIBLE
            binding.croot2.visibility=View.GONE
        }
        activity?.let {act->
                    loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
            stateList= ArrayList()
        }
        backPressedCheck()


    }

    private fun submit() {
        val (isLogin, loginResponse) = sharedPreff.getLoginData()
        loginResponse?.let { loginData ->
            loginData.userid?.let {

                val data = mapOf(
                    "userid" to loginData.userid,
                    "mobile" to viewModel?.ratailerMobile?.value.toString()
                )
                val gson = Gson()
                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {
                    viewModel?.addRetailer(it, jsonString.encrypt())
                }
            }
        }
    }
    private fun submit2() {
        val (isLogin, loginResponse) = sharedPreff.getLoginData()
        loginResponse?.let { loginData ->
            loginData.userid?.let {
/*
{"userid":"100001",
"retailerid":"100450",
"retailername":"Great boy",
"emailaddress":"Greatboy@gmail.com",
"shopaddress":"Baruipur",
"state":"WestBengal",
"city":"Baruipur",
"shopname":"Great Boy Stores"}
 */
                val data = mapOf(
                    "userid" to loginData.userid,
                    "retailerid" to retailerid,
                    "retailername" to viewModel?.ratailerName?.value,
                    "emailaddress" to viewModel?.ratailerEmail?.value,
                    "shopaddress" to viewModel?.ratailerAddress?.value,
                    "state" to viewModel?.state?.value,
                    "city" to viewModel?.district?.value,
                    "shopname" to viewModel?.ratailerShopName?.value,
                )
                val gson = Gson()
                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {
                    viewModel?.add_retailer_dtls(it, jsonString.encrypt())
                }
            }
        }
    }
    fun setObserver() {
        viewModel?.addRetailerResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    viewModel.popup_message.value="${it?.data?.Description}"
                    it?.data?.retailerid?.let {
                        retailerid=it
                    }
                    val successPopupFragment = SuccessPopupFragment2(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            viewModel.popup_message.value="Success"
                            val (isLogin, loginResponse) = sharedPreff.getLoginData()
                            loginResponse?.let { loginData ->
                                loginData.userid?.let {

                                    val data = mapOf(
                                        "userid" to loginData.userid
                                    )
                                    val gson = Gson()
                                    var jsonString = gson.toJson(data)
                                    loginData.AuthToken?.let {
                                        viewModel?.StateList(it, jsonString.encrypt())
                                    }
                                }
                            }
                            viewModel?.addRetailerResponseLiveData?.value=null
                        }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)


                   /* val successPopupFragment = RetailerSuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            context?.let { ctx->
                                ctx.userLogout()
                            }

                          }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)*/
                    //binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    //binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    viewModel?.addRetailerResponseLiveData?.value=null
                }
            }
        }

        viewModel?.StateListResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    stateList.clear()
                    activity?.let { act ->
                        it?.data?.data?.let {
                            if (it.size>0){
                                it.forEach {
                                    stateList.add(BottomSheetStateListModelWithId(it.title.toString(),false,it.id.toString()))
                                }
                            }
                        }
                        val stateListDialog = AddRetailerStateListDialog(object : CallBack2 {
                            override fun getValue2(s: String,stateId:String) {
                                viewModel?.state?.value = s
                                if (s.isEmpty()){
                                    findNavController().popBackStack()
                                }
                                val (isLogin, loginResponse) = sharedPreff.getLoginData()
                                loginResponse?.let { loginData ->
                                    loginData.userid?.let {

                                        val data = mapOf(
                                            "userid" to loginData.userid,
                                            "stateid" to stateId
                                        )
                                        val gson = Gson()
                                        var jsonString = gson.toJson(data)
                                        loginData.AuthToken?.let {
                                            viewModel?.CityList(it, jsonString.encrypt())
                                        }
                                    }
                                }
                            }

                        },stateList)
                        stateListDialog.show(act.supportFragmentManager, stateListDialog.tag)

                    }
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
        viewModel?.CityListResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    stateList.clear()
                    activity?.let { act ->
                        it?.data?.data?.let {
                            if (it.size>0){
                                it.forEach {
                                    stateList.add(BottomSheetStateListModelWithId(it.district.toString(),false,""))
                                }
                            }
                        }
                        val stateListDialog = AddRetailerDistrictListDialog(object : CallBack2 {
                            override fun getValue2(s: String,stateId:String) {
                                if (s.isEmpty()){
                                    findNavController().popBackStack()
                                }
                                viewModel?.district?.value = s
                                binding.croot1.visibility=View.GONE
                                binding.croot2.visibility=View.VISIBLE
                            }

                        },stateList)
                        stateListDialog.show(act.supportFragmentManager, stateListDialog.tag)

                    }
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
        viewModel?.add_retailer_dtlsResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                    val successPopupFragment = RetailerSuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            context?.let { ctx->
                                ctx.userLogout()
                            }

                        }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
                    viewModel?.add_retailer_dtlsResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    //binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    viewModel?.add_retailer_dtlsResponseLiveData?.value=null
                }
            }
        }
    }

    fun backPressedCheck() {}

}
