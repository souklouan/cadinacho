package io.cadi.souklou.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.activity.ChildActivity;
import io.cadi.souklou.activity.RegisteredChildActivity;
import io.cadi.souklou.adapter.ChildrenAdapter;
import io.cadi.souklou.database.ParentDb;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisActivity;

/**
 * Created by HOUESSOU Narech on 09/07/2016.
 */
public class ListChildFragment extends Fragment {

    //@BindView(R.id.btnChildrenAdd)


    private ParentDb parentDb;
    private UtilisActivity utilis;
    private String typeOfLogin;
    ChildActivity childActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_children, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbarPrincipal);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //ButterKnife.bind((AppCompatActivity)getActivity());
        initListViews(view);
        //ButterKnife.bind((AppCompatActivity)getActivity());

       parentDb = new ParentDb(childActivity);
        Button btnChildrenAdd = (Button)view.findViewById(R.id.btnChildrenAdd);


           btnChildrenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                startActivity(new Intent(view.getContext(), RegisteredChildActivity.class));
            }});

        DrawerLayout drawer = (DrawerLayout)((AppCompatActivity)getActivity()).findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle((AppCompatActivity)getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbarPrincipal);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mes enfants");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(getActivity(), "Informations", Toast.LENGTH_LONG).show();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        //if (Utilis.getSharePreference(AppConstant.PREF_PARENT_ID) == null) {
            //parentDb.showParentInfoDialog();
        //}
    }

    private void initListViews(View rootView){
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.rcwChildren);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(rootView.getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        ChildrenAdapter adapter = new ChildrenAdapter(rootView.getContext());
        recyclerView.setAdapter(adapter);

    }


















}
