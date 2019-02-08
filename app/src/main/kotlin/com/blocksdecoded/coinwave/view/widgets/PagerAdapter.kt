package com.blocksdecoded.coinwave.view.widgets

import java.util.*

class PagerAdapter(fm: androidx.fragment.app.FragmentManager, fragments: ArrayList<androidx.fragment.app.Fragment>): androidx.fragment.app.FragmentPagerAdapter(fm){

    private val mFragments = fragments

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }
}
