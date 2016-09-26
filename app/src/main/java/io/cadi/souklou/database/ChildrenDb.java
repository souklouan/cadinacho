package io.cadi.souklou.database;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.models.Children;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisDb;

/**
 * Created by arcadius on 8/25/2016.
 */
public class ChildrenDb extends UtilisDb {

    private static DatabaseReference refRoot = DbConstant.FIREBASE_DB;
    private static String parentKey = Utilis.getSharePreference(AppConstant.PREF_PARENT_ID);
    //table des enfants
    private static DatabaseReference refChildren = refRoot.child(DbConstant.TABLE_PARENT+"/"+parentKey+"/"+DbConstant.TABLE_CHILDREN);

    public ChildrenDb() {
    }

    private static  void generateNewKey(final ListenerDb callback) {
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


    public static void addPictureChildren(final ImageView imageChild, final ListenerDb callback) {
        generateNewKey(new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                final String key = (String) o;
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://souklou-15ea7.appspot.com");

                StorageReference mountainsRef = storageRef.child("souklou_images/Child/"+key+".jpg");

                StorageReference mountainImagesRef = storageRef.child("souklou_images/Child/"+key+".jpg");

                mountainsRef.getName().equals(mountainImagesRef.getName());
                mountainsRef.getPath().equals(mountainImagesRef.getPath());

                imageChild.setDrawingCacheEnabled(true);
                imageChild.buildDrawingCache();
                Bitmap bitmap = imageChild.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("voir back", exception.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("voir back", "success");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });

            }

            @Override
            public void onFailed(Object o) {

            }
        });


    }
}
