package com.makeuseof.cryptocurrency.view.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.makeuseof.cryptocurrency.R
import com.makeuseof.utils.ResourceUtil
import com.makeuseof.utils.inflate

/**
 * Created by Tameki on 2/9/18.
 */
class OptionSelectorView: LinearLayout, View.OnClickListener {
	private var mOptions = arrayListOf("first", "second")
	
	private var mSelectedBackground = android.R.attr.selectableItemBackground
	private var mDefaultBackground = android.R.attr.selectableItemBackground
	
	private var mSelectedTextColor = R.color.white
	private var mDefaultTextColor = R.color.light_grey
	
	private var mListener: ((number: Int) -> Unit)? = null
	private var mNumberViews = HashMap<Int, View>()
	
	constructor(context: Context): super(context)
	
	constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
		val a = context.theme.obtainStyledAttributes(
				attributeSet,
				R.styleable.OptionSelectorView,
				0, 0
		)
		
		try {
			mSelectedTextColor = a.getInt(R.styleable.OptionSelectorView_selectedTextColor, R.color.green)
			mDefaultTextColor = a.getInt(R.styleable.OptionSelectorView_defaultTextColor, R.color.light_text)
			
			mSelectedBackground = a.getInt(R.styleable.OptionSelectorView_selectedNumBackground, android.R.attr.selectableItemBackground)
			mDefaultBackground = a.getInt(R.styleable.OptionSelectorView_defaultNumBackground, android.R.attr.selectableItemBackground)

            mOptions.clear()
            mOptions.addAll(a.getString(R.styleable.OptionSelectorView_osv_options).split(","))

			inflateViews(mOptions)
		} finally {
			a.recycle()
		}
	}

	private fun inflateViews(options: ArrayList<String>){
        removeAllViewsInLayout()

        options.forEachIndexed { index, it ->
            val view = this.inflate(R.layout.selector_text) as TextView
            view.text = it
            mNumberViews[index] = view
            this.addView(view)
            view.setOnClickListener(this)
        }
		
		setSelectedView(0)
	}

    fun setOptions(options: ArrayList<String>){
        mOptions.clear()
        mOptions.addAll(options)
        inflateViews(mOptions)
    }
	
	private fun selectView(view: View){
		deselectAll()
		
		if (view is TextView){
			view.setTextColor(ResourceUtil.getColor(context, mSelectedTextColor))
		}
	}
	
	private fun setDefaultView(view: View){
		if (view is TextView){
			view.setTextColor(ResourceUtil.getColor(context, mDefaultTextColor))
		}
	}
	
	private fun deselectAll(){
		mNumberViews.values.forEach { setDefaultView(it) }
	}
	
	private fun onViewTouch(view: View?){
		view?.let{
			if (it is TextView){
				it.text?.let { text ->
                    val index = mOptions.indexOfFirst { it == text }
					setSelectedView(index)
					mListener?.invoke(index)
				}
			}
		}
	}
	
	fun setSelectedView(value: Int){
		mNumberViews[value]?.let {
			selectView(it)
		}
	}
	
	fun addClickListener(listener: (position: Int) -> Unit){
		mListener = listener
	}


    override fun onClick(v: View?) {
        v?.let{
            if (it is TextView){
                it.text?.let { text ->
                    val index = mOptions.indexOfFirst { it == text }
                    setSelectedView(index)
                    mListener?.invoke(index)
                }
            }
        }
    }
}