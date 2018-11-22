package com.makeuseof.cryptocurrency.view.main

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.domain.UseCaseProvider
import com.makeuseof.cryptocurrency.view.currencylist.CurrencyListContract
import com.makeuseof.cryptocurrency.view.currencylist.CurrencyListFragment
import com.makeuseof.cryptocurrency.view.currencylist.CurrencyListPresenter
import com.makeuseof.cryptocurrency.view.postlist.PostListContract
import com.makeuseof.cryptocurrency.view.postlist.PostListFragment
import com.makeuseof.cryptocurrency.view.postlist.PostListPresenter
import com.makeuseof.cryptocurrency.view.settings.SettingsContract
import com.makeuseof.cryptocurrency.view.settings.SettingsFragment
import com.makeuseof.cryptocurrency.view.settings.SettingsPresenter
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
    private var mSettingsPresenter: SettingsContract.Presenter? = null

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
            R.id.navigation_settings -> {
                main_view_pager.currentItem = 3
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

        initStatusBar()
    }

    override fun onBackPressed() {
        when(main_view_pager.currentItem){
            0 -> super.onBackPressed()
            else -> {main_view_pager.currentItem = 0}
        }
    }

    //endregion

    //region Status bar

    private fun initStatusBar() {
        // Ensure `setStatusBarImmersiveMode()`
        if (Build.VERSION.SDK_INT >= 19) { // 19, 4.4, KITKAT
            // Ensure content view `fitsSystemWindows` is false.
            val contentParent = findViewById<View>(android.R.id.content) as ViewGroup
            val content = contentParent.getChildAt(0)
            // If using `DrawerLayout`, must ensure its subviews `fitsSystemWindows` are all false.
            // Because in some roms, such as MIUI, it will fits system windows for each subview.
            setFitsSystemWindows(content, false, true)

            // Add a view to hold the status bar place.
            // Note: if using appbar_scrolling_view_behavior of CoordinatorLayout, however,
            // the holder view could be scrolled to outside as it above the app bar.
            //holdStatusBar(toolbar, R.color.colorPrimary);
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

        // Setup immersive mode on third-party rom
//		if (Build.VERSION.SDK_INT >= 19) { // 19, 4.4, KITKAT
//			//FlymeUtils.setStatusBarDarkIcon(win, false);
//			MIUIUtils.setStatusBar(win, MIUIUtils.StatusBarMode.TRANSPARENT)
//		}
    }

    private fun setFitsSystemWindows(view: View?, fitSystemWindows: Boolean, applyToChildren: Boolean) {
        if (view == null) return
        view.fitsSystemWindows = fitSystemWindows
        if (applyToChildren && view is ViewGroup) {
            val viewGroup = view as ViewGroup?
            var i = 0
            val n = viewGroup !!.childCount
            while (i < n) {
                viewGroup.getChildAt(i).fitsSystemWindows = fitSystemWindows
                i ++
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setStatusBarImmersiveMode(ResourceUtil.getColor(this, R.color.status_bar_bg))
    }

    //endregion


    //region Init

    private fun init(){
        val fragments = ArrayList<Fragment>()

        fragments.add(createWatchListScreen())
        fragments.add(createCurrencyListScreen())
        fragments.add(createPostListScreen())
        fragments.add(createSettingsScreen())

        initViewPager(fragments)

        main_add_container.setOnClickListener {
            main_view_pager.currentItem = 1
        }
    }

    //region Screens

    private fun createWatchListScreen(): Fragment {
        return WatchListFragment.newInstance().apply {
            mWatchListPresenter = WatchListPresenter(
                    this,
                    UseCaseProvider.getCurrencyListUseCases(applicationContext)
            )
        }
    }

    private fun createCurrencyListScreen(): Fragment {
        return CurrencyListFragment.newInstance().apply {
            mCurrencyListPresenter = CurrencyListPresenter(
                    this,
                    UseCaseProvider.getCurrencyListUseCases(applicationContext)
            )
        }
    }

    private fun createPostListScreen(): Fragment {
        return PostListFragment.newInstance().apply {
            mPostListPresenter = PostListPresenter(
                    this,
                    UseCaseProvider.getPostUseCases()
            )
        }
    }

    private fun createSettingsScreen(): Fragment {
        return SettingsFragment().apply {
            mSettingsPresenter = SettingsPresenter(
                    this
            )
        }
    }

    //endregion

    private fun initViewPager(fragments: ArrayList<Fragment>){
        main_nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        main_nav_view.enableAnimation(false)
        main_nav_view.isItemHorizontalTranslationEnabled = false
        main_nav_view.removeShiftMode()

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
        3 -> R.id.navigation_settings
        else -> R.id.navigation_watchlist
    }

    //endregion
}
