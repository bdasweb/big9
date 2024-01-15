package com.big9.app.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentLoanPaymentBinding

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.fragment.fragmentDialog.BankListOnlyNameBottomSheetDialog
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4

class LoanPaymentFragment : BaseFragment() {
    lateinit var binding: FragmentLoanPaymentBinding
    private val viewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loan_payment, container, false)
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

            btnCustomerInfo.setOnClickListener{
                context?.let {ctx->
                    viewModel?.userName?.value = "Sample user"
                    viewModel?.balence?.value = "1009"
                    viewModel?.nextRecharge?.value = "01-01-2023"
                    viewModel?.monthly?.value = "789"
                    viewModel?.type?.value = "Active"
                    binding.cardUserDetails.visibility=View.VISIBLE
                }
            }

            etSelectBank.setOnClickListener {
                activity?.let {act->
                    val bankListOnlyNameBottomSheetDialog = BankListOnlyNameBottomSheetDialog(object :
                        CallBack {
                        override fun getValue(s: String) {
                           // Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                        }
                    })
                    bankListOnlyNameBottomSheetDialog.show(
                        act.supportFragmentManager,
                        bankListOnlyNameBottomSheetDialog.tag
                    )
                }
            }

            btnSubmit.setOnClickListener{
                if (viewModel?.loanValidation() == true){
                    val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                        override fun getValue(s: String) {
                            viewModel?.popup_message?.value="Your loan payment was successful."
                            val successPopupFragment = SuccessPopupFragment(object :
                                CallBack4 {
                                override fun getValue4(
                                    s1: String,
                                    s2: String,
                                    s3: String,
                                    s4: String
                                ) {
                                    viewModel?.popup_message?.value="Success"
                                    findNavController().popBackStack()
                                }
                            })
                            successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
                        }
                    })
                    activity?.let {act->
                        tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)
                    }
                }
            }

        }

        binding.imgClose.setOnClickListener{
            binding.cardUserDetails.visibility=View.GONE
        }

    }

    fun initView() {
        binding.apply {
            viewModel?.selectBank?.value=""
            etAmt.setupAmount()
        }

    }

    fun setObserver() {

    }


}