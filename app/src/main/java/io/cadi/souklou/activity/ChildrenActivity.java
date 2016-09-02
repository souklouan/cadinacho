package io.cadi.souklou.activity;


import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.adapter.ChildrenAdapter;
import io.cadi.souklou.database.ListenerDb;
import io.cadi.souklou.database.ParentDb;
import io.cadi.souklou.models.Parent;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisForActivity;

public class ChildrenActivity extends AppCompatActivity {
    @BindView(R.id.btnChildrenAdd) Button btnChildrenAdd;

    private ParentDb parentDb;
    private UtilisForActivity utilis;
    private int typeOfLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        ButterKnife.bind(this);
        initListViews();
        ButterKnife.bind(this);

        parentDb = new ParentDb();
        utilis = new UtilisForActivity(this);

        showParentDialog();

        btnChildrenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChildrenActivity.this, RegisteredChildActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Log.e("givenNamed",user.getDisplayName());
        //Log.e("FamNamed",Utilis.getSharePreference("FamilyName"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void initListViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rcwChildren);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        ChildrenAdapter adapter = new ChildrenAdapter(this);
        recyclerView.setAdapter(adapter);

    }


    private void showParentDialog() {
        typeOfLogin = Integer.parseInt(Utilis.getSharePreference(AppConstant.PREF_AUTH_TYPE));
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.dialog_add_infos_parent, null);
        final EditText firstName = (EditText)alertDialogView.findViewById(R.id.firstName);
        final EditText lastName = (EditText)alertDialogView.findViewById(R.id.lastName);
        final EditText area = (EditText)alertDialogView.findViewById(R.id.area);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Complètez votre profil");
        adb.setCancelable(false);
        adb.setIcon(R.drawable.icone1);//TODO: change icone with the appropriate
        if (typeOfLogin == 1) {
            firstName.setText(Utilis.getSharePreference(AppConstant.PREF_PARENT_NAME));
            lastName.setText(Utilis.getSharePreference(AppConstant.PREF_FAMILY_NAME));
        }
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(utilis.viewInputValidator(alertDialogView))
                   saveParent();
                else
                    Snackbar.make(alertDialogView,"Veuillez remplir tous les champs",Snackbar.LENGTH_LONG).show();
            } });

        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            } });
        adb.show();
    }

    private void saveParent() {
        //addNewParent(firstName.getText().toString(),lastName.getText().toString(),area.getText().toString());
    }

    private void addNewParent(String firstName,String lastName,String area) {
        Parent parent = getParentFromInput(firstName,lastName,area);
        parentDb.addNewParent(parent, new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                Parent c = (Parent) o;
                Log.e("voir back", c.getFirstName());
            }

            @Override
            public void onFailed(Object o) {
                Log.e("faillllll", "echeccccc");
            }
        });
    }

    private Parent getParentFromInput(String firstName,String lastName,String area) {
        Parent parent = new Parent();
        parent.setFirstName(firstName);
        parent.setLastName(lastName);
        parent.setArea(area);
        parent.setCreated(Utilis.getCurrentTime());
        return parent;
    }
}
