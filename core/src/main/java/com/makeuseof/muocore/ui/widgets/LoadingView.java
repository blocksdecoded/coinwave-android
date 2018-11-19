package com.makeuseof.muocore.ui.widgets;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.makeuseof.muocore.R;
import com.makeuseof.muocore.context.MuoCoreContext;


/**
 * Created by Alisher Alikulov on 12/12/16.
 * for project makeuseof-android
 * with Android Studio
 */

public class LoadingView extends FrameLayout {
    private boolean useDefaultSpinner = true;
    private ImageView foreground;
    private ImageView background;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        useDefaultSpinner = !MuoCoreContext.getInstance().isMuoApp();
        if(useDefaultSpinner) {
            ProgressBar progress = new ProgressBar(context);
            progress.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            addView(progress);
        }else {
            background = new ImageView(getContext());
            background.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            background.setImageResource(R.drawable.loading_bg);
            this.addView(background);

            foreground = new ImageView(getContext());
            foreground.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            foreground.setImageResource(R.drawable.loading_fg);
            boolean blackWhite = MuoCoreContext.getInstance().isBlackWhiteThemeEnabled();
            int color = context.getResources().getColor(blackWhite ? R.color.black : R.color.primary);
            foreground.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

            this.addView(foreground);

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
            foreground.startAnimation(animation);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(!useDefaultSpinner && visibility == VISIBLE){
            foreground.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
            foreground.startAnimation(animation);
        }
    }
}
