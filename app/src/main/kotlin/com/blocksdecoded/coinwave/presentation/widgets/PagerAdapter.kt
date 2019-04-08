package com.blocksdecoded.coinwave.presentation.widgets

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class PagerAdapter(
    fm: FragmentManager,
    private val mFragments: ArrayList<Fragment>
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = mFragments[position]
    override fun getCount(): Int = mFragments.size
}
