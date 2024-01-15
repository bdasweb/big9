package com.big9.app.ui.fragment


import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R
import com.big9.app.adapter.UserDetailsAdapter
import com.big9.app.data.model.UserDetails
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentUserDetailsBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.*
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter


class UserDetailsFragment : BaseFragment() {
    lateinit var binding: FragmentUserDetailsBinding
    private val viewModel: MyViewModel by activityViewModels()
    var userDetailsAdapter:UserDetailsAdapter?=null
    var userDetailsList = ArrayList<UserDetails>()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_details, container, false)
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

    private fun onViewClick() {

        binding.apply {
          imgBack.back()

          }
        }


    fun initView() {
        userDetailsList.clear()
        callProfile()
        binding.recycleViewUserdetails.apply {
           /* try {
                val bitmap = encodeAsBitmap("HELLO")
                binding.imgQrcode.setImageBitmap(bitmap)
            } catch (ex: WriterException) {
                ex.printStackTrace()
            }*/
            //adapter= UserDetailsAdapter(userDetailsList)
            userDetailsAdapter= UserDetailsAdapter(ArrayList())
            adapter=userDetailsAdapter
            /*userDetailsList.add(UserDetails("Name","Test User"))
            userDetailsList.add(UserDetails("Business Name","Test Business Name"))
            userDetailsList.add(UserDetails("Registered Mobile Number","9999999999"))
            userDetailsList.add(UserDetails("Registered Email Id","test@test.com"))
            userDetailsList.add(UserDetails("Address","123, Park Street,Kolkata - 700001,West Bengal, India"))
            userDetailsList.add(UserDetails("District/City","Kolkata"))
            userDetailsList.add(UserDetails("State","West Bengal"))
            userDetailsList.add(UserDetails("Account Type","Distributor"))
            userDetailsList.add(UserDetails("Super Distributor","9999999999"))
            userDetailsList.add(UserDetails("Distributor","9999999999"))*/

        }
    }

    private fun callProfile() {
        val context = binding.root.context // or activity.getApplicationContext()

        val packageManager = context.packageManager
        val packageName = context.packageName

        var myVersionName: String? = "0.0.0" // initialize String


        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        loginResponse?.let {loginData->


            val data = mapOf(

                "userid" to loginData.userid,
                "app_version" to myVersionName

            )
            /*"referenceid" to loginData.,*/
            val gson= Gson()
            var jsonString = gson.toJson(data)


            loginData.AuthToken?.let {
                viewModel?.profile2(it,jsonString.encrypt())
            }


        }


    }
    private fun observer() {
        viewModel?.profile2Response?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    // loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    it.data?.data?.let {
                       // binding.recycleViewUserdetails.apply {

                            userDetailsList.clear()
                            //sharedPreff?.setUserInfoData(it)
                            userDetailsList.add(UserDetails("Name",it.name.toString()))
                            userDetailsList.add(UserDetails("MobileNo",it.mobileNo.toString()))
                            userDetailsList.add(UserDetails("Alternate Number",it.AlternateNumber.toString()))
                            userDetailsList.add(UserDetails("Email ID",it.emailId.toString()))
                            userDetailsList.add(UserDetails("Address",it.address.toString()))
                            userDetailsList.add(UserDetails("Gender",it.gender.toString()))
                            userDetailsList.add(UserDetails("Pincode",it.pincode.toString()))
                            userDetailsList.add(UserDetails("DOB",it.dob.toString()))
                            //userDetailsList.add(UserDetails("Payout Balance",it.payoutBalance.toString()))
                            //userDetailsList.add(UserDetails("Payabhi Wallet",it.payabhiWallet.toString()))
                            //userDetailsList.add(UserDetails("Payabhi Wallet",it.payabhiWallet.toString()))
                            userDetailsAdapter?.items=userDetailsList
                            userDetailsAdapter?.notifyDataSetChanged()

                        try {
                            var dataUserDetails="Name ${it.name.toString()}\n"+
                                    "MobileNo: ${it.mobileNo.toString()}\n"+
                                    "Alternate Number: ${it.AlternateNumber.toString()}\n"+
                                    "Email ID: ${it.emailId.toString()}\n"+
                                    "Address: ${it.address.toString()}\n"+
                                    "Gender: ${it.gender.toString()}\n"+
                                    "Pincode: ${it.pincode.toString()}\n"+
                                    "DOB: ${it.dob.toString()}\n"+
                                    "Payout Balance: ${it.payoutBalance.toString()}\n"+
                                    "Payabhi Wallet: ${it.payabhiWallet.toString()}\n"
                            val bitmap = encodeAsBitmap(dataUserDetails)
                            binding.imgQrcode.setImageBitmap(bitmap)
                        } catch (ex: WriterException) {
                            ex.printStackTrace()
                        }
                        //}

                    }
                    //  Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
    }

    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String): Bitmap? {
        val writer = QRCodeWriter()
        val bitMatrix: BitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, 400, 400)
        val w: Int = bitMatrix.getWidth()
        val h: Int = bitMatrix.getHeight()
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                pixels[y * w + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }
}