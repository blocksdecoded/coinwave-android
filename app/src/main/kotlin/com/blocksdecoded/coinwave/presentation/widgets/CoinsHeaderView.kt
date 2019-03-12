package com.blocksdecoded.coinwave.presentation.widgets

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache.CoinSortEnum.*
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum.*
import com.blocksdecoded.utils.extensions.getColorRes
import com.blocksdecoded.utils.extensions.inflate

class CoinsHeaderView : FrameLayout {

    var currentState: CoinsCache.CoinSortEnum = DEFAULT
        set(value) {
            field = value
            updateViews()
        }

    private val activeColor by lazy { getColorRes(R.color.header_active) }
    private val inactiveColor by lazy { getColorRes(R.color.header_inactive) }

    @BindView(R.id.coins_header_market_cap_title)
    lateinit var marketCapTitle: TextView
    @BindView(R.id.coins_header_volume_title)
    lateinit var volumeTitle: TextView
    @BindView(R.id.coins_header_name_title)
    lateinit var nameTitle: TextView
    @BindView(R.id.coins_header_price_title)
    lateinit var priceTitle: TextView

    @BindView(R.id.coins_header_cap_sort_icon)
    lateinit var capSortIcon: ImageView
    @BindView(R.id.coins_header_volume_sort_icon)
    lateinit var volumeSortIcon: ImageView
    @BindView(R.id.coins_header_name_sort_icon)
    lateinit var nameSortIcon: ImageView
    @BindView(R.id.coins_header_price_sort_icon)
    lateinit var priceSortIcon: ImageView

    @OnClick(
            R.id.coins_header_market_cap,
            R.id.coins_header_name,
            R.id.coins_header_price,
            R.id.coins_header_volume)
    fun onClick(view: View) {
        when (view.id) {
            R.id.coins_header_market_cap -> mListener?.onItemClick(CAP)
            R.id.coins_header_name -> mListener?.onItemClick(NAME)
            R.id.coins_header_price -> mListener?.onItemClick(PRICE)
            R.id.coins_header_volume -> mListener?.onItemClick(VOLUME)
        }
    }

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

        ButterKnife.bind(this)
    }

    fun setListener(listener: HeaderClickListener) {
        mListener = listener
    }

    private fun updateViews() {
        resetAllViews()

        setActiveColor()
    }

    private fun resetAllViews() {
        arrayListOf(nameTitle, priceTitle, volumeTitle, marketCapTitle).forEach { it.setTextColor(inactiveColor) }
        arrayListOf(nameSortIcon, priceSortIcon, volumeSortIcon, capSortIcon).forEach { it.setColorFilter(inactiveColor) }
    }

    private fun setActiveColor() {
        when (currentState) {
            DEFAULT -> { }
            NAME_ASC -> {
                nameTitle.setTextColor(activeColor)
                nameSortIcon.setColorFilter(activeColor)
            }
            NAME_DES -> {
                nameTitle.setTextColor(activeColor)
                nameSortIcon.setColorFilter(activeColor)
            }
            CAP_ASC -> {
                marketCapTitle.setTextColor(activeColor)
                capSortIcon.setColorFilter(activeColor)
            }
            CAP_DES -> {
                marketCapTitle.setTextColor(activeColor)
                capSortIcon.setColorFilter(activeColor)
            }
            VOL_ASC -> {
                volumeTitle.setTextColor(activeColor)
                volumeSortIcon.setColorFilter(activeColor)
                volumeSortIcon.rotation = 0f
            }
            VOL_DES -> {
                volumeTitle.setTextColor(activeColor)
                volumeSortIcon.setColorFilter(activeColor)
                volumeSortIcon.rotation = 180f
            }
            PRICE_ASC -> {
                priceTitle.setTextColor(activeColor)
                priceSortIcon.setColorFilter(activeColor)
                priceSortIcon.rotation = 0f
            }
            PRICE_DES -> {
                priceTitle.setTextColor(activeColor)
                priceSortIcon.setColorFilter(activeColor)
                priceSortIcon.rotation = 180f
            }
        }
    }

    interface HeaderClickListener {
        fun onItemClick(headerType: ViewSortEnum)
    }
}