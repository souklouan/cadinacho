package io.cadi.souklou;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

//import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKit;

/**
 * Created by arcadius on 08/08/16.
 */
public class Souklou extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppContext.getInstance().initialize(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            enableStrictMode();
        }
        AccountKit.initialize(getApplicationContext());
        //FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
