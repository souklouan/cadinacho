package io.cadi.souklou.utilitaire;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

/**
 * Created by arcadius on 14/08/16.
 */
public class UtilisActivity {
    Context context;

    public UtilisActivity(Context context) {
        this.context = context;
    }

    public void replaceFragment(Fragment fragment, int ressource) {
        FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(ressource, fragment);
        transaction.commit();
    }

    public static boolean viewInputValidator(View v) {
        if (!(v instanceof ViewGroup)) {
            Log.e("viewInputValidator", "can not chech the input");
            return false;
        } else {
            ViewGroup viewGroup = (ViewGroup) v;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof EditText) {
                    EditText edt = (EditText) view;
                    if (edt.getText().toString().equals(""))
                        return false;
                }
            }
            return true;
        }
    }
}
