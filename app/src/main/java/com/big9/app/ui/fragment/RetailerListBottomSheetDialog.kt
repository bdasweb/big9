package com.big9.app.ui.fragment

import ViewRetailerData
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.adapter.RetailerLististAdapter
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.BankListBottomsheetLayoutBinding

import com.big9.app.ui.base.BaseCenterSheetFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.big9.app.utils.`interface`.CallBack7

class RetailerListBottomSheetDialog(val callBack: CallBack7, var retailer: ArrayList<ViewRetailerData>?) : BaseCenterSheetFragment() {
    lateinit var binding: BankListBottomsheetLayoutBinding
    private val myViewModel: MyViewModel by activityViewModels()
    //var bankList = ArrayList<BankListModel>()
    private var loader: Dialog? = null
    var retailerLististAdapter:RetailerLististAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bank_list_bottomsheet_layout, container, false)
        binding.viewModel = myViewModel
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

    private fun onViewClick()
    {
        binding.apply {

        }
    }

    private fun setObserver() {

    }

    private fun initView() {

        activity?.let {act->
         loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        binding.apply {
            binding.apply {
                recycleViewPaymentRequest.apply {
                   /* bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))*/
                    var retailerArray: ArrayList<ViewRetailerData>
                    retailerArray=ArrayList()
                    if (retailer?.isNullOrEmpty()==true){
                        retailerArray=ArrayList()
                    }
                    else{
                        retailer?.let {
                            retailerArray =it
                        }

                    }

                    retailerLististAdapter= RetailerLististAdapter(retailerArray, object : CallBack7 {
                        override fun getValue7(ID: String, name: String, mobileNo: String, currBalance: String, bname: String,s1:String,s2:String) {

                            viewModel?.beneficiary_bank_name?.value=s1
                            viewModel?.beneficiary_ifsc?.value=s2
                            callBack.getValue7(ID,name,mobileNo,currBalance,bname,"","")
                            dismiss()
                        }
                    })
                    adapter=retailerLististAdapter
                    // Setup SearchView
                    binding.etSearch.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {

                            retailerLististAdapter?.filter(s.toString())
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            // Not needed
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            // Not needed
                        }
                    })
                }
                }
            }
        }

    }


