package io.cadi.souklou.adapter;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import io.cadi.souklou.models.Classroom;
import io.cadi.souklou.models.School;

/**
 * Created by arcadius on 23/08/16.
 */
public class AutoCompletionTextFilter<T> extends Filter{

    AutoCompletionTextAdapter<T> adapter;
    List<T> originalList;
    List<T> filteredList;

    public AutoCompletionTextFilter(AutoCompletionTextAdapter<T>  adapter, List<T> originalList) {
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
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();



            // Your filtering logic goes in here
            for (final T t : originalList) {
                if (t instanceof School ){
                    if (((School) t).getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(t);
                    }
                }
                if (t instanceof Classroom ){
                    if (((Classroom) t).getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(t);
                    }
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
        adapter.filteredObjects.addAll((List) results.values);
        adapter.notifyDataSetChanged();
    }
}
