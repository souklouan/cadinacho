package io.cadi.souklou.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import io.cadi.souklou.ApplicationConstant;
import io.cadi.souklou.models.Parent;

/**
 * Created by arcadius on 8/25/2016.
 */
public class ParentDb {

    private DatabaseReference refRoot = ApplicationConstant.FIREBASE_DB;
    private DatabaseReference refParent = refRoot.child(DbConstant.TABLE_PARENT+"/1");
    private String refKey;

    public ParentDb() {
    }


    /**
     *
     * @param callback call when in error case
     */
    public void addNewParent(final Parent parent, final ListenerDb callback) {
       DatabaseReference newParent  = refParent;
       newParent.child(DbConstant.TABLE_PARENT).setValue(parent, new DatabaseReference.CompletionListener() {
           @Override
           public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
               if (databaseError != null)
                callback.onFailed(databaseError);
               else
                   callback.onSuccess(parent);
           }
       });
    }

    public String getParentKey() {
        return refKey;
    }
}
