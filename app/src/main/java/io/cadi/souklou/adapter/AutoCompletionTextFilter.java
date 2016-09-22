package io.cadi.souklou.adapter;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import io.cadi.souklou.models.Classroom;
import io.cadi.souklou.models.School;

/**
 * Created by arcadius on 23/08/16.
 */
public class AutoCompletionTextFilter extends Filter{

    AutoCompletionTextAdapter adapter;
    ArrayList<String> originalList;
    ArrayList<String> filteredList;

    public AutoCompletionTextFilter(AutoCompletionTextAdapter  adapter, ArrayList<String> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList = originalList;
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();



            // Your filtering logic goes in here
            for (final String t : originalList) {
                    if (t.toLowerCase().contains(filterPattern)) {
                        filteredList.add(t);
                    }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.filteredObjects.clear();
        adapter.filteredObjects.addAll((ArrayList<String>)results.values);
        adapter.notifyDataSetChanged();
    }
}
