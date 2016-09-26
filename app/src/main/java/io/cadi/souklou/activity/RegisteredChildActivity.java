package io.cadi.souklou.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.R;
import io.cadi.souklou.database.ChildrenDb;
import io.cadi.souklou.database.ListenerDb;
import io.cadi.souklou.models.Children;
import io.cadi.souklou.utilitaire.Utilis;

/**
 * Created by arcadius on 20/08/16.
 */
public class RegisteredChildActivity extends AppCompatActivity {

    //TODO change all name later
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.txtSchool)
    EditText txtSchool;
    @BindView(R.id.txtClassroom)
    EditText txtClassroom;
    @BindView(R.id.btnRegisterChild)
    Button btnRegisterChild;
    String mCurrentPhotoPath;
    ImageView imageChild;

    static final int REQUEST_TAKE_PHOTO = 2;
    private ChildrenDb childrenDb;
    private static int RESULT_LOAD_IMAGE = 1;
    FloatingActionButton buttonLoadPicture,buttontakePicture ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);

        ButterKnife.bind(this);
        buttonLoadPicture = (FloatingActionButton)findViewById(R.id.loadPicture);
        buttontakePicture = (FloatingActionButton)findViewById(R.id.takePicture);

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

        childrenDb = new ChildrenDb();

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
        Children children = getChildrenFromInput();
        finish();
        childrenDb.addNewChildren(children, new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                Children c = (Children) o;
                Log.e("voir back", c.getFirstName());

                imageChild = (ImageView) findViewById(R.id.imageChild);

                ChildrenDb.addPictureChildren(imageChild,new ListenerDb() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.e("voir back", "success");
                        Toast.makeText(getApplicationContext(), "Votre enfant a été enrégistré avec succès!",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(Object o) {
                        Log.e("failed", "echec");
                    }
                });

            }

            @Override
            public void onFailed(Object o) {
                Log.e("faillllll", "echeccccc");
            }
        });
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
}
