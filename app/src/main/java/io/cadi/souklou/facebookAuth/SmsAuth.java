package io.cadi.souklou.facebookAuth;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

/**
 * Created by arcadius on 08/08/16.
 */
public class SmsAuth {
    private static Context context;
    private static Class className;
    private static String phoneNumber;
    public static int APP_REQUEST_CODE = 42;

    public SmsAuth(Context context, Class className, String phoneNumber) {
        SmsAuth.context = context;
        SmsAuth.className = className;
        SmsAuth.phoneNumber = phoneNumber;
    }

    public Intent onLoginPhone(final View view) {
        final Intent intent = new Intent(SmsAuth.context, SmsAuth.className);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE);
        PhoneNumber phoneNumber = new PhoneNumber("229",SmsAuth.phoneNumber);
        configurationBuilder.setInitialPhoneNumber(phoneNumber);
        configurationBuilder.setTitleType(AccountKitActivity.TitleType.APP_NAME);
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());

        return intent;
    }
}
