package io.cadi.souklou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.adapter.PreviewChildAdapter;
import io.cadi.souklou.database.DbConstant;
import io.cadi.souklou.models.Children;
import io.cadi.souklou.models.Mark;
import io.cadi.souklou.utilitaire.Utilis;

/**
 * Created by arcadius on 12/08/16.
 */
public class PreviewChildActivity extends AppCompatActivity {
    @BindView(R.id.rcwPreviewChild)
    RecyclerView rcwPreviewChild;
    @BindView(R.id.btnPrevChilSeeMore)
    Button btnPrevChilSeeMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_child);
        ButterKnife.bind(this);

        initialize();

        btnPrevChilSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PreviewChildActivity.this, ReportMarkActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_child, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_report) {
            startActivity(new Intent(PreviewChildActivity.this, ReportActivity.class));
        }
        if (id == R.id.action_calendar) {
            startActivity(new Intent(PreviewChildActivity.this, CalendarActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        String parentKey = Utilis.getSharePreference(AppConstant.PREF_PARENT_ID);
        Children child = new Gson().fromJson(Utilis.getSharePreference(AppConstant.PREF_CHILD_ACTIVE), Children.class);//set in childrenAdapter when we explore a child
        DatabaseReference refChildActiveMark = DbConstant.FIREBASE_DB.child(DbConstant.TABLE_PARENT+"/"+
                parentKey+"/"+DbConstant.TABLE_CHILDREN+"/"+child.getKey()+"/schools/1/classrooms/1/marks");

        refChildActiveMark.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Mark> list = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Mark mark = d.getValue(Mark.class);
                    mark.setKey(d.getKey());
                    list.add(mark);
                }
                initRecycleView(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initRecycleView(List<Mark> list) {
        rcwPreviewChild.setHasFixedSize(true);
        rcwPreviewChild.setLayoutManager(new LinearLayoutManager(this));
        rcwPreviewChild.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//to avoid scrolling
            }
        });
        PreviewChildAdapter adapter = new PreviewChildAdapter(list);
        rcwPreviewChild.setAdapter(adapter);
    }
}
