package com.makeuseof.muocore.ui.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.makeuseof.muocore.R;
import com.makeuseof.muocore.ui.listeners.OnAnimationEndListener;
import com.makeuseof.muocore.util.DimenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alisher Alikulov on 12/17/16.
 * for project makeuseof-android
 * with Android Studio
 */

public class RadialMenuView extends FrameLayout {
    private static final long APPEARING_ANIM_DURATION = 200;
    private static final int TOTAL_RADIAL_ANGLE = 100;
    private static final int FIRST_BUTTON_ANGLE = 70;
    private static final int LEFT_MARGIN_DP = 10;
    private static final int BUTTON_SIZE_DP = 55;
    private static final float RADIUS_DP = 80;
    private int deviceHeight = 640;
    private int deviceWidth = 320;
    private Window window;
    private int backgroundColor = Color.WHITE;

    private List<RadialButton> buttonsData = new ArrayList<>();
    private List<View> buttons = new ArrayList<>();
    private float lastX, lastY;
    private RadialMenuExtraMarginDelegate extraMarginDelegate;
    private int statusBarColor;
    private View clientView;
    private View clientViewClone;
    private RadialMenuListener listener;
    private int itemPosition;
    private Thread animationThread;

    public RadialMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            deviceHeight = displaymetrics.heightPixels;
            deviceWidth = displaymetrics.widthPixels;
        }

        setOnClickListener(onRadialMenuClickListener);
    }

    private OnClickListener onRadialMenuClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };

    private OnTouchListener onItemViewTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            dispatchViewTouch(motionEvent);
            return false;
        }
    };

    private OnTouchListener onScrollViewTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            dispatchViewTouch(motionEvent);
            return isVisible();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void setScale(final int button, final float scale) {
        buttonsData.get(button).setScale(scale);
    }

    private Runnable animateButtons = new Runnable() {
        @Override
        public void run() {
            while (isVisible()){
                for (final RadialButton button: buttonsData){
                    final View buttonView = buttons.get(buttonsData.indexOf(button));
                    if(!equal(button.scale, buttonView.getScaleY(), 0.01)){
                        float scale = button.scale;
                        if(Math.abs(buttonView.getScaleY() - scale) >= 0.05){
                            scale = (float) (buttonView.getScaleY()+(buttonView.getScaleY()<scale?0.05:-0.05));
                        }
                        final float finalScale = scale;
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonView.setScaleX(finalScale);
                                buttonView.setScaleY(finalScale);
                                if(equal(finalScale, 1, 0.001)){
                                    buttonView.setTranslationX(0);
                                    buttonView.setTranslationY(0);
                                }else{
                                    float dx = (float) (Math.cos(button.angle) * button.r *(finalScale - 1));
                                    float dy = - (float) (Math.sin(button.angle) * button.r *(finalScale - 1));
                                    buttonView.setTranslationX(dx);
                                    buttonView.setTranslationY(dy);
                                }
                            }
                        });
                    }
                }
                try{Thread.sleep(1);}catch (InterruptedException ignored){}
            }
        }
    };

    private boolean equal(float num1, float num2, double eps) {
        return Math.abs(num1-num2)<eps;
    }

    public void dispatchViewTouch(MotionEvent motionEvent){
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = motionEvent.getRawX();
            lastY = motionEvent.getRawY();
        }
        boolean buttonSelected = false;
        int closestButtonIndex = 0;
        if(isVisible()) {
            int closestDistance = 20000;
            for (RadialButton button : buttonsData) {
                int distance = button.calcDistance(motionEvent.getRawX(), motionEvent.getRawY());
                if (closestDistance > distance) {
                    closestDistance = distance;
                    closestButtonIndex = buttonsData.indexOf(button);
                }
            }
            setScale(closestButtonIndex, (float) (1.0 + (100.0 - closestDistance) / 300.0));

            if(closestDistance < 75){
                buttonSelected = true;
            }

            for(int i=0; i<buttonsData.size();i++){
                ImageView buttonBg = (ImageView) buttons.get(i).findViewById(R.id.radial_button_bg);
                ImageView buttonIcon = (ImageView) buttons.get(i).findViewById(R.id.radial_button_img);
                if(i != closestButtonIndex){
                    setScale(i, 1);
                    buttonBg.setImageResource(R.drawable.radial_white);
                    buttonIcon.setColorFilter(Color.argb(255, 97, 97, 97));
                }else {
                    buttonBg.setImageResource(buttonSelected ? R.drawable.radial_red:R.drawable.radial_white);
                    buttonIcon.setColorFilter(buttonSelected ? Color.WHITE : Color.argb(255, 97, 97, 97));
                }
            }
        }
        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            if(buttonSelected){
                RadialButton radialButton = buttonsData.get(closestButtonIndex);
                radialButton.listener.onClick(radialButton.buttonId, itemPosition);
            }
            dismiss();
        }
    }

    public OnTouchListener getOnItemViewTouchListener() {
        return onItemViewTouchListener;
    }

    public void addParentView(View parentView) {
        parentView.setOnTouchListener(onScrollViewTouchListener);
    }

    public void setExtraMarginDelegate(RadialMenuExtraMarginDelegate extraMarginDelegate) {
        this.extraMarginDelegate = extraMarginDelegate;
    }

    public void addButton(int buttonId, @DrawableRes int drawableResourceId, OnRadialButtonClickListener clickListener) {
        buttonsData.add(new RadialButton(buttonId, drawableResourceId, clickListener));
    }

    public void dismiss(){
        if(isVisible()) {
            for(View button: buttons){
                try {
                    button.clearAnimation();
                }catch (Exception ignored){}
            }
            removeAllViews();
            setVisibility(GONE);
            clientView = null;
            if (window != null) {
                try {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(statusBarColor);
                } catch (Throwable ignored) {
                }
            }
            if (listener != null) {
                listener.onRadialMenuDismiss();
            }
            for(RadialButton button:buttonsData){
                button.setScale(1f);
            }
        }
    }

    public void showAroundView(View view, View clone) {
        this.clientView = view;
        this.clientViewClone = clone;
        this.show();
    }

    public void show(){
        if(!isVisible()) {
            removeAllViews();
            if (window != null) {
                try {
                    statusBarColor = window.getStatusBarColor();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.WHITE);
                } catch (Throwable ignored) {
                }
            }
            final DimenUtils dimenUtils = new DimenUtils(getContext());

            float extraTopMargin = 0;
            if (extraMarginDelegate != null) {
                extraTopMargin = extraMarginDelegate.getExtraMargin();
            }

            int[] position = new int[2];
            clientView.getLocationOnScreen(position);
            LayoutParams lp = makeLayoutParams(clientView.getWidth(), clientView.getHeight());
            lp.setMargins(position[0], position[1] - (int) extraTopMargin - dimenUtils.dpToPx(24), 0, 0);
            clientViewClone.setLayoutParams(lp);
            clientViewClone.setBackgroundColor(Color.WHITE);
            addView(clientViewClone);
            clientViewClone.setOnClickListener(onRadialMenuClickListener);
            clientViewClone.setOnLongClickListener(null);
            final int buttonSize = dimenUtils.dpToPx(BUTTON_SIZE_DP);
            final ImageView circle = new ImageView(getContext());
            LayoutParams centerLP = new LayoutParams(buttonSize, buttonSize);
            centerLP.setMargins((int) lastX - buttonSize / 2, (int) lastY - dimenUtils.dpToPx(24) - (int) extraTopMargin - buttonSize / 2, 0, 0);
            circle.setLayoutParams(centerLP);
            circle.setImageResource(R.drawable.circle);
            addView(circle);

            buttons.clear();

            for (final RadialButton buttonData : buttonsData) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View buttonView = inflater.inflate(R.layout.radial_button, null);
                ImageView image = (ImageView) buttonView.findViewById(R.id.radial_button_img);
                image.setImageResource(buttonData.drawableResId);
                try {
                    buttonView.setForeground(getResources().getDrawable(R.drawable.circlur_selectable, getContext().getTheme()));
                } catch (NoSuchMethodError ignored) {
                }

                buttonView.setLayoutParams(centerLP);

                addView(buttonView);
                buttons.add(buttonView);

                int radius = dimenUtils.dpToPx(RADIUS_DP);
                int startAngle = getStartAngle(radius, buttonSize, dimenUtils);
                int i = buttonsData.indexOf(buttonData);
                double buttonAngle = Math.toRadians(startAngle + i * TOTAL_RADIAL_ANGLE / (buttonsData.size() - 1));

                final int dx = (int) (radius * Math.cos(buttonAngle));
                final int dy = -(int) (radius * Math.sin(buttonAngle));

                Animation animation = new TranslateAnimation(0, dx, 0, dy);
                animation.setDuration(APPEARING_ANIM_DURATION);
                buttonView.startAnimation(animation);

                buttonData.setPosition(lastX+dx, lastY+dy, radius*0.75, buttonAngle);

                animation.setAnimationListener(new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        float extraTopMargin = 0;
                        if (extraMarginDelegate != null) {
                            extraTopMargin = extraMarginDelegate.getExtraMargin();
                        }
                        buttonView.clearAnimation();
                        LayoutParams params = new LayoutParams(buttonSize, buttonSize);
                        params.setMargins((int) lastX - buttonSize / 2 + dx, (int) lastY - dimenUtils.dpToPx(24) - (int) extraTopMargin - buttonSize / 2 + dy, 0, 0);
                        buttonView.setLayoutParams(params);
                    }
                });
            }
            setVisibility(View.VISIBLE);
            animationThread = new Thread(animateButtons);
            animationThread.start();
        }
    }

    private LayoutParams makeLayoutParams(int width, int height) {
        return new LayoutParams(width, height);
    }

    private int getStartAngle(int radialRadius, int buttonSize, DimenUtils dimenUtils) {
        int beta = 180 - TOTAL_RADIAL_ANGLE - FIRST_BUTTON_ANGLE;
        int leftMarginPx = dimenUtils.dpToPx(LEFT_MARGIN_DP);
        int alpha = (int) Math.toDegrees((Math.acos((lastX-buttonSize/2.0 - leftMarginPx) / (float) radialRadius) - Math.toRadians(beta)));
        if(lastY < radialRadius+buttonSize/2+dimenUtils.dpToPx(24)) {
            if(deviceWidth - lastX < radialRadius+buttonSize/2){
                return 100 + FIRST_BUTTON_ANGLE - alpha;
            } else {
                return 200 + FIRST_BUTTON_ANGLE ;
            }
        }else{
            return FIRST_BUTTON_ANGLE - alpha;
        }
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setRadialMenuListener(RadialMenuListener listener) {
        this.listener = listener;
    }

    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public void removeAllButtons() {
        buttonsData.clear();
    }

    class RadialButton{
        public int buttonId;
        public int drawableResId;
        public OnRadialButtonClickListener listener;
        public double x, y, r;
        public float scale = 1.0f;
        private double angle;

        public RadialButton(int buttonId, int drawableResId, OnRadialButtonClickListener clickListener){
            this.buttonId = buttonId;
            this.drawableResId = drawableResId;
            this.listener = clickListener;
        }

        public void setPosition(double x, double y, double r, double angle) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.angle = angle;
        }

        public int calcDistance(float currentX, float currentY){
            int distance = (int) (Math.sqrt(sqr(x - currentX) + sqr(y - currentY)) / r * 100);
            return distance > 100? 100:distance;
        }

        private double sqr(double n){
            return n*n;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }
    }

    public interface RadialMenuExtraMarginDelegate {
        float getExtraMargin();
    }

    public interface RadialMenuListener {
        void onRadialMenuDismiss();
    }

    public interface OnRadialButtonClickListener {
        void onClick(int buttonId, int itemPosition);
    }
}
