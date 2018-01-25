package com.train.lingshi.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.train.lingshi.base.MvpBaseFragment;
import com.lingshi.framework.base.MvpPresenter;
import com.train.lingshi.LSApplication;
import com.train.lingshi.R;
import com.train.lingshi.widget.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by guomin on 2018/1/18.
 * 登陆
 */
public class LoginFragment extends MvpBaseFragment<LoginView, LoginPresenter> implements LoginView {

    private LoginComponet loginComponent;

    @BindView(R.id.phone)
    ClearEditText phoneET;
    @BindView(R.id.password)
    ClearEditText passwordET;

    @Override
    protected int getLayoutRes() {
        return R.layout.login_layout;
    }

    @Override
    public MvpPresenter createPresenter() {
        return loginComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        loginComponent = DaggerLoginComponet.builder()
                .lSAppComponet(LSApplication.getLSComponents())
                .build();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.login)
    public void onLogin() {
        phoneET.getText();
        passwordET.getText();
        // 调用登陆接口

    }

    @OnClick(R.id.find)
    public void find() {
        //跳转到找回密码界面
    }

    @OnClick(R.id.register)
    public void regsiter() {
        //跳转到注册界面
    }

    @Override
    public void showError(int code, String error) {
        // 根据错误类型
        // 账号不存在，账号或密码错误
    }
}
