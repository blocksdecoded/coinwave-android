package com.blocksdecoded.core

import android.os.Bundle
import android.view.View
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.Utils
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper
import androidx.appcompat.app.AppCompatActivity
import com.blocksdecoded.utils.KeyboardUtil

abstract class SwipeableActivity : AppCompatActivity(), SwipeBackActivityBase {
    private var mHelper: SwipeBackActivityHelper? = null

    private var mSwipeListener = object : SwipeBackLayout.SwipeListener {
        override fun onScrollStateChange(state: Int, scrollPercent: Float) {
            if (SwipeBackLayout.SCREEN_STATE_ON == state) {
                KeyboardUtil.hideKeyboard(this@SwipeableActivity)
            }
        }

        override fun onEdgeTouch(edgeFlag: Int) {
        }

        override fun onScrollOverThreshold() {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHelper = SwipeBackActivityHelper(this)
        mHelper?.onActivityCreate()
        mHelper?.swipeBackLayout?.addSwipeListener(mSwipeListener)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper?.onPostCreate()
    }

    override fun <T : View?> findViewById(id: Int): T {
        val v: View? = super.findViewById(id)
        val t = mHelper!!.findViewById(id)
        return if (v == null && mHelper != null) (t as T) else (v as T)
    }

    override fun getSwipeBackLayout(): SwipeBackLayout? {
        return mHelper?.swipeBackLayout
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout?.scrollToFinishActivity()
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout?.setEnableGesture(enable)
    }
}
