package com.makeuseof.cryptocurrency.view.widgets

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class RecyclerItemSpaceDecoration(private val mTopSpace: Int = 0,
                                  private val mBottomSpace: Int = 0): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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