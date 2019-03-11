package com.blocksdecoded.coinwave.presentation.widgets

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum.*
import com.blocksdecoded.utils.extensions.inflate

class CoinsHeaderView : FrameLayout, View.OnClickListener {

    private var mListener: HeaderClickListener? = null

    constructor(context: Context) : super(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun init() {
        val view = inflate(R.layout.partial_coins_header)!!
        addView(view)

        view.findViewById<View>(R.id.coins_header_market_cap).setOnClickListener(this)
        view.findViewById<View>(R.id.coins_header_name).setOnClickListener(this)
        view.findViewById<View>(R.id.coins_header_price).setOnClickListener(this)
        view.findViewById<View>(R.id.coins_header_volume_24h).setOnClickListener(this)
    }

    fun setListener(listener: HeaderClickListener) {
        mListener = listener
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.coins_header_market_cap -> mListener?.onItemClick(CAP)
            R.id.coins_header_name -> mListener?.onItemClick(NAME)
            R.id.coins_header_price -> mListener?.onItemClick(PRICE)
            R.id.coins_header_volume_24h -> mListener?.onItemClick(VOLUME)
        }
    }

    interface HeaderClickListener {
        fun onItemClick(headerType: ViewSortEnum)
    }
}