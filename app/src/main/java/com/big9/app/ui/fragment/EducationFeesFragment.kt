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
import com.big9.app.databinding.FragmentEducationFeeBinding


import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.fragment.fragmentDialog.BankListWithAddBankBottomSheetDialog
import com.big9.app.ui.fragment.fragmentDialog.InstituteListDialogFragment
import com.big9.app.ui.receipt.InstituteReceptDialogFragment
import com.big9.app.utils.`interface`.CallBack
import java.util.Objects

class EducationFeesFragment : BaseFragment() {
    lateinit var binding: FragmentEducationFeeBinding
    private val viewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_education_fee, container, false)
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
            etBank.setOnClickListener {
                rlBank.performClick()
            }
            rlBank.setOnClickListener{
                activity?.let {act->
                    val bankListWithAddBankBottomSheetDialog = BankListWithAddBankBottomSheetDialog(object :
                        CallBack {
                        override fun getValue(s: String) {

                        }
                    })
                    bankListWithAddBankBottomSheetDialog.show(
                        act.supportFragmentManager,
                        bankListWithAddBankBottomSheetDialog.tag
                    )
                }
            }
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

            btnSubmit.setOnClickListener{
                if (viewModel?.educationValidation() == true){
                    val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                        override fun getValue(s: String) {
                            val dialogFragment = InstituteReceptDialogFragment(object: CallBack {
                                override fun getValue(s: String) {
                                    if (Objects.equals(s,"back")) {
                                        findNavController().popBackStack()
                                    }
                                }
                            })
                            dialogFragment.show(childFragmentManager, dialogFragment.tag)
                        }
                    })
                    activity?.let {act->
                        tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)
                    }
                }
            }
            /*etOperator.setOnClickListener {
                rlOperator.performClick()
            }
            rl_instititute.setOnClickListener{
                activity?.let {act->
                    isDthOperator=true
                    findNavController().navigate(R.id.action_DTHRechargeFragment_to_operatorFragment)
                }

            }

            */
            etInstitute.setOnClickListener {
                rlInstitute.performClick()
            }
            rlInstitute.setOnClickListener{
                activity?.let {act->
                    val instituteListDialogFragment = InstituteListDialogFragment(object :
                        CallBack {
                        override fun getValue(s: String) {
                            // Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                        }
                    })
                    instituteListDialogFragment.show(
                        act.supportFragmentManager,
                        instituteListDialogFragment.tag
                    )
                }
            }
        }

        binding.imgClose.setOnClickListener{
            binding.cardUserDetails.visibility=View.GONE
        }

    }

    fun initView() {
        binding.apply {
            etAmt.setupAmount()
        }

    }

    fun setObserver() {

    }


}