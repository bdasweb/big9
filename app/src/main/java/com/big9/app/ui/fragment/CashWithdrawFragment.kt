package com.big9.app.ui.fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.adapter.AdminBankListAdapter
import com.big9.app.adapter.UserDetailsAdapter
import com.big9.app.data.model.AdminBankListModel
import com.big9.app.data.model.UserDetails
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentCashWithdrawBinding


import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.fragment.TpinBottomSheetDialog
import com.big9.app.ui.receipt.CashWithdrawReceptDialogFragment
import com.big9.app.utils.helpers.Constants.isCashWithdraw
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.paysprint.onboardinglib.activities.HostActivity
import java.io.IOException
import java.util.Objects

class CashWithdrawFragment : BaseFragment() {
    lateinit var binding: FragmentCashWithdrawBinding
    private val viewModel: MyViewModel by activityViewModels()
    var userDetailsList = ArrayList<UserDetails>()
    var bankList = ArrayList<AdminBankListModel>()
    var adminBankListAdapter: AdminBankListAdapter?=null
    var selectBank=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cash_withdraw, container, false)
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
            //etProvidedAmount.setupAmount()



            btnSubmit.setOnClickListener{
                var lat=""
                var lng=""
                viewModel?.cash_withdraw_pin_code?.value?.let {

                    val zipCode = it// Replace with the actual zip code
                    val coordinates = getLatLngFromZipCode(btnSubmit.context, zipCode) // Assuming you are calling this from an Activity or Fragment

                    if (coordinates != null) {
                        val (latitude, longitude) = coordinates // Destructure the Pair into separate variables
                        lat=latitude.toString()
                        lng=longitude.toString()
                        // Now you can use latitude and longitude as needed
                        println("Latitude: $latitude, Longitude: $longitude")
                    } else {
                        println("Failed to retrieve coordinates for zip code: $zipCode")
                    }


                }

                activity?.let {act->
                    val intent = Intent(act, HostActivity::class.java).apply {
                        putExtra("pId", "PS00560")
                        putExtra("pApiKey", "UFMwMDU2MDlhM2JjYmZmZTE5MjVhMDI4MmRlN2QxZWM2ODI2OTZi")
                        putExtra("mobile", etPan.text.toString())
                        putExtra("email", "debbiswajit738@gmail.com")
                        putExtra("mCode", "big9000126")
                        putExtra("lat", "22.572645")
                        putExtra("lng", "88.363892")
                        putExtra("firm", "bdas")
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    someActivityResultLauncher.launch(intent)
                }


                /*activity?.let {act->
                    if (viewModel?.cashWithdrawValidation() == true){
                        val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                            override fun getValue(s: String) {
                                if (s=="123456"){
                                    Toast.makeText(requireActivity(), "ok", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })

                    }
                }*/

            }
           /* btnCashWithdraw.setOnClickListener {


                if (viewModel?.cashWithdrawalValidation()==true){
                    if (selectBank.isNotEmpty()) {
                        activity?.let { act ->
                            *//*val aadharAuthBottomSheetDialog =
                                AadharAuthBottomSheetDialog(object : CallBack {
                                    override fun getValue(s: String) {*//*


                                        val tpinBottomSheetDialog = TpinBottomSheetDialog(object :
                                            CallBack {
                                            override fun getValue(s: String) {
                                                val dialogFragment = CashWithdrawReceptDialogFragment(object:
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



                                 *//*   }
                                })
                            aadharAuthBottomSheetDialog.show(
                                act.supportFragmentManager,
                                aadharAuthBottomSheetDialog.tag
                            )*//*
                        }
                    }
                    else{
                        Toast.makeText(requireActivity(), "Select Bank", Toast.LENGTH_SHORT).show()
                    }
                }
            }*/


        }



    }

    fun initView() {
        aepsCall()
        binding.apply {
            etAmt.setupAmount()

            group1.isVisible= isCashWithdraw
            //group2.isVisible=!group1.isVisible
            binding.recycleViewUserdetails.apply {
                userDetailsList.clear()
                userDetailsList.add(UserDetails("Name","Test User"))
                userDetailsList.add(UserDetails("Outlet Name","Test Outlet Name"))
                userDetailsList.add(UserDetails("Mobile Number","9999999999"))
                userDetailsList.add(UserDetails("Email Id","test@test.com"))
                userDetailsList.add(UserDetails("Address","123, Park Street,Kolkata - 700001,West Bengal, India"))

                adapter= UserDetailsAdapter(userDetailsList)
            }

            /*binding.recycleViewBankList.apply {
                bankList.add(AdminBankListModel(R.drawable.axix_bank_logo,"AXIX Bank",false))
                bankList.add(AdminBankListModel(R.drawable.icici,"ICICI Bank",false))


                adminBankListAdapter= AdminBankListAdapter(bankList, object : CallBack4 {
                    override fun getValue4(s1: String, s2: String, s3: String, s4: String) {
                        selectBank=s1
                    }



                })
                adapter=adminBankListAdapter
            }*/


        }
    }

    fun setObserver() {
       /* binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adminBankListAdapter?.filter?.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })*/
    }

    private fun aepsCall() {
        activity?.let {act->

            /*val intent = Intent(act, HostActivity::class.java).apply {
                putExtra("pId", "PS00560")
                putExtra("pApiKey", "UFMwMDU2MDlhM2JjYmZmZTE5MjVhMDI4MmRlN2QxZWM2ODI2OTZi")
                putExtra("mobile", "9674375434")
                putExtra("email", "debbiswajit738@gmail.com")
                putExtra("mCode", "saura124")
                putExtra("lat", "22.572645")
                putExtra("lng", "88.363892")
                putExtra("firm", "bdas")
            }*/
            val intent = Intent(act, HostActivity::class.java).apply {
                putExtra("pId", "PS00560")
                putExtra("pApiKey", "UFMwMDU2MDlhM2JjYmZmZTE5MjVhMDI4MmRlN2QxZWM2ODI2OTZi")
                putExtra("mobile", "8574124740")
                putExtra("email", "debbiswajit738@gmail.com")
                putExtra("mCode", "big9000132")
                putExtra("lat", "22.572645")
                putExtra("lng", "88.363892")
                putExtra("firm", "bdas")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            someActivityResultLauncher.launch(intent)


            /*val intent = Intent(act, HostActivity::class.java)
            intent.putExtra("pId", "PS00560")
            intent.putExtra("pApiKey", "UFMwMDU2MDlhM2JjYmZmZTE5MjVhMDI4MmRlN2QxZWM2ODI2OTZi")
            intent.putExtra("mobile", "9674375433")
            intent.putExtra("email", "debbiswajit737@gmail.com")
            intent.putExtra("mCode", "saura123")
            intent.putExtra("lat", "22.572645")
            intent.putExtra("lng", "88.363892")
            intent.putExtra("firm", "bdas")
           *//* intent.putExtra("mCode", "saura123") //merchant unique code and should not contain special character
            intent.putExtra("mobile", "9674375433") // merchant mobile no.
            intent.putExtra("lat", "22.572645")
            intent.putExtra("lng", "88.363892")
            intent.putExtra("firm", "Test Telecom")
            intent.putExtra("email", "abc@gmail.com")*//*
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, 999)*/
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 999) {
            if (resultCode == Activity.RESULT_OK) {
                val status = data?.getBooleanExtra("status", false)
                val response = data?.getIntExtra("response", 0)
                val message = data?.getStringExtra("message")


                val detailedResponse = "Status: $status,  " +
                        "Response: $response, " +
                        "Message: $message "
                Toast.makeText(binding.root.context, detailedResponse, Toast.LENGTH_LONG).show()

                Log.i("logTag", detailedResponse)
            }
        }
    }

    private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            // Handle the result here
            val status = data?.getBooleanExtra("status", false)
            val response = data?.getIntExtra("response", 0)
            val message = data?.getStringExtra("message")

            val detailedResponse = "Status: $status, " +
                    "Response: $response, " +
                    "Message: $message"
            Toast.makeText(binding.root.context, detailedResponse, Toast.LENGTH_LONG).show()

            Log.i("logTag", detailedResponse)
        }
    }

    fun getLatLngFromZipCode(context: Context, zipCode: String): Pair<Double, Double>? {
        val geocoder = Geocoder(context)
        try {
            val addresses = geocoder.getFromLocationName(zipCode, 1)
            if (addresses?.isNotEmpty() == true) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                return Pair(latitude, longitude)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}