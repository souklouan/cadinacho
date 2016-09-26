package io.cadi.souklou.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.adapter.ChildrenAdapter;
import io.cadi.souklou.database.ParentDb;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisActivity;

public class ChildrenActivity extends AppCompatActivity {
    @BindView(R.id.btnChildrenAdd) Button btnChildrenAdd;
    @BindView(R.id.relativeChildren) RelativeLayout relativeChildren;

    private static ParentDb parentDb;
    private UtilisActivity utilis;
    //private String typeOfLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        ButterKnife.bind(this);
        initListViews();
        ButterKnife.bind(this);

        //parentDb = new ParentDb(this);
        //utilis = new UtilisActivity(this);

        //PREF_PARENT_ID set in ParentDb.java
        //if (Utilis.getSharePreference(AppConstant.PREF_PARENT_ID) == null)
        // parentDb.showParentInfoDialog();

        btnChildrenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChildrenActivity.this, RegisteredChildActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilis.getSharePreference(AppConstant.PREF_PARENT_ID) == null)
            parentDb.showParentInfoDialog();
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


    @Override
    protected void onStart() {
        super.onStart();
    }

}
