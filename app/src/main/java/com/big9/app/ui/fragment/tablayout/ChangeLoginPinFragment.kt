package com.big9.app.ui.fragment.tablayout


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentChangeLoginBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson


class ChangeLoginPinFragment : BaseFragment() {
    lateinit var binding: FragmentChangeLoginBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_login, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observer()
        onViewClick()
    }



    private fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
    }

    private fun onViewClick() {

        binding.apply {
            rootView.setOnClickListener {
                activity?.let { act -> rootView.hideSoftKeyBoard(act) }
            }
            btnSubmit.setOnClickListener{
                if (viewModel?.changeLoginPinValidation() == true){
                    changePassword()
                }
            }
        }

    }

    private fun changePassword() {
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        if (isLogin){
            loginResponse?.let {loginData->
                val data = mapOf(
                    "userid" to loginData.userid,
                    "new_login_pin" to viewModel?.newPin?.value,
                    "confirm_login_pin" to viewModel?.confirmPin?.value,
                    "old_login_pin" to viewModel?.oldPin?.value,

                    )
                val gson= Gson()

                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {
                    viewModel?.changePin(it, jsonString.encrypt())
                }

            }
        }
    }

    private fun observer() {
        viewModel.changePinResponseLiveData.observe(viewLifecycleOwner){
            when(it){
                is ResponseState.Loading -> {
                    loader?.show()
                }
                is ResponseState.Success->{
                    loader?.dismiss()
                    viewModel?.newPin?.value=""
                    viewModel?.confirmPin?.value=""
                    viewModel?.oldPin?.value=""
                    it.data?.let {
                        viewModel.popup_message.value=it.Description
                        val successPopupFragment = SuccessPopupFragment(object :
                            CallBack4 {
                            override fun getValue4(
                                s1: String,
                                s2: String,
                                s3: String,
                                s4: String
                            ) {
                                findNavController().popBackStack()
                            }
                        })
                        successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
                    }
                    loader?.dismiss()
                    viewModel.changePinResponseLiveData.value=null
                }
                is ResponseState.Error->{
                    loader?.dismiss()
                    viewModel?.newPin?.value=""
                    viewModel?.confirmPin?.value=""
                    viewModel?.oldPin?.value=""

                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel.changePinResponseLiveData.value=null
                }
            }
        }
    }
}