package io.cadi.souklou.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cadi.souklou.AppConstant;
import io.cadi.souklou.R;
import io.cadi.souklou.activity.ChildActivity;
import io.cadi.souklou.activity.RegisteredChildActivity;
import io.cadi.souklou.adapter.ChildrenAdapter;
import io.cadi.souklou.database.DbConstant;
import io.cadi.souklou.database.ParentDb;
import io.cadi.souklou.models.Children;
import io.cadi.souklou.utilitaire.TransparentLoading;
import io.cadi.souklou.utilitaire.Utilis;
import io.cadi.souklou.utilitaire.UtilisActivity;

/**
 * Created by HOUESSOU Narech on 09/07/2016.
 */
public class ListChildFragment extends Fragment {
    static RecyclerView recyclerView;

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
        recyclerView = (RecyclerView)view.findViewById(R.id.rcwChildren);
        initListViews(view);


        FloatingActionButton fabChildrenAdd = (FloatingActionButton)view.findViewById(R.id.fabChildrenAdd);
        fabChildrenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), RegisteredChildActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout)((AppCompatActivity)getActivity()).findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle((AppCompatActivity)getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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
    }

    private void initListViews(final View rootView){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(rootView.getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        final List<Children> list = new ArrayList<Children>();
        String parentKey = Utilis.getSharePreference(AppConstant.PREF_PARENT_ID);
        DatabaseReference refChildren = DbConstant.FIREBASE_DB.child(DbConstant.TABLE_PARENT+"/"+parentKey+"/"+DbConstant.TABLE_CHILDREN);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressLoadingChildren);
        progressBar.setVisibility(View.VISIBLE);
        refChildren.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Children children = d.getValue(Children.class);
                    children.setKey(d.getKey());
                    Log.e("voirrr", children.getKey()+"et"+children.getFirstName());
                    list.add(children);
                }
                progressBar.setVisibility(View.GONE);
                ChildrenAdapter adapter = new ChildrenAdapter(rootView.getContext(),list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


















}
