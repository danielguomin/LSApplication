package com.train.lingshi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * Created by guomin on 2018/1/19.
 * 带下滑线TabLayot
 */
public class LSTabLayout extends TabLayout {

    public LSTabLayout(Context context) {
        super(context);
    }

    public LSTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LSTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectf = new RectF(getPaddingLeft(), getHeight() - 5, getWidth() - getPaddingRight(), getHeight());
        canvas.drawRoundRect(rectf,2,1,new Paint());
    }
}
