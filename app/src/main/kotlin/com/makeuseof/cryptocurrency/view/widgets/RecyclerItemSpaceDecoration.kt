package com.makeuseof.cryptocurrency.view.widgets

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class RecyclerItemSpaceDecoration(private val mTopSpace: Int = 0,
                                  private val mBottomSpace: Int = 0): androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            outRect.bottom = mBottomSpace
            outRect.top = 0
        }
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mTopSpace
            outRect.bottom = 0
        }
    }
}