package com.train.lingshi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.train.lingshi.R;

/**
 * Created by guomin on 2018/1/23.
 */

public class WheelView extends FrameLayout {

    private WheelListView wheelListView;
    private View mSelectView;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 初始化视图
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
//        int floatId = typedArray.getResourceId(R.styleable.WheelView_float_layout, -1);
//        int bgId = typedArray.getResourceId(R.styleable.WheelView_background_resources, -1);
//        typedArray.recycle();
//        if (-1 == floatId) {
//            throw new IllegalArgumentException("resId is invalid");
//        }
//        //背景
//        ImageView imageView = new ImageView(context);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        try {
//            imageView.setLayerType(LAYER_TYPE_SOFTWARE, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (-1 != bgId) {
//            imageView.setImageResource(bgId);
//        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
//        addView(imageView, params);

        //listview
        wheelListView = new WheelListView(context);
        wheelListView.setBackgroundColor(Color.TRANSPARENT);
        wheelListView.setClipToPadding(false);
        wheelListView.setClipChildren(false);
        params = new FrameLayout.LayoutParams(-1, -1);
        params.gravity = Gravity.CENTER;
        addView(wheelListView, params);

        //悬浮视图
//        mSelectView = LayoutInflater.from(context).inflate(floatId, this, false);
//        params = new FrameLayout.LayoutParams(-1, -2);
//        params.gravity = Gravity.CENTER;
//        addView(mSelectView, params);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        wheelListView.setUp(mSelectView, this);
    }

    public void setAdapter(WheelAdapter adapter) {
        wheelListView.setAdapter(adapter);
    }

    public WheelListView getWheelListView() {
        return wheelListView;
    }
}
