package io.cadi.souklou.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import io.cadi.souklou.LoginActivity;
import io.cadi.souklou.R;
import io.cadi.souklou.utilitaire.MainListener;

/**
 * Created by arcadius on 8/28/2016.
 */
public class GoogleAuth implements GoogleApiClient.OnConnectionFailedListener{

    private LoginActivity context;
    private FirebaseAuth mAuth;
    public static int RC_SIGN_IN = 9999;


    public GoogleAuth(LoginActivity context) {
        this.context =  context;
        mAuth = FirebaseAuth.getInstance();
    }

    public void signInGoogle() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(context /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        context.startActivityForResult(signInIntent, GoogleAuth.RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct, final MainListener callback) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        callback.onSuccess(acct);
                        if (!task.isSuccessful()) {
                            callback.onFailed(null);
                        }
                    }
                });
    }

    public void handlerResult(GoogleSignInResult result, MainListener callback) {
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account, callback);
        } else {
            callback.onFailed(null);
            Log.e("connect failed", "firebaseAuthWithGoogle:failed");
            Log.e("connect failed", result.toString());
            // Google Sign In failed, update UI appropriately
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Authentication connection failed.",
                Toast.LENGTH_LONG).show();
    }
}
