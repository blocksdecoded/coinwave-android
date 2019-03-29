package com.blocksdecoded.coinwave.presentation.widgets

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.blocksdecoded.coinwave.R

import com.blocksdecoded.coinwave.presentation.post.widgets.LoadingView

class FooterViewHolder(itemView: View, private val mClickListener: ClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var progressView: LoadingView
    var errorView: View

    init {
        progressView = itemView.findViewById(R.id.footer_progress)
        errorView = itemView.findViewById(R.id.footer_error_container)
        val footerText = itemView.findViewById<TextView>(R.id.footer_text)
        footerText.text = "No connection!"

        itemView.setOnClickListener(this)
    }

    interface ClickListener {
        fun onFooterClick(v: View, position: Int)
    }

    override fun onClick(v: View) {
        mClickListener?.onFooterClick(v, adapterPosition)
    }
}
