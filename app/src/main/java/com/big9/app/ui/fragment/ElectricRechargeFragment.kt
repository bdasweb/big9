package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import billerData
import com.big9.app.R

import com.big9.app.adapter.BillerListAdapter
import com.big9.app.data.model.ElectricModel2
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentElectricRechargeBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants.eOpid
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack2
import com.google.gson.Gson

class ElectricRechargeFragment : BaseFragment() {
    lateinit var binding: FragmentElectricRechargeBinding
    private val viewModel: MyViewModel by activityViewModels()
    var billerList = ArrayList<ElectricModel2>()
    var billerListAdapter: BillerListAdapter? = null
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_electric_recharge, container, false)
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

    private fun onViewClick() {

        binding.apply {
            imgBack.back()
        }
    }

    fun initView() {
        binding.apply {

            activity?.let { act ->
                loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
            }

            //
            val (isLogin, loginResponse) = sharedPreff.getLoginData()
            loginResponse?.let { loginData ->
                loginData.userid?.let {
                    val data = mapOf(
                        "userid" to loginData.userid,
                        "statename" to viewModel?.state?.value.toString()
                    )
                    val gson = Gson()
                    var jsonString = gson.toJson(data)
                    loginData.AuthToken?.let {
                        viewModel?.electricbillerlist(it, jsonString.encrypt())
                    }
                }
            }


        }

    }

    fun setObserver() {
        //
        viewModel?.electricbillerlistResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    setRecycleView(it?.data?.billerData)
                    viewModel?.electricbillerlistResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel?.electricbillerlistResponseLiveData?.value=null
                }
            }
        }
    }

    private fun setRecycleView(billerData: ArrayList<billerData>?) {
        billerList.clear()
        billerData?.let {
            it.forEach{
                billerList.add(ElectricModel2(it.opname.toString(), R.drawable.electric, false,it.opid.toString()))
            }
        }
        /*billerList.add(ElectricModel("West Bengal Electricity", R.drawable.wbsedcl, false))
        billerList.add(ElectricModel("CESC Limited", R.drawable.cesc, false))
*/

        binding.recycleElectric.apply {

            billerListAdapter = BillerListAdapter(billerList, object : CallBack2 {
                override fun getValue2(s: String,opid: String) {
                    viewModel?.apply {
                        consumerId?.value=""

                        consumerIdPrice?.value=""

                        billerAddress?.value = s
                    }

                    eOpid=opid

                    findNavController().navigate(R.id.action_electricRechargeFragment_to_utilityBillPaymentFragment)
                }

            })
            adapter = billerListAdapter


            binding.etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    billerListAdapter?.filter?.filter(s)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }


}