package io.cadi.souklou;

import android.content.Context;


/**
 * Created by arcadius on 08/08/16.
 */
public class ApplicationContext {
    private static ApplicationContext mInstance;
    private Context context;

    public static ApplicationContext getInstance() {
        if (mInstance == null) mInstance = getSync();
        return mInstance;
    }

    private static synchronized ApplicationContext getSync() {
        if (mInstance == null) mInstance = new ApplicationContext();
        return mInstance;
    }

    public void initialize(Context context) {
        this.context = context;
    }

    public Context getApplicationContext() {
        return context;
    }
}
