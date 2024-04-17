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

import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.Model.Notification;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;

import java.util.ArrayList;
import java.util.List;

public class AutoUserAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    public AutoUserAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        users = new ArrayList<>(objects);
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
        User user = getItem(position);
        txtid.setText("User ID: " + user.getId());
        txtname.setText(user.getUsername());
        try {
            byte[] encodeByte = Base64.decode(user.getAvatar(), Base64.DEFAULT);
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
                ArrayList<User> userSuggest = new ArrayList<>();
                if(constraint == null || constraint.length() == 0)
                {
                    userSuggest.addAll(users);
                }
                else
                {
                    String filter = constraint.toString().toLowerCase().trim();
                    for(User user : users)
                    {
                        if(user.getId().toLowerCase().contains(filter))
                        {
                            userSuggest.add(user);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = userSuggest;
                filterResults.count = userSuggest.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<User>)results.values);
                notifyDataSetInvalidated();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((User)resultValue).getId();
            }
        };
    }
}
