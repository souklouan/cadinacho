package io.cadi.souklou.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.database.ChildrenDb;
import io.cadi.souklou.database.DbConstant;
import io.cadi.souklou.database.ListenerDb;
import io.cadi.souklou.models.Children;
import io.cadi.souklou.models.Classroom;
import io.cadi.souklou.models.Parent;
import io.cadi.souklou.models.School;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisActivity;

/**
 * Created by arcadius on 20/08/16.
 */
public class RegisteredChildActivity extends AppCompatActivity {

    //TODO change all name later
    @BindView(R.id.linearRegisterChild)
    LinearLayout linearRegisterChild;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.txtSchool)
    AutoCompleteTextView txtSchool;
    @BindView(R.id.txtClassroom)
    AutoCompleteTextView txtClassroom;
    @BindView(R.id.btnRegisterChild)
    Button btnRegisterChild;
    @BindView(R.id.loadPicture)
    FloatingActionButton buttonLoadPicture;
    @BindView(R.id.takePicture)
    FloatingActionButton buttontakePicture;
    @BindView(R.id.menuLoadPicture)
    FloatingActionMenu buttonmenuLoadPicture;

    String mCurrentPhotoPath;
    ImageView imageChild;

    static final int REQUEST_TAKE_PHOTO = 2;
    private ChildrenDb childrenDb;
    private static int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);
        ButterKnife.bind(this);
        childrenDb = new ChildrenDb(this);

        String json = Utilis.getSharePreference(AppConstant.PREF_PARENT_INFO);//define after set parentInfo in dialog
        Parent user = new Gson().fromJson(json,Parent.class);
        txtLastName.setText(user.getLastName());

        setUpAutoComplete(txtSchool);
        setUpAutoComplete(txtClassroom);

        buttonLoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        buttontakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                takePicture(arg0);
            }
        });



        btnRegisterChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewChildren();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 2){
                requestCode = 1;
                ImageView image = (ImageView) findViewById(R.id.imageChild);
                image.setImageURI(Uri.parse(mCurrentPhotoPath));
        }else if(requestCode == 1){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imageView = (ImageView) findViewById(R.id.imageChild);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
        buttonmenuLoadPicture.close(false);
    }
    public void takePicture(View v){

        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "sellandbuy" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void addNewChildren() {
        if(UtilisActivity.viewInputValidator(linearRegisterChild)) {
            Children children = getChildrenFromInput();
            imageChild = (ImageView) findViewById(R.id.imageChild);
            finish();
            childrenDb.addNewChildren(imageChild, children, new ListenerDb() {
                @Override
                public void onSuccess(Object o) {
                    Children c = (Children) o;
                    Log.e("voir back", c.getFirstName());
                    Toast.makeText(getApplicationContext(), "Votre enfant a été enrégistré avec succès!",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailed(Object o) {
                    Log.e("failed", "echec");
                }
            });
        }else
            Snackbar.make(linearRegisterChild,"Veuillez remplir tout les champs",Snackbar.LENGTH_SHORT).show();
    }

    private Children getChildrenFromInput() {
        Children children = new Children();
        children.setFirstName(txtFirstName.getText().toString());
        children.setLastName(txtLastName.getText().toString());
        children.setSchool(txtSchool.getText().toString());
        children.setClassroom(txtClassroom.getText().toString());
        children.setCreated(Utilis.getCurrentTime());
        return children;
    }

    private void setUpAutoComplete(final AutoCompleteTextView textView) {
        final ArrayList<String> listArea = new ArrayList<>();
        DatabaseReference ref = DbConstant.FIREBASE_DB.child(DbConstant.TABLE_SCHOOL);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listArea.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    School a = data.getValue(School.class);
                    a.setId(data.getKey());
                    listArea.add(a.getName());
                }
                textView.setAdapter(new ArrayAdapter<>(RegisteredChildActivity.this,
                        android.R.layout.simple_list_item_1, listArea.toArray()));
                textView.setDropDownHeight(230);
                //textView.showDropDown(); may be
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setUpAutoComplet(final AutoCompleteTextView textView) {
        final ArrayList<String> listArea = new ArrayList<>();
        DatabaseReference ref = DbConstant.FIREBASE_DB.child(DbConstant.TABLE_CLASSROOM);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listArea.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Classroom a = data.getValue(Classroom.class);
                    a.setId(data.getKey());
                    listArea.add(a.getName());
                }
                textView.setAdapter(new ArrayAdapter<>(RegisteredChildActivity.this,
                        android.R.layout.simple_list_item_1, listArea.toArray()));
                textView.setDropDownHeight(230);
                //textView.showDropDown(); may be
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
