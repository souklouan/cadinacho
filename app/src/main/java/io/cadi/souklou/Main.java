package io.cadi.souklou;

import android.app.Application;
import android.util.Log;

import butterknife.ButterKnife;

/**
 * Created by arcadius on 30/06/16.
 */
public class Main extends Application {
    @Override
    public void onCreate() {
        Log.e("Info","on start show");
        super.onCreate();
        //ButterKnife.bind(getApplicationContext());
    }
}
