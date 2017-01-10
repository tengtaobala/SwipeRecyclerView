package com.why.recyclerviewdemo;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.why.recyclerviewdemo.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by tengtao on 2017/1/4.
 */
public class PtrClassicHeader extends FrameLayout implements PtrUIHandler {

    private ImageView mImageView;
    private AnimationDrawable mAnimation;
    private int mImgSize;


    public PtrClassicHeader(Context context) {
        this(context, null);
    }

    public PtrClassicHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    private void setupView() {
        mImageView = new ImageView(getContext());
        FrameLayout.LayoutParams params = new LayoutParams(200, 150);
        params.topMargin = 50;
        params.bottomMargin = 50;
        params.gravity = Gravity.CENTER;
        addView(mImageView, params);
        mImageView.setBackgroundResource(R.mipmap.loading0);


    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mImageView.setBackgroundResource(R.mipmap.loading0);

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mImageView.setBackgroundResource(R.drawable.loading_dialog);
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        mAnimation.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        if (mAnimation != null && mAnimation.isRunning()) {
            mAnimation.stop();
            mAnimation = null;
        }

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        mImgSize = mImageView.getHeight();


    }
}
