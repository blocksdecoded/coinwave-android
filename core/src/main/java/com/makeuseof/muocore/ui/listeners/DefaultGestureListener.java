package com.makeuseof.muocore.ui.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Alisher Alikulov on 11/23/16.
 * for project makeuseof-android
 * with Android Studio
 */

public class DefaultGestureListener implements GestureDetector.OnGestureListener {
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
        return false;
    }
}
