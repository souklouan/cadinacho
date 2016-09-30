package io.cadi.souklou.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

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

    private static Context context;
    private static DatabaseReference refRoot = DbConstant.FIREBASE_DB;
    private static String parentKey = Utilis.getSharePreference(AppConstant.PREF_PARENT_ID);
    //table des enfants
    private static DatabaseReference refChildren = refRoot.child(DbConstant.TABLE_PARENT+"/"+parentKey+"/"+DbConstant.TABLE_CHILDREN);

    public ChildrenDb(Context context) {
        this.context = context;
    }

    private static  void generateNewKey(final ListenerDb callback) {
        newRefChildKey(refChildren,"ch",callback);
    }

    /**
     *
     * @param callback call when in error case
     */
    public void addNewChildren(final ImageView imageChild, final Children children, final ListenerDb callback) {
        generateNewKey(new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                final String key = (String) o;
                DatabaseReference newChild = refChildren.child(key);
                final String imagetitle = parentKey+key+".jpg";
                children.setImageTitle(imagetitle);
                newChild.setValue(children, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null)
                            callback.onFailed(databaseError);
                        else {
                            children.setKey(key);
                            addPictureChildren(imagetitle, imageChild);
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

    private void addPictureChildren(String key, final ImageView imageChild) {
                StorageReference mountainsRef = DbConstant.FIREBASE_STORAGE_CHILD.child(key);

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
                        Toast.makeText(context,"Image non téléchargée ",Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("voir back", "success");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });

    }
}
