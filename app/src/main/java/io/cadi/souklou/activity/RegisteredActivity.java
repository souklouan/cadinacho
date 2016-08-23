package io.cadi.souklou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.R;
import io.cadi.souklou.models.Children;

/**
 * Created by arcadius on 20/08/16.
 */
public class RegisteredActivity extends AppCompatActivity {

    //TODO change all name later
    @BindView(R.id.ed1)
    EditText ed1;
    @BindView(R.id.ed2)
    EditText ed2;
    @BindView(R.id.ed3)
    EditText ed3;
    @BindView(R.id.ed4)
    EditText ed4;
    @BindView(R.id.btnRegisterChild)
    Button btnRegisterChild;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_child);
        ButterKnife.bind(this);

         mDatabase = FirebaseDatabase.getInstance().getReference("souklou-15ea7");

        btnRegisterChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Children children = new Children();
                children.setId(getTimeStamp());
                children.setFirstName(ed1.getText().toString());
                children.setLastName(ed2.getText().toString());
                children.setSchool(ed3.getText().toString());
                children.setClassroom(ed4.getText().toString());

                writeNewChildren(children);
            }
        });


    }

    private void sh(Children children){
        //mDatabase.child("posts").push().;


    }

    private void writeNewChildren(Children children) {

        //mDatabase.child("users").child("fdsffdf").setValue("arcadius");

        //String key = mDatabase.child("posts").push().getKey();
        DatabaseReference post = mDatabase.child("parent").push();
        DatabaseReference post1 = mDatabase.child("children").push();
        DatabaseReference post2 = mDatabase.child("mark").push();
        DatabaseReference post3 = mDatabase.child("course").push();
        DatabaseReference post4 = mDatabase.child("metaCalendar").push();

        post.setValue(children);
        post1.setValue(children);
        post2.setValue(children);
        post3.setValue(children);
        post4.setValue(children);


/*
        Map<String, Object> postValues = children.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + children.getId() + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);*/

        Log.e("write", "whrite "+getTimeStamp());
        //mDatabase.push();
        //mDatabase.child("users").child(userId).setValue(user);
    }

    private String getTimeStamp() {
        int time = (int) (System.currentTimeMillis());
        Timestamp tsTemp = new Timestamp(time);
        String ts =  tsTemp.toString();
        return ts;
    }
}
