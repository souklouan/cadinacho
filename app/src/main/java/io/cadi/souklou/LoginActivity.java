package io.cadi.souklou;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
        loginLogoImg.hasFocus();

        connectSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumberEdt.getText().toString();
                if (phoneNumber.equals(""))
                    phoneNumberEdt.hasFocus();
                else
                    smsAuth.signInSms(v, phoneNumber);
            }
        });

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
        if(Utilis.getSharePreference("authInfo") != null) {
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
                    authSucces(phoneNumber);
                }
            }
        if (requestCode == GoogleAuth.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleAuth.handlerResult(result, new MainListener() {
                @Override
                public void onSuccess(Object object) {
                    GoogleSignInAccount account = (GoogleSignInAccount) object;//TODO
                    authSucces(account.getDisplayName());
                }

                @Override
                public void onFailed(Object object) {
                    authFailed();
                }
            });
        }
    }

    private void authSucces(String str) {
        Utilis.setSharePreference("authInfo", str);
        startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
        finish();
    }

    private void authFailed() {
        Toast.makeText(LoginActivity.this, "Authentication falses.",
                Toast.LENGTH_SHORT).show();
    }
}

