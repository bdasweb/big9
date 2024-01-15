package com.big9.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.big9.app.ui.fragment.tablayout.ChangeLoginPinFragment
import com.big9.app.ui.fragment.tablayout.ChangeTPINPinFragment
import com.big9.app.ui.fragment.tablayout.ResetTPinFragment


class YourFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ChangeLoginPinFragment()
            1 -> ChangeTPINPinFragment()
            else -> ResetTPinFragment()

        }
    }

    override fun getCount(): Int {
        return 3 // Number of tabs/fragments
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Change Login PIM"
            1 -> "Change TPIN"
            else -> "Reset TPIN"

        }
    }
}