package io.cadi.souklou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.cadi.souklou.R;
import io.cadi.souklou.adapter.ReportMarkAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by arcadius on 16/08/16.
 */
public class ReportMarkActivity extends AppCompatActivity {

    private ArrayList<Map<String, String>> data =new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_mark);
        initView();

    }



    private void initView() {
        initData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcwReportMark);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(getAdapter());
    }

    private SectionedRecyclerViewAdapter getAdapter() {
        SectionedRecyclerViewAdapter sectionAdapter;//get out if you need
        sectionAdapter = new SectionedRecyclerViewAdapter();
        String[] title = {"JANVIER", "MARS", "AVRIL", "MAI","JUILLET"};
        for(int i = 0; i < title.length; i++) {
            sectionAdapter.addSection(new ReportMarkAdapter(this,data, title[i], sectionAdapter));
        }
        return sectionAdapter;
    }

    private void initData() {
        String[] subject = {"Mathematique", "anglais", "Francais", "Philosophie", "Physique", "Svt"};
        String[] note = {"13", "07", "16", "03", "19", "11"};
        for (int i = 0; i < subject.length; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 3)
                map.put("date","Lundi 15");
            else
                map.put("date","Mercredi 18");
            map.put("value",subject[i]);
            map.put("note",note[i]);
            data.add(map);
            //Log.e("data", );
        }
    }
}
