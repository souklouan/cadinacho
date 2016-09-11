package io.cadi.souklou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
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
        String[] note = {"13", "07", "16", "03", "19", "11"};
        for (int i = 0; i < subject.length; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 3) {
                map.put("date", "Lundi 15");
                map.put("jour", "Lundi");
            }else {
                map.put("date", "Mercredi 18");
                map.put("jour", "Mercredi");
            }
                map.put("value",subject[i]);
                map.put("note",note[i]);
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
            headerHolder.txtReportHeader.setText(data.get(position).get("jour"));
            headerHolder.viewHolder.setLayoutResource(R.layout.report_normal_row);
            View itemHolderInflate = headerHolder.viewHolder.inflate();
            TextView txtReportMediumSubject = (TextView) itemHolderInflate.findViewById(R.id.txtReportNormalSubject);
            txtReportMediumSubject.setText(data.get(position).get("value"));
            TextView txtReportMediumDate = (TextView) itemHolderInflate.findViewById(R.id.txtReportNormalDate);
            txtReportMediumSubject.setText(data.get(position).get("date"));
            TextView txtNote = (TextView) itemHolderInflate.findViewById(R.id.txtNote);
            LinearLayout id_report_normal_row = (LinearLayout) itemHolderInflate.findViewById(R.id.id_report_normal_row);
            if (Float.parseFloat(data.get(position).get("note")) > 17){
                id_report_normal_row.setBackgroundResource(R.color.bg_very_well);
            }else if (Float.parseFloat(data.get(position).get("note")) > 15){
                id_report_normal_row.setBackgroundResource(R.color.bg_well);
            }
            else if (Float.parseFloat(data.get(position).get("note")) > 12){
                id_report_normal_row.setBackgroundResource(R.color.bg_quite_well);

            }else if (Float.parseFloat(data.get(position).get("note")) > 10){
                id_report_normal_row.setBackgroundResource(R.color.bg_fair);

            }else if (Float.parseFloat(data.get(position).get("note")) > 5){
                id_report_normal_row.setBackgroundResource(R.color.bg_low);
                txtReportMediumSubject.setTextColor(Color.parseColor("#FFFFFF"));
                txtReportMediumDate.setTextColor(Color.parseColor("#FFFFFF"));
                txtNote.setTextColor(Color.parseColor("#FFFFFF"));


            }else if (Float.parseFloat(data.get(position).get("note")) > 0){
                id_report_normal_row.setBackgroundResource(R.color.bg_very_low);
                txtReportMediumSubject.setTextColor(Color.parseColor("#FFFFFF"));
                txtReportMediumDate.setTextColor(Color.parseColor("#FFFFFF"));
                txtNote.setTextColor(Color.parseColor("#FFFFFF"));

            }
            txtNote.setText(data.get(position).get("note"));
        } else if(holder instanceof ReportNormalHolder) {
            ReportNormalHolder itemHolder = (ReportNormalHolder) holder;
            itemHolder.txtReportMediumSubject.setText(data.get(position).get("value"));
            itemHolder.txtReportNormalDate.setText(data.get(position).get("date"));
            if (Float.parseFloat(data.get(position).get("note")) > 17){
                itemHolder.id_report_normal_row.setBackgroundResource(R.color.bg_very_well);
            }else if (Float.parseFloat(data.get(position).get("note")) > 15){
                itemHolder.id_report_normal_row.setBackgroundResource(R.color.bg_well);
            }
            else if (Float.parseFloat(data.get(position).get("note")) > 12){
                itemHolder.id_report_normal_row.setBackgroundResource(R.color.bg_quite_well);

            }else if (Float.parseFloat(data.get(position).get("note")) > 10){
                itemHolder.id_report_normal_row.setBackgroundResource(R.color.bg_fair);

            }else if (Float.parseFloat(data.get(position).get("note")) > 5){
                itemHolder.id_report_normal_row.setBackgroundResource(R.color.bg_low);
                itemHolder.txtReportMediumSubject.setTextColor(Color.parseColor("#FFFFFF"));
                itemHolder.txtReportNormalDate.setTextColor(Color.parseColor("#FFFFFF"));
                itemHolder.txtNote.setTextColor(Color.parseColor("#FFFFFF"));


            }else if (Float.parseFloat(data.get(position).get("note")) > 0){
                itemHolder.id_report_normal_row.setBackgroundResource(R.color.bg_very_low);
                itemHolder.txtReportMediumSubject.setTextColor(Color.parseColor("#FFFFFF"));
                itemHolder.txtReportNormalDate.setTextColor(Color.parseColor("#FFFFFF"));
                itemHolder.txtNote.setTextColor(Color.parseColor("#FFFFFF"));

            }
            itemHolder.txtNote.setText(data.get(position).get("note"));
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
