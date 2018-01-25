package com.train.lingshi.login;

import com.lingshi.framework.base.MvpView;

/**
 * Created by guomin on 2018/1/18.
 */

public interface LoginView extends MvpView {

    void showError(int code, String error);
}
