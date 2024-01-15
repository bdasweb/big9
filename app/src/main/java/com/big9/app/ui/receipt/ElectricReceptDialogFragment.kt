package com.big9.app.ui.receipt


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import billpaytransactionData
import com.big9.app.R
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentElectricReceptDialogBinding
import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.ui.base.BaseCenterSheetFragment
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.CallBack


class ElectricReceptDialogFragment(val callBack: CallBack,val eTransDAta: billpaytransactionData?) : BaseCenterSheetFragment() {
    lateinit var binding: FragmentElectricReceptDialogBinding
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_electric_recept_dialog, container, false)
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
            imgBack.setOnClickListener {
               dismiss()
            }
            //imgBack.back()
           // imgHome.backToHome()
            imgBack.setOnClickListener{
              //  imgHome.performClick()
            }
            imgHome.setOnClickListener {
                activity?.let {act->

                    val intent = Intent(act, DashboardActivity::class.java).apply {
                        putExtra(Constants.isAfterReg, true)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)

                }
            }
            fabShare.setOnClickListener{
                shareImage()
            }
            imgHome.setOnClickListener{
                callBack.getValue("back")
                /*(activity as? DashboardActivity)?.let {
                    it.startActivity(Intent(it,DashboardActivity::class.java).putExtra(isRecept,true).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                }*/
            }
          }
        }
    private fun shareImage() {
        activity?.let {
            binding.apply {
                var screenshotBitmap =cardView2.takeScreenshot()
                (activity as? DashboardActivity)?.shareImage(screenshotBitmap)
            }
        }
    }

    fun initView() {
        setCrdViewMinHeight()
        eTransDAta?.let {
            binding.apply {
                textView26.text="Consumer Id: ${it.customerId}"
                textView29.text="${it.txnAmount}"
                tvBillDate.text="${it.txnDate}"
                textView30.text="${it.txnAmount}"
                tvTransactionId.text="${it.txnID}"
                tvInvoiceValue.text="${it.integratorid}"
                tvBillPaidNonth.text="${it.txnstatue}"
                if ((it.txnstatue?.lowercase()=="success")==true){
                    imgWriteTick.setImageResource(R.drawable.right_tick)
                }
                else{
                    imgWriteTick.setImageResource(R.drawable.close_icon)
                }

            }
            /*
             "customer_id": "303357803",
        "txnAmount": "00",
        "txnDate": "2024-01-05 12:50:48",
        "txnID": 300000455,
        "txnstatue": "SUCCESS",
        "integratorid": null,
        "operatorid": null
             */
        }
    }

    private fun setCrdViewMinHeight() {
    }

    fun setObserver() {
        binding.apply {
            if ((eTransDAta?.txnstatue?.lowercase()=="success")==true){
                binding.imgWriteTick.setImageResource(R.drawable.right_tick)
            }
            else{
                binding.imgWriteTick.setImageResource(R.drawable.close_icon)
            }
            viewModel?.selectrdOperator?.observe(viewLifecycleOwner){operatorImage->
                if (operatorImage!=null){
                   try {
                       imgOperator.setImageResource(operatorImage.toInt())
                   } catch (e:Exception){}
                }
            }
        }

    }


}