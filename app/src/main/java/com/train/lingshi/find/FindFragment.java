package com.train.lingshi.find;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lingshi.framework.base.MvpPresenter;
import com.train.lingshi.LSApplication;
import com.train.lingshi.base.MvpBaseFragment;
import com.train.lingshi.R;
import com.train.lingshi.widget.ClearEditText;
import com.train.lingshi.widget.VerifyEditText;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by guomin on 2018/1/18.
 * 找回密码
 */
public class FindFragment extends MvpBaseFragment<FindView, FindPresenter> implements FindView {


    private FindComponet findComponent;

    @BindView(R.id.phone)
    ClearEditText phoneET;
    @BindView(R.id.sms)
    VerifyEditText smsET;
    @BindView(R.id.password)
    ClearEditText passwordET;
    @BindView(R.id.confirm)
    ClearEditText confirmPasswordET;
    @BindView(R.id.find_sms_info)
    TextView smsInfoTV;
    @BindDrawable(R.mipmap.youxiang)
    Drawable mailDrawable;
    @BindDrawable(R.mipmap.shuruzhanghao)
    Drawable phoneDrawable;
    @BindView(R.id.byPhone)
    TextView phoneTV;
    @BindView(R.id.byEmail)
    TextView emailTV;
    @BindColor(R.color.white)
    int selectedBg;
    @BindColor(R.color.find_tab_normal_bg_color)
    int unSelectedBg;
    @BindColor(R.color.find_tab_text_selected_color)
    int selectedTextColor;
    @BindColor(R.color.find_tab_text_normal_color)
    int unSelectedTextColor;


    @Override
    protected int getLayoutRes() {
        return R.layout.find_layout;
    }

    @Override
    public MvpPresenter createPresenter() {
        return findComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        findComponent = DaggerFindComponet.builder()
                .lSAppComponet(LSApplication.getLSComponents())
                .build();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.find)
    public void onConfirm() {

    }

    @OnClick(R.id.byPhone)
    public void changeByPhone() {
        switchToPhone(true);

    }

    @OnClick(R.id.byEmail)
    public void changeByEmail() {
        switchToPhone(false);
    }


    private void switchToPhone(boolean byPhone) {
        smsInfoTV.setVisibility(byPhone ? View.GONE : View.VISIBLE);
        phoneET.setLeftDrawable(byPhone ? phoneDrawable : mailDrawable);
        phoneET.setHint(byPhone ? R.string.find_hint_phone : R.string.find_hint_email);
        phoneTV.setBackgroundColor(byPhone ? selectedBg : unSelectedBg);
        emailTV.setBackgroundColor(byPhone ? unSelectedBg : selectedBg);
        phoneTV.setTextColor(byPhone ? selectedTextColor : unSelectedTextColor);
        emailTV.setTextColor(byPhone ? unSelectedTextColor : selectedTextColor);
    }

}
