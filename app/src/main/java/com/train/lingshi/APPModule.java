package com.train.lingshi;

import org.simple.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by guomin on 2018/1/18.
 */
@Module
public class APPModule {

    @Singleton
    @Provides
    public EventBus providesEventBus() {
        return EventBus.getDefault();
    }
}
