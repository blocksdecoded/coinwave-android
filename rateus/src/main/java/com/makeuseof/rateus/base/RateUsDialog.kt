package com.makeuseof.rateus.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.makeuseof.rateus.R
import com.makeuseof.rateus.base.RateUsDialog.State.*
import com.makeuseof.rateus.util.IntentUtil
import com.makeuseof.rateus.util.ResourceUtil
import com.makeuseof.rateus.util.SharedPrefsUtil
import android.view.WindowManager
import com.makeuseof.rateus.SFUITypeface
import com.makeuseof.rateus.util.AnimationUtil

/**
 * Created by Tameki on 3/19/18.
 */
internal class RateUsDialog(
		context: Context,
		private var mAppName: String = context.applicationInfo.name,
		private var mAppID: String = context.packageName,
		private var mListener: RateUsListener? = null): Dialog(context), View.OnClickListener, RateUsDialogContract {
	
	enum class State{
		DEFAULT,
		FEEDBACK,
		RATE,
		THANKS
	}
	
	private var mCurrentState = DEFAULT
	
	private var mRatingBar: RatingBar? = null
	private var mNegativeBtn: TextView? = null
	private var mPositiveBtn: TextView? = null
	
	private var mTitle: TextView? = null
	private var mSecondTitle: TextView? = null

	private var mOwner: AppCompatActivity? = null
	
	init {
		if (context is AppCompatActivity){
			mOwner = context
		}
		
		window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		requestWindowFeature(Window.FEATURE_NO_TITLE)

		setContentView(R.layout.rate_dialog)
		
		initView()
		
		loadPreferences()
		
		setState(DEFAULT)

        window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT

        setTitleTypeface(SFUITypeface.getSemibold(context))
        setButtonsTypeface(SFUITypeface.getBold(context))
	}

	override fun dismiss() {
		if (mCurrentState != THANKS){ mListener?.onDismiss() }
		mOwner = null
		super.dismiss()
	}
	
	private fun showMessage(text: String) = try {
			Toast.makeText(context, text, Toast.LENGTH_LONG).show()
		} catch (e: Exception){
			Log.d("ololo", "Exception " + e.message)
		}
	
	private fun loadPreferences(){
		mRatingBar?.let {
			it.rating = SharedPrefsUtil.getFloatPreference(context, RateUtil.LAST_SUBMITTED_RATING)
		}
	}
	
	private fun initView(){
		mTitle = findViewById(R.id.dialog_title)
		mSecondTitle = findViewById(R.id.dialog_second_title)
		mNegativeBtn = findViewById(R.id.dialog_negative)
		mPositiveBtn = findViewById(R.id.dialog_positive)

		mNegativeBtn?.setOnClickListener(this)
		mPositiveBtn?.setOnClickListener(this)
		
		mRatingBar = findViewById(R.id.dialog_rating)
		
		setStartsColors()
	}
	
	private fun setStartsColors(){
		try {
			val stars = mRatingBar?.progressDrawable as LayerDrawable
			setRatingStarColor(stars.getDrawable(0), R.color.rating_inactive)
		} catch (e: Exception) {
		
		}
	}
	
	private fun setRatingStarColor(drawable: Drawable, color: Int) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			DrawableCompat.setTint(drawable, ResourceUtil.getColor(context, color))
		} else {
			drawable.setColorFilter(ResourceUtil.getColor(context, color), PorterDuff.Mode.SRC_IN)
		}
	}
	
	override fun onClick(v: View?) {
		when(v){
			mNegativeBtn -> onNegativeClick()
			
			mPositiveBtn -> onPositiveClick()
		}
	}
	
	private fun onNegativeClick(){
		dismiss()
	}
	
	private fun onPositiveClick(){
		when(mCurrentState){
			DEFAULT -> {
				var rating = 0f
				mRatingBar?.let { rating = it.rating }
				SharedPrefsUtil.setPreference(context, RateUtil.LAST_SUBMITTED_RATING, rating)
				
				if (rating == 5f){
					setState(RATE)
				} else {
					setState(FEEDBACK)
				}
			}
			
			FEEDBACK -> {
				mOwner?.let {
					IntentUtil.contactUs(it, mAppName)
					mCurrentState = THANKS
				}
				
				dismiss()
			}
			
			RATE -> {
				mOwner?.let {
					IntentUtil.openAppPAge(it, mAppID)
					mCurrentState = THANKS
				}
				
				dismiss()
			}
		}
	}
	
	//region States
	
	private fun setState(state: State){
		resetState()
		mCurrentState = state
		
		when(state){
			DEFAULT -> setDefaultState()
			FEEDBACK -> setFeedbackState()
			RATE -> setRateState()
		}
	}
	
	private fun resetState(){
		mPositiveBtn?.visibility = View.VISIBLE
		mNegativeBtn?.visibility = View.VISIBLE
		mTitle?.visibility = View.VISIBLE
		mSecondTitle?.visibility = View.GONE
		mRatingBar?.visibility = View.VISIBLE
	}

    private fun playCrossfadeAnimation(){
        AnimationUtil.crossFade(
                arrayListOf(mRatingBar, mTitle),
                arrayListOf(mSecondTitle),
                0.8f
        )
    }
	
	private fun setDefaultState(){
		mTitle?.text = "How would you rate\n$mAppName?"
		
		mNegativeBtn?.text = "Later"
		mPositiveBtn?.text = "Submit"
	}
	
	private fun setFeedbackState(){
        mNegativeBtn?.text = "NO"
        mPositiveBtn?.text = "YES"
        mSecondTitle?.text = "Would you like to\nshare your\nfeedback with us?"

        playCrossfadeAnimation()

//		mSecondTitle?.visibility = View.VISIBLE
//		mRatingBar?.visibility = View.INVISIBLE
//		mTitle?.visibility = View.INVISIBLE
	}
	
	private fun setRateState(){
        mNegativeBtn?.text = "Later"
        mPositiveBtn?.text = "Rate"
        mSecondTitle?.text = "Would you like to rate\nus on the Play Store?"

        playCrossfadeAnimation()

//		mSecondTitle?.visibility = View.VISIBLE
//		mRatingBar?.visibility = View.INVISIBLE
//		mTitle?.visibility = View.INVISIBLE
	}
	
	//endregion
	
	//region Contract

    override fun setPositiveTypeface(typeface: Typeface): RateUsDialogContract {
        mPositiveBtn?.typeface = typeface

        return this
    }

    override fun setNegativeTypeface(typeface: Typeface): RateUsDialogContract {
        mNegativeBtn?.typeface = typeface

        return this
    }

    override fun setTitleTypeface(typeface: Typeface): RateUsDialogContract {
		mTitle?.typeface = typeface
		mSecondTitle?.typeface = typeface
		
		return this
	}
	
	override fun setDescriptionTypeface(typeface: Typeface): RateUsDialogContract {
		return this
	}
	
	override fun setButtonsTypeface(typeface: Typeface): RateUsDialogContract {
		mNegativeBtn?.typeface = typeface
		mPositiveBtn?.typeface = typeface
		
		return this
	}
	
	override fun showRate(): RateUsDialogContract {
		this.show()
		
		return this
	}
	
	override fun dismissRate(callListener: Boolean) {
		if (callListener){ mListener?.onDismiss() }
		
		mOwner = null
		this.dismiss()
	}
	
	override fun setListener(listener: RateUsListener): RateUsDialogContract {
		mListener = listener
		
		return this
	}
	
	override fun showFeedbackThanks(): Boolean {
		try {
			if (mCurrentState == THANKS){
				showMessage(context.getString(R.string.thanks_for_feedback))
				return true
			}
		} catch (e: Exception) {
			return true
		}
		
		return false
	}
	
	//endregion
}