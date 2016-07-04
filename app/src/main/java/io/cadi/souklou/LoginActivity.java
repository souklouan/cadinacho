package io.cadi.souklou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.cadi.souklou.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment login = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.loginFrame, login).commit();

    }


}
