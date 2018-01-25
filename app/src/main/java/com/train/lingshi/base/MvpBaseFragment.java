package com.train.lingshi.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lingshi.framework.base.MvpFragment;
import com.lingshi.framework.base.MvpPresenter;
import com.lingshi.framework.base.MvpView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by guomin on 2018/1/18.
 */

public abstract class MvpBaseFragment<V extends MvpView, P extends MvpPresenter> extends MvpFragment {

    private Unbinder unbinder;

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this,view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    /**
     * Inject the dependencies
     */
    protected void injectDependencies() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
