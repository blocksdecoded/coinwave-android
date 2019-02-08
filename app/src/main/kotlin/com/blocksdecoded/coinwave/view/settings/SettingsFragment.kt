package com.blocksdecoded.coinwave.view.settings

import android.view.View
import com.blocksdecoded.core.mvp.BaseMVPFragment
import com.blocksdecoded.coinwave.R

class SettingsFragment : BaseMVPFragment<SettingsContract.Presenter>(), SettingsContract.View {
    override var mPresenter: SettingsContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_settings

    override fun initView(rootView: View) {

    }
}