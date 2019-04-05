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
    fun tryShowRateUs(context: Context): IRateUsDialog? {
        return RateUs
                .checkRateShow(
                        context,
                        context.getString(R.string.const_app_name)
                )
                ?.setTitleTypeface(SFProTextTypeface.getBold(context))
                ?.setDescriptionTypeface(SFProTextTypeface.getBold(context))
                ?.setNegativeTypeface(SFProTextTypeface.getRegular(context))
                ?.setPositiveTypeface(SFProTextTypeface.getBold(context))
    }

    fun getDialog(context: Context): IRateUsDialog {
        return RateUs
                .showRate(
                        context,
                        context.getString(R.string.const_app_name)
                )
                .setTitleTypeface(SFProTextTypeface.getBold(context))
                .setDescriptionTypeface(SFProTextTypeface.getBold(context))
                .setNegativeTypeface(SFProTextTypeface.getRegular(context))
                .setPositiveTypeface(SFProTextTypeface.getBold(context))
    }
}