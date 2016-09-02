package io.cadi.souklou;

import android.content.Context;


/**
 * Created by arcadius on 08/08/16.
 */
public class AppContext {
    private static AppContext mInstance;
    private Context context;

    public static AppContext getInstance() {
        if (mInstance == null) mInstance = getSync();
        return mInstance;
    }

    private static synchronized AppContext getSync() {
        if (mInstance == null) mInstance = new AppContext();
        return mInstance;
    }

    public void initialize(Context context) {
        this.context = context;
    }

    public Context getApplicationContext() {
        return context;
    }
}
