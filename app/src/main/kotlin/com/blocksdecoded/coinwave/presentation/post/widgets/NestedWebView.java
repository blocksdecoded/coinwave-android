/*
 * Copyright (C) 2015 takahirom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blocksdecoded.coinwave.presentation.post.widgets;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;

public class NestedWebView extends WebView implements NestedScrollingChild, Scrollable {
    private int mLastY;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedOffsetY;
    private NestedScrollingChildHelper mChildHelper;
    private OnReachEndCallback onReachEndCallback;
    // Fields that should be saved onSaveInstanceState
    private int mPrevScrollY;
    private int mScrollY;

    // Fields that don't need to be saved onSaveInstanceState
    private ObservableScrollViewCallbacks mCallbacks;
    private ScrollState mScrollState;
    private boolean mFirstScroll;
    private boolean mDragging;
    private boolean mIntercepted;
    private MotionEvent mPrevMoveEvent;
    private ViewGroup mTouchInterceptionViewGroup;
    private boolean reachedEnd = false;


    public NestedWebView(Context context) {
        this(context, null);
    }

    public NestedWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public NestedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

//    @Override
//    public void onRestoreInstanceState(Parcelable state) {
//        SavedState ss = (SavedState) state;
//        mPrevScrollY = ss.prevScrollY;
//        mScrollY = ss.scrollY;
//        super.onRestoreInstanceState(ss.getSuperState());
//    }
//
//    @Override
//    public Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState ss = new SavedState(superState);
//        ss.prevScrollY = mPrevScrollY;
//        ss.scrollY = mScrollY;
//        return ss;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean returnValue = false;

        MotionEvent event = MotionEvent.obtain(ev);
        final int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedOffsetY = 0;
        }
        int eventY = (int) event.getY();
        event.offsetLocation(0, mNestedOffsetY);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int deltaY = mLastY - eventY;
                // NestedPreScroll
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    mLastY = eventY - mScrollOffset[1];
                    event.offsetLocation(0, -mScrollOffset[1]);
                    mNestedOffsetY += mScrollOffset[1];
                }
                returnValue = super.onTouchEvent(event);

                // NestedScroll
                if (dispatchNestedScroll(0, mScrollOffset[1], 0, deltaY, mScrollOffset)) {
                    event.offsetLocation(0, mScrollOffset[1]);
                    mNestedOffsetY += mScrollOffset[1];
                    mLastY -= mScrollOffset[1];
                }

                if(mCallbacks != null){
                    if (mPrevMoveEvent == null) {
                        mPrevMoveEvent = ev;
                    }
                    float diffY = ev.getY() - mPrevMoveEvent.getY();
                    mPrevMoveEvent = MotionEvent.obtainNoHistory(ev);
                    if (getCurrentScrollY() - diffY <= 0) {
                        // Can't scroll anymore.

                        if (mIntercepted) {
                            // Already dispatched ACTION_DOWN event to parents, so stop here.
                            return false;
                        }

                        // Apps can set the interception target other than the direct parent.
                        final ViewGroup parent;
                        if (mTouchInterceptionViewGroup == null) {
                            parent = (ViewGroup) getParent();
                        } else {
                            parent = mTouchInterceptionViewGroup;
                        }

                        // Get offset to parents. If the parent is not the direct parent,
                        // we should aggregate offsets from all of the parents.
                        float offsetX = 0;
                        float offsetY = 0;
                        for (View v = this; v != null && v != parent; v = (View) v.getParent()) {
                            offsetX += v.getLeft() - v.getScrollX();
                            offsetY += v.getTop() - v.getScrollY();
                        }
                        final MotionEvent motionEvent = MotionEvent.obtainNoHistory(ev);
                        motionEvent.offsetLocation(offsetX, offsetY);

                        if (parent.onInterceptTouchEvent(motionEvent)) {
                            mIntercepted = true;

                            // If the parent wants to intercept ACTION_MOVE events,
                            // we pass ACTION_DOWN event to the parent
                            // as if these touch events just have began now.
                            motionEvent.setAction(MotionEvent.ACTION_DOWN);

                            // Return this onTouchEvent() first and set ACTION_DOWN event for parent
                            // to the queue, to keep events sequence.
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    parent.dispatchTouchEvent(motionEvent);
                                }
                            });
                            return false;
                        }
                        // Even when this can't be scrolled anymore,
                        // simply returning false here may cause subView's click,
                        // so delegate it to super.
                        return super.onTouchEvent(ev);
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                returnValue = super.onTouchEvent(event);
                mLastY = eventY;
                // start NestedScroll
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(mCallbacks != null){
                    mIntercepted = false;
                    mDragging = false;
                    mCallbacks.onUpOrCancelMotionEvent(mScrollState);
                }
                returnValue = super.onTouchEvent(event);
                stopNestedScroll();
                break;
        }
        return returnValue;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(onReachEndCallback != null) {
            int height = (int) Math.floor(this.getContentHeight() * this.getScale());
            int webViewHeight = this.getMeasuredHeight();
            if (!reachedEnd && this.getScrollY() + webViewHeight +10 >= height) {
                reachedEnd = true;
                onReachEndCallback.onWebViewReachedEnd(this);
            }else{
                reachedEnd = false;
            }
        }
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mScrollY = t;

            mCallbacks.onScrollChanged(t, mFirstScroll, mDragging);
            if (mFirstScroll) {
                mFirstScroll = false;
            }

            if (mPrevScrollY < t) {
                mScrollState = ScrollState.UP;
            } else if (t < mPrevScrollY) {
                mScrollState = ScrollState.DOWN;
            } else {
                mScrollState = ScrollState.STOP;
            }
            mPrevScrollY = t;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    // Whether or not motion events are consumed by children,
                    // flag initializations which are related to ACTION_DOWN events should be executed.
                    // Because if the ACTION_DOWN is consumed by children and only ACTION_MOVEs are
                    // passed to parent (this view), the flags will be invalid.
                    // Also, applications might implement initialization codes to onDownMotionEvent,
                    // so call it here.
                    mFirstScroll = mDragging = true;
                    mCallbacks.onDownMotionEvent();
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    // Nested Scroll implements
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                                        int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
    @Override
    public void setScrollViewCallbacks(ObservableScrollViewCallbacks listener) {
        mCallbacks = listener;
    }

    @Override
    public void setTouchInterceptionViewGroup(ViewGroup viewGroup) {
        mTouchInterceptionViewGroup = viewGroup;
    }

    @Override
    public void scrollVerticallyTo(int y) {
        scrollTo(0, y);
    }

    @Override
    public int getCurrentScrollY() {
        return mScrollY;
    }

    public void setOnReachEndCallback(OnReachEndCallback onReachEndCallback) {
        this.onReachEndCallback = onReachEndCallback;
    }

    static class SavedState extends BaseSavedState {
        int prevScrollY;
        int scrollY;

        /**
         * Called by onSaveInstanceState.
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Called by CREATOR.
         */
        private SavedState(Parcel in) {
            super(in);
            prevScrollY = in.readInt();
            scrollY = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(prevScrollY);
            out.writeInt(scrollY);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface OnReachEndCallback{
        void onWebViewReachedEnd(NestedWebView webView);
    }
}