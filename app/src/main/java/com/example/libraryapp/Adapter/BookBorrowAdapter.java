package com.example.libraryapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.Fragment.LibrarianBookBorrowFragment;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.GetBookListener;
import com.example.libraryapp.Utils.GetLibcardListener;
import com.example.libraryapp.Utils.GetUserListener;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class BookBorrowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    ArrayList<BookInBorrow> bookInBorrows;
    ArrayList<BookInBorrow> bookInBorrowsold;
    Context context;
    int layoutid;
    FirebaseFirestore db;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                {
                    bookInBorrows = bookInBorrowsold;
                }
                else
                {
                    ArrayList<BookInBorrow> tempbook = new ArrayList<>();
                    for(BookInBorrow bookInBorrow : bookInBorrowsold)
                    {
                        bookInBorrow.libcardobjSet(new GetLibcardListener() {
                            @Override
                            public void OnComplete(Libcard libcard) {
                                if(libcard.getId().toLowerCase().contains(search.toLowerCase()))
                                {
                                    tempbook.add(bookInBorrow);
                                }
                            }
                        });
                    }
                    bookInBorrows = tempbook;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = bookInBorrows;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                bookInBorrows = (ArrayList<BookInBorrow>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public BookBorrowAdapter(ArrayList<BookInBorrow> bookInBorrows, Context context, int layoutid) {
        this.bookInBorrows = bookInBorrows;
        this.context = context;
        this.layoutid = layoutid;
        bookInBorrowsold = bookInBorrows;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        if(layoutid == R.layout.borrowingbook_item)
            return 1;
        else if(layoutid == R.layout.borrowingbooklib_item)
            return 2;
        else if(layoutid == R.layout.borrowingbook_item2)
            return 3;
        else
            return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1)
            return new BookBorrowViewHolder(LayoutInflater.from(context).inflate(layoutid,parent,false));
        else if(viewType == 2)
            return new BookBorrowView2(LayoutInflater.from(context).inflate(layoutid,parent,false));
        else if(viewType == 3)
            return new BookBorrowView3(LayoutInflater.from(context).inflate(layoutid,parent,false));
        else
            return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookInBorrow bookInBorrow = bookInBorrows.get(position);
        int pos = position;
        if(getItemViewType(position) == 1)
        {
            BookBorrowViewHolder bookBorrowViewHolder = (BookBorrowViewHolder) holder;
            if (bookInBorrow.isBookOffline())
            {
                bookInBorrow.bookOfflineobjSet(new GetBookListener() {
                    @Override
                    public void OnComplete(BookOffline bookOffline) {
                        if (bookOffline.getImg_url() != null) {
                            Picasso.get().load(bookOffline.getImg_url()).into(bookBorrowViewHolder.bookimg);
                        }
                        else
                        {
                            Picasso.get().load(bookOffline.getImg_blob()).into(bookBorrowViewHolder.bookimg);
                        }
                        bookBorrowViewHolder.booktitle.setText(bookOffline.getName());
                        bookBorrowViewHolder.borrow_date.
                                setText("Your borrow date: " + Utils.dateToString(bookInBorrow.getBorrow_date().toDate()));
                        if (bookInBorrow.isAccepted())
                        {
                            int dayleft = bookInBorrow.calDayLeft();
                            if(!bookInBorrow.isReturned())
                            {
                                if (dayleft > 0) {
                                    bookBorrowViewHolder.removebtn.setVisibility(View.GONE);
                                    bookBorrowViewHolder.requesting.setVisibility(View.GONE);
                                    bookBorrowViewHolder.expire.setVisibility(View.GONE);
                                    bookBorrowViewHolder.dayleft.setVisibility(View.VISIBLE);
                                    bookBorrowViewHolder.dayleft.setText(Integer.toString(dayleft) + " days left");
                                }
                                else
                                {
                                    bookBorrowViewHolder.removebtn.setVisibility(View.GONE);
                                    bookBorrowViewHolder.requesting.setVisibility(View.GONE);
                                    bookBorrowViewHolder.expire.setVisibility(View.VISIBLE);
                                    bookBorrowViewHolder.dayleft.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                bookBorrowViewHolder.removebtn.setVisibility(View.GONE);
                                bookBorrowViewHolder.expire.setVisibility(View.GONE);
                                bookBorrowViewHolder.dayleft.setVisibility(View.GONE);
                                bookBorrowViewHolder.requesting.setVisibility(View.GONE);
                                bookBorrowViewHolder.removebtn.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            bookBorrowViewHolder.expire.setVisibility(View.GONE);
                            bookBorrowViewHolder.dayleft.setVisibility(View.GONE);
                            bookBorrowViewHolder.requesting.setVisibility(View.VISIBLE);
                        }
                    }
                });
                bookBorrowViewHolder.removebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.collection("BookInBorrow").document(bookInBorrow.getId())
                                .delete();
                        bookInBorrows.remove(pos);
                        notifyDataSetChanged();
                    }
                });
            }
        }
        else if(getItemViewType(position) == 2)
        {
            BookBorrowView2 bookBorrowView2 = (BookBorrowView2) holder;
            bookInBorrow.bookOfflineobjSet(new GetBookListener() {
                @Override
                public void OnComplete(BookOffline bookOffline) {
                    if(bookOffline.getImg_url()!= null)
                    {
                        Picasso.get().load(bookOffline.getImg_url()).into(bookBorrowView2.imgbook);
                    }
                    else
                    {
                        Picasso.get().load(bookOffline.getImg_blob()).into(bookBorrowView2.imgbook);
                    }
                    bookBorrowView2.txtbookname.setText(bookOffline.getName());
                    bookBorrowView2.txtbookid.setText("Id book: " + bookOffline.getId());
                    DocumentReference libref = db.document(bookInBorrow.getLibcard());
                    bookBorrowView2.txtlibid.setText("Id card: " + libref.getId());
                    bookBorrowView2.txtrqdate.setText("Request date: " +
                            Utils.dateToString(bookInBorrow.getBorrow_date().toDate()));
                    if(LibrarianBookBorrowFragment.optionselect == 2) {
                        bookBorrowView2.txtrqdate.setText("Borrow date: " +
                                Utils.dateToString(bookInBorrow.getBorrow_date().toDate()));
                        bookBorrowView2.verifybtn.setText("Return");
                    }
                    bookInBorrow.libcardobjSet(new GetLibcardListener() {
                        @Override
                        public void OnComplete(Libcard libcard) {
                            libcard.userobjSet(new GetUserListener() {
                                @Override
                                public void OnComplete(User user) {
                                    try {
                                        byte[] encodeByte = Base64.decode(user.getAvatar() ,Base64.DEFAULT);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                                                encodeByte.length);
                                        bookBorrowView2.imglib.setImageBitmap(bitmap);
                                    } catch (Exception e) {
                                        e.getMessage();
                                    }
                                }
                            });
                        }
                    });
                    bookBorrowView2.verifybtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(LibrarianBookBorrowFragment.optionselect == 1)
                                bookBorrowView2.setPopupVerify(bookInBorrow,bookOffline,pos);
                            else if(LibrarianBookBorrowFragment.optionselect == 2)
                                bookBorrowView2.setPopupReturn(bookInBorrow,bookOffline,pos);
                        }
                    });
                }
            });
        }
        else
        {
            BookBorrowView3 bookBorrowView3 = (BookBorrowView3) holder;
            bookInBorrow.libcardobjSet(new GetLibcardListener() {
                @Override
                public void OnComplete(Libcard libcard) {
                    bookBorrowView3.txtcardid.setText("ID thẻ: "+libcard.getId());
                    bookBorrowView3.txtcardname.setText(libcard.getFirstname() + " " + libcard.getLastname());
                    try {
                        byte[] encodeByte = Base64.decode(libcard.getImage_url() ,Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                                encodeByte.length);
                        bookBorrowView3.img.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });
            bookInBorrow.bookOfflineobjSet(new GetBookListener() {
                @Override
                public void OnComplete(BookOffline bookOffline) {
                    bookBorrowView3.txtbookid.setText("ID sách: " +bookOffline.getName());
                }
            });
            bookBorrowView3.txtborrowdate.setText("Ngày mượn: " +Utils.dateToString(bookInBorrow.getBorrow_date().toDate()));
            bookBorrowView3.txtduedate.setText("Ngày trả: " +Utils.dateToString(bookInBorrow.getDue_date().toDate()));
        }
    }

    @Override
    public int getItemCount() {
        return bookInBorrows.size();
    }

    public class BookBorrowViewHolder extends RecyclerView.ViewHolder {
        ImageView bookimg;
        TextView booktitle,expire,dayleft,requesting,borrow_date;
        ImageButton removebtn;
        public BookBorrowViewHolder(@NonNull View itemView) {
            super(itemView);
            bookimg = itemView.findViewById(R.id.borrowingbook_img);
            booktitle = itemView.findViewById(R.id.borrowingbook_txtname);
            expire = itemView.findViewById(R.id.borrowingbook_due);
            dayleft = itemView.findViewById(R.id.borrowingbook_leftday);
            requesting = itemView.findViewById(R.id.borrowingbook_waiting);
            borrow_date = itemView.findViewById(R.id.borrowingbook_txtborrowdate);
            removebtn = itemView.findViewById(R.id.borrowingbook_removebtn);
        }
    }
    public class BookBorrowView2 extends RecyclerView.ViewHolder
    {
        Button verifybtn;
        TextView txtbookname,txtlibid,txtbookid,txtrqdate;
        ImageView imgbook,imglib;
        public BookBorrowView2(@NonNull View itemView) {
            super(itemView);
            txtbookid = itemView.findViewById(R.id.borrowingbooklib_idbook);
            txtbookname = itemView.findViewById(R.id.borrowingbooklib_bookname);
            txtlibid = itemView.findViewById(R.id.borrowingbooklib_idcard);
            txtrqdate = itemView.findViewById(R.id.borrowingbooklib_rqdate);
            imgbook = itemView.findViewById(R.id.borrowingbooklib_img);
            imglib = itemView.findViewById(R.id.borrowingbooklib_libimg);
            verifybtn = itemView.findViewById(R.id.borrowingbooklib_verifybtn);
        }
        public void setPopupVerify(BookInBorrow bookInBorrow,BookOffline bookOffline,int pos)
        {
            Dialog dialog = Utils.getPopup(context,R.layout.alert_popup);
            TextView title = dialog.findViewById(R.id.alert_popup_title),
                    maintxt = dialog.findViewById(R.id.alert_popup_maintxt);
            ImageView img = dialog.findViewById(R.id.alert_popup_img);
            Button igbtn = dialog.findViewById(R.id.alert_popup_igbtn),
                    accbtn = dialog.findViewById(R.id.alert_popup_accbtn);
            title.setText("XÁC NHẬN YÊU CẦU MƯỢN SÁCH");
            maintxt.setText("Bạn đồng ý xác nhận yêu cầu này chứ ?");
            img.setImageResource(context.getResources().getIdentifier("kitty3","drawable",context.getPackageName()));
            accbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int due_date = context.getResources().getInteger(R.integer.due_date);
                    MainActivity.db.collection("BookInBorrow")
                            .document(bookInBorrow.getId()).
                            update(
                                   "borrow_date",Timestamp.now(),
                                   "accepted",true);
                    MainActivity.db.collection("BookInBorrow")
                                    .document(bookInBorrow.getId())
                                            .update("due_date",bookInBorrow.calDuedate(due_date));
                    bookInBorrows.remove(pos);
                    notifyDataSetChanged();
                    MainActivity.db.collection("BookOffline").document(bookOffline.getId())
                            .update("remain_quantity",bookOffline.getRemain_quantity() - bookInBorrow.getQuantity());
                    dialog.dismiss();
                }
            });
            igbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        public void setPopupReturn(BookInBorrow bookInBorrow,BookOffline bookOffline,int pos)
        {
            Dialog dialog = Utils.getPopup(context,R.layout.alert_popup);
            TextView title = dialog.findViewById(R.id.alert_popup_title),
                    maintxt = dialog.findViewById(R.id.alert_popup_maintxt);
            ImageView img = dialog.findViewById(R.id.alert_popup_img);
            Button igbtn = dialog.findViewById(R.id.alert_popup_igbtn),
                    accbtn = dialog.findViewById(R.id.alert_popup_accbtn);
            title.setText("XÁC NHẬN KHÁCH ĐÃ TRẢ SÁCH");
            maintxt.setText("Đồng ý xác nhận khách đã trả sách ?");
            img.setImageResource(context.getResources().getIdentifier("kitty3","drawable",context.getPackageName()));
            accbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int due_date = context.getResources().getInteger(R.integer.due_date);
                    MainActivity.db.collection("BookInBorrow")
                            .document(bookInBorrow.getId()).
                            update("return_date", Timestamp.now(),
                                    "returned",true);
                    bookInBorrows.remove(pos);
                    notifyDataSetChanged();
                    MainActivity.db.collection("BookOffline").document(bookOffline.getId())
                            .update("remain_quantity",bookOffline.getRemain_quantity() + bookInBorrow.getQuantity());
                    dialog.dismiss();
                }
            });
            igbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
    public class BookBorrowView3 extends RecyclerView.ViewHolder
    {
        TextView txtcardname,txtcardid,txtbookid,txtborrowdate
                ,txtduedate;
        ImageView img;
        public BookBorrowView3(@NonNull View itemView) {
            super(itemView);
            txtbookid = itemView.findViewById(R.id.borrowingbook2_txtbookid);
            txtborrowdate = itemView.findViewById(R.id.borrowingbook2_txtborrowdate);
            txtduedate = itemView.findViewById(R.id.borrowingbook2_txtduedate);
            txtcardname = itemView.findViewById(R.id.borrowingbook2_txtname);
            txtcardid = itemView.findViewById(R.id.borrowingbook2_txtlibid);
            img = itemView.findViewById(R.id.borrowingbook2_img);
        }
    }
}
