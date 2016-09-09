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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.adapter.ChildrenAdapter;
import io.cadi.souklou.database.DbConstant;
import io.cadi.souklou.database.ListenerDb;
import io.cadi.souklou.database.ParentDb;
import io.cadi.souklou.models.Parent;
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
            showParentDialog();

        btnChildrenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChildrenActivity.this, RegisteredChildActivity.class));
            }
        });

        check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilis.getSharePreference(AppConstant.PREF_PARENT_ID) == null)
            showParentDialog();
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
        typeOfLogin = Utilis.getSharePreference(AppConstant.PREF_AUTH_TYPE);
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.dialog_add_infos_parent, null);
        final TextView authInfo = (TextView)alertDialogView.findViewById(R.id.authInfo);
        final EditText firstName = (EditText)alertDialogView.findViewById(R.id.firstName);
        final EditText lastName = (EditText)alertDialogView.findViewById(R.id.lastName);
        final EditText phoneNumberEdt = (EditText)alertDialogView.findViewById(R.id.phoneNumber);
        final EditText area = (EditText)alertDialogView.findViewById(R.id.area);
        final AlertDialog adb = new AlertDialog.Builder(this)
                .setView(alertDialogView)
                .setTitle("Complètez votre profil")
                .setCancelable(false)
                .setIcon(R.drawable.icone1)//TODO: change icone with the appropriate
                .setPositiveButton("OK", null) //Set to null. We override the onclick
                .setNegativeButton("Annuler", null)
                .create();
        authInfo.setText(Utilis.getSharePreference(AppConstant.PREF_AUTH_INFO));
        final Parent parent = new Parent();
        if (typeOfLogin.equals("google")) {
            //hide firstname input and lastname input
            firstName.setText("empty");
            firstName.setVisibility(View.GONE);
            lastName.setText("empty");
            lastName.setVisibility(View.GONE);
            parent.setFirstName(Utilis.getSharePreference(AppConstant.PREF_PARENT_NAME));
            parent.setLastName(Utilis.getSharePreference(AppConstant.PREF_FAMILY_NAME));
            parent.setEmail(Utilis.getSharePreference(AppConstant.PREF_PARENT_EMAIL));
        }else if (typeOfLogin.equals("sms")) {
            phoneNumberEdt.setText("empty");
            phoneNumberEdt.setVisibility(View.GONE);
        }

        adb.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                adb.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(utilis.viewInputValidator(alertDialogView)) {
                            if (typeOfLogin.equals("google")) {
                                parent.setPhone(phoneNumberEdt.getText().toString());
                                parent.setArea(area.getText().toString());
                            }else if (typeOfLogin.equals("sms")) {
                                parent.setFirstName(firstName.getText().toString());
                                parent.setLastName(lastName.getText().toString());
                                parent.setArea(area.getText().toString());
                                parent.setPhone(Utilis.getSharePreference(AppConstant.PREF_AUTH_INFO));
                            }
                            saveParent(parent, dialog);

                        } else {
                            Snackbar.make(alertDialogView,"Veuillez remplir tous les champs",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                adb.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       finish();
                    }
                });

            }
        });
        adb.show();
    }

    private void saveParent(Parent parent, final DialogInterface dialog) {
        parent.setCreated(Utilis.getCurrentTime());
        parentDb.addNewParent(parent, new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                Parent c = (Parent) o;
                Log.e("parent",c.getFirstName());
                dialog.dismiss();
            }

            @Override
            public void onFailed(Object o) {
                Log.e("fail", "echec");
            }
        });
    }

    private void check(Utilis.AuthType type,String authInfo, ListenerDb callback) {
        String orderTag="";
        if(type == Utilis.AuthType.GOOGLE)
            orderTag = "email";
        else if(type == Utilis.AuthType.SMS)
            orderTag = "phone";
        Query myTopPostsQuery = DbConstant.FIREBASE_DB.child("parent")
                .orderByChild(orderTag)
                .equalTo(authInfo);

        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Parent parent = data.getValue(Parent.class);
                    Log.e("check",parent.getPhone());
                    Log.e("keyCheck",data.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("check error",databaseError.getMessage());
            }
        });

    }
}
