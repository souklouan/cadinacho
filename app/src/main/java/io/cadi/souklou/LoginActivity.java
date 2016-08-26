package io.cadi.souklou;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.activity.ChildrenActivity;
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
    }

    public void onLoginPhone(final View view, String phoneNumber) {
        SmsAuth smsAuth = new SmsAuth(getApplicationContext(), AccountKitActivity.class, phoneNumber);
        Intent intent = smsAuth.onLoginPhone(view);
        startActivityForResult(intent, SmsAuth.APP_REQUEST_CODE);
    }

}

