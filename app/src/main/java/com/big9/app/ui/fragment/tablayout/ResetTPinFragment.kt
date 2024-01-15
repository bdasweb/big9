package com.big9.app.ui.fragment.tablayout


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentResetBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson


class ResetTPinFragment : BaseFragment() {
    lateinit var binding: FragmentResetBinding
    private val viewModel: MyViewModel by activityViewModels()

    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset, container, false)
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
            btnSubmit.setOnClickListener {
                val (isLogin, loginResponse) = sharedPreff.getLoginData()
                if (isLogin) {
                    loginResponse?.let { loginData ->
                        viewModel?.apply {

                            val data = mapOf(
                                "userid" to loginData.userid
                            )

                            val gson = Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                               var test= "IU/aH5WfC3EghIAD/7SrnuvW6D43p8twUBhgS59MRTLZNGHWRN6tqf6Wb4BU/RUZqSB7nR3hPiN3qDb7wKqo1dnp9JKKGOlP4AVaQS9blGrgmxh5EvPKYjns7X7/rRto".decrypt()
                                Log.d("TAG_printdata", "onViewClick: "+test)
                                resetTPINResponse(it, jsonString.encrypt())
                            }
                        }

                    }
                }
            }
        }
    }


    fun initView() {

        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
    }

    fun setObserver() {
        binding.apply {

        }

        viewModel?.resetTPINResponseReceptLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                        it?.data?.Description?.let { msg ->
                                viewModel.popup_message.value =msg
                        }

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

                    viewModel?.resetTPINResponseReceptLiveData?.value = null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    viewModel?.resetTPINResponseReceptLiveData?.value = null
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }

    }


}