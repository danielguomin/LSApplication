package com.train.lingshi.register;

import com.train.lingshi.APPModule;
import com.train.lingshi.LSAppComponet;
import com.train.lingshi.login.LoginPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by guomin on 2018/1/18.
 */
@Singleton
@Component(modules = APPModule.class, dependencies = LSAppComponet.class)
public interface RegisterComponet {

    RegisterPresenter presenter();
}
