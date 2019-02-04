package com.makeuseof.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.makeuseof.cryptocurrency.R

/**
 * Created by Tameki on 2/15/18.
 */
private const val TRANSITIONS_ENABLED = true

fun Activity.inRightTransition(){
	if (TRANSITIONS_ENABLED){
		this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
	}
}

fun Activity.outRightTransition(){
	if (TRANSITIONS_ENABLED){
		this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right )
	}
}

fun Activity.inRightWithFinish(): Boolean{
    if (TRANSITIONS_ENABLED){
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        if (this is AppCompatActivity){
            this.supportFinishAfterTransition()
        }
    }

    return TRANSITIONS_ENABLED
}