package com.big9.app.ui.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.adapter.PhonePadAdapter
import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.FragmentLoginBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError


import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.KeyPadOnClickListner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    lateinit var binding: FragmentLoginBinding
    var keyPad = ArrayList<Int>()
    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.viewModel = authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        onViewClick()
        setObserver()
    }

    fun initView() {

        /*var a="abc".encrypt()
        Log.d("TAG_edata", "encript data : "+a)
        val b=a.decrypt()
        Log.d("TAG_edata", "decript data : "+b)*/
        keyPad.clear()
        keyPad.add(1)
        keyPad.add(2)
        keyPad.add(3)
        keyPad.add(4)
        keyPad.add(5)
        keyPad.add(6)
        keyPad.add(7)
        keyPad.add(8)
        keyPad.add(9)
        keyPad.add(10)
        keyPad.add(0)
        keyPad.add(11)
        binding.recyclePhonePad.apply {
            //authViewModel.keyPadValue.value=getString(R.string.mobile_no_hint)
            adapter= PhonePadAdapter(keyPad,object : KeyPadOnClickListner{
                override fun onClick(item: Int) {
                    authViewModel.mobError.value=""
                    authViewModel.keyPadValue.value?.apply {
                        if (item<=9 ) {
                            if (this.length!=10) {
                                authViewModel.keyPadValue.value = "${this}$item"
                            }
                        }
                        else if(item==10){
                            //authViewModel.keyPadValue.value =""
                        }
                        else {
                            if (this.isNotEmpty()) {
                                authViewModel.keyPadValue.value = this.substring(0, this.length - 1)
                            }

                        }
                    }

                }

            })
            isNestedScrollingEnabled=false
        }
    }

    private fun onViewClick() {
        binding.apply {
            btnConfirmLocation.setOnClickListener {
                if (authViewModel.keyPadValue.value?.isEmpty() == true || authViewModel.keyPadValue.value?.length!=10){
                    authViewModel.mobError.value="Please enter a valid mobile number."
                }
                else{

                    authViewModel.mobError.value=""
                    viewModel?.keyPadValue?.value?.let {
                        /*val loginModel=LoginModel(authData=it)
                        val gson= Gson()
                        val jsonString = gson.toJson(loginModel)
                        viewModel.authLoginRegistration(jsonString.encrypt())*/
                    }

                    findNavController().navigate(R.id.action_loginFragment_to_otpFragment)
                }
            }
        }

    }

    fun setObserver() {
        authViewModel?.appupdateResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    //loader?.show()
                    Log.d("TAGupdate", "observer: 1")
                }

                is ResponseState.Success -> {
                   // loader?.dismiss()
                    Log.d("TAGupdate", "observer: 2")
                    Constants.appUpdateUrl =it?.data?.appUpdateUrl
                    it?.data?.appUpdateUrl?.let {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    }
                    authViewModel?.appupdateResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    Log.d("TAGupdate", "observer: 3")
                  //  loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    authViewModel?.appupdateResponseLiveData?.value=null
                }
            }
        }
    }


}