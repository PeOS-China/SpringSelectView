package com.peos.springselect;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/31.
 */

public class SpringSelectView extends FrameLayout implements Checkable {

    private static final int Default_Color_Checked = Color.BLUE;
    private static final int Default_Color_UnChecked = Color.GRAY;
    private static final int Default_Res_Src = R.drawable.icon_favours_no;
    private static final boolean Default_Boolean_Checked = false;
    private static final boolean Default_Boolean_Animate = true;
    private static final boolean Default_Boolean_Network = false;
    private static final boolean Default_Boolean_ClickSpring = true;
    private static final int Default_Animate_TimeMS = 1000;
    private static final int Default_Spring_TimeMS = 1000;

    private ImageView imgvIcon;
    private TextView textTip;

    private int iconId;
    private int colorIconChecked;
    private int colorIconUnChecked;

    private int animateTime;
    private int springTime;

    private boolean isChecked;
    private boolean isAnimate;
    private boolean isNetworkCheck;
    private boolean isEnableClickSpring;

    private ClickSpring clickSpring;

    private SpringSelectListener springSelectListener;
    private SpringSelectNetworkListener springSelectNetworkListener;
    private SpringSelectConditionListener springSelectConditionListener;

    public SpringSelectView(Context context) {
        super(context, null);
    }

    public SpringSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(null == attrs){
            iconId = Default_Res_Src;
            colorIconChecked = Default_Color_Checked;
            colorIconUnChecked = Default_Color_UnChecked;
            isChecked = Default_Boolean_Checked;
            isAnimate = Default_Boolean_Animate;
            animateTime = Default_Animate_TimeMS;
            springTime = Default_Spring_TimeMS;
            isNetworkCheck = Default_Boolean_Network;
            isEnableClickSpring = Default_Boolean_ClickSpring;
        }
        else{
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpringSelectView, 0, 0);

            iconId = a.getResourceId(R.styleable.SpringSelectView_src, R.drawable.icon_favours_no);
            colorIconChecked = a.getColor(R.styleable.SpringSelectView_checkedColor, Default_Color_Checked);
            colorIconUnChecked = a.getColor(R.styleable.SpringSelectView_unCheckColor, Default_Color_UnChecked);
            isChecked = a.getBoolean(R.styleable.SpringSelectView_isChecked, Default_Boolean_Checked);
            isAnimate = a.getBoolean(R.styleable.SpringSelectView_isAnimate, Default_Boolean_Animate);
            isNetworkCheck = a.getBoolean(R.styleable.SpringSelectView_isNetworkCheck, Default_Boolean_Network);
            isEnableClickSpring = a.getBoolean(R.styleable.SpringSelectView_isEnableClickSpring, Default_Boolean_ClickSpring);
            animateTime = a.getInt(R.styleable.SpringSelectView_animateTime, Default_Animate_TimeMS);
            springTime = a.getInt(R.styleable.SpringSelectView_springTime, Default_Spring_TimeMS);

            a.recycle();
        }

        setClickable(true);

        int px = DensityUtil.dp2px(4);

        imgvIcon = new ImageView(context);
        imgvIcon.setImageResource(iconId);
        imgvIcon.setPadding(px, px, px, px);

        if(isChecked){
            imgvIcon.setColorFilter(colorIconChecked, PorterDuff.Mode.SRC_ATOP);
        }
        else{
            imgvIcon.setColorFilter(colorIconUnChecked, PorterDuff.Mode.SRC_ATOP);
        }

        FrameLayout.LayoutParams flpImgV = new LayoutParams(
                LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
                , Gravity.CENTER);

        addView(imgvIcon, flpImgV);

        clickSpring = new ClickSpring(springTime);
        clickSpring.setClickSpringListener(cickSpringListener);

    }

    @Override
    public void onDetachedFromWindow(){
        if(clickSpring != null)
            clickSpring.cancel();

        super.onDetachedFromWindow();
    }

    /************************************************************************************************/
    @Override
    public void toggle(){
        toggleCheck();
    }

    @Override
    public void setChecked(boolean checked){
        checkChange(checked, isAnimate);
    }

    public void setChecked(boolean checked, boolean isAnimate){
        checkChange(checked, isAnimate);
    }

    @Override
    public boolean isChecked(){
        return isChecked;
    }

    @Override
    public boolean performClick(){
        toggleCheck();
        return super.performClick();
    }

    /************************************************************************************************/
    public boolean isAnimate() {
        return isAnimate;
    }

    public void setAnimate(boolean animate) {
        isAnimate = animate;
    }

    public boolean isNetworkCheck() {
        return isNetworkCheck;
    }

    public void setNetworkCheck(boolean networkCheck) {
        isNetworkCheck = networkCheck;
    }

    public boolean isEnableClickSpring() {
        return isEnableClickSpring;
    }

    public void setEnableClickSpring(boolean enableClickSpring) {
        isEnableClickSpring = enableClickSpring;
    }

    public void setSpringSelectListener(SpringSelectListener springSelectListener) {
        this.springSelectListener = springSelectListener;
    }

    public void setSpringSelectNetworkListener(SpringSelectNetworkListener springSelectNetworkListener) {
        this.springSelectNetworkListener = springSelectNetworkListener;
    }

    public void setSpringSelectConditionListener(SpringSelectConditionListener springSelectConditionListener) {
        this.springSelectConditionListener = springSelectConditionListener;
    }

    private void toggleCheck(){
        if(isChecked)
            checkChange(false, isAnimate);
        else
            checkChange(true, isAnimate);
    }

    private void checkChange(boolean checked, boolean isAnimate){
        if(isNetworkCheck){
            if(!NetworkUtil.isHasNetwork(getContext())) {
                if(springSelectNetworkListener != null)
                    springSelectNetworkListener.onSpringSelectNetworkNoConnect();
                return;
            }
        }

        if(springSelectConditionListener != null){
            if(!springSelectConditionListener.getSpringSelectCondition())
                return ;
        }

        isChecked = checked;

        if(checked){
            setImgvIcon(true);
            if(isAnimate)
                startAnimator();
        }
        else{
            setImgvIcon(false);
        }

        if(springSelectListener != null){
            if(isEnableClickSpring){
                clickSpring.start(isChecked);
                return ;
            }

            springSelectListener.onSpringSelectClick(isChecked);
        }
    }

    private void setImgvIcon(boolean isChecked){
        if(isChecked){
            imgvIcon.setColorFilter(colorIconChecked, PorterDuff.Mode.SRC_ATOP);
            return ;
        }

        imgvIcon.setColorFilter(colorIconUnChecked, PorterDuff.Mode.SRC_ATOP);
    }

    private void startAnimator(){
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imgvIcon, View.SCALE_X, 0.8f, 1.2f, 1.f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imgvIcon, View.SCALE_Y, 0.8f, 1.2f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(imgvIcon, View.ALPHA, 0.5f, 1f);

        AnimatorSet enterAnimatorSet = new AnimatorSet();
        enterAnimatorSet.setDuration(animateTime);
        enterAnimatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
        enterAnimatorSet.start();
    }

    private ClickSpringListener cickSpringListener = new ClickSpringListener() {
        @Override
        public void onClickSpring() {
            handler.sendEmptyMessage(1);
        }
    };

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            //更新UI
            switch (msg.what)
            {
                case 1:
                    if(springSelectListener != null){
                        springSelectListener.onSpringSelectClick(isChecked);
                    }
                    break;
            }
        }
    };
}
