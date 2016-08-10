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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.activity.ChildrenActivity;
import io.cadi.souklou.facebookAuth.SmsAuth;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.connectSmsBtn)
    Button connectSmsBtn ;
    @BindView(R.id.connectFbkBtn)
    LoginButton connectFbkBtn ;
    @BindView(R.id.phoneNumberEdt)
    EditText phoneNumberEdt ;
    @BindView(R.id.loginLogoImg)
    ImageView loginLogoImg;

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener mAuthListener ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        loginLogoImg.setImageBitmap(
                Utilis.decodeSampledBitmapFromResource(getResources(), R.drawable.icone_simple, 500, 500));

        connectSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginPhone(v, phoneNumberEdt.getText().toString());
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        connectFbkBtn.setReadPermissions("email", "public_profile");
        connectFbkBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

               //Log.e('userrrr',firebaseAuth.getCurrentUser());
                Log.e("facebook:onSuccess:" ,"dc"+loginResult.getAccessToken().toString());
                handleFacebookAccessToken(loginResult.getAccessToken());
                startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
            }

            @Override
            public void onCancel() {
                Log.e("facebook:onCancel:","cancel");
                //Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("facebook:onError", error.toString());
                //Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("facebook", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("facebook", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("ff", "handleFacebookAccessToken:" + token);
        FirebaseAuth mAuth;
// ...
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("d", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.e("dd", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == SmsAuth.APP_REQUEST_CODE) { // SMS RESPONSE
                AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
                String toastMessage;
                if (loginResult.getError() != null) {
                    toastMessage = loginResult.getError().getErrorType().getMessage();
                } else if (loginResult.wasCancelled()) {
                } else {
                    if (loginResult.getAccessToken() != null) {
                        Log.e("error face","Success:" + loginResult.getAccessToken().getAccountId());
                    } else {
                        Log.e("error face", String.format(
                                "Success:%s...",
                                loginResult.getAuthorizationCode().substring(0,10)));
                    }
                    startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
                }
            }
    }

    public void onLoginPhone(final View view, String phoneNumber) {
        SmsAuth smsAuth = new SmsAuth(getApplicationContext(), AccountKitActivity.class, phoneNumber);
        Intent intent = smsAuth.onLoginPhone(view);
        startActivityForResult(intent, SmsAuth.APP_REQUEST_CODE);
    }


}

