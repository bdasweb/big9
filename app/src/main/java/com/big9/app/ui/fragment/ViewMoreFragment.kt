package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.adapter.ViewMoreAdapter
import com.big9.app.data.model.ListIcon
import com.big9.app.data.model.UserDetails
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentViewMoreBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.fragment.fragmentDialog.GasBillerListDialog
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants.isFromUtilityPage
import com.big9.app.utils.helpers.Constants.utilityValue
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack2
import com.google.gson.Gson


class ViewMoreFragment : BaseFragment() {
    lateinit var binding: FragmentViewMoreBinding
    private val viewModel: MyViewModel by activityViewModels()
    var userDetailsList = ArrayList<UserDetails>()
    var utilityBillList = ArrayList<ListIcon>()
    private var loader: Dialog? = null
    var naviGationValue=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_more, container, false)
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
        isFromUtilityPage=true
    }
    private fun onViewClick() {

        binding.apply {
          imgBack.back()

          }
        }


    fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
            binding.recycleUtility.apply {
            utilityBillList.clear()
            utilityBillList.add(ListIcon(getString(R.string.education_fees), R.drawable.ioc_tuition_fees,"no slag"))
            utilityBillList.add(ListIcon(getString(R.string.broadband), R.drawable.router,getString(R.string.broadband)))
            utilityBillList.add(ListIcon(getString(R.string.gas_booking), R.drawable.gas_booking_ioc,"no slag"))
            utilityBillList.add(ListIcon(getString(R.string.loan_payment), R.drawable.loan_ioc_new,"no slag"))

            //utilityBillList.add(ListIcon(getString(R.string.view_more), R.drawable.view_more))
            adapter= ViewMoreAdapter(utilityBillList,R.drawable.circle_shape2, object : CallBack2 {
                override fun getValue2(s: String,slag: String) {
                    utilityValue=s
                    viewModel.from_page_message.value="view_more"
                    //checkService(navParameter = s,slag)
                    Toast.makeText(requireContext(), "Service unavailable. Coming soon.", Toast.LENGTH_SHORT).show()
                   // serviceNavigation(s)
                    //findNavController().popBackStack()

                    /*when(s){

                        getString(R.string.electric)->{
                            activity?.let {act->
                                val stateListDialog = StateListDialog(object : CallBack {
                                    override fun getValue(s: String) {
                                        viewModel?.state?.value=s
                                        findNavController().navigate(R.id.action_homeFragment2_to_electricRechargeFragment)
                                    }

                                })
                                stateListDialog.show(act.supportFragmentManager, stateListDialog.tag)

                            }
                        }
                        getString(R.string.view_more)->{}

                    }*/
                }

            })
        }
    }

    fun setObserver() {
        binding.apply {
            viewModel?.ServiceCheckViewMoreResponseLiveData?.observe(viewLifecycleOwner){
                when (it) {
                    is ResponseState.Loading -> {
                        loader?.show()
                    }

                    is ResponseState.Success -> {
                        loader?.dismiss()
                        it?.data?.slug?.let {slug->
                            serviceNavigation(naviGationValue,slug)
                        }
                        //viewModel?.checkServiceReceiptResponseLiveData?.value=null
                    }

                    is ResponseState.Error -> {
                        loader?.dismiss()

                        //delete this code. it is for testing
                       // serviceNavigation(naviGationValue,"slug")


                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        //viewModel?.checkServiceReceiptResponseLiveData?.value=null
                    }
                }
            }
        }

    }

    private fun serviceNavigation(s: String, slug: String) {
        when(s){
            //findNavController().navigate(R.id.action_viewMoreFragment_to_formFragment)
            getString(R.string.education_fees)->{
                findNavController().navigate(R.id.action_viewMoreFragment_to_educationFeesFragment)
            }

            getString(R.string.broadband)->{
                findNavController().navigate(R.id.action_viewMoreFragment_to_broadBandFragment)
            }

            getString(R.string.gas_booking)->{
                activity?.let {act->
                    val gasBillerListDialog = GasBillerListDialog(object : CallBack {
                        override fun getValue(s: String) {
                            viewModel?.gasBiller?.value=s
                            findNavController().navigate(R.id.action_viewMoreFragment_to_gasBookingFragment)
                        }

                    })
                    gasBillerListDialog.show(act.supportFragmentManager, gasBillerListDialog.tag)

                }

            }

            getString(R.string.loan_payment)->{
                findNavController().navigate(R.id.action_viewMoreFragment_to_loanPaymentFragment)
            }
        }
    }
    fun checkService(navParameter: String,slag:String){
        naviGationValue=navParameter
        //Toast.makeText(requireContext(), ""+slag, Toast.LENGTH_SHORT).show()
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        if (isLogin){
            loginResponse?.let {loginData->
                viewModel?.apply {

                    val  data = mapOf(
                        "userid" to loginData.userid,
                        "service" to slag
                    )

                    val gson= Gson()
                    var jsonString = gson.toJson(data)
                    loginData.AuthToken?.let {ServiceCheckViewMore(it,jsonString.encrypt())
                    }
                }

            }
        }
    }
}