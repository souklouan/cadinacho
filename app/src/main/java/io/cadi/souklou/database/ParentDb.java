package io.cadi.souklou.database;

import android.content.DialogInterface;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.cadi.souklou.AppConstant;
import io.cadi.souklou.models.Parent;
import io.cadi.souklou.utilitaire.ListenerApp;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisDb;

/**
 * Created by arcadius on 8/25/2016.
 */
public class ParentDb extends UtilisDb {

    private DatabaseReference refRoot = DbConstant.FIREBASE_DB;
    private DatabaseReference refParent = refRoot.child(DbConstant.TABLE_PARENT);

    public ParentDb() {
        super();
    }

    private void generateNewKey(final ListenerDb callback) {
        newRefChildKey(refParent,"pa",callback);
    }

    /**
     *
     * @param callback call when in error case
     */
    public void addNewParent(final String existenceKey,final Parent parent, final ListenerDb callback) {
        if (existenceKey != null) {
            DatabaseReference newParent  = refParent.child(existenceKey);
            newParent.setValue(parent, new DatabaseReference.CompletionListener() {
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

                }
            });
        }
    }

    public void saveParent(final Parent parent, final ListenerApp callback) {
        check(parent.getPhone(), new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof Parent){
                    //parent already
                    if (!o.equals(parent))//old parent date not equal to new parent data so we save the new data
                        save(parent.getId(),parent,callback);

                }else {
                    save(null, parent, callback);
                }
            }

            @Override
            public void onFailed(Object o) {

            }
        });


    }


    private void save(final String existenceKey, final Parent parent, final ListenerApp callback) {
        parent.setCreated(Utilis.getCurrentTime());
        addNewParent(existenceKey, parent, new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                Parent p = (Parent) o;
                callback.onSuccess(p);
            }

            @Override
            public void onFailed(Object o) {
                Log.e("failSaveParent", "echec");
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
                    }

                }
                callback.onSuccess(false);//number not find
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("check error",databaseError.getMessage());
                callback.onFailed(databaseError);
            }
        });

    }

}
