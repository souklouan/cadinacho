package io.cadi.souklou.activity;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.R;
import io.cadi.souklou.adapter.ChildrenAdapter;
import io.cadi.souklou.database.ListenerDb;
import io.cadi.souklou.database.ParentDb;
import io.cadi.souklou.models.Parent;
import io.cadi.souklou.utilitaire.Utilis;

public class ChildrenActivity extends AppCompatActivity {
    @BindView(R.id.btnChildrenAdd) Button btnChildrenAdd;

    private ParentDb parentDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        ButterKnife.bind(this);
        initListViews();
        ButterKnife.bind(this);


        parentDb = new ParentDb();
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.dialog_add_infos_parent, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Complètez votre profil");
        adb.setIcon(R.drawable.icone1);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText firstName = (EditText)alertDialogView.findViewById(R.id.firstName);
                EditText lastName = (EditText)alertDialogView.findViewById(R.id.lastName);
                EditText area = (EditText)alertDialogView.findViewById(R.id.area);
                addNewParent(firstName.getText().toString(),lastName.getText().toString(),area.getText().toString());

            } });

        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            } });
        adb.show();


        btnChildrenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChildrenActivity.this, RegisteredChildActivity.class));
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void initListViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rcwChildren);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        ChildrenAdapter adapter = new ChildrenAdapter(this);
        recyclerView.setAdapter(adapter);

    }


    private void showParentDialog() {

    }

    private void addNewParent(String firstName,String lastName,String area) {
        Parent parent = getParentFromInput(firstName,lastName,area);
        parentDb.addNewParent(parent, new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                Parent c = (Parent) o;
                Log.e("voir back", c.getFirstName());
            }

            @Override
            public void onFailed(Object o) {
                Log.e("faillllll", "echeccccc");
            }
        });
    }

    private Parent getParentFromInput(String firstName,String lastName,String area) {
        Parent parent = new Parent();
        parent.setFirstName(firstName);
        parent.setLastName(lastName);
        parent.setArea(area);
        parent.setCreated(Utilis.getCurrentTime());
        return parent;
    }
}
