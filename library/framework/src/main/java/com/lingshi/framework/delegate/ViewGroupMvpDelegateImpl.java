/*
 * Copyright 2017 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.lingshi.framework.delegate;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;


import com.lingshi.framework.base.MvpPresenter;
import com.lingshi.framework.base.MvpView;


/**
 * The mvp delegate used for everything that derives from {@link View} like {@link FrameLayout}
 * etc.
 * <p>
 * <p>
 * The following methods must be called from the corresponding View lifecycle method:
 * <ul>
 * <li>{@link #onAttachedToWindow()}</li>
 * <li>{@link #onDetachedFromWindow()}</li>
 * </ul>
 * </p>
 *
 * @author Hannes Dorfmann
 * @since 1.1.0
 */
public class ViewGroupMvpDelegateImpl<V extends MvpView, P extends MvpPresenter<V>>
        implements ViewGroupMvpDelegate<V, P>, Application.ActivityLifecycleCallbacks {

    // TODO allow custom save state hook in

    public static boolean DEBUG = false;
    private static final String DEBUG_TAG = "ViewGroupMvpDelegateImp";

    private ViewGroupDelegateCallback<V, P> delegateCallback;
    private final boolean keepPresenterDuringScreenOrientationChange;
    private final Activity activity;
    private final boolean isInEditMode;

    private boolean checkedActivityFinishing = false;
    private boolean presenterDetached = false;
    private boolean presenterDestroeyed = false;

    public ViewGroupMvpDelegateImpl(@NonNull View view,
                                    @NonNull ViewGroupDelegateCallback<V, P> delegateCallback,
                                    boolean keepPresenterDuringScreenOrientationChange) {
        if (view == null) {
            throw new NullPointerException("View is null!");
        }

        if (delegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
        }

        this.delegateCallback = delegateCallback;
        this.keepPresenterDuringScreenOrientationChange = keepPresenterDuringScreenOrientationChange;

        isInEditMode = view.isInEditMode();

        if (!isInEditMode) {
            this.activity = (Activity) (delegateCallback.getContext());
            this.activity.getApplication().registerActivityLifecycleCallbacks(this);
        } else {
            this.activity = null;
        }
    }

    /**
     * Generates the unique (mosby internal) viewState id and calls {@link
     * MvpDelegateCallback#createPresenter()}
     * to create a new presenter instance
     *
     * @return The new created presenter instance
     */
    protected P createViewIdAndCreatePresenter() {

        P presenter = delegateCallback.createPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter returned from createPresenter() is null.");
        }
        return presenter;
    }

    @NonNull
    private Context getContext() {
        Context c = delegateCallback.getContext();
        if (c == null) {
            throw new NullPointerException("Context returned from " + delegateCallback + " is null");
        }
        return c;
    }

    @Override
    public void onAttachedToWindow() {
        if (isInEditMode) return;

        P presenter = createViewIdAndCreatePresenter();

        // presenter is ready, so attach viewState
        V view = delegateCallback.getMvpView();
        if (view == null) {
            throw new NullPointerException(
                    "MvpView returned from getMvpView() is null. Returned by " + delegateCallback);
        }

        if (presenter == null) {
            throw new IllegalStateException(
                    "Oops, Presenter is null. This seems to be a Mosby internal bug. Please report this issue here: https://github.com/sockeqwe/mosby/issues");
        }

    /*
    if (viewStateWillBeRestored) {
      delegateCallback.setRestoringViewState(true);
    }
    */

        delegateCallback.setPresenter(presenter);
        presenter.attachView(view);

    /*
    if (viewStateWillBeRestored) {
      delegateCallback.setRestoringViewState(false);
    }
    */

        if (DEBUG) {
            Log.d(DEBUG_TAG,
                    "MvpView attached to Presenter. MvpView: " + view + "   Presenter: " + presenter);
        }
    }

    /**
     * Must be called from {@link View#onSaveInstanceState()}
     */
    public Parcelable onSaveInstanceState() {
        if (isInEditMode) return null;

        Parcelable superState = delegateCallback.superOnSaveInstanceState();

        return superState;
    }


    @Override
    public void onDetachedFromWindow() {
        if (isInEditMode) return;

        detachPresenterIfNotDoneYet();

        if (!checkedActivityFinishing) {

            boolean destroyPermanently = !ActivityMvpDelegateImpl.retainPresenterInstance(
                    keepPresenterDuringScreenOrientationChange, activity);

            if (destroyPermanently) {
                destroyPresenterIfnotDoneYet();
            } else if (!activity.isChangingConfigurations()) {
                // View removed manually from screen
                destroyPresenterIfnotDoneYet();
            }
        } // else --> see onActivityDestroyed()
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

        if (activity == this.activity) {
            // The hosting activity of this view has been destroyed, so time to destoryed the presenter too?

            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
            checkedActivityFinishing = true;

            boolean destroyedPermanently = !ActivityMvpDelegateImpl.retainPresenterInstance(
                    keepPresenterDuringScreenOrientationChange, activity);

            if (destroyedPermanently) {
                // Whole activity will be destroyed
                detachPresenterIfNotDoneYet();
                destroyPresenterIfnotDoneYet();
            }
        }

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    private void destroyPresenterIfnotDoneYet() {
        if (!presenterDestroeyed) {
            P presenter = delegateCallback.getPresenter();
            presenter.destroy();
            presenterDestroeyed = true;
            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
            if (DEBUG) {
                Log.d(DEBUG_TAG, "Presenter destroyed: " + presenter);
            }
        }
    }

    private void detachPresenterIfNotDoneYet() {
        if (!presenterDetached) {
            P presenter = delegateCallback.getPresenter();
            presenter.detachView();
            presenterDetached = true;
            if (DEBUG) {
                Log.d(DEBUG_TAG,
                        "view " + delegateCallback.getMvpView() + " detached from Presenter " + presenter);
            }
        }
    }
}
