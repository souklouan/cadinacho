package io.cadi.souklou.recycleViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import io.cadi.souklou.R;

/**
 * Created by arcadius on 16/08/16.
 */
public class ReportHeaderHolder extends RecyclerView.ViewHolder {
    public TextView txtReportHeader;
    public View rootView;
    public ImageView imgReportHeaderArrow;
    public ViewStub viewHolder;

    public ReportHeaderHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        txtReportHeader = (TextView) itemView.findViewById(R.id.txtReportHeader);
        imgReportHeaderArrow = (ImageView) itemView.findViewById(R.id.imgReportHeaderArrow);
        viewHolder = (ViewStub) itemView.findViewById(R.id.vsReportHeader);

    }
}
