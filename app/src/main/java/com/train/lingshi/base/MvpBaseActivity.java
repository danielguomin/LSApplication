package com.train.lingshi.base;

import android.os.Bundle;

import com.lingshi.framework.base.MvpActivity;
import com.lingshi.framework.base.MvpPresenter;
import com.lingshi.framework.base.MvpView;

import butterknife.ButterKnife;

/**
 * Created by guomin on 2018/1/18.
 */

public abstract class MvpBaseActivity<V extends MvpView,P extends MvpPresenter> extends MvpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    protected void injectDependencies(){

    }
}
