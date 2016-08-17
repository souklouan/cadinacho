package io.cadi.souklou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.cadi.souklou.R;
import io.cadi.souklou.recycleViewHolder.ReportHeaderHolder;
import io.cadi.souklou.recycleViewHolder.ReportNormalHolder;

/**
 * Created by arcadius on 12/08/16.
 */
public class PreviewChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private String headerChange = "";
    private ArrayList<Map<String, String>> data =new ArrayList<>();

    private void initData() {
        String[] subject = {"Mathematique", "anglais", "Francais", "Philosophie", "Physique", "Svt"};
        for (int i = 0; i < subject.length; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 3)
                map.put("date","Lundi");
            else
                map.put("date","Mercredi");
            map.put("value",subject[i]);
            data.add(map);
            //Log.e("data", );
        }
    }

    public PreviewChildAdapter(Context context /* get LIST also */) {
        initData();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("create view", String.valueOf(viewType));
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_hearder, parent, false);
            return new ReportHeaderHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_normal_row, parent, false);
            return new ReportNormalHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("data", String.valueOf(position)+" "+ data.get(position).get("value"));
        if (holder instanceof ReportHeaderHolder) {
            ReportHeaderHolder headerHolder = (ReportHeaderHolder) holder;
            headerHolder.txtReportHeader.setText(data.get(position).get("date"));
            headerHolder.viewHolder.setLayoutResource(R.layout.report_normal_row);
            View itemHolderInflate = headerHolder.viewHolder.inflate();
            TextView txtReportMediumSubject = (TextView) itemHolderInflate.findViewById(R.id.txtReportNormalSubject);
            txtReportMediumSubject.setText(data.get(position).get("value"));
        } else if(holder instanceof ReportNormalHolder) {
            ReportNormalHolder itemHolder = (ReportNormalHolder) holder;
            itemHolder.txtReportMediumSubject.setText(data.get(position).get("value"));
            //TODO
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position))
            return TYPE_HEADER;
        else
            return 1;

    }

    private boolean isHeaderView(int position) {
        if (data.get(position).get("date").equals(headerChange))
            return false;
        else {
            headerChange = data.get(position).get("date");
            return true;
        }

    }
}
