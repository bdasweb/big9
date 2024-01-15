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
import com.big9.app.databinding.FragmentTermsAndConditionBinding

import com.big9.app.ui.base.BaseFragment


class TermsAndConditionFragment : BaseFragment() {
    lateinit var binding: FragmentTermsAndConditionBinding
    private val viewModel: MyViewModel by activityViewModels()
    var contactModelList = ArrayList<ContactModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms_and_condition, container, false)
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