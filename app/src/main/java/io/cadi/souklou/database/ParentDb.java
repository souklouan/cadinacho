package io.cadi.souklou.database;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.activity.ChildActivity;
import io.cadi.souklou.activity.ChildrenActivity;
import io.cadi.souklou.models.Area;
import io.cadi.souklou.models.Parent;
import io.cadi.souklou.utilitaire.ListenerApp;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisActivity;
import io.cadi.souklou.utilitaire.UtilisDb;

/**
 * Created by arcadius on 8/25/2016.
 */
public class ParentDb extends UtilisDb {

    private DatabaseReference refRoot = DbConstant.FIREBASE_DB;
    private DatabaseReference refParent = refRoot.child(DbConstant.TABLE_PARENT);
    private AlertDialog show;
    private ChildActivity context;
    private UtilisActivity utilis;

    public ParentDb(ChildActivity context) {
        super();
        this.context = context;
        utilis = new UtilisActivity(context);
    }

    private void generateNewKey(final ListenerDb callback) {
        newRefChildKey(refParent,"pa",callback);
    }

    /**
     *
     * @param callback call when in error case
     */
    private void addNewParent(final String existenceKey,final Parent parent, final ListenerDb callback) {
        if (existenceKey != null) {
            DatabaseReference newParent  = refParent.child(existenceKey);
            String json = new Gson().toJson(parent);
            Map<String, Object> retMap = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
            newParent.updateChildren(retMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null)
                        callback.onFailed(databaseError);
                    else {
                        Utilis.setSharePreference(AppConstant.PREF_PARENT_ID,existenceKey);
                        callback.onSuccess(parent);
                    }
                }
            });
        }else {
            generateNewKey(new ListenerDb() {
                @Override
                public void onSuccess(Object o) {
                    final String key = (String) o;//get new parent reference key
                    DatabaseReference newParent  = refParent.child(key);
                    newParent.setValue(parent, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null)
                                callback.onFailed(databaseError);
                            else {
                                Utilis.setSharePreference(AppConstant.PREF_PARENT_ID,key);
                                callback.onSuccess(parent);
                            }
                        }
                    });
                }
                @Override
                public void onFailed(Object o) {
                    callback.onFailed(o);
                }
            });
        }
        Log.e("exitence","val:"+existenceKey);
    }

    private void saveParent(final Parent parent, final ListenerDb callback) {
        check(parent.getPhone(), new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                String key = null;
                if (o instanceof Parent) //parent already exit
                    key = ((Parent) o).getId();
                save(key, parent, callback);
            }

            @Override
            public void onFailed(Object o) {
                callback.onFailed(o);
            }
        });
    }


    private void save(final String existenceKey, final Parent parent, final ListenerDb callback) {
        parent.setCreated(Utilis.getCurrentTime());
        addNewParent(existenceKey, parent, new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                Parent p = (Parent) o;
                callback.onSuccess(p);
            }

            @Override
            public void onFailed(Object o) {
                callback.onFailed("parentSave Failed");
            }
        });
    }

    private void check(final String number, final ListenerDb callback) {
        Query myTopPostsQuery = DbConstant.FIREBASE_DB.child("parent")
                .orderByChild("phone")
                .equalTo(number);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String phoneNumber = data.getValue(Parent.class).getPhone();
                    if (phoneNumber.equals(number)) {
                        Parent p = data.getValue(Parent.class);
                        p.setId(data.getKey());
                        callback.onSuccess(p);
                        return;//stop search
                    }
                }
                callback.onSuccess(false);//number not find
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailed(databaseError);
            }
        });

    }

    public void showParentInfoDialog() {
        final String typeOfLogin = Utilis.getSharePreference(AppConstant.PREF_AUTH_TYPE);
        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.dialog_add_infos_parent, null);
        final TextView authInfo = (TextView)alertDialogView.findViewById(R.id.authInfo);
        final EditText firstName = (EditText)alertDialogView.findViewById(R.id.firstName);
        final EditText lastName = (EditText)alertDialogView.findViewById(R.id.lastName);
        final EditText phoneNumberEdt = (EditText)alertDialogView.findViewById(R.id.phoneNumber);
        final AutoCompleteTextView area = (AutoCompleteTextView)alertDialogView.findViewById(R.id.area);
        final Button btnDiaInfoPar = (Button)alertDialogView.findViewById(R.id.btnDiaInfoPar);

        final AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setView(alertDialogView);
        adb.setTitle("Complètez votre profil");
        adb.setCancelable(false);
        adb.setIcon(R.drawable.ic_person_black_18dp);//TODO: change icone with the appropriate

        setUpAutoComplete(area);

        authInfo.setText(Utilis.getSharePreference(AppConstant.PREF_PARENT_LOGIN_INFO));
        final Parent parent = new Parent();
        if (typeOfLogin.equals(Utilis.AuthType.GOOGLE.name())) {
            //hide firstname input and lastname input
            firstName.setText("empty");
            firstName.setVisibility(View.GONE);
            lastName.setText("empty");
            lastName.setVisibility(View.GONE);
            String json = Utilis.getSharePreference(AppConstant.PREF_PARENT_INFO);
            Parent user = new Gson().fromJson(json,Parent.class);
            parent.setFirstName(user.getFirstName());
            parent.setLastName(user.getLastName());
            parent.setEmail(user.getEmail());
        }else if (typeOfLogin.equals(Utilis.AuthType.SMS.name())) {
            phoneNumberEdt.setText("empty");
            phoneNumberEdt.setVisibility(View.GONE);
        }
        show = adb.create();
        show.show();

        btnDiaInfoPar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                btnDiaInfoPar.requestFocus();
                btnDiaInfoPar.setClickable(false);
                btnDiaInfoPar.setBackgroundColor(context.getResources().getColor(R.color.colorAccentAlpha));
                if(utilis.viewInputValidator(alertDialogView)) {
                    if (typeOfLogin.equals(Utilis.AuthType.GOOGLE.name())) {
                        parent.setPhone(phoneNumberEdt.getText().toString());
                        parent.setArea(area.getText().toString());
                    }else if (typeOfLogin.equals(Utilis.AuthType.SMS.name())) {
                        parent.setFirstName(firstName.getText().toString());
                        parent.setLastName(lastName.getText().toString());
                        parent.setArea(area.getText().toString());
                        parent.setPhone(Utilis.getSharePreference(AppConstant.PREF_PARENT_LOGIN_INFO));
                    }

                    //final ListenerDb callback

                    saveParent(parent, new ListenerDb() {
                        @Override
                        public void onSuccess(Object object) {
                            show.dismiss();
                            v.getContext().startActivity(new Intent(context, ChildrenActivity.class));
                            Utilis.setSharePreference(AppConstant.PREF_PARENT_INFO, new Gson().toJson(object));
                            context.finish();
                        }

                        @Override
                        public void onFailed(Object object) {
                            //parent data not add to firebase
                            Snackbar.make(alertDialogView,"Echec: veuillez reéssayer",Snackbar.LENGTH_SHORT).show();
                            btnDiaInfoPar.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                            btnDiaInfoPar.setClickable(true);
                        }
                    });

                } else {
                    btnDiaInfoPar.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    btnDiaInfoPar.setClickable(true);
                    Snackbar.make(alertDialogView,"Veuillez remplir tous les champs",Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void setUpAutoComplete(final AutoCompleteTextView textView) {
        final ArrayList<String> listArea = new ArrayList<>();
        DatabaseReference ref = DbConstant.FIREBASE_DB.child(DbConstant.TABLE_AREA);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listArea.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Area a = data.getValue(Area.class);
                    a.setId(data.getKey());
                    listArea.add(a.getName());
                }
                textView.setAdapter(new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1, listArea.toArray()));
                textView.setDropDownHeight(230);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
