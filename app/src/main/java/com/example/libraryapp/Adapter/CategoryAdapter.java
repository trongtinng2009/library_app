package com.example.libraryapp.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.Fragment.GuestCategoryViewFragment;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Category;
import com.example.libraryapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Category> categories;
    private int layoutid;
    private FirebaseFirestore db;

    public CategoryAdapter(Context context, ArrayList<Category> categories, int layoutid) {
        this.context = context;
        this.categories = categories;
        this.layoutid = layoutid;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutid,parent,false);
        if(viewType == 1)
            return new ViewHolder(view);
        else
            return new CateViewHolder2(view);
    }

    @Override
    public int getItemViewType(int position) {
        if(layoutid == R.layout.cate_item)
            return 1;
        else if(layoutid == R.layout.cate_item2)
            return 2;
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Category category = categories.get(position);
        if(getItemViewType(position) == 1) {
            ViewHolder viewHolder = (ViewHolder) holder;
            int[] bgcolor = {R.color.pas_cream, R.color.pas_pinkpur, R.color.pas_orange
                    , R.color.pas_yellow, R.color.pas_pink, R.color.pas_green};
            int imgid = context.getResources().getIdentifier(category.getImg(), "drawable",
                    context.getPackageName());

            viewHolder.cate_img.setImageResource(imgid);
            viewHolder.cate_txt.setText(category.getName());
            viewHolder.cate_bg.setBackgroundTintList(ContextCompat.getColorStateList(context, bgcolor[position]));
            viewHolder.setClick(category);
        }
        else
        {
            CateViewHolder2 cateViewHolder2 = (CateViewHolder2) holder;
            int[] bgcolor = {R.color.pas_cream, R.color.pas_pinkpur, R.color.pas_orange
                    , R.color.pas_yellow, R.color.pas_pink, R.color.pas_green,R.color.pas_redbrown
            ,R.color.pas_red,R.color.medium_blue};
            int imgid = context.getResources().getIdentifier(category.getImg(), "drawable",
                    context.getPackageName());
            cateViewHolder2.img.setImageResource(imgid);
            cateViewHolder2.txt.setText(category.getName());
            Random random = new Random();
            int randoncolor = random.nextInt(bgcolor.length);
            cateViewHolder2.layout.setBackgroundTintList(ContextCompat.getColorStateList(context, bgcolor[randoncolor]));
            cateViewHolder2.setClick(category);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private FrameLayout cate_bg;
        private ImageView cate_img;
        private TextView cate_txt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cate_bg = itemView.findViewById(R.id.cate_item_bg);
            cate_img = itemView.findViewById(R.id.cate_item_img);
            cate_txt = itemView.findViewById(R.id.cate_item_txt);
        }
        public void setClick(Category category)
        {
            cate_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GuestCategoryViewFragment guestCategoryViewFragment = new GuestCategoryViewFragment();
                    Bundle b = new Bundle();
                    b.putSerializable("cate",category);
                    db.collection("Category").document(category.getId())
                            .update("view",category.getView() + 1);
                    guestCategoryViewFragment.setArguments(b);
                    FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                    transaction.replace(R.id.mainact_fragmentcontainer,guestCategoryViewFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }
    public class CateViewHolder2 extends RecyclerView.ViewHolder
    {
        LinearLayout layout;
        TextView txt;
        ImageView img;
        public CateViewHolder2(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.cate_item2_layout);
            txt = itemView.findViewById(R.id.cate_item2_txt);
            img = itemView.findViewById(R.id.cate_item2_img);
        }
        public void setClick(Category category)
        {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GuestCategoryViewFragment guestCategoryViewFragment = new GuestCategoryViewFragment();
                    Bundle b = new Bundle();
                    b.putSerializable("cate",category);
                    db.collection("Category").document(category.getId())
                            .update("view",category.getView() + 1);
                    guestCategoryViewFragment.setArguments(b);
                    FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                    transaction.replace(R.id.mainact_fragmentcontainer,guestCategoryViewFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }
}
