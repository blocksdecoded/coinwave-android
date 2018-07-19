package com.makeuseof.cryptocurrency.view.widgets

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

class PagerAdapter(fm: FragmentManager, fragments: ArrayList<Fragment>): FragmentPagerAdapter(fm){

    private val mFragments = fragments

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }
}
