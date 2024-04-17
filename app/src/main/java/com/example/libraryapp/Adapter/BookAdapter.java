package com.example.libraryapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.Activity.AdminActivity;
import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Activity.WMActivity;
import com.example.libraryapp.Fragment.GuestBookDetailFragment;
import com.example.libraryapp.Fragment.LibBookDetailConfirmFragment;
import com.example.libraryapp.Fragment.LibrarianBookDetailFragment;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<BookOffline> bookOfflinesOld;
    private ArrayList<BookOffline> bookOfflines;
    private Context context;
    private int layoutid;
    private boolean enableClick = true;
    private FirebaseFirestore db;
    private boolean isGuest = true;
    private int searchoption = 1;

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public void setSearchoption(int searchoption) {
        this.searchoption = searchoption;
    }

    public BookAdapter(ArrayList<BookOffline> bookOfflines, Context context, int layoutid, boolean enableClick) {
        this.bookOfflines = bookOfflines;
        this.bookOfflinesOld = bookOfflines;
        this.context = context;
        this.layoutid = layoutid;
        this.enableClick = enableClick;
        db = FirebaseFirestore.getInstance();

    }

    public BookAdapter(ArrayList<BookOffline> bookOfflines, Context context, int layoutid,int searchoption) {
        this.bookOfflines = bookOfflines;
        this.bookOfflinesOld = bookOfflines;
        this.context = context;
        this.layoutid = layoutid;
        this.searchoption = searchoption;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutid, parent, false);
        if(viewType == 1) {
            return new BookViewHolder(view);
        }
        else if(viewType ==2)
        {
            return new BookViewHolder2(view);
        }
        else if(viewType == 3)
            return new BookViewHolder3(view);
        else
            return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookOffline book = bookOfflines.get(position);
        if(getItemViewType(position) == 1)
        {
            BookViewHolder bookViewHolder = (BookViewHolder) holder;
            if (book.getImg_url() != null) {
                Picasso.get().load(book.getImg_url()).into(bookViewHolder.bookimg);
            } else if (book.getImg_blob() != null) {
                Picasso.get().load(book.getImg_blob()).into(bookViewHolder.bookimg);
            }
            bookViewHolder.booktitle.setText(book.getName());
            if(book.getRemain_quantity() <= 0)
                bookViewHolder.bookremain.setText("Hết sách");
            if(enableClick == false)
            {
                bookViewHolder.bookdetailbtn.setVisibility(View.GONE);
            }
            else
            {
            bookViewHolder.setClickDetail(book);}
        }

        else if(getItemViewType(position) == 2)
        {
            BookViewHolder2 bookViewHolder2 = (BookViewHolder2) holder;
            if (book.getImg_url() != null) {
                Picasso.get().load(book.getImg_url()).into(bookViewHolder2.img);
            } else if (book.getImg_blob() != null) {
                Picasso.get().load(book.getImg_blob()).into(bookViewHolder2.img);
            }
            bookViewHolder2.txtquantity.setText("Số lượng: "+Integer.toString(book.getQuantity()));
            bookViewHolder2.txtname.setText(book.getName());
            if(book.getAuthor().length() != 0)
                bookViewHolder2.txtauthor.setText("Tác giả: "+book.getAuthor());
            else
                bookViewHolder2.txtauthor.setText("Tác giả: Đang cập nhật");
            bookViewHolder2.txtadddate.setText("Thêm vào: " + Utils.dateToString(book.getAdd_date().toDate()));
            bookViewHolder2.btndetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putParcelable("book",book);
                    LibBookDetailConfirmFragment libBookDetailConfirmFragment = new LibBookDetailConfirmFragment();
                    libBookDetailConfirmFragment.setArguments(b);
                    FragmentTransaction transaction = LibrarianActivity.fragmentManager.beginTransaction();
                    transaction.replace(R.id.libact_fragmentcontainer,libBookDetailConfirmFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
        else if(getItemViewType(position) == 3)
        {
             BookViewHolder3 bookViewHolder3 = (BookViewHolder3) holder;
             bookViewHolder3.txtname.setText(book.getName());
            if (book.getImg_url() != null) {
                Picasso.get().load(book.getImg_url()).into(bookViewHolder3.img);
            } else if (book.getImg_blob() != null) {
                Picasso.get().load(book.getImg_blob()).into(bookViewHolder3.img);
            }
             if(book.getAuthor().length() != 0)
                bookViewHolder3.txtauthor.setText("Tác giả: " +book.getAuthor());
             else
                bookViewHolder3.txtauthor.setText("Tác giả: Đang cập nhật");
             if(book.getRemain_quantity() <= 0)
                 bookViewHolder3.txtavailable.setText("Hết sách");
             bookViewHolder3.setClick(book);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(layoutid == R.layout.book_item)
            return 1;
        else if(layoutid == R.layout.book_item_confirm)
            return 2;
        else if (layoutid == R.layout.book_item2)
            return 3;
        else
            return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                {
                    bookOfflines = bookOfflinesOld;
                }
                else
                {
                    ArrayList<BookOffline> tempbook = new ArrayList<>();
                    for(BookOffline bookOffline : bookOfflinesOld)
                    {
                        if(searchoption == 1)
                        {
                            if(bookOffline.getName().toLowerCase().contains(search.toLowerCase()))
                                tempbook.add(bookOffline);
                        }
                        else if(searchoption == 2)
                        {
                            if(bookOffline.getAuthor().toLowerCase().contains(search.toLowerCase()))
                                tempbook.add(bookOffline);
                        }
                        else if (searchoption == 3)
                        {
                            if(bookOffline.getId().toLowerCase().contains(search.toLowerCase()))
                                tempbook.add(bookOffline);
                        }
                    }
                    bookOfflines = tempbook;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = bookOfflines;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                bookOfflines = (ArrayList<BookOffline>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return bookOfflines.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageButton bookdetailbtn;
        ImageView bookimg;
        TextView booktitle, bookrating,bookremain;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookimg = itemView.findViewById(R.id.book_item_bookimg);
            bookrating = itemView.findViewById(R.id.book_item_bookrating);
            booktitle = itemView.findViewById(R.id.book_item_booktitle);
            bookdetailbtn = itemView.findViewById(R.id.book_item_detailbtn);
            bookremain = itemView.findViewById(R.id.book_item_remaintext);
        }
        public void setClickDetail(BookOffline book)
        {
            bookdetailbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putParcelable("book",book);
                    db.collection("BookOffline").document(book.getId())
                            .update("view_point",book.getView_point() + 1);
                    GuestBookDetailFragment guestBookDetailFragment = new GuestBookDetailFragment();
                    FragmentTransaction trans = MainActivity.fragmentManager.beginTransaction();
                    guestBookDetailFragment.setArguments(b);
                    trans.replace(R.id.mainact_fragmentcontainer, guestBookDetailFragment);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            });
        }
    }
    public class BookViewHolder2 extends RecyclerView.ViewHolder
    {
        TextView txtname,txtauthor,txtquantity,txtadddate;
        ImageView img;
        Button btndetail;
        public BookViewHolder2(@NonNull View itemView) {
            super(itemView);
            txtadddate = itemView.findViewById(R.id.bookconfirm_adddate);
            txtauthor = itemView.findViewById(R.id.bookconfirm_author);
            txtname = itemView.findViewById(R.id.bookconfirm_bookname);
            txtquantity = itemView.findViewById(R.id.bookconfirm_quantitybook);
            img = itemView.findViewById(R.id.bookconfirm_img);
            btndetail = itemView.findViewById(R.id.bookconfirm_detailbtn);
        }
    }
    public class BookViewHolder3 extends RecyclerView.ViewHolder
    {
        CardView layout;
        TextView txtname,txtauthor,txtavailable;
        ImageView img;
        public BookViewHolder3(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.book_item2_img);
            txtauthor = itemView.findViewById(R.id.book_item2_author);
            txtname = itemView.findViewById(R.id.book_item2_name);
            txtavailable = itemView.findViewById(R.id.book_item2_isavailable);
            layout = itemView.findViewById(R.id.book_item2_layout);
        }
        public void setClick(BookOffline book)
        {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putParcelable("book",book);
                    if(MainActivity.user != null) {
                        GuestBookDetailFragment guestBookDetailFragment = new GuestBookDetailFragment();
                        FragmentTransaction trans = MainActivity.fragmentManager.beginTransaction();
                        guestBookDetailFragment.setArguments(b);
                        db.collection("BookOffline").document(book.getId())
                                .update("view_point",book.getView_point() + 1);
                        trans.replace(R.id.mainact_fragmentcontainer, guestBookDetailFragment);
                        trans.addToBackStack(null);
                        trans.commit();
                    }
                    else if(LibrarianActivity.user != null)
                    {
                        LibrarianBookDetailFragment librarianBookDetailFragment = new LibrarianBookDetailFragment();
                        librarianBookDetailFragment.setArguments(b);
                        FragmentTransaction transaction = LibrarianActivity.fragmentManager.beginTransaction();
                        transaction.replace(R.id.libact_fragmentcontainer,librarianBookDetailFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else if(AdminActivity.user != null)
                    {
                        LibrarianBookDetailFragment librarianBookDetailFragment = new LibrarianBookDetailFragment();
                        librarianBookDetailFragment.setArguments(b);
                        FragmentTransaction transaction = AdminActivity.fragmentManager2.beginTransaction();
                        transaction.replace(R.id.adminact_fragmentcontainer,librarianBookDetailFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else
                    {
                        GuestBookDetailFragment guestBookDetailFragment = new GuestBookDetailFragment();
                        FragmentTransaction trans = MainActivity.fragmentManager.beginTransaction();
                        guestBookDetailFragment.setArguments(b);
                        db.collection("BookOffline").document(book.getId())
                                .update("view_point",book.getView_point() + 1);
                        trans.replace(R.id.mainact_fragmentcontainer, guestBookDetailFragment);
                        trans.addToBackStack(null);
                        trans.commit();
                    }
                }
            });
        }
    }
}
