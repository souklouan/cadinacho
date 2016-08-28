package io.cadi.souklou.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import io.cadi.souklou.ApplicationConstant;
import io.cadi.souklou.models.Children;

/**
 * Created by arcadius on 8/25/2016.
 */
public class ChildrenDb {

    private DatabaseReference refRoot = ApplicationConstant.FIREBASE_DB;
    private DatabaseReference refParent = refRoot.child(DbConstant.TABLE_PARENT+"/1");
    private String refKey;

    public ChildrenDb() {
    }

    private String generateNewKey() {
        String key;
        key = refParent.limitToLast(1).getRef().getKey();
        int intval = Integer.parseInt(key);
        intval++;
        key = "ch"+intval;
        refKey = key;
        return key;
    }

    /**
     *
     * @param callback call when in error case
     */
    public void addNewChildren(final Children children, final ListenerDb callback) {
       DatabaseReference newChild = refParent.child(generateNewKey());
       newChild.child(DbConstant.TABLE_CHILDREN).setValue(children, new DatabaseReference.CompletionListener() {
           @Override
           public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
               if (databaseError != null)
                callback.onFailed(databaseError);
               else
                   callback.onSuccess(children);
           }
       });
    }

    public String getChildKey() {
        return refKey;
    }
}
