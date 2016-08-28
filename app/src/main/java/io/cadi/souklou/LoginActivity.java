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

   // private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String phoneNumber;



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
                phoneNumber = phoneNumberEdt.getText().toString();
                startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
                onLoginPhone(v, phoneNumber);
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("phone", Utilis.getSharePreference("phoneNumber")+"...");
        if(Utilis.getSharePreference("phoneNumber") == null) {
            startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == SmsAuth.APP_REQUEST_CODE) { // SMS RESPONSE
                AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
                if (loginResult.getError() != null) {
                    Toast.makeText(this, "ERROR: Une erreur est survenue", Toast.LENGTH_LONG).show();
                } else if (loginResult.wasCancelled()) {
                    Toast.makeText(this, "ERROR: Veillez verifier votre numeros et reesasyer", Toast.LENGTH_LONG).show();
                } else {
                    Utilis.setSharePreference("phoneNumber", phoneNumber);
                    startActivity(new Intent(LoginActivity.this, ChildrenActivity.class));
                    finish();
                }
            }

        if (requestCode == GoogleAuth.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    public void onLoginPhone(final View view, String phoneNumber) {
        SmsAuth smsAuth = new SmsAuth(getApplicationContext(), AccountKitActivity.class, phoneNumber);
        Intent intent = smsAuth.onLoginPhone(view);
        startActivityForResult(intent, SmsAuth.APP_REQUEST_CODE);
    }

    private void signInGoogle() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GoogleAuth.RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

}

