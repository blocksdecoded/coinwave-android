package com.blocksdecoded.coinwave.presentation.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.ColorInt
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.blocksdecoded.coinwave.BuildConfig
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.domain.UseCaseProvider
import com.blocksdecoded.coinwave.util.CryptoRateUtil
import com.blocksdecoded.coinwave.util.SFProTextTypeface
import com.blocksdecoded.coinwave.presentation.addtowatchlist.AddToWatchlistActivity
import com.blocksdecoded.coinwave.presentation.coinslist.CoinsListFragment
import com.blocksdecoded.coinwave.presentation.pickfavorite.PickFavoriteActivity
import com.blocksdecoded.coinwave.presentation.posts.PostsFragment
import com.blocksdecoded.coinwave.presentation.watchlist.WatchListFragment
import com.blocksdecoded.coinwave.presentation.watchlist.WatchListPresenter
import com.blocksdecoded.coinwave.presentation.widgets.PagerAdapter
import com.blocksdecoded.rateus.base.RateUsDialogContract
import com.blocksdecoded.rateus.base.RateUsListener
import com.blocksdecoded.utils.*
import com.blocksdecoded.utils.customtabs.openUrl
import com.blocksdecoded.utils.extensions.getColorRes
import com.blocksdecoded.utils.extensions.removeShiftMode
import com.blocksdecoded.utils.extensions.setConstraintTopMargin
import com.blocksdecoded.utils.extensions.statusBarHeight
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_content.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity :
        AppCompatActivity(),
        View.OnClickListener,
        MenuClickListener {

    private var mRateDialog: RateUsDialogContract? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_currencies -> {
                main_view_pager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_watchlist -> {
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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            when (main_view_pager.currentItem) {
                0 -> super.onBackPressed()
                else -> { main_view_pager.currentItem = 0 }
            }
        }
    }

    //region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        init()

        initStatusBar()

        mRateDialog = CryptoRateUtil.tryShowRateUs(this)
                ?.setListener(object : RateUsListener {
                    override fun onDismiss() { mRateDialog = null }
                })

        updateStatusBar()

        setVersionInfo()
    }

    override fun onPause() {
        super.onPause()
        mRateDialog?.dismissRate(true)
    }

    //endregion

    //region Status bar

    private fun initStatusBar() {
        if (Build.VERSION.SDK_INT >= 19) { // 19, 4.4, KITKAT
            val contentParent = findViewById<View>(android.R.id.content) as ViewGroup
            val content = contentParent.getChildAt(0)
            setFitsSystemWindows(content, false, true)
        }
    }

    private fun setStatusBarImmersiveMode(@ColorInt color: Int) {
        val win = window

        // StatusBar
        if (Build.VERSION.SDK_INT >= 19) { // 19, 4.4, KITKAT
            win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= 21) { // 21, 5.0, LOLLIPOP
            win.attributes.systemUiVisibility = win.attributes.systemUiVisibility or (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            win.statusBarColor = color
        }
    }

    private fun setFitsSystemWindows(view: View?, fitSystemWindows: Boolean, applyToChildren: Boolean) {
        if (view == null) return
        view.fitsSystemWindows = fitSystemWindows
        if (applyToChildren && view is ViewGroup) {
            val viewGroup = view as ViewGroup?
            var i = 0
            viewGroup?.let {
                val n = viewGroup.childCount
                while (i < n) {
                    viewGroup.getChildAt(i).fitsSystemWindows = fitSystemWindows
                    i ++
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setStatusBarImmersiveMode(getColorRes(R.color.status_bar_bg))
    }

    private fun updateStatusBar(drawerOpen: Boolean = false) = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val flags = if (drawerOpen) {
                0
            } else {
                when (main_view_pager.currentItem) {
                    0, 2 -> 0
                    else -> View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }

            window.decorView.systemUiVisibility = flags
        }
        Unit
    } catch (e: Exception) {
        logE(e)
    }

    //endregion

    //region Init

    private fun init() {
        initNavigationDrawer()

        initViewPager(arrayListOf(
            CoinsListFragment.newInstance(),
            WatchListFragment.newInstance(),
            PostsFragment.newInstance())
        )

        main_menu.setOnClickListener {
            openDrawer()
        }
    }

    private fun setVersionInfo() {
        drawer_app_version.text = if (BuildConfig.DEBUG) {
            "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\ndebug"
        } else {
            "v${BuildConfig.VERSION_NAME}"
        }
    }

    private fun initNavigationDrawer() {
        drawer_add_watchlist.setOnClickListener(this)
        drawer_contact_us.setOnClickListener(this)
        drawer_favorite.setOnClickListener(this)
        drawer_rate_us.setOnClickListener(this)
        drawer_share_this_app.setOnClickListener(this)
        drawer_close_menu.setOnClickListener(this)
        drawer_company_selectable.setOnClickListener(this)

        drawer_top_container.setConstraintTopMargin(statusBarHeight)

        drawer_layout.addDrawerListener(object : androidx.drawerlayout.widget.DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(p0: Int) {
            }

            override fun onDrawerSlide(p0: View, p1: Float) {
            }

            override fun onDrawerClosed(p0: View) {
                updateStatusBar()
            }

            override fun onDrawerOpened(p0: View) {
                updateStatusBar(true)
            }
        })
    }

    private fun initViewPager(fragments: ArrayList<Fragment>) {
        main_nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        main_nav_view.enableAnimation(false)
        main_nav_view.isItemHorizontalTranslationEnabled = false
        main_nav_view.removeShiftMode()
        main_nav_view.setTypeface(SFProTextTypeface.getRegular(this))

        val adapter = PagerAdapter(supportFragmentManager, fragments)
        main_view_pager.adapter = adapter
        main_view_pager.offscreenPageLimit = 3

        main_view_pager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                main_nav_view.selectedItemId = getNavigationItemId(position)

                updateStatusBar()
            }
        })
    }

    private fun getNavigationItemId(position: Int): Int = when (position) {
        0 -> R.id.navigation_currencies
        1 -> R.id.navigation_watchlist
        2 -> R.id.navigation_news
        else -> R.id.navigation_watchlist
    }

    //endregion

    //region Click

    override fun onMenuClick() {
        openDrawer()
    }

    override fun onClick(v: View?) {
        when (v) {
            drawer_add_watchlist -> drawerItemClick { AddToWatchlistActivity.start(this) }
            drawer_favorite -> drawerItemClick { PickFavoriteActivity.start(this) }
            drawer_contact_us -> drawerItemClick { ShareUtils.contactUs(this) }
            drawer_rate_us -> drawerItemClick(::onRateClick)
            drawer_share_this_app -> drawerItemClick { ShareUtils.shareApp(this) }
            drawer_close_menu -> closeDrawer()
            drawer_company_selectable -> openUrl(BuildConfig.COMPANY_URL)
        }
    }

    private inline fun drawerItemClick(body: () -> Unit) {
        body()
        closeDrawer()
    }

    private fun onRateClick() {
        mRateDialog = CryptoRateUtil.getDialog(this)
            .setListener(object : RateUsListener {
                override fun onDismiss() { mRateDialog = null }
            })
    }

    //endregion

    //region Navigation Drawer

    private fun openDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer(delay: Long = 0L) {
        Handler().postDelayed({
            drawer_layout.closeDrawer(GravityCompat.START)
        }, delay)
    }

    //endregion
}
