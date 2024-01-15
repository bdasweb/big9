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

import com.big9.app.adapter.OperatorAdapter
import com.big9.app.data.model.OperatorModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.OperatorFragmentLayoutBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.isDthOperator
import com.big9.app.utils.helpers.Constants.isFirstPageOpeenPostPaidMobile
import com.big9.app.utils.`interface`.CallBack7
import com.google.gson.Gson
import kotlinx.coroutines.launch

class OperatorFragment : BaseFragment() {
    lateinit var binding: OperatorFragmentLayoutBinding
    private val viewModel: MyViewModel by activityViewModels()
    var operator = ArrayList<OperatorModel>()
    private var loader: Dialog? = null
    var operatorAdapter:OperatorAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.operator_fragment_layout, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
        }

    }

    private fun setObserver() {
        viewModel?.postpaid_mobile_operator_listResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    operator?.clear()
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    if (it.data?.data?.isNotEmpty() == true){
                        it.data?.data?.forEach {
                            operator.add(OperatorModel(it.imglink,it.opname,false,minrecharge=it.minrecharge,maxrecharge=it.maxrecharge,minlen=it.minlen,maxlen=it.maxlen,opcode=it.opcode))
                        }
                        operatorAdapter?.let {
                            it.operatorList =operator
                            it.notifyDataSetChanged()
                        }
                     //   setRecycleView()
                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }

    viewModel?.prepaid_mobile_operator_listResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    operator?.clear()
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    if (it.data?.data?.isNotEmpty() == true){
                        it.data?.data?.forEach {
                            operator.add(OperatorModel(it.imglink,it.opname,false,minrecharge=it.minrecharge,maxrecharge=it.maxrecharge,minlen=it.minlen,maxlen=it.maxlen,opcode=it.opcode))
                        }
                        operatorAdapter?.let {
                            it.operatorList =operator
                            it.notifyDataSetChanged()
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

    private fun initView() {
        setRecycleView()
        isFirstPageOpeenPostPaidMobile=false
        /*operatorAdapter?.let {
            it.operatorList= ArrayList()
            it.notifyDataSetChanged()
        }*/
        activity?.let {
                loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
                val operator_mode=if(isDthOperator){
                    "operator_dth"
                }else if (viewModel?.prepaitOrPostPaid?.value== Constants.Postpaid){
                    "operator_postpaid"
                }
                else{
                    "operator_prepaid"
                }
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                  //  if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "action" to operator_mode
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                if (viewModel?.prepaitOrPostPaid?.value== Constants.Postpaid || isDthOperator) {
                                    viewModel?.postpaid_mobile_operator_list(
                                        it,
                                        jsonString.encrypt()
                                    )
                                }
                                else if (viewModel?.prepaitOrPostPaid?.value== Constants.Prepaid) {
                                    viewModel?.prepaid_mobile_operator_list(it,jsonString.encrypt())
                                }

                                //loader?.show()
                            }
                        }
                   // }

            }

    }

   fun  setRecycleView(){
       binding.recycleOperator.apply {
           //
          /* if (!isDthOperator) {
               *//*operator.add(OperatorModel(R.drawable.airtel_com_logo, "Airtel", false))
               operator.add(OperatorModel(R.drawable.bharat_sanchar_logo, "BSNL Topup", false))
               operator.add(OperatorModel(R.drawable.bharat_sanchar_logo, "BSNL-Validity", false))
               operator.add(OperatorModel(R.drawable.jio, "Reliance Jio", false))
               operator.add(OperatorModel(R.drawable.vi, "VI", false))*//*
           }
           else{
               *//*operator.add(OperatorModel(R.drawable.airtel_dth,"Airtel DTH",false))
               operator.add(OperatorModel(R.drawable.dish_tv_4,"Dish Tv",false))
               operator.add(OperatorModel(R.drawable.sun_direct,"Sun Direct",false))
               operator.add(OperatorModel(R.drawable.tata_sky,"TATA Sky",false))
               operator.add(OperatorModel(R.drawable.videocon,"VideoCon D2h",false))*//*
           }*/

           operatorAdapter=OperatorAdapter(ArrayList(), object:CallBack7{
               override fun getValue7(opcodeData: String, minlenData: String, maxlenData: String, minrechargeData: String, maxrechargeData: String, titleData: String, imageData: String) {
                   viewModel?.apply {
                       operatorCode.value=opcodeData
                       operator.value=titleData
                       dthOperator.value =titleData
                       selectrdOperator.value=imageData
                       try {
                           minMobileLength.value = minlenData?.toInt()
                           maxMobileLength.value = maxlenData?.toInt()

                           minrecharge.value = minrechargeData?.toInt()
                           maxrecharge.value = maxrechargeData?.toInt()
                       }catch (e:Exception){}
                       findNavController().popBackStack()
                   }

               }
           })
           adapter=operatorAdapter
       }


    }

}