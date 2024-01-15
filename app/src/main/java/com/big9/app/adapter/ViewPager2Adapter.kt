package com.big9.app.adapter
import androidx.fragment.app.Fragment


import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(private val fragmentList: List<Fragment>) : FragmentStateAdapter(fragmentList[0].requireActivity()) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}