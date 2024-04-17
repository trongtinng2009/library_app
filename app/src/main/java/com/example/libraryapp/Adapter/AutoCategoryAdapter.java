package com.example.libraryapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Category;
import com.example.libraryapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AutoCategoryAdapter extends ArrayAdapter<Category> {
    private ArrayList<Category> categories;

    public AutoCategoryAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        categories = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cate_item_auto,parent,false);
        }
        TextView txtname = convertView.findViewById(R.id.cate_item_auto_name),
                txtid = convertView.findViewById(R.id.cate_item_auto_id);
        Category category = getItem(position);
        txtid.setText("ID: " + category.getId());
        txtname.setText(category.getName());
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Category> categorySuggest = new ArrayList<>();
                if(constraint == null || constraint.length() == 0)
                {
                    categorySuggest.addAll(categories);
                }
                else
                {
                    String filter = constraint.toString().toLowerCase().trim();
                    for(Category category : categories)
                    {
                        if(category.getId().toLowerCase().contains(filter))
                        {
                            categorySuggest.add(category);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = categorySuggest;
                filterResults.count = categorySuggest.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<Category>)results.values);
                notifyDataSetInvalidated();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Category)resultValue).getId();
            }
        };
    }
}
