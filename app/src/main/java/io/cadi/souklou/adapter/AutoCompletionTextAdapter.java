package io.cadi.souklou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.cadi.souklou.R;

/**
 * Created by arcadius on 23/08/16.
 */
public class AutoCompletionTextAdapter<T> extends ArrayAdapter<T> {

    private final List<T> objects;
    public List<T> filteredObjects = new ArrayList<>();

    public AutoCompletionTextAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return filteredObjects.size();
    }

    @Override
    public Filter getFilter() {
        return new AutoCompletionTextFilter<T>(this, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item from filtered list.
        T object = filteredObjects.get(position);

        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //view = inflater.inflate(R.layout.row_dog, parent, false);

        return convertView;
    }


}
