package io.cadi.souklou;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.activity.ChildrenActivity;
import io.cadi.souklou.authentication.GoogleAuth;
import io.cadi.souklou.authentication.SmsAuth;
import io.cadi.souklou.utilitaire.MainListener;
import io.cadi.souklou.utilitaire.Utilis;


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.connectSmsBtn)
    Button connectSmsBtn ;
   // @BindView(R.id.connectFbkBtn)
   // LoginButton connectFbkBtn ;
    @BindView(R.id.phoneNumberEdt)
    EditText phoneNumberEdt ;
    @BindView(R.id.loginLogoImg)
    ImageView loginLogoImg;
    @BindView(R.id.btnLoginGoogle)
    SignInButton btnLoginGoogle;

    private GoogleAuth googleAuth;
    private SmsAuth smsAuth;
    private String phoneNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        googleAuth = new GoogleAuth(this);
        smsAuth = new SmsAuth(this, AccountKitActivity.class);

        loginLogoImg.setImageBitmap(
                Utilis.decodeSampledBitmapFromResource(getResources(), R.drawable.icone_simple, 500, 500));
        loginLogoImg.requestFocus();

        connectSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumberEdt.getText().toString();
                if (phoneNumber.equals("")) {
                    Snackbar.make(v,"Entrer votre numero de téléphone",Snackbar.LENGTH_LONG).show();
                    phoneNumberEdt.hasFocus();
                }
                else
                    smsAuth.signInSms(v, phoneNumber);
            }
        });
        setGooglePlusButtonText(btnLoginGoogle,"Compte google");
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleAuth.signInGoogle();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utilis.getSharePreference(AppConstant.PREF_AUTH_INFO) != null) {
            startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == SmsAuth.APP_REQUEST_CODE) { // SMS RESPONSE
                AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
                if (loginResult.getError() != null || loginResult.wasCancelled()) {
                    authFailed();
                } else {
                    authSucces(phoneNumber, 0);
                }
            }
        if (requestCode == GoogleAuth.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleAuth.handlerResult(result, new MainListener() {
                @Override
                public void onSuccess(Object object) {
                    GoogleSignInAccount account = (GoogleSignInAccount) object;//TODO
                    Utilis.setSharePreference(AppConstant.PREF_FAMILY_NAME,account.getFamilyName());
                    Utilis.setSharePreference(AppConstant.PREF_PARENT_NAME,account.getGivenName());
                    authSucces(account.getDisplayName(), 1);
                }

                @Override
                public void onFailed(Object object) {
                    authFailed();
                }
            });
        }
    }

    //type : 0 for sms and 1 for google
    private void authSucces(String str, int type) {
        Utilis.setSharePreference(AppConstant.PREF_AUTH_INFO, str);
        Utilis.setSharePreference(AppConstant.PREF_AUTH_TYPE, String.valueOf(type));
        startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
        finish();
    }

    private void authFailed() {
        Toast.makeText(LoginActivity.this, "Authentication falses.",
                Toast.LENGTH_SHORT).show();
    }

    private void setGooglePlusButtonText(SignInButton signInButton,
                                           String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(18);
                tv.setBackgroundResource(R.drawable.google_btn_backgroung);
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_plus_48, 0, 0, 0);
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setText(buttonText);
            }
        }
    }
}

