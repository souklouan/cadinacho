package io.cadi.souklou.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import io.cadi.souklou.AppConstant;
import io.cadi.souklou.models.Children;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisDb;

/**
 * Created by arcadius on 8/25/2016.
 */
public class ChildrenDb extends UtilisDb {

    private DatabaseReference refRoot = DbConstant.FIREBASE_DB;
    private String parentKey = Utilis.getSharePreference(AppConstant.PREF_PARENT_ID);
    //table des enfants
    private DatabaseReference refChildren = refRoot.child(DbConstant.TABLE_PARENT+"/"+parentKey+"/"+DbConstant.TABLE_CHILDREN);

    public ChildrenDb() {
    }

    private void generateNewKey(final ListenerDb callback) {
        newRefChildKey(refChildren,"ch",callback);
    }

    /**
     *
     * @param callback call when in error case
     */
    public void addNewChildren(final Children children, final ListenerDb callback) {
        generateNewKey(new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                final String key = (String) o;
                DatabaseReference newChild = refChildren.child(key);
                newChild.child(DbConstant.TABLE_CHILDREN).setValue(children, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null)
                            callback.onFailed(databaseError);
                        else {
                            children.setKey(key);
                            callback.onSuccess(children);
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
