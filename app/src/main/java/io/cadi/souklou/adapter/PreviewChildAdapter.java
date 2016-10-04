package io.cadi.souklou.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.cadi.souklou.R;
import io.cadi.souklou.models.Mark;
import io.cadi.souklou.recycleViewHolder.ReportNormalHolder;

/**
 * Created by arcadius on 12/08/16.
 */
public class PreviewChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Mark> data =new ArrayList<>();

    public PreviewChildAdapter(List<Mark> list) {
        this.data = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_normal_row, parent, false);
        return new ReportNormalHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("data", String.valueOf(position)+" "+ data.get(position).getSubject());
        ReportNormalHolder itemHolder = (ReportNormalHolder) holder;
        itemHolder.txtReportMediumSubject.setText(data.get(position).getSubject());
        itemHolder.txtReportNormalDate.setText(data.get(position).getCreated()  );
        itemHolder.txtNote.setText(String.valueOf(data.get(position).getMark()));
        setBackgroundColor(Float.parseFloat(String.valueOf(data.get(position).getMark())), itemHolder.id_report_normal_row);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void setBackgroundColor(Float note, LinearLayout linearLayout){
        if (note > 17){
            linearLayout.setBackgroundResource(R.color.bg_very_well);
        }else if (note > 15){
            linearLayout.setBackgroundResource(R.color.bg_well);
        }
        else if (note > 12){
            linearLayout.setBackgroundResource(R.color.bg_quite_well);

        }else if (note > 10){
            linearLayout.setBackgroundResource(R.color.bg_fair);

        }else if (note > 5){
            linearLayout.setBackgroundResource(R.color.bg_low);

        }else if (note > 0){
            linearLayout.setBackgroundResource(R.color.bg_very_low);

        }
    }
}
