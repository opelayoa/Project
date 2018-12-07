package com.tiendas3b.almacen.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.EqualsBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Es necesario que cada pinche clase que quiera usar este adapter implemente el método toSting
 * Created by dfa on 08/08/2016.
 */
@SuppressWarnings("unchecked")
public class AutoCompleteAdapter<T extends EqualsBase> extends ArrayAdapter<T> {

    private final List<T> items;
    private final List<T> itemsAll;
    private final List<T> suggestions;
//    private final Activity mContext;

    public AutoCompleteAdapter(Activity context, int resource, List<T> items) {
        super(context, resource, items);
//        this.mContext = context;
        this.items = items;
        this.itemsAll = (ArrayList<T>) ((ArrayList<T>) items).clone();
        this.suggestions = new ArrayList<>();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        T item = items.get(position);
        if (item != null) {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_item_autocomplete, parent, false);
            }
            TextView customerNameLabel = (TextView) v.findViewById(R.id.lbl);
//            if (customerNameLabel != null) {
            customerNameLabel.setText(item.toString());
//            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            T item = ((T) (resultValue));
            return item.toString();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null) {
                return new FilterResults();
            } else {
                suggestions.clear();
                for (T item : itemsAll) {
                    String alias = item.toString();
                    if (/*alias != null && */alias.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(item);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                ArrayList<T> filteredList = (ArrayList<T>) results.values;
                clear();
                for (T c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
