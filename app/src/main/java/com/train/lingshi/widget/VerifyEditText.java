package com.train.lingshi.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.train.lingshi.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guomin on 2018/1/23.
 * 验证码输入框
 */
public class VerifyEditText extends LinearLayout {

    private int time = 60;

    private TextView verifyTV;

    private Timer timer;

    private TimerTask timerTask;

    public VerifyEditText(Context context) {
        this(context, null);
    }

    public VerifyEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.verity_input, null);
        verifyTV = view.findViewById(R.id.verify);
        verifyTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time == 60 || time <= 0) {
                    time = 59;
                    initTimer();
                    timer.schedule(timerTask, 0, 1000);
                }
            }
        });
        this.addView(view, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void initTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                VerifyEditText.this.post(new Runnable() {
                    @Override
                    public void run() {
                        if (time <= 0) {
                            timer.cancel();
                            timer = null;
                            timerTask.cancel();
                            timerTask = null;
                            verifyTV.setText(R.string.common_get_verity_again);
                        } else {
                            verifyTV.setText(time + "s");
                        }
                        time--;
                    }
                });
            }
        };

        timer = new Timer();
    }
}
