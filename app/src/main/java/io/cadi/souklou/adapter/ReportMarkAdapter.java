package io.cadi.souklou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.cadi.souklou.R;
import io.cadi.souklou.recycleViewHolder.ReportHeaderHolder;
import io.cadi.souklou.recycleViewHolder.ReportNormalHolder;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by arcadius on 16/08/16.
 */
public class ReportMarkAdapter extends StatelessSection {

    private ArrayList<Map<String, String>> data =new ArrayList<>();
    private String title;
    private SectionedRecyclerViewAdapter sectionAdapter;
    boolean expanded = true;


    public ReportMarkAdapter(Context context, ArrayList<Map<String, String>> data, String title, SectionedRecyclerViewAdapter sectionAdapter) {
        super(R.layout.report_hearder, R.layout.report_normal_row);
        this.data = data;
        this.title = title;
        this.sectionAdapter = sectionAdapter;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded? data.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ReportNormalHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new ReportHeaderHolder(view);
    }


    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final ReportHeaderHolder headerHolder = (ReportHeaderHolder) holder;
        headerHolder.txtReportHeader.setText(title);

        headerHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                headerHolder.imgReportHeaderArrow.setImageResource(
                        expanded ? R.drawable.ic_keyboard_arrow_up_black_18dp : R.drawable.ic_keyboard_arrow_down_black_18dp
                );
                sectionAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReportNormalHolder itemHolder = (ReportNormalHolder) holder;
        itemHolder.txtReportMediumSubject.setText(data.get(position).get("value"));
        itemHolder.txtReportNormalDate.setText(data.get(position).get("date"));
    }


}
