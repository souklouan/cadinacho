package io.cadi.souklou.database;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.cadi.souklou.AppConstant;
import io.cadi.souklou.models.Parent;
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
    public void addNewParent(final Parent parent, final ListenerDb callback) {
       generateNewKey(new ListenerDb() {
           @Override
           public void onSuccess(Object o) {
               final String key = (String) o;//get new parent reference key
               DatabaseReference newParent  = refParent.child(key);
               newParent.child(DbConstant.TABLE_PARENT).setValue(parent, new DatabaseReference.CompletionListener() {
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
