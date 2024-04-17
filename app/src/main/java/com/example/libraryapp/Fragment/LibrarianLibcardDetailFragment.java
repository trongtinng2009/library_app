package com.example.libraryapp.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Adapter.BookAdapter;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.GetBookListener;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianLibcardDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianLibcardDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView img;
    private TextView nametxt,cardidtxt,firstnametxt
            ,lastnametxt,addresstxt,dobtxt,accdatetxt,phonetxt;
    private Button infobtn,bookborrowbtn,bookduebtn;
    private LinearLayout layoutinfo;
    private RecyclerView rcvbookborrow;
    private ImageButton btnback;
    private ArrayList<BookOffline> bookInBorrows;
    private int buttonselect;
    private FirebaseFirestore db;
    private Libcard libcard;

    public LibrarianLibcardDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianLibcardDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianLibcardDetailFragment newInstance(String param1, String param2) {
        LibrarianLibcardDetailFragment fragment = new LibrarianLibcardDetailFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_libcard_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        buttonselect = 1;
        bookInBorrows = new ArrayList<>();
        libcard = getArguments().getParcelable("libcard");
        initView(view);
        setContentForView();
        handleSelectClick();
    }
    private void initView(View view)
    {
        nametxt = view.findViewById(R.id.fraglibcarddel_name);
        addresstxt = view.findViewById(R.id.fraglibcarddel_address);
        accdatetxt = view.findViewById(R.id.fraglibcarddel_adddate);
        dobtxt = view.findViewById(R.id.fraglibcarddel_dob);
        cardidtxt = view.findViewById(R.id.fraglibcarddel_id);
        firstnametxt = view.findViewById(R.id.fraglibcarddel_firstname);
        lastnametxt = view.findViewById(R.id.fraglibcarddel_lastname);
        phonetxt = view.findViewById(R.id.fraglibcarddel_phone);
        img = view.findViewById(R.id.fraglibcarddel_cardimg);
        layoutinfo = view.findViewById(R.id.fraglibcarddel_cardinfo);
        btnback = view.findViewById(R.id.fraglibcarddel_backbtn);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager2.popBackStack();
            }
        });
        infobtn = view.findViewById(R.id.fraglibcarddel_infobtn);
        bookborrowbtn = view.findViewById(R.id.fraglibcarddel_bookborrow);
        bookduebtn = view.findViewById(R.id.fraglibcarddel_bookdue);
        rcvbookborrow = view.findViewById(R.id.fraglibcarddel_rcvbookborrow);
    }
    private void handleSelectClick()
    {
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonselect != 1)
                {
                    buttonselect = 1;
                    handleShowView(buttonselect);
                }
            }
        });
        bookborrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonselect != 2)
                {
                    buttonselect = 2;
                    handleShowView(buttonselect);
                    bookInBorrows.clear();
                    setRcvbookborrow();
                }
            }
        });
        bookduebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonselect != 3)
                {
                    buttonselect = 3;
                    handleShowView(buttonselect);
                    bookInBorrows.clear();
                    setRcvbookborrow();
                }
            }
        });
    }
    private void handleShowView(int select)
    {
        if(select == 1)
        {
            layoutinfo.setVisibility(View.VISIBLE);
            rcvbookborrow.setVisibility(View.GONE);
            infobtn.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),R.color.pas_redbrown
            ));
            bookborrowbtn.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),R.color.pas_cream
            ));
            bookduebtn.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),R.color.pas_cream
            ));
        }
        else if(select == 2)
        {
            layoutinfo.setVisibility(View.GONE);
            rcvbookborrow.setVisibility(View.VISIBLE);
            infobtn.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),R.color.pas_cream
            ));
            bookborrowbtn.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),R.color.pas_redbrown
            ));
            bookduebtn.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),R.color.pas_cream
            ));
        }
        else
        {
            layoutinfo.setVisibility(View.GONE);
            rcvbookborrow.setVisibility(View.VISIBLE);
            infobtn.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),R.color.pas_cream
            ));
            bookborrowbtn.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),R.color.pas_cream
            ));
            bookduebtn.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),R.color.pas_redbrown
            ));
        }
    }
    private void setRcvbookborrow()
    {
        DocumentReference libcardref = db.collection("Libcard").document(libcard.getId());
        db.collection("BookInBorrow").
        whereEqualTo("accepted",true).
        whereEqualTo("returned",false).
                whereEqualTo("libcard",libcardref.getPath())
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    BookAdapter bookAdapter = new BookAdapter(bookInBorrows,getContext(),R.layout.book_item,false);
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        BookInBorrow bookInBorrow = queryDocumentSnapshot.toObject(BookInBorrow.class);
                        if(buttonselect == 2)
                        {
                            if (bookInBorrow.calDayLeft() > 0)
                            {
                                bookInBorrow.bookOfflineobjSet(new GetBookListener() {
                                    @Override
                                    public void OnComplete(BookOffline bookOffline) {
                                        bookInBorrows.add(bookOffline);
                                        bookAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                        else if(buttonselect == 3)
                        {
                            if (bookInBorrow.calDayLeft() <= 0)
                            {
                                bookInBorrow.bookOfflineobjSet(new GetBookListener() {
                                    @Override
                                    public void OnComplete(BookOffline bookOffline) {
                                        bookInBorrows.add(bookOffline);
                                        bookAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                    rcvbookborrow.setAdapter(bookAdapter);
                    rcvbookborrow.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                }
            }
        });
    }
    private void setContentForView()
    {
        phonetxt.setText(libcard.getPhonenum());
        lastnametxt.setText(libcard.getLastname());
        firstnametxt.setText(libcard.getFirstname());
        cardidtxt.setText(libcard.getId());
        dobtxt.setText(Utils.dateToString(libcard.getDob().toDate()));
        addresstxt.setText(libcard.getAddress());
        accdatetxt.setText(Utils.dateToString(libcard.getAcc_date().toDate()));
        nametxt.setText(libcard.getFirstname() + " " + libcard.getLastname());
        try {
            byte[] encodeByte = Base64.decode(libcard.getImage_url(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}