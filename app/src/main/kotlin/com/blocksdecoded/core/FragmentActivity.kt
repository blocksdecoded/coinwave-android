package com.blocksdecoded.core

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by Tameki on 2/7/18.
 */
abstract class FragmentActivity<V : Fragment>(
    var fragment: V
) : SwipeableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commit()
        }
    }
}