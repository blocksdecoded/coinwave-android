package com.blocksdecoded.coinwave.view.widgets

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class PagerAdapter(fm: FragmentManager, fragments: ArrayList<androidx.fragment.app.Fragment>): FragmentPagerAdapter(fm){

    private val mFragments = fragments

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }
}
