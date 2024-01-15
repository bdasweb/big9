package com.big9.app.ui.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R
import com.big9.app.adapter.BankListAdapter2
import com.big9.app.data.model.BankListModel2
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.BankListBottom2sheetLayoutBinding
import com.big9.app.ui.base.BaseBottomSheetFullPageFragment
import com.big9.app.ui.base.BaseCenterSheetFragment
import com.big9.app.utils.*
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class BankListBottom2SheetDialog(val callBack: CallBack, val bankList:ArrayList<BankListModel2>) : BaseCenterSheetFragment() {
    lateinit var binding: BankListBottom2sheetLayoutBinding
    private val myViewModel: MyViewModel by activityViewModels()
    private lateinit var bankListAdapter: BankListAdapter2
    //var bankList = ArrayList<BankListModel>()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bank_list_bottom2sheet_layout, container, false)
        binding.viewModel = myViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
   /* override fun getTheme(): Int {
        return R.style.SheetDialog
    }*/
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*dialog?.let { dialog ->
            dialog.setOnShowListener {
                val bottomSheetDialog = dialog as BottomSheetDialog
                val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.peekHeight = 0
                }
            }
        }*/
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {
        binding.apply {}

    }

    private fun setObserver() {
       /* binding.rootView.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener {
            val heightDiff: Int = binding.rootView.getRootView().getHeight() - binding.rootView.getHeight()
            if (heightDiff > 100) { // Value should be less than keyboard's height
                binding.view.visibility=View.VISIBLE
                //Log.e("MyActivity", "keyboard opened")
            } else {
                binding.view.visibility=View.GONE
               // Log.e("MyActivity", "keyboard closed")
            }
        })*/
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
                    bankListAdapter= BankListAdapter2( object : CallBack4 {
                        override fun getValue4(s1: String, s2: String, s3: String, s4: String) {
                            viewModel?.beneficiary_bank_name?.value=s1
                            viewModel?.beneficiary_ifsc?.value=s2
                            viewModel?.bankIdBene?.value=s4
                            dismiss()
                        }
                    })
                    adapter = bankListAdapter

                    bankListAdapter.submitList(bankList)
                    binding.etSearch.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            /*dialog?.let { dialog ->
                                dialog.setOnShowListener {
                                    val bottomSheetDialog = dialog as BottomSheetDialog
                                    val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                                    bottomSheet?.let {
                                        val behavior = BottomSheetBehavior.from(it)
                                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                                        behavior.peekHeight = 0
                                    }
                                }
                            }*/
                            bankListAdapter.filter.filter(s.toString())
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