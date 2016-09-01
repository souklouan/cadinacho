package io.cadi.souklou.authentication;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import io.cadi.souklou.LoginActivity;

/**
 * Created by arcadius on 08/08/16.
 */
public class SmsAuth {
    private static LoginActivity context;
    private static Class className;
    public static int APP_REQUEST_CODE = 42;

    public SmsAuth(LoginActivity context, Class className) {
        SmsAuth.context = context;
        SmsAuth.className = className;
    }

    public void signInSms(final View view, String number) {
        final Intent intent = new Intent(SmsAuth.context, SmsAuth.className);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE);
        PhoneNumber phoneNumber = new PhoneNumber("229",number);
        configurationBuilder.setInitialPhoneNumber(phoneNumber);
        configurationBuilder.setTitleType(AccountKitActivity.TitleType.APP_NAME);
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());

        context.startActivityForResult(intent, SmsAuth.APP_REQUEST_CODE);
    }
}
