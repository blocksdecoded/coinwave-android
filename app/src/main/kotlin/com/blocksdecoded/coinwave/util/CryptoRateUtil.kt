package com.blocksdecoded.coinwave.util

import android.content.Context
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.rateus.base.RateUsDialogContract
import com.blocksdecoded.rateus.base.RateUtil

/**
 * Created by askar on 11/23/18
 * with Android Studio
 */
object CryptoRateUtil {
    fun tryShowRateUs(context: Context): RateUsDialogContract? {
        return RateUtil
                .checkRateShow(
                        context,
                        context.getString(R.string.const_app_name)
                )
                ?.setTitleTypeface(SFProTextTypeface.getBold(context))
                ?.setDescriptionTypeface(SFProTextTypeface.getBold(context))
                ?.setNegativeTypeface(SFProTextTypeface.getRegular(context))
                ?.setPositiveTypeface(SFProTextTypeface.getBold(context))
    }

    fun getDialog(context: Context): RateUsDialogContract {
        return RateUtil
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