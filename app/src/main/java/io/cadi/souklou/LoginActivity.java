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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.activity.ChildrenActivity;
import io.cadi.souklou.authentication.GoogleAuth;
import io.cadi.souklou.authentication.SmsAuth;
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
    @BindView(R.id.relativeL)
    RelativeLayout relativeL;


    private GoogleAuth googleAuthNew;
    private SmsAuth smsAuth;
    private String phoneNumber;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        googleAuthNew = new GoogleAuth(this);
        smsAuth = new SmsAuth(this, AccountKitActivity.class);


        if(Utilis.getSharePreference(AppConstant.PREF_PARENT_ID) != null) {
            startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
            finish();
        }

        loginLogoImg.setImageBitmap(
                Utilis.decodeSampledBitmapFromResource(getResources(), R.drawable.icone_simple, 500, 500));


        connectSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumberEdt.getText().toString();
                if (phoneNumber.equals("")) {
                    Snackbar.make(v,"Entrer votre numero de téléphone",Snackbar.LENGTH_LONG).show();
                    phoneNumberEdt.hasFocus();
                }
                else{
                    smsAuth.signInSms(v, phoneNumber);
                }

            }
        });
        customGoogleBtn(btnLoginGoogle,"Compte google");
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleAuthNew.signIn();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e("signIn", "onAuthStateChanged:signed_in:" + user.getUid());
                        authSuccess(user.getDisplayName(), Utilis.AuthType.GOOGLE);
                } else {
                    // User is signed out
                    Log.e("signIn", "onAuthStateChanged:signed_out");
                    disconnection(null);
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utilis.getSharePreference(AppConstant.PREF_PARENT_ID) != null) {
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
                    authSuccess(phoneNumber, Utilis.AuthType.SMS);
                }
        }

        if (requestCode == GoogleAuth.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                googleAuthNew.firebaseAuthWithGoogle(account, relativeL);
            } else {
                // Google Sign In failed, update UI appropriately
                authFailed();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void disconnection(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {

        } else {

        }
    }


    //type :  sms and google
    private void authSuccess(String str, Utilis.AuthType type) {
        Utilis.setSharePreference(AppConstant.PREF_PARENT_PHONENUMBER, str);
        Utilis.setSharePreference(AppConstant.PREF_AUTH_TYPE, type.name());
        startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
        finish();
    }

    private void authFailed() {
        //TODO when login failed
        Toast.makeText(LoginActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    private void customGoogleBtn(SignInButton signInButton,
                                           String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(14);
                tv.setBackgroundResource(R.drawable.google_btn_backgroung);
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_plus_48, 0, 0, 0);
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setText(buttonText);
            }
        }
    }
}

