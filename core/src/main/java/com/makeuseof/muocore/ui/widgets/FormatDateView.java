/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui.widgets;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.socratica.mobile.TypefaceTextView;

import java.util.TimeZone;

/**
 * Format Date View
 */

public class FormatDateView extends TypefaceTextView {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private long mReferenceTime;

    public FormatDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setReferenceTime(long referenceTime) {
        TimeZone tz = TimeZone.getDefault();
        long offsetFromUtc = tz.getOffset(System.currentTimeMillis());
        mReferenceTime = referenceTime +  offsetFromUtc;
        updateTextDisplay();
    }

    private void updateTextDisplay() {
        if(this.mReferenceTime != -1L) {
            setText(getRelativeTimeDisplayString());
        }
    }

    private CharSequence getRelativeTimeDisplayString() {
        long now = System.currentTimeMillis();
        long diff = now - mReferenceTime;

        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1m";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "m";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1h";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "h";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "1d";
        } else {

            long days = diff / DAY_MILLIS;

            if(days > 365){
                return days/365 + "y";
            } else if(days > 30){
                return days/30 + "M";
            } else {
                return days + "d";
            }
        }
    }

    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
        } else {
            SavedState ss = (SavedState)state;
            mReferenceTime = ss.referenceTime;
            super.onRestoreInstanceState(ss.getSuperState());
        }
    }

    public static class SavedState extends BaseSavedState {
        private long referenceTime;
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(referenceTime);
        }

        private SavedState(Parcel in) {
            super(in);
            referenceTime = in.readLong();
        }
    }
}
