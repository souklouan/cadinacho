package io.cadi.souklou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.R;
import io.cadi.souklou.adapter.CalendarPageAdapter;
import io.cadi.souklou.fragment.CalendarFragment;

/**
 * Created by arcadius on 20/08/16.
 */
public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.tabCalendar) TabLayout tabCalendar;
    @BindView(R.id.vwpCalendar)ViewPager vwpCalendar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ButterKnife.bind(this);
        setupViewPager(vwpCalendar);
        tabCalendar.setupWithViewPager(vwpCalendar);
    }

    private void setupViewPager(ViewPager viewPager) {
        CalendarPageAdapter adapter = new CalendarPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new CalendarFragment(), "Lun");
        adapter.addFragment(new CalendarFragment(), "Mar");
        adapter.addFragment(new CalendarFragment(), "Mer");
        adapter.addFragment(new CalendarFragment(), "Jeu");
        adapter.addFragment(new CalendarFragment(), "Ven");
        viewPager.setAdapter(adapter);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
