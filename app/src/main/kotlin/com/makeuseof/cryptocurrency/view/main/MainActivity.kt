package com.makeuseof.cryptocurrency.view.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.domain.UseCaseProvider
import com.makeuseof.cryptocurrency.view.currencylist.CurrencyListContract
import com.makeuseof.cryptocurrency.view.currencylist.CurrencyListFragment
import com.makeuseof.cryptocurrency.view.currencylist.CurrencyListPresenter
import com.makeuseof.cryptocurrency.view.postlist.PostListContract
import com.makeuseof.cryptocurrency.view.postlist.PostListFragment
import com.makeuseof.cryptocurrency.view.postlist.PostListPresenter
import com.makeuseof.cryptocurrency.view.watchlist.WatchListContract
import com.makeuseof.cryptocurrency.view.watchlist.WatchListFragment
import com.makeuseof.cryptocurrency.view.watchlist.WatchListPresenter
import com.makeuseof.cryptocurrency.view.widgets.PagerAdapter
import com.makeuseof.utils.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mWatchListPresenter: WatchListContract.Presenter? = null
    private var mCurrencyListPresenter: CurrencyListContract.Presenter? = null
    private var mPostListPresenter: PostListContract.Presenter? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_watchlist -> {
                main_view_pager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_currencies -> {
                main_view_pager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_news -> {
                main_view_pager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        init()
    }

    override fun onBackPressed() {
        when(main_view_pager.currentItem){
            0 -> super.onBackPressed()
            else -> {main_view_pager.currentItem = 0}
        }
    }

    //endregion

    //region Init

    private fun init(){
        val fragments = ArrayList<Fragment>()

        fragments.add(createWatchListScreen())
        fragments.add(createCurrencyListScreen())
        fragments.add(createPostListScreen())

        initViewPager(fragments)

        main_add_container.setOnClickListener {
            main_view_pager.currentItem = 1
        }
    }

    //region Screens

    private fun createWatchListScreen(): Fragment {
        val fragment = WatchListFragment.newInstance()

        mWatchListPresenter = WatchListPresenter(
                fragment,
                UseCaseProvider.getCurrencyListUseCases(applicationContext)
        )

        return fragment
    }

    private fun createCurrencyListScreen(): Fragment {
        val fragment = CurrencyListFragment.newInstance()

        mCurrencyListPresenter = CurrencyListPresenter(
                fragment,
                UseCaseProvider.getCurrencyListUseCases(applicationContext)
        )

        return fragment
    }

    private fun createPostListScreen(): Fragment {
        val fragment = PostListFragment.newInstance()

        mPostListPresenter = PostListPresenter(
                fragment,
                UseCaseProvider.getPostUseCases()
        )

        return fragment
    }

    //endregion

    private fun initViewPager(fragments: ArrayList<Fragment>){
        main_nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val adapter = PagerAdapter(supportFragmentManager, fragments)
        main_view_pager.adapter = adapter
        main_view_pager.offscreenPageLimit = 4

        main_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                main_nav_view.selectedItemId = getNavigationItemId(position)

                when(position){
                    0 -> setAddVisibility(true)
                    else -> setAddVisibility(false)
                }
            }
        })

    }

    private fun setAddVisibility(visible: Boolean){
        if (visible){
            main_add_container.visible()
            main_add_container.animate().alpha(1f).setDuration(400L).start()
        } else {
            main_add_container.hide()
            main_add_container.animate().alpha(0f).setDuration(50L).start()
        }
    }

    private fun getNavigationItemId(position: Int): Int = when (position) {
        0 -> R.id.navigation_watchlist
        1 -> R.id.navigation_currencies
        2 -> R.id.navigation_news
        else -> R.id.navigation_watchlist
    }

//    private fun getTab(title: String): TabLayout.Tab {
//        val textView = FontTextView(this)
//        textView.gravity = Gravity.CENTER
//        textView.text = title
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
//
//        val colorStates = ColorStateList(
//                arrayOf(
//                        intArrayOf(android.R.attr.state_selected), intArrayOf()
//                ),
//                intArrayOf(
//                        ResourceUtil.getColor(this, R.color.white),
//                        ResourceUtil.getColor(this, R.color.light_text)
//                )
//        )
//
//        textView.setTextColor(colorStates)
//
//        return main_tab_layout.newTab().setCustomView(textView)
//    }

    //endregion
}
