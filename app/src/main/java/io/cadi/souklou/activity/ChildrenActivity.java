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


import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.adapter.ChildrenAdapter;
import io.cadi.souklou.database.ParentDb;
import io.cadi.souklou.models.Parent;
import io.cadi.souklou.utilitaire.ListenerApp;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisActivity;

public class ChildrenActivity extends AppCompatActivity {
    @BindView(R.id.btnChildrenAdd) Button btnChildrenAdd;

    private ParentDb parentDb;
    private UtilisActivity utilis;
    private String typeOfLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        ButterKnife.bind(this);
        initListViews();
        ButterKnife.bind(this);

        parentDb = new ParentDb();
        utilis = new UtilisActivity(this);

        if (Utilis.getSharePreference(AppConstant.PREF_PARENT_ID) == null)
            showParentInfoDialog();

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
        if (Utilis.getSharePreference(AppConstant.PREF_PARENT_ID) == null)
            showParentInfoDialog();
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


    private void showParentInfoDialog() {
        typeOfLogin = Utilis.getSharePreference(AppConstant.PREF_AUTH_TYPE);
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.dialog_add_infos_parent, null);
        final TextView authInfo = (TextView)alertDialogView.findViewById(R.id.authInfo);
        final EditText firstName = (EditText)alertDialogView.findViewById(R.id.firstName);
        final EditText lastName = (EditText)alertDialogView.findViewById(R.id.lastName);
        final EditText phoneNumberEdt = (EditText)alertDialogView.findViewById(R.id.phoneNumber);
        final EditText area = (EditText)alertDialogView.findViewById(R.id.area);
        final Button btnDiaInfoPar = (Button)alertDialogView.findViewById(R.id.btnDiaInfoPar);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Complètez votre profil");
        adb.setCancelable(false);
        adb.setIcon(R.drawable.ic_person_black_18dp);//TODO: change icone with the appropriate
        adb.create();
                //.create();
        authInfo.setText(Utilis.getSharePreference(AppConstant.PREF_PARENT_PHONENUMBER));
        final Parent parent = new Parent();
        if (typeOfLogin.equals(Utilis.AuthType.GOOGLE.name())) {
            //hide firstname input and lastname input
            firstName.setText("empty");
            firstName.setVisibility(View.GONE);
            lastName.setText("empty");
            lastName.setVisibility(View.GONE);
            parent.setFirstName(Utilis.getSharePreference(AppConstant.PREF_PARENT_NAME));
            parent.setLastName(Utilis.getSharePreference(AppConstant.PREF_FAMILY_NAME));
            parent.setEmail(Utilis.getSharePreference(AppConstant.PREF_PARENT_EMAIL));
        }else if (typeOfLogin.equals(Utilis.AuthType.SMS.name())) {
            phoneNumberEdt.setText("empty");
            phoneNumberEdt.setVisibility(View.GONE);
        }
        final AlertDialog show = adb.show();

        btnDiaInfoPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(utilis.viewInputValidator(alertDialogView)) {
                    if (typeOfLogin.equals(Utilis.AuthType.GOOGLE.name())) {
                        parent.setPhone(phoneNumberEdt.getText().toString());
                        parent.setArea(area.getText().toString());
                    }else if (typeOfLogin.equals(Utilis.AuthType.SMS.name())) {
                        parent.setFirstName(firstName.getText().toString());
                        parent.setLastName(lastName.getText().toString());
                        parent.setArea(area.getText().toString());
                        parent.setPhone(Utilis.getSharePreference(AppConstant.PREF_PARENT_PHONENUMBER));
                    }
                    parentDb.saveParent(parent, new ListenerApp() {
                        @Override
                        public void onSuccess(Object object) {
                            show.dismiss();
                        }

                        @Override
                        public void onFailed(Object object) {
                            //parent data not add to firebase
                        }
                    });

                } else {
                    Snackbar.make(alertDialogView,"Veuillez remplir tous les champs",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }


}
