package com.big9.app.ui.receipt


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentBalenceEnquaryReceptDialogBinding
import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.ui.base.BaseCenterSheetFragment
import com.big9.app.utils.`interface`.CallBack


class BalenceEnquaryReceptDialogFragment(val callBack: CallBack) : BaseCenterSheetFragment() {
    lateinit var binding: FragmentBalenceEnquaryReceptDialogBinding
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_balence_enquary_recept_dialog, container, false)
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
            imgHome.backToHome()
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

    }

    private fun setCrdViewMinHeight() {
    }

    fun setObserver() {
        binding.apply {
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