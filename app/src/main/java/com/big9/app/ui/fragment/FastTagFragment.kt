package com.big9.app.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentFastTagBinding

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.`interface`.CallBack

class FastTagFragment : BaseFragment() {
    lateinit var binding: FragmentFastTagBinding
    private val viewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fast_tag, container, false)
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
                activity?.let {act->
                    val fasttagOperatorListBottomSheetDialog = FasttagOperatorListBottomSheetDialog(object :
                        CallBack {
                        override fun getValue(s: String) {
                            Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                        }
                    })
                    fasttagOperatorListBottomSheetDialog.show(
                        act.supportFragmentManager,
                        fasttagOperatorListBottomSheetDialog.tag
                    )
                }
            }

            btnSubmit.setOnClickListener{
                if (viewModel?.fastTagValidation() == true){
                    Toast.makeText(btnSubmit.context, "ok", Toast.LENGTH_SHORT).show()
                }
            }
            etOperator.setOnClickListener {
                rlOperator.performClick()
            }

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