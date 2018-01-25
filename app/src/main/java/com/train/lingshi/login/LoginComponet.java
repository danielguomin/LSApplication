package com.train.lingshi.login;

import com.train.lingshi.APPModule;
import com.train.lingshi.LSAppComponet;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by guomin on 2018/1/18.
 */
@Singleton
@Component(modules = APPModule.class, dependencies = LSAppComponet.class)
public interface LoginComponet {

    LoginPresenter presenter();
}
