package com.big9.app.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.adapter.YourFragmentPagerAdapter
import com.big9.app.data.model.ContactModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentChangePasswordBinding

import com.big9.app.ui.base.BaseFragment


class ChangePasswordFragment : BaseFragment() {
    lateinit var binding: FragmentChangePasswordBinding
    private val viewModel: MyViewModel by activityViewModels()
    var contactModelList = ArrayList<ContactModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
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
        binding.viewPager.apply {

             adapter =  YourFragmentPagerAdapter(childFragmentManager)
            binding.tabLayout.setupWithViewPager(this)
            // adapter = MyAdapter(this.context, arrayListOf("Change Login PIN","Change TPIN","Reset TPIN"))//ViewPagerAdapter(requireActivity())


           /* TabLayoutMediator(binding.tabLayout, this) { tab, position ->
                tab.text = adapter.getPageTitle(position)
            }.attach()*/




            /*var adapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
            TabLayoutMediator(binding.tabLayout, this) { tab, position ->
                tab.text = adapter.getPageTitle(position)
            }.attach()

            adapter = adapter*/
        }

    }

    fun setObserver() {
        binding.apply {

        }

    }


}