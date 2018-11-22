package com.makeuseof.cryptocurrency.view.settings

import android.view.View
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R

class SettingsFragment : BaseMVPFragment<SettingsContract.Presenter>(), SettingsContract.View {
    override var mPresenter: SettingsContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_settings

    override fun initView(rootView: View) {

    }
}