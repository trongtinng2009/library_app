package com.example.libraryapp.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Category;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianBookDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianBookDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private TextView txtbookid,txtquantity,txtremainquantity,txtadddate;
    private EditText edtname,edtsum,edtpublisher,edtauthor;
    private AutoCompleteTextView cate;
    private ImageView img;
    private ImageButton backbtn;
    private Button btnhide,btnshowguest;
    private BookOffline bookOffline;
    private FirebaseFirestore db;
    private String catetxt;
    private String mParam2;

    public LibrarianBookDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianBookDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianBookDetailFragment newInstance(String param1, String param2) {
        LibrarianBookDetailFragment fragment = new LibrarianBookDetailFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setContentForView();
        setBtnshowguest();
        checkBookState();
        setBtnhide();
    }
    private void initView(View view)
    {
        img = view.findViewById(R.id.fraglibbookdel_imgbook);
        backbtn = view.findViewById(R.id.fraglibbookdel_backbtn);
        bookOffline = getArguments().getParcelable("book");
        txtbookid = view.findViewById(R.id.fraglibbookdel_bookid);
        txtadddate = view.findViewById(R.id.fraglibbookdel_adddate);
        txtremainquantity = view.findViewById(R.id.fraglibbookdel_remainquantity);
        txtquantity = view.findViewById(R.id.fraglibbookdel_quantity);
        btnhide = view.findViewById(R.id.fraglibbookdel_btnhide);
        edtauthor = view.findViewById(R.id.fraglibbookdel_authoredt);
        edtname = view.findViewById(R.id.fraglibbookdel_booknameedt);
        edtsum = view.findViewById(R.id.fraglibbookdel_sumedt);
        edtpublisher = view.findViewById(R.id.fraglibbookdel_publisheredt);
        cate = view.findViewById(R.id.fraglibbookdel_cateedt);
        btnhide = view.findViewById(R.id.fraglibbookdel_btnhide);
        btnshowguest = view.findViewById(R.id.fraglibbookdel_btnshowguest);
    }
    private void setBtnhide()
    {
        btnhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookOffline.getBookstate() == BookOffline.BookState.UnAvailable.value) {
                    db.collection("BookOffline").document(bookOffline.getId())
                            .update("bookstate", BookOffline.BookState.ACCEPTED.value);
                    bookOffline.setBookstate(BookOffline.BookState.ACCEPTED.value);
                }
                else if(bookOffline.getBookstate() == BookOffline.BookState.ACCEPTED.value) {
                    db.collection("BookOffline").document(bookOffline.getId())
                            .update("bookstate", BookOffline.BookState.UnAvailable.value);
                    bookOffline.setBookstate(BookOffline.BookState.UnAvailable.value);
                }
                checkBookState();
            }
        });

    }
    private void checkBookState()
    {
        if(bookOffline.getBookstate() == BookOffline.BookState.UnAvailable.value)
        {
            btnhide.setText("Mở sách");

        }
        else
        {
            btnhide.setText("Ẩn sách");
        }
    }
    private void setBtnshowguest()
    {
        btnshowguest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LibrarianActivity.user != null) {
                    LibrarianListGuestFragment librarianListGuestFragment = new LibrarianListGuestFragment();
                    Bundle b = new Bundle();
                    b.putParcelable("book", bookOffline);
                    librarianListGuestFragment.setArguments(b);
                    FragmentTransaction transaction = LibrarianActivity.fragmentManager.beginTransaction();
                    transaction.replace(R.id.libact_fragmentcontainer, librarianListGuestFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(MainActivity.user != null)
                {
                    LibrarianListGuestFragment librarianListGuestFragment = new LibrarianListGuestFragment();
                    Bundle b = new Bundle();
                    b.putParcelable("book", bookOffline);
                    librarianListGuestFragment.setArguments(b);
                    FragmentTransaction transaction = MainActivity.fragmentManager2.beginTransaction();
                    transaction.replace(R.id.adminact_fragmentcontainer, librarianListGuestFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }
    private void setContentForView()
    {

        for(String i : bookOffline.getCategories())
        {

            DocumentReference ref = db.document(i);
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Category category = task.getResult().toObject(Category.class);
                    if(task.isSuccessful())
                    {
                        catetxt += category.getId() + ",";
                        cate.setText(catetxt);
                    }
                }
            });
        }
        txtbookid.setText("Book ID: " +bookOffline.getId());
        txtquantity.setText("Tổng: " + bookOffline.getQuantity());
        txtremainquantity.setText("Hiện còn: " + bookOffline.getRemain_quantity());
        txtadddate.setText("Thêm vào: " +Utils.dateToString(bookOffline.getAdd_date().toDate()));
        edtauthor.setText(bookOffline.getAuthor());
        edtpublisher.setText(bookOffline.getPublisher());
        edtname.setText(bookOffline.getName());
        edtsum.setText(bookOffline.getSummary());
        if(bookOffline.getImg_url() != null)
        {
            Picasso.get().load(bookOffline.getImg_url()).into(img);
        }
        else
        {
            Picasso.get().load(bookOffline.getImg_blob()).into(img);
        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
    }
}