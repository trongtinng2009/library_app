package com.example.libraryapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.Model.Comment;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    int layoutid;
    ArrayList<Comment> comments;

    public CommentAdapter(Context context, int layoutid, ArrayList<Comment> comments) {
        this.context = context;
        this.layoutid = layoutid;
        this.comments = comments;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(context).inflate(layoutid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        CommentHolder commentHolder = (CommentHolder) holder;
        commentHolder.user.setText(comment.getUser().getLoginname());
        commentHolder.content.setText(comment.getContent());
        commentHolder.adddate.setText("Đăng vào: " + Utils.dateToString(comment.getAdd_date().toDate()));
        try {
            byte[] encodeByte = Base64.decode(comment.getUser().getAvatar(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            commentHolder.img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder
    {
        TextView adddate,content,user;
        ImageView img;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            img =itemView.findViewById(R.id.comment_item_img);
            adddate = itemView.findViewById(R.id.comment_item_adddate);
            content = itemView.findViewById(R.id.comment_item_content);
            user = itemView.findViewById(R.id.comment_item_user);
        }
    }
}
