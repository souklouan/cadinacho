package io.cadi.souklou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.R;
import io.cadi.souklou.adapter.PreviewChildAdapter;

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

        rcwPreviewChild.setHasFixedSize(true);
        rcwPreviewChild.setLayoutManager(new LinearLayoutManager(this));
        rcwPreviewChild.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//to avoid scrolling
            }
        });

        PreviewChildAdapter adapter = new PreviewChildAdapter(this);
        rcwPreviewChild.setAdapter(adapter);

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
}
