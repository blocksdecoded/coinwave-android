package com.makeuseof.cryptocurrency.view.main

import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.Gravity
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.domain.UseCaseProvider
import com.makeuseof.cryptocurrency.view.list.CurrencyListContract
import com.makeuseof.cryptocurrency.view.list.CurrencyListFragment
import com.makeuseof.cryptocurrency.view.list.CurrencyListPresenter
import com.makeuseof.cryptocurrency.view.watchlist.WatchListContract
import com.makeuseof.cryptocurrency.view.watchlist.WatchListFragment
import com.makeuseof.cryptocurrency.view.watchlist.WatchListPresenter
import com.makeuseof.cryptocurrency.view.widgets.FontTextView
import com.makeuseof.cryptocurrency.view.widgets.PagerAdapter
import com.makeuseof.utils.ResourceUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mWatchListPresenter: WatchListContract.Presenter? = null
    private var mCurrencyListPresenter: CurrencyListContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        init()
    }

    override fun onBackPressed() {
        when(main_view_pager.currentItem){
            1 -> {main_view_pager.currentItem = 0}
            else -> super.onBackPressed()
        }
    }

    //region Init

    private fun init(){
        val fragments = ArrayList<Fragment>()

        fragments.add(createWatchListScreen())
        fragments.add(createCurrencyListScreen())

        initViewPager(fragments)
    }

    private fun createWatchListScreen(): Fragment{
        val fragment = WatchListFragment()

        mWatchListPresenter = WatchListPresenter(
                fragment,
                UseCaseProvider.getCurrencyListUseCases(applicationContext)
        )

        return fragment
    }

    private fun createCurrencyListScreen(): Fragment{
        val fragment = CurrencyListFragment()

        mCurrencyListPresenter = CurrencyListPresenter(
                fragment,
                UseCaseProvider.getCurrencyListUseCases(applicationContext)
        )

        return fragment
    }

    private fun initViewPager(fragments: ArrayList<Fragment>){
        main_tab_layout.addTab(getTab("Watchlist"))
        main_tab_layout.addTab(getTab("Cryptocurrencies"))
        main_tab_layout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = PagerAdapter(supportFragmentManager, fragments)
        main_view_pager.adapter = adapter
        main_view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tab_layout))
        main_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { main_view_pager.currentItem = tab.position }
            }
        })
    }

    private fun getTab(title: String): TabLayout.Tab {
        val textView = FontTextView(this)
        textView.gravity = Gravity.CENTER
        textView.text = title
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

        val colorStates = ColorStateList(
                arrayOf(
                        intArrayOf(android.R.attr.state_selected), intArrayOf()
                ),
                intArrayOf(
                        ResourceUtil.getColor(this, R.color.light_blue),
                        ResourceUtil.getColor(this, R.color.subtitle_text)
                )
        )

        textView.setTextColor(colorStates)


        return main_tab_layout.newTab().setCustomView(textView)
    }

    //endregion
}
