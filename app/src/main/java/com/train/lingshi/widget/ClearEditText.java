package com.train.lingshi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.train.lingshi.R;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by guomin on 2018/1/19.
 * 带清除EditText
 */
public class ClearEditText extends AppCompatEditText implements TextWatcher, View.OnFocusChangeListener {

    int leftWidth, leftHeight, rightWidth, rightHeight;

    private Unbinder unbinder;

    private boolean hasFoucs;

    private Drawable leftDrawable, rightDrawable;

    private boolean showRightDrawable;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unbinder.unbind();
    }

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.edit);

        showRightDrawable = ta.getBoolean(R.styleable.edit_showRight, false);

        ta.recycle();

        init();
    }

    private void init() {

        leftWidth = this.getResources().getDimensionPixelSize(R.dimen.common_input_left_image_width);
        leftHeight = this.getResources().getDimensionPixelSize(R.dimen.common_input_left_image_height);
        rightWidth = this.getResources().getDimensionPixelSize(R.dimen.common_input_right_image_width);
        rightHeight = this.getResources().getDimensionPixelSize(R.dimen.common_input_right_image_height);

        // 初始化左右两侧图标大小
        leftDrawable = getCompoundDrawables()[0];

        rightDrawable = getCompoundDrawables()[2];

        if (rightDrawable != null) {
            rightDrawable.setBounds(0, 0, rightWidth, rightHeight);
        }

        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftWidth, leftHeight);
        }

        setImageVisible(false);

        addTextChangedListener(this);

        setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setImageVisible(getText().length() > 0);
        } else {
            setImageVisible(false);
        }
    }

    private void setImageVisible(boolean visible) {
        if (visible || showRightDrawable) {
            setCompoundDrawables(leftDrawable, null, rightDrawable, null);
        } else {
            setCompoundDrawables(leftDrawable, null, null, null);
        }
    }

    public void setLeftDrawable(Drawable leftDrawable) {
        this.leftDrawable = leftDrawable;
        leftDrawable.setBounds(0, 0, leftWidth, leftHeight);
        setImageVisible(false);
    }


    public void setRightDrawable(Drawable rightDrawable) {
        this.rightDrawable = rightDrawable;
        rightDrawable.setBounds(0, 0, rightWidth, rightHeight);
        setImageVisible(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFoucs) {
            setImageVisible(s.length() > 0 ? true : false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (getCompoundDrawables()[2] != null) {
//                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
//                        && (event.getX() < ((getWidth() - getPaddingRight())));
//                if (touchable) {
//                    if (this.clickListener != null) {
//                        clickListener.onClick();
//                    }
//                    postInvalidate();
//                }
//            }
//        }
        return super.onTouchEvent(event);
    }

}
