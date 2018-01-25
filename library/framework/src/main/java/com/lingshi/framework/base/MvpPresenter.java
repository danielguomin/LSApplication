package com.lingshi.framework.base;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

/**
 * Created by guomin on 2018/1/18.
 */

public interface MvpPresenter<V extends MvpView> {

    @UiThread
    void attachView(@NonNull V view);


    @UiThread
    void detachView();


    @UiThread
    void destroy();
}
