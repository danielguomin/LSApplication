package com.train.lingshi;

import android.app.Application;

/**
 * Created by guomin on 2018/1/18.
 */

public class LSApplication extends Application {

    private static LSAppComponet LSComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        LSComponent = DaggerLSAppComponet.create();
    }


    public static LSAppComponet getLSComponents() {
        return LSComponent;

    }
}
