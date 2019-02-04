package com.makeuseof.core

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by Tameki on 2/7/18.
 */
abstract class FragmentActivity<V>(
		var mFragment: V
): SwipeableActivity() {
	
	@SuppressLint("CommitTransaction")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		if (savedInstanceState == null) {
			(mFragment as androidx.fragment.app.Fragment).let{
				supportFragmentManager
						.beginTransaction()
						.add(android.R.id.content, it)
						.commit()
			}
		}
	}
}