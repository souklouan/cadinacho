package io.cadi.souklou.recycleViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.cadi.souklou.R;

/**
 * Created by arcadius on 16/08/16.
 */
public class ReportNormalHolder extends RecyclerView.ViewHolder {
    public TextView txtReportMediumSubject,txtReportNormalDate;

    public ReportNormalHolder(View itemView) {
        super(itemView);
        txtReportMediumSubject = (TextView) itemView.findViewById(R.id.txtReportNormalSubject);
        txtReportNormalDate = (TextView) itemView.findViewById(R.id.txtReportNormalDate);
    }
}
