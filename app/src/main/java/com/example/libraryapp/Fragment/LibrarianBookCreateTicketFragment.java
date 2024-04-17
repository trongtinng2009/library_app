package com.example.libraryapp.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Adapter.AutoBookAdapter;
import com.example.libraryapp.Adapter.AutoLibCardAdapter;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.GetBookListener;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianBookCreateTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianBookCreateTicketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView libidedt,bookidedt;
    private EditText borrowdatedt;
    private Button btncancel,btnacc;
    private LinearLayout layoutinfo;
    private TextView txtbook;
    private ImageView imgbook;
    ImageButton backbtn;
    int bookBorrowInWeek;
    private ArrayList<BookOffline> bookOfflines;
    private ArrayList<Libcard> libcards;
    FirebaseFirestore db;
    boolean valid;

    public LibrarianBookCreateTicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianBookCreateTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianBookCreateTicketFragment newInstance(String param1, String param2) {
        LibrarianBookCreateTicketFragment fragment = new LibrarianBookCreateTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_librarian_book_create_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setDataForBookID();
        setDataForLibID();
        setOnChangeBookID();
        setBtn();
        valid = true;
    }
    private void initView(View view)
    {
        bookBorrowInWeek = 0;
        libidedt = view.findViewById(R.id.fraglibbookcreateticket_libidauto);
        bookidedt = view.findViewById(R.id.fraglibbookcreateticket_bookidauto);
        borrowdatedt = view.findViewById(R.id.fraglibbookcreateticket_borrowdateedt);
        btnacc = view.findViewById(R.id.fraglibbookcreateticket_btncreate);
        btncancel = view.findViewById(R.id.fraglibbookcreateticket_btncancel);
        layoutinfo = view.findViewById(R.id.fraglibbookcreateticket_bookinfolayout);
        imgbook = view.findViewById(R.id.fraglibbookcreateticket_bookimg);
        txtbook = view.findViewById(R.id.fraglibbookcreateticket_bookname);
        backbtn = view.findViewById(R.id.fraglibbookcreateticket_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
        borrowdatedt.setText(Utils.dateToString(Timestamp.now().toDate()));
        bookOfflines = new ArrayList<>();
        libcards = new ArrayList<>();
    }
    private void setBtn()
    {
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!libidedt.getText().toString().isEmpty())
                {
                    db.collection("Libcard").
                            whereEqualTo("id", libidedt.getText().toString()).
                            get().
                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().isEmpty()) {
                                            libidedt.setError("Not available library card ID");
                                            valid = false;
                                        }
                                    }
                                }
                            });
                }
                else
                {
                    libidedt.setError("Must have value");
                    valid = false;
                }
                if(!bookidedt.getText().toString().isEmpty()) {
                    db.collection("BookOffline")
                            .whereEqualTo("id", bookidedt.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().isEmpty()) {
                                            bookidedt.setError("Not available book ID");
                                            valid = false;
                                        }
                                    }
                                }
                            });
                }
                else
                {
                    bookidedt.setError("Must have value");
                    valid = false;
                }
                if(valid)
                {
                    checkBookValid(libidedt.getText().toString().trim(),bookidedt.getText().toString().trim());
                }
                valid = true;
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
    }
    private void showSuccessPopup(String titletxt,String main)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.reg_libcard_popup);
        TextView title = dialog.findViewById(R.id.reg_libcard_popup_title),
                maintxt = dialog.findViewById(R.id.reg_libcard_popup_txt);
        Button btnacc = dialog.findViewById(R.id.reg_libcard_popup_accept);
           ImageButton btnig = dialog.findViewById(R.id.reg_libcard_popup_cancel);
        title.setText(titletxt);
        maintxt.setText(main);
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void showPopup(int borrowedbook,BookInBorrow bookInBorrow)
    {
        int borrow_limit = getContext().getResources().getInteger(R.integer.limit_borrow_books_per_week) - borrowedbook;
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView title = dialog.findViewById(R.id.alert_popup_title),
                maintxt = dialog.findViewById(R.id.alert_popup_maintxt);
        ImageView img = dialog.findViewById(R.id.alert_popup_img);
        Button btnacc = dialog.findViewById(R.id.alert_popup_accbtn),
                btnig = dialog.findViewById(R.id.alert_popup_igbtn);
        title.setText("XÁC NHẬN TẠO PHIẾU MƯỢN SÁCH");
        maintxt.setText("Thẻ này đang còn: " + borrow_limit + " lượt mượn trong tuần này");
        img.setImageResource(getContext().getResources().getIdentifier("kitty3","drawable",getContext().getPackageName()));
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference book = db.collection("BookOffline").
                                document(bookidedt.getText().toString().trim());
                int due_day = getContext().getResources().getInteger(R.integer.due_date);
                bookInBorrow.setDue_date(bookInBorrow.calDuedate(due_day));
                db.collection("BookInBorrow")
                        .document(bookInBorrow.getId()).set(bookInBorrow);
                bookInBorrow.bookOfflineobjSet(new GetBookListener() {
                    @Override
                    public void OnComplete(BookOffline bookOffline)
                    {
                        book.update("borrow_point",bookOffline.getBorrow_point() + 1,"remain_quantity",
                                bookOffline.getRemain_quantity() - bookInBorrow.getQuantity());
                    }
                });
                dialog.cancel();
                showSuccessPopup("THÀNH CÔNG","Bạn đã tạo phiếu mượn sách thành công");
            }
        });
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void setOnChangeBookID()
    {
        bookidedt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                layoutinfo.setVisibility(View.VISIBLE);
                BookOffline bookOffline = bookOfflines.get(position);
                if(bookOffline.getImg_url() == null) {
                    Picasso.get().load(bookOfflines.get(position).getImg_blob()).into(imgbook);
                }
                else
                {
                    Picasso.get().load(bookOfflines.get(position).getImg_url()).into(imgbook);
                }
                txtbook.setText(bookOfflines.get(position).getName());
            }
        });
    }
    private void setDataForBookID()
    {
        db.collection("BookOffline").whereEqualTo("bookstate",BookOffline.BookState.ACCEPTED.value)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           if(task.getResult().size() > 0)
                           {
                               for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                               {
                                   BookOffline bookOffline = queryDocumentSnapshot.toObject(BookOffline.class);
                                   bookOfflines.add(bookOffline);
                               }
                               bookidedt.setAdapter(new AutoBookAdapter(getContext(),R.layout.book_item_for_auto,bookOfflines));
                           }
                       }
                    }
                });
    }
    private void setDataForLibID()
    {
        db.collection("Libcard")
                .whereIn("cardstate", Arrays.asList
                        (Libcard.CardState.JUSTACCEPTED.value,Libcard.CardState.ACCEPTED.value))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                     {
                        if(task.getResult().size() > 0)
                        {
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                Libcard libcard = queryDocumentSnapshot.toObject(Libcard.class);
                                libcards.add(libcard);
                            }
                            libidedt.setAdapter(new AutoLibCardAdapter(getContext(),R.layout.book_item_for_auto,libcards));
                        }
                     }
                    }
                });
    }
    private void checkBookValid(String libid,String bookid)
    {
        int limit_book = getContext().getResources().getInteger(R.integer.limit_borrow_books_per_week);
        Timestamp[] currentmonthdate = Utils.getStartToCurrentMonthDate();
        DocumentReference libref = db.collection("Libcard").document(libid)
                ,bookref = db.collection("BookOffline").document(bookid);
        BookInBorrow bookInBorrow = new
                BookInBorrow(Utils.getRandomAlphabeticalId(8),
                bookref.getPath(), libref.getPath(), Timestamp.now(), null,
                false, true, true, 1);
        db.collection("BookInBorrow").
        whereEqualTo("libcard", libref.getPath())
                .whereGreaterThanOrEqualTo("borrow_date", currentmonthdate[0])
                .whereLessThanOrEqualTo("borrow_date", currentmonthdate[1])
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        BookInBorrow bookInBorrow2 = queryDocumentSnapshot.toObject(BookInBorrow.class);
                        if (bookInBorrow2.getWeekOfThisBook() == bookInBorrow.getWeekOfThisBook()) {
                            bookBorrowInWeek++;
                        }
                    }
                    if (bookBorrowInWeek < limit_book)
                    {
                        db.collection("BookInBorrow").
                                        whereEqualTo("libcard", libref.getPath())
                                        .whereEqualTo("book", bookref.getPath())
                                        .whereGreaterThanOrEqualTo("borrow_date", currentmonthdate[0])
                                        .whereLessThanOrEqualTo("borrow_date", currentmonthdate[1])
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    if (task.getResult().size() >= 1)
                                    {
                                        boolean isDuplicate = false;
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                        {
                                            BookInBorrow bookInBorrow1 = queryDocumentSnapshot.
                                                    toObject(BookInBorrow.class);
                                            if (bookInBorrow1.getWeekOfThisBook() == bookInBorrow.getWeekOfThisBook())
                                            {
                                                isDuplicate = true;
                                                break;
                                            }
                                        }
                                        if (isDuplicate)
                                        {
                                            showSuccessPopup("ĐÃ MƯỢN",
                                                    "Thẻ này đã mượn sách này trong tuần rồi !");
                                        }
                                        else
                                        {
                                            db.collection("BookInBorrow").
                                                    whereEqualTo("returned",false)
                                                    .whereEqualTo("accepted",true)
                                                    .whereEqualTo("libcard",libref.getPath())
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                boolean valid1 = true;
                                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                                                {
                                                                    BookInBorrow bookInBorroww = queryDocumentSnapshot.toObject(BookInBorrow.class);
                                                                    if(bookInBorroww.calDayLeft() <= 0)
                                                                    {
                                                                        valid1 = false;
                                                                        break;
                                                                    }
                                                                }
                                                                if(valid1)
                                                                {
                                                                    showPopup(bookBorrowInWeek, bookInBorrow);
                                                                }
                                                                else
                                                                {
                                                                    showSuccessPopup("CHƯA TRẢ SÁCH",
                                                                            "Hiện thẻ này không thể mượn sách vẫn còn sách chưa trả !");
                                                                }
                                                            }
                                                        }

                                                    });
                                        }
                                    }
                                    else
                                    {
                                        db.collection("BookInBorrow").
                                                whereEqualTo("returned",false)
                                                .whereEqualTo("accepted",true)
                                                .whereEqualTo("libcard",libref.getPath())
                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            boolean valid1 = true;
                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                                            {
                                                                BookInBorrow bookInBorroww = queryDocumentSnapshot.toObject(BookInBorrow.class);
                                                                if(bookInBorroww.calDayLeft() <= 0)
                                                                {
                                                                    valid1 = false;
                                                                    break;
                                                                }
                                                            }
                                                            if(valid1)
                                                            {
                                                                showPopup(bookBorrowInWeek, bookInBorrow);
                                                            }
                                                            else
                                                            {
                                                                showSuccessPopup("CHƯA TRẢ SÁCH",
                                                                        "Hiện thẻ này không thể mượn sách vẫn còn sách chưa trả !");
                                                            }
                                                        }
                                                    }

                                                });
                                    }
                                }
                            }
                        });
                    }
                    else
                    {
                        showSuccessPopup("HẾT LƯỢT MƯỢN",
                                "Thẻ này đã hết lượt mượn trong tuần");
                    }
                }

            }
        });
        bookBorrowInWeek = 0;
    }
}