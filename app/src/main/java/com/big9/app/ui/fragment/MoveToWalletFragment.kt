package com.big9.app.ui.fragment


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentMoveToWalletBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.ui.receipt.MoveToWalletPayabhiReceptDialogFragment
import com.big9.app.ui.receipt.MoveToWalletReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson
import java.util.Objects


class MoveToWalletFragment : BaseFragment() {
    lateinit var binding: FragmentMoveToWalletBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var isRotatedWallet = true
    private var isRotatedPayabhi = true
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_move_to_wallet, container, false)
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
            rootView.setOnClickListener{
                activity?.let {act-> rootView.hideSoftKeyBoard(act) }
            }
            val collapseAnimation: Animation =
                AnimationUtils.loadAnimation(requireActivity(), R.anim.collapse_animation)
          imgBack.back()

            arrowImageViewSettlement.setOnClickListener {

                if (isRotatedWallet) {

                    rotateView(arrowImageViewSettlement, 0f)


                    val layoutParams = llContainer.layoutParams
                    layoutParams.height = 40
                    llContainer.layoutParams = layoutParams
                    llContainer.startAnimation(collapseAnimation)

                    llContainer.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageViewSettlement, 180f)
                    val layoutParams = llContainer.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    llContainer.layoutParams = layoutParams
                    llContainer.visibility = View.VISIBLE
                    llContainer.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotatedWallet = !isRotatedWallet
            }


            arrowImageViewpayabhi.setOnClickListener {

                if (isRotatedPayabhi) {

                    rotateView(arrowImageViewpayabhi, 0f)


                    val layoutParams = llContainerPayabhi.layoutParams
                    layoutParams.height = 40
                    llContainerPayabhi.layoutParams = layoutParams
                    llContainerPayabhi.startAnimation(collapseAnimation)

                    llContainerPayabhi.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageViewpayabhi, 180f)
                    val layoutParams = llContainerPayabhi.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    llContainerPayabhi.layoutParams = layoutParams
                    llContainerPayabhi.visibility = View.VISIBLE
                    llContainerPayabhi.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotatedPayabhi = !isRotatedPayabhi
            }

            activity?.let { act ->
                tvSettleToWalletSubmit.setOnClickListener {


                    if (viewModel?.settleWalletValidation() == true) {
                        val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                            override fun getValue(s: String) {
                                val (isLogin, loginResponse) =sharedPreff.getLoginData()
                                if (isLogin){
                                loginResponse?.let {loginData->
                                viewModel?.apply {

                                val  data = mapOf(
                                "userid" to loginData.userid,
                                "tpin" to s,
                                "amount" to amtMoveToWallet?.value
                                )

                                val gson= Gson()
                                var jsonString = gson.toJson(data)
                                loginData.AuthToken?.let {
                                    submitMoveToWallet(it,jsonString.encrypt())
                                }
                                }

                                }
                                }





                            }
                        })
                        activity?.let {act->
                            tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)
                        }
                        /*val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                            override fun getValue(s: String) {
                                viewModel?.receiveStatus?.value="Failed"
                                val dialogFragment = ReceptDialogFragment()
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                //Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                            }
                        })
                        tpinBottomSheetDialog.show(
                            act.supportFragmentManager,
                            tpinBottomSheetDialog.tag
                        )*/

                    }

                }

                tvSettleToPayabhiSubmit.setOnClickListener {

                    if (viewModel?.payabhiValidation() == true) {
                        val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                            override fun getValue(s: String) {
                                val dialogFragment = MoveToWalletPayabhiReceptDialogFragment(object:
                                    CallBack {
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
                        /*val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                            override fun getValue(s: String) {
                                viewModel?.receiveStatus?.value="Success"

                                *//*val dialogFragment = ReceptDialogFragment()
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()*//*
                            }
                        })
                        tpinBottomSheetDialog.show(
                            act.supportFragmentManager,
                            tpinBottomSheetDialog.tag
                        )*/

                    }
                }

            }


          }
        }


    fun initView() {
        activity?.let {act->
            viewModel?.amtMoveToWallet?.value=""
          loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        apiCall()
       binding?.apply {
           etAmtWallet.setupAmount()
           etAmtPayabhi.setupAmount()
       }

    }

    private fun apiCall() {
        viewModel?.apply {
            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            if (isLogin){
            loginResponse?.let {loginData->
            viewModel?.apply {

            val  data = mapOf(
            "userid" to loginData.userid
            )

            val gson= Gson()
            var jsonString = gson.toJson(data)
            loginData.AuthToken?.let {
            moveToWallet(it,jsonString.encrypt())
            }
            }

            }
            }
        }
    }

    fun setObserver() {
        viewModel.apply {

            moveToWalletLiveData?.observe(viewLifecycleOwner){
                when (it) {
                    is ResponseState.Loading -> {
                        loader?.show()
                    }
            
                    is ResponseState.Success -> {
                        loader?.dismiss()
            
            
                        moveToWalletLiveData?.value=null
                    }
            
                    is ResponseState.Error -> {
            
                        loader?.dismiss()
            
                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        moveToWalletLiveData?.value=null
            
                    }
                }
            }
            submitMoveToWalletLiveData?.observe(viewLifecycleOwner){
                when (it) {
                    is ResponseState.Loading -> {
                        loader?.show()
                    }

                    is ResponseState.Success -> {
                        loader?.dismiss()
                        popup_message?.value=it.data?.Description
                        val successPopupFragment = SuccessPopupFragment(object :
                            CallBack4 {
                            override fun getValue4(
                                s1: String,
                                s2: String,
                                s3: String,
                                s4: String
                            ) {
                                popup_message?.value="Success"
                                findNavController().popBackStack()
                                /*val dialogFragment = MoveToWalletReceptDialogFragment(object:
                                    CallBack {
                                    override fun getValue(s: String) {
                                        if (Objects.equals(s,"back")) {
                                            findNavController().popBackStack()
                                        }
                                    }
                                })
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)*/
                            }

                        })
                        successPopupFragment.show(childFragmentManager, successPopupFragment.tag)


                        submitMoveToWalletLiveData?.value=null
                    }

                    is ResponseState.Error -> {

                        loader?.dismiss()

                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        submitMoveToWalletLiveData?.value=null

                    }
                }
            }
        }




    }

    private fun composeEmail(addresses: Array<String>, subject: String?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)

        //if (intent.resolveActivity(binding.root.context.packageManager) != null) {
            startActivity(intent)
        //}
    }

    private fun makePhoneCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse(phoneNumber)

        if (callIntent.resolveActivity(binding.root.context.packageManager) != null) {
            startActivity(callIntent)
        }
    }
}