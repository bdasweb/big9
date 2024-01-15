package com.big9.app.ui.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.adapter.BrowserAdapter
import com.big9.app.data.model.BrowserModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.BrowserBottomsheetLayoutBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.base.BaseFragment

import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BrowserPlainFragment : BaseFragment() {
    lateinit var binding: BrowserBottomsheetLayoutBinding
    private val viewModel: MyViewModel by activityViewModels()
    var browser = ArrayList<BrowserModel>()
    private var loader: Dialog? = null
    var browserAdapter:BrowserAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.browser_bottomsheet_layout, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
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
            imgBack.back()
        }

    }



    private fun initView() {
        activity?.let {
            browserAdapter?.let {
                it.operatorList=ArrayList()
                it.notifyDataSetChanged()
            }
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            if (isLogin){
                loginResponse?.let {loginData->
                    val data = mapOf(
                        "userid" to loginData.userid,
                        "mobileno" to viewModel.mobile.value
                    )
                    val gson= Gson()
                    var jsonString = gson.toJson(data)
                    loginData.AuthToken?.let {
                        viewModel?.prePaidMobilePlainList(it,jsonString.encrypt())

                        //loader?.show()
                    }
                }
            }

        }
        binding.recycleBrowser.apply {
            browser.clear()

            /*browser.add(BrowserModel("152","Truly Unlimited","1GB","SMS 300","20 days",false))
            browser.add(BrowserModel("199","Truly unlimited calls","1GB","SMS 100/day","180 days",false))
            browser.add(BrowserModel("1000","Truly Unlimited","1GB/day","SMS 300","54 days",false))
            browser.add(BrowserModel("2199","Truly unlimited calls","1GB","SMS 100/day","84 days",false))
            browser.add(BrowserModel("3000","Truly Unlimited","1GB","SMS 100","24 days",false))*/

            browserAdapter= BrowserAdapter(browser, object : CallBack {
                override fun getValue(s: String) {
                    viewModel.amt.value=s
                    //callBack.getValue(s)
                    //dismiss()
                    findNavController().popBackStack()
                }

            })
            adapter=browserAdapter
        }
    }
    private fun setObserver() {
        viewModel?.prePaidMobilePlainListResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {

                    loader?.show()
                }

                is ResponseState.Success -> {

                    if (it.data?.data?.isNotEmpty() == true){
                        it.data?.data?.forEach {

                            val inputString = it.desc.toString()

                            val pattern = Regex("Data\\s*:\\s*([^|]+)")

                            // Find the match in the input string
                            val matchResult = pattern.find(inputString)

                            // Extract the desired value if a match is found
                            val result = matchResult?.groups?.get(1)?.value

                            browser.add(BrowserModel(it.rs.toString(),it.desc.toString(),result.toString(),"",it.validity.toString(),false))
                        }
                        browserAdapter?.operatorList=browser
                        lifecycleScope.launch {
                            val job=CoroutineScope(Dispatchers.Main).launch {
                                browserAdapter?.notifyDataSetChanged()
                            }

                            delay(2000)
                            loader?.dismiss()
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

    override fun onPause() {
        super.onPause()
        loader?.dismiss()
    }
}