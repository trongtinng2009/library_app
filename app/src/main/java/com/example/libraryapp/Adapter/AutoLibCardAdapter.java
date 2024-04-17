package com.example.libraryapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
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
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AutoLibCardAdapter extends ArrayAdapter<Libcard> {
    private ArrayList<Libcard> libcards;
    public AutoLibCardAdapter(@NonNull Context context, int resource, @NonNull List<Libcard> objects) {
        super(context, resource, objects);
        libcards = new ArrayList<>(objects);
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
        Libcard libcard = getItem(position);
        txtid.setText("Library Card ID: " + libcard.getId());
        txtname.setText(libcard.getFirstname() + " " + libcard.getLastname());
        try {
            byte[] encodeByte = Base64.decode(libcard.getImage_url(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Libcard> libcardSuggest = new ArrayList<>();
                if(constraint == null || constraint.length() == 0)
                {
                    libcardSuggest.addAll(libcards);
                }
                else
                {
                    String filter = constraint.toString().toLowerCase().trim();
                    for(Libcard libcard : libcards)
                    {
                        if(libcard.getId().toLowerCase().contains(filter))
                        {
                            libcardSuggest.add(libcard);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = libcardSuggest;
                filterResults.count = libcardSuggest.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<Libcard>)results.values);
                notifyDataSetInvalidated();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Libcard)resultValue).getId();
            }
        };
    }
}
