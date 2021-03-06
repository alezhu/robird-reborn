package com.aaplab.robird.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.aaplab.robird.Analytics;
import com.aaplab.robird.R;
import com.aaplab.robird.data.model.PrefsModel;

import icepick.Icepick;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by majid on 07.05.15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(preferedTheme());
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Analytics.onResume(this);
    }

    @Override
    protected void onPause() {
        Analytics.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    public void compositeSubscription(Subscription subscription) {
        mSubscriptions.add(subscription);

    }

    @Override
    public boolean onSupportNavigateUp() {
        ActivityCompat.finishAfterTransition(this);
        return true;
    }

    protected boolean isTransparent() {
        return false;
    }

    protected int preferedTheme() {
        if (isTransparent()) {
            return new PrefsModel().isDarkTheme() ?
                    R.style.AppTheme_Transparent :
                    R.style.AppTheme_Light_Transparent;

        } else {
            return new PrefsModel().isDarkTheme() ?
                    R.style.AppTheme :
                    R.style.AppTheme_Light;
        }
    }
}
