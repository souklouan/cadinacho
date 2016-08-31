package io.cadi.souklou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private ChildrenDb childrenDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);
        ButterKnife.bind(this);

        childrenDb = new ChildrenDb();

        btnRegisterChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewChildren();
            }
        });


    }

    private void addNewChildren() {
        Children children = getChildrenFromInput();
        childrenDb.addNewChildren(children, new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                Children c = (Children) o;
                Log.e("voir back", c.getFirstName());
                finish();//end the activity
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
