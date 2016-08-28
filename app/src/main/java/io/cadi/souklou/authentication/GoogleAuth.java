package io.cadi.souklou.authentication;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import io.cadi.souklou.ApplicationContext;
import io.cadi.souklou.R;

/**
 * Created by arcadius on 8/28/2016.
 */
public class GoogleAuth {

    private Context context;
    public static int RC_SIGN_IN = 422;
    public GoogleAuth(Context context) {
        this.context = context;
    }

    public GoogleSignInOptions init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return gso;
    }


}
