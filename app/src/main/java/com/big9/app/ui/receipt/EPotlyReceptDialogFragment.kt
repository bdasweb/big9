package com.big9.app.ui.receipt


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.big9.app.R
import com.big9.app.data.model.EpotlyData
import com.big9.app.data.model.profile.Data
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentEPotlyReceptDialogBinding
import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.ui.base.BaseCenterSheetFragment
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.CallBack


class EPotlyReceptDialogFragment(
    val callBack: CallBack,
    val epotlyData: EpotlyData,
    val userData: Data
) : BaseCenterSheetFragment() {
    lateinit var binding: FragmentEPotlyReceptDialogBinding
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_e_potly_recept_dialog, container, false)
        binding.viewModel = viewModel
        binding.epotlyData=epotlyData
        binding.usere=userData
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
        userData?.let{
            setUserData(it)
        }
        epotlyData?.let {
            binding.apply {
                textView26.text= Constants.epotlyMoboleNo.replace("null","")
                textView30.text=epotlyData?.curramt.toString().replace("null","")
                tvTransaction.text= epotlyData?.id.toString().replace("null","")
                tvPrice.text= epotlyData?.LastTransactionAmount.toString().replace("null","")
                textView29.text= epotlyData?.LastTransactionAmount.toString().replace("null","")
                tvBankPrice.text= epotlyData?.LastTransactionAmount.toString().replace("null","")
            }


        }
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
    private fun setUserData(data: Data) {
        binding.tvUtr.text=data?.name
        data.SelfieImageData?.let {
            val decodedString: ByteArray = Base64.decode(it, Base64.DEFAULT)
            val decodedByte =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            Glide.with(this)
                .asBitmap() // Use asBitmap() instead of asGif()
                .load(decodedByte)
                .error(R.drawable.ic_success) // Set the default image resource

                .into(binding.imgBank)
        }

    }

}