package com.example.libraryapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import com.example.libraryapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AutoBookAdapter extends ArrayAdapter<BookOffline> {
    private ArrayList<BookOffline> bookOfflines;
    public AutoBookAdapter(@NonNull Context context, int resource, @NonNull List<BookOffline> objects) {
        super(context, resource, objects);
        bookOfflines = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item_for_auto,parent,false);
        }
        TextView txtname = convertView.findViewById(R.id.book_itemauto_name),
                txtid = convertView.findViewById(R.id.book_itemauto_id);
        ImageView img = convertView.findViewById(R.id.book_itemauto_img);
        BookOffline bookOffline = getItem(position);
        txtid.setText("Book ID: " + bookOffline.getId());
        txtname.setText(bookOffline.getName());
        if(bookOffline.getImg_url() != null)
        {
            Picasso.get().load(bookOffline.getImg_url()).into(img);
        }
        else
        {
            Picasso.get().load(bookOffline.getImg_blob()).into(img);
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<BookOffline> bookOfflinesSuggest = new ArrayList<>();
                if(constraint == null || constraint.length() == 0)
                {
                    bookOfflinesSuggest.addAll(bookOfflines);
                }
                else
                {
                    String filter = constraint.toString().toLowerCase().trim();
                    for(BookOffline bookOffline : bookOfflines)
                    {
                        if(bookOffline.getId().toLowerCase().contains(filter))
                        {
                            bookOfflinesSuggest.add(bookOffline);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = bookOfflinesSuggest;
                filterResults.count = bookOfflinesSuggest.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<BookOffline>)results.values);
                notifyDataSetInvalidated();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((BookOffline)resultValue).getId();
            }
        };
    }
}
