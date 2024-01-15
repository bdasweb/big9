package com.big9.app.ui.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.adapter.ElectricPriceListAdapter
import com.big9.app.data.model.ElectricListModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.ElectricPriceListFragmentBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.CallBack
import com.google.gson.Gson

class ElectricPriceListFragment() : BaseFragment() {
    lateinit var binding: ElectricPriceListFragmentBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    var electricList = ArrayList<ElectricListModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.electric_price_list_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    /*override fun getTheme(): Int {
        return R.style.SheetDialog
    }*/
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {
        binding.apply {
            binding.apply {
                imgBack.back()
            }
            tvProcidToPay.setOnClickListener {
                viewModel?.consumerIdPrice?.value?.let {
                    if (it.isNotEmpty()) {
                        findNavController().popBackStack()
                    }
                    else{
                        Toast.makeText(tvProcidToPay.context, "Please select price", Toast.LENGTH_SHORT).show()
                    }
                    }
            }
        }

    }

    private fun setObserver() {
    //
viewModel.electricBillbillFetchResponseLiveData?.observe(viewLifecycleOwner) {
    when (it) {
        is ResponseState.Loading -> {
            loader?.show()
        }
        is ResponseState.Success -> {
            loader?.dismiss()
            setRecycleView(it?.data?.amt)
        }
        is ResponseState.Error -> {
            loader?.dismiss()
            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
        }
    }
}
    }

    private fun setRecycleView(amt: String?) {
        binding.recycleOperator.apply {
            electricList.clear()
            electricList.add(ElectricListModel(amt.toString(),"","",false));
           /* electricList.add(ElectricListModel("770","Dec 2023","2 month",false));
            electricList.add(ElectricListModel("770","Jan 2024","3 month",false));*/





            adapter= ElectricPriceListAdapter(electricList, object : CallBack {
                override fun getValue(s: String) {
                    viewModel?.apply {
                        viewModel?.consumerIdPrice?.value=s
                        //  findNavController().popBackStack()
                    }

                    // callBack.getValue(s)
                    //findNavController().popBackStack()
                }

            })
        }
    }

    private fun initView() {


        activity?.let {act->
                    loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        //
val (isLogin, loginResponse) = sharedPreff.getLoginData()
loginResponse?.let { loginData ->
    loginData.userid?.let {
        val data = mapOf(
            "userid" to loginData.userid,
            "custid" to viewModel?.consumerId?.value.toString(),
            "opid" to Constants.eOpid
        )
        val gson = Gson()
        var jsonString = gson.toJson(data)
        loginData.AuthToken?.let {
            viewModel?.electricBillbillFetch(it, jsonString.encrypt())
        }
    }
}

    }

}