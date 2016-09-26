package io.cadi.souklou.utilitaire;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.cadi.souklou.database.ListenerDb;

/**
 * Created by arcadius on 9/8/2016.
 */
public class UtilisDb {

    public static void newRefChildKey(DatabaseReference ref, final String prefix, final ListenerDb callback) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long count = dataSnapshot.getChildrenCount();
                count++;
                String key = prefix+count;
                callback.onSuccess(key);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailed(databaseError);
            }
        });
    }
}
