package com.big9.app.ui.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.data.model.ContactModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentSupportBinding

import com.big9.app.ui.base.BaseFragment


class SupportFragment : BaseFragment() {
    lateinit var binding: FragmentSupportBinding
    private val viewModel: MyViewModel by activityViewModels()
    var contactModelList = ArrayList<ContactModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_support, container, false)
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
          tvNavigation.setOnClickListener{

              // Specify the coordinates (latitude and longitude) for the location
              val latitude = 22.5137
              val longitude = 88.3933

              val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
              val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
              mapIntent.setPackage("com.google.android.apps.maps")
              //mapIntent.resolveActivity(binding.root.context.packageManager)?.let {
                  startActivity(mapIntent)
              //}
          }
            tvWebsite.setOnClickListener {
                val url="https://big9.co.in/"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
            tvEmail.setOnClickListener{
                    val recipient = arrayOf("recipient@example.com") // Replace with the recipient's email
                    val subject = "Hello from my app!"
                    composeEmail(recipient, subject)
            }
            tvMobile.setOnClickListener{
                val phoneNumber = "tel:+123456789"

                makePhoneCall(phoneNumber)
            }



          imgBack.back()

          }
        }


    fun initView() {
        /*binding.recycleViewContact.apply {
            contactModelList.add(ContactModel("Office Address","123, Park Street,Kolkata - 700001,West Bengal, India","Navigate"))
            contactModelList.add(ContactModel("Office Address","123, Park Street,Kolkata - 700001,West Bengal, India","Navigate"))

            adapter= ContactAdapter(contactModelList)
        }*/
    }

    fun setObserver() {
        binding.apply {

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