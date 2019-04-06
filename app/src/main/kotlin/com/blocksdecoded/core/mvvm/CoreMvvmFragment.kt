package com.blocksdecoded.core.mvvm

import androidx.lifecycle.ViewModel
import com.blocksdecoded.core.CoreFragment

abstract class CoreMvvmFragment<T : ViewModel> : CoreFragment() {
    abstract val mViewModel: T
}