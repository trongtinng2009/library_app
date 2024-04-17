package com.example.libraryapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Layout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Fragment.LibrarianCardDetailConfirmFragment;
import com.example.libraryapp.Fragment.LibrarianConfirmCardFragment;
import com.example.libraryapp.Fragment.LibrarianLibcardDetailFragment;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.GetUserListener;
import com.example.libraryapp.Utils.Utils;

import java.util.ArrayList;
import java.util.Date;

public class LibcardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    int layoutid;
    ArrayList<Libcard> libcards;

    public LibcardAdapter(Context context, int layoutid, ArrayList<Libcard> libcards) {
        this.context = context;
        this.layoutid = layoutid;
        this.libcards = libcards;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutid,parent,false);
        if(viewType == 1)
            return new LibcardHolder(view);
        else
            return new LibcardHolder2(view);
    }

    @Override
    public int getItemViewType(int position) {
        if(layoutid == R.layout.libcard_request_item)
            return 1;
        else
            return 2;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Libcard libcard = libcards.get(position);
        if(getItemViewType(position) == 1)
        {
            LibcardHolder libcardHolder = (LibcardHolder) holder;
            try {
                byte[] encodeByte = Base64.decode(libcard.getImage_url(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                        encodeByte.length);
                libcardHolder.libcardimg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.getMessage();
            }
            Date request_date = libcard.getRequest_date().toDate();
            libcardHolder.txtdate.setText("Ngày yêu cầu: " +Utils.dateToString(request_date));
            libcardHolder.txtname.setText(libcard.getFirstname() + " " + libcard.getLastname());
            libcardHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LibrarianCardDetailConfirmFragment librarianConfirmCardFragment = new LibrarianCardDetailConfirmFragment();
                    Bundle b = new Bundle();
                    b.putParcelable("libcard", libcard);
                    librarianConfirmCardFragment.setArguments(b);
                    FragmentTransaction transaction = LibrarianActivity.fragmentManager2.beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                    transaction.replace(R.id.libact_fragmentcontainer, librarianConfirmCardFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
        else
        {
            LibcardHolder2 libcardHolder2 = (LibcardHolder2) holder;
            try {
                byte[] encodeByte = Base64.decode(libcard.getImage_url(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                        encodeByte.length);
                libcardHolder2.libcardimg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.getMessage();
            }
            libcardHolder2.txtdate.setText("Ngày xác nhận: " +Utils.dateToString(libcard.getAcc_date().toDate()));
            libcardHolder2.txtname.setText(libcard.getFirstname() + " " + libcard.getLastname());
            libcardHolder2.txtid.setText("ID: " + libcard.getId());
            libcardHolder2.setClickView(libcard);
        }
    }

    @Override
    public int getItemCount() {
        return libcards.size();
    }

    public class LibcardHolder extends RecyclerView.ViewHolder
    {
        LinearLayout layout;
        ImageView libcardimg;
        TextView txtname,txtdate;
        Button btnacc,btnig;
        public LibcardHolder(@NonNull View itemView) {
            super(itemView);
            libcardimg = itemView.findViewById(R.id.libcard_request_libcardimg);
            txtname = itemView.findViewById(R.id.libcard_request_libcardname);
            txtdate = itemView.findViewById(R.id.libcard_request_libcarddate);
            btnacc = itemView.findViewById(R.id.libcard_request_libcardaccbtn);
            btnig = itemView.findViewById(R.id.libcard_request_libcardigbtn);
            layout = itemView.findViewById(R.id.libcard_request_layout);
        }
    }
    public class LibcardHolder2 extends RecyclerView.ViewHolder
    {
        ImageView libcardimg;
        TextView txtname,txtdate,txtid;
        Button btnview;
        public LibcardHolder2(@NonNull View itemView) {
            super(itemView);
            libcardimg = itemView.findViewById(R.id.libcard_img);
            txtname = itemView.findViewById(R.id.libcard_name);
            txtdate = itemView.findViewById(R.id.libcard_adddate);
            txtid = itemView.findViewById(R.id.libcard_id);
            btnview = itemView.findViewById(R.id.libcard_viewbtn);
        }
        public void setClickView(Libcard libcard)
        {
            btnview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LibrarianLibcardDetailFragment librarianLibcardDetailFragment =new LibrarianLibcardDetailFragment();
                    Bundle b = new Bundle();
                    b.putParcelable("libcard",libcard);
                    librarianLibcardDetailFragment.setArguments(b);
                    FragmentTransaction transaction = LibrarianActivity.fragmentManager2.beginTransaction();
                    transaction.replace(R.id.libact_fragmentcontainer,librarianLibcardDetailFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }
}
