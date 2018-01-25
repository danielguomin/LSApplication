package com.train.lingshi.register;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lingshi.framework.base.MvpPresenter;
import com.train.lingshi.LSApplication;
import com.train.lingshi.R;
import com.train.lingshi.base.MvpBaseFragment;
import com.train.lingshi.widget.ClearEditText;
import com.train.lingshi.widget.VerifyEditText;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by guomin on 2018/1/18.
 * 注册
 */
public class RegisterFragment extends MvpBaseFragment<RegisterView, RegisterPresenter> implements RegisterView {


    private RegisterComponet registerComponent;

    @BindView(R.id.username)
    ClearEditText userNameET;
    @BindView(R.id.phone)
    ClearEditText phoneET;
    @BindView(R.id.password)
    ClearEditText passwordED;
    @BindView(R.id.sms)
    VerifyEditText smsET;
    @BindView(R.id.email)
    ClearEditText emailET;
    @BindView(R.id.subject)
    ClearEditText subjectET;
    @BindView(R.id.phone_tip)
    TextView phoneTV;
    @BindView(R.id.sms_tip)
    TextView smsTV;
    @BindView(R.id.email_tip)
    TextView emailTV;
    @BindDimen(R.dimen.register_pop_width)
    int popWidth;
    @BindDimen(R.dimen.register_pop_height)
    int popHeight;
    @BindDimen(R.dimen.register_pop_margin_buttom)
    int buttom;
    private float alpha;
    private PopupWindow subjectPopupWindow;


    @Override
    protected int getLayoutRes() {
        return R.layout.register_layout;
    }

    @Override
    public MvpPresenter createPresenter() {
        return registerComponent.presenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void injectDependencies() {
        registerComponent = DaggerRegisterComponet.builder()
                .lSAppComponet(LSApplication.getLSComponents())
                .build();
    }

    @OnClick(R.id.register)
    public void onRegister() {
    }

    @OnClick(R.id.subject)
    public void changeSubject() {
        showSubjects();
    }

    private void showSubjects() {

        final ArrayList<String> subjects = new ArrayList<>();
        subjects.add("语文");
        subjects.add("数学");
        subjects.add("英语");

        ListView listView = new ListView(getContext());
        listView.setVerticalScrollBarEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subjectET.setText(subjects.get(position));
                if (subjectPopupWindow != null) {
                    subjectPopupWindow.dismiss();
                    subjectPopupWindow = null;
                }
            }
        });
        listView.setBackgroundResource(R.drawable.register_pop_bg);
        listView.setAdapter(new SubjectAdapter(subjects));

        subjectPopupWindow = new PopupWindow(listView, popWidth, popHeight);
        subjectPopupWindow.setFocusable(true);
        subjectPopupWindow.setAnimationStyle(R.style.register_pop_style);
        subjectPopupWindow.setOutsideTouchable(false);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        alpha = lp.alpha;
        subjectPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (alpha < 1f) {
                            try {
                                Thread.sleep(4);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            alpha += 0.01f;
                            msg.obj = alpha;
                            mHandler.sendMessage(msg);
                        }
                    }

                }).start();
            }
        });
        subjectPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, buttom);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha -= 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    backgroundAlpha((float) msg.obj);
                    break;
            }
        }
    };


    private class SubjectAdapter extends BaseAdapter {

        private ArrayList<String> subjects;

        SubjectAdapter(ArrayList<String> subjects) {
            this.subjects = subjects;
        }

        @Override
        public int getCount() {
            return this.subjects.size();
        }

        @Override
        public Object getItem(int position) {
            return this.subjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_item, null, true);
                viewHolder.subject = convertView.findViewById(R.id.subject);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.subject.setText(subjects.get(position));
            return convertView;
        }
    }

    private final class ViewHolder {
        TextView subject;
    }
}
