package io.cadi.souklou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.cadi.souklou.R;
import io.cadi.souklou.activity.PreviewChildActivity;

/**
 * Created by arcadius on 09/08/16.
 */
public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ViewHolder> {

    private Context context;

    public ChildrenAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.btnChildRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PreviewChildActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnChildRow;
        public ViewHolder(View itemView) {
            super(itemView);
            btnChildRow = (Button) itemView.findViewById(R.id.btnChildRow);
        }
    }
}
