package com.blocksdecoded.coinwave.util

import android.content.Context
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.rateus.widget.IRateUsDialog
import com.blocksdecoded.rateus.RateUs

/**
 * Created by askar on 11/23/18
 * with Android Studio
 */
object CryptoRateUtil {
    fun tryShowRateUs(context: Context): IRateUsDialog? = RateUs
            .checkRateShow(context, context.getString(R.string.const_app_name))
            ?.setTitleFont(R.font.sf_pro_text_bold)
            ?.setDescriptionFont(R.font.sf_pro_text_bold)
            ?.setNegativeFont(R.font.sf_pro_text_regular)
            ?.setPositiveFont(R.font.sf_pro_text_bold)

    fun getDialog(context: Context): IRateUsDialog = RateUs
            .showRate(context, context.getString(R.string.const_app_name))
            .setTitleFont(R.font.sf_pro_text_bold)
            .setDescriptionFont(R.font.sf_pro_text_bold)
            .setNegativeFont(R.font.sf_pro_text_regular)
            .setPositiveFont(R.font.sf_pro_text_bold)
}