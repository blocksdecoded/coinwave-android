package com.blocksdecoded.coinwave.view.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.blocksdecoded.coinwave.R

// Created by askar on 7/3/18.
class ActionConfirmDialog(
        context: Context
): Dialog(context), View.OnClickListener {
    private var mConfirmButton: TextView? = null
    private var mCancelButton: TextView? = null
    private var mTitle: TextView? = null

    private var mTitleText = ""
    private var mConfirmText = ""
    private var mCancelText = ""

    private var mConfirmListener: ((dialog: Dialog) -> Unit)? = null
    private var mCancelListener: ((dialog: Dialog) -> Unit)? = null

    init {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    //region Builder

    fun showDialog(): ActionConfirmDialog{
        show()
        return this
    }

    fun setTitle(text: String): ActionConfirmDialog{
        mTitleText = text
        return this
    }

    fun setConfirmText(text: String): ActionConfirmDialog{
        mConfirmText = text
        return this
    }

    fun setCancelText(text: String): ActionConfirmDialog{
        mCancelText = text
        return this
    }

    fun setConfirmListener(body: (dialog: Dialog) -> Unit): ActionConfirmDialog {
        mConfirmListener = body
        return this
    }

    fun setCancelListener(body: (dialog: Dialog) -> Unit): ActionConfirmDialog {
        mCancelListener = body
        return this
    }

    fun setDismissListener(body: (dialog: Dialog) -> Unit): ActionConfirmDialog {
        setOnDismissListener { body.invoke(this) }
        return this
    }

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.dialog_delete)

        initView()

        window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
    }

    private fun initView(){
        mTitle = findViewById(R.id.dialog_delete_title)
        mConfirmButton = findViewById(R.id.dialog_delete_confirm)
        mCancelButton = findViewById(R.id.dialog_delete_cancel)

        mConfirmButton?.setOnClickListener(this)
        mCancelButton?.setOnClickListener(this)
    }

    override fun show() {
        super.show()
        if (mTitleText.isNotEmpty()) mTitle?.text = mTitleText
        if (mConfirmText.isNotEmpty()) mConfirmButton?.text = mConfirmText
        if (mCancelText.isNotEmpty()) mCancelButton?.text = mCancelText
    }

    override fun dismiss() {
        mCancelListener = null
        mConfirmListener = null
        super.dismiss()
    }

    override fun onClick(v: View?) {
        when(v){
            mConfirmButton -> mConfirmListener?.invoke(this)

            mCancelButton -> mCancelListener?.invoke(this)
        }
    }
}