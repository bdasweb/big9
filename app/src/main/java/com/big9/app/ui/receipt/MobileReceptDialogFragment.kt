package com.big9.app.ui.receipt


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.big9.app.R
import com.big9.app.data.model.PrepaidMoboleTranspherModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentMobileReceptDialogBinding
import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.ui.base.BaseCenterSheetFragment
import com.big9.app.utils.`interface`.CallBack
import kotlinx.coroutines.launch


class MobileReceptDialogFragment(val callBack: CallBack, val data: PrepaidMoboleTranspherModel?) : BaseCenterSheetFragment() {
    lateinit var binding: FragmentMobileReceptDialogBinding
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_recept_dialog, container, false)
        binding.viewModel = viewModel
        binding.model=data

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

      /*  binding.apply {
            imgBack.setOnClickListener{
               dismiss()
                viewModel?.apply {
                    mobile.value=""
                    operator.value=""
                    amt.value=""
                }

            }
            imgHome.setOnClickListener{
                dismiss()
                findNavController().navigate(R.id.action_mobileRechargeFragment_to_homeFragment2)
                viewModel?.apply {
                    mobile.value=""
                    operator.value=""
                    amt.value=""
                }
                //imgHome.backToHome()
            }

            fabShare.setOnClickListener{
                shareImage()
            }
            *//*imgHome.setOnClickListener{
                callBack.getValue("back")
                *//**//*(activity as? DashboardActivity)?.let {
                    it.startActivity(Intent(it,DashboardActivity::class.java).putExtra(isRecept,true).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                }*//**//*
            }*//*
          }*/
        }



    private fun shareImage() {

        activity?.let {
            binding.apply {

              //  var screenshotBitmap =cardView2.takeScreenshot()
              //  (activity as? DashboardActivity)?.shareImage(screenshotBitmap)
            }
        }


    }

    fun initView() {
       /* if ((data?.status?.lowercase()=="success")==true){
            binding.imgWriteTick.setImageResource(R.drawable.right_tick)
        }
        else{
            binding.imgWriteTick.setImageResource(R.drawable.close_icon)
        }*/
        setCrdViewMinHeight()
        lifecycleScope.launch {
            //delay(1000)
      /*      Glide.with(binding.root.context)

                .load(data?.image)
               // .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgOperator)*/
            /*viewModel?.selectrdOperator?.observe(viewLifecycleOwner){operatorImage->
                if (operatorImage!=null){
                    try {
                         imgOperator.setImageResource()

                        Glide.with(binding.root.context)

                            .load(data?.image)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.imgOperator)
                    } catch (e:Exception){}
                }
            }*/
        }




    }

    private fun setCrdViewMinHeight() {
    }

    fun setObserver() {
        binding.apply {
            viewModel?.selectrdOperator?.observe(viewLifecycleOwner){operatorImage->
                if (operatorImage!=null){
                 /*  try {
                       imgOperator.setImageResource(operatorImage.toInt())
                   } catch (e:Exception){}*/
                }
            }
        }

    }


}