package io.cadi.souklou;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by arcadius on 14/08/16.
 */
public class UtilisForActivity {
    AppCompatActivity activity;

    public UtilisForActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void replaceFragment(Fragment fragment, int ressource) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(ressource, fragment);
        transaction.commit();
    }
}
