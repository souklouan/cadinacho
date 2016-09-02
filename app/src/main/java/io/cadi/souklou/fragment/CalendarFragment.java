package io.cadi.souklou.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.AppContext;
import io.cadi.souklou.R;
import io.cadi.souklou.adapter.CalendarListAdapter;

/**
 * Created by arcadius on 20/08/16.
 */
public class CalendarFragment extends Fragment {

    @BindView(R.id.rcwCalendar)
    RecyclerView rcwCalendar;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_calendar,container,false);
        ButterKnife.bind(this, rootview);

        initListViews();
        return rootview;
    }

    private void initListViews(){
        Context context = AppContext.getInstance().getApplicationContext();
        rcwCalendar.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
        rcwCalendar.setLayoutManager(layoutManager);

        CalendarListAdapter adapter = new CalendarListAdapter(context);//TODO set List to load
        rcwCalendar.setAdapter(adapter);

    }
}
