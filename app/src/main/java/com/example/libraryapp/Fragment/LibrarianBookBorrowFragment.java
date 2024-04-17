package com.example.libraryapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Adapter.BookBorrowAdapter;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianBookBorrowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianBookBorrowFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View requestselect,acceptedselect;
    private LinearLayout requestlayout,acceptedlayout;
    private Button addbtn;
    public static int optionselect;
    ImageButton backbtn;
    private RecyclerView rcvBookRequesting,rcvAccepted;
    private ArrayList<BookInBorrow> bookInBorrows;
    private CollectionReference bookinborrowcollection = MainActivity.db.collection("BookInBorrow");

    public LibrarianBookBorrowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianBookBorrowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianBookBorrowFragment newInstance(String param1, String param2) {
        LibrarianBookBorrowFragment fragment = new LibrarianBookBorrowFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_book_borrow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setClickOption();
        setRcvBook();
        setAddbtn();
    }

    private void initView(View view)
    {
        rcvBookRequesting = view.findViewById(R.id.fraglibbookborrow_rcvrequesting);
        rcvAccepted = view.findViewById(R.id.fraglibbookborrow_rcvaccepted);
        requestlayout = view.findViewById(R.id.fraglibbookborrow_requestbtn);
        requestselect = view.findViewById(R.id.fraglibbookborrow_requestunderline);
        backbtn = view.findViewById(R.id.fraglibbookborrow_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
        acceptedselect = view.findViewById(R.id.fraglibbookborrow_acceptedunderline);
        acceptedlayout = view.findViewById(R.id.fraglibbookborrow_acceptedbtn);
        addbtn = view.findViewById(R.id.fraglibbookborrow_addbtn);
        optionselect = 1;
    }
    private void setAddbtn()
    {
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = LibrarianActivity.fragmentManager.beginTransaction();
                transaction.replace(R.id.libact_fragmentcontainer,new LibrarianBookCreateTicketFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void selectOption()
    {
        if(optionselect == 1)
        {
            requestselect.setVisibility(View.VISIBLE);
            rcvBookRequesting.setVisibility(View.VISIBLE);
            acceptedselect.setVisibility(View.GONE);
            rcvAccepted.setVisibility(View.GONE);
        }
        else
        {
            requestselect.setVisibility(View.GONE);
            rcvBookRequesting.setVisibility(View.GONE);
            acceptedselect.setVisibility(View.VISIBLE);
            rcvAccepted.setVisibility(View.VISIBLE);
        }
    }
    private void setClickOption()
    {
        requestlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionselect = 1;
                selectOption();
                setRcvBook();
            }
        });
        acceptedlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionselect = 2;
                selectOption();
                setRcvBook();
            }
        });
    }
    private void setRcvBook()
    {
        bookInBorrows = new ArrayList<>();
        Query query = null;
        if(optionselect == 1)
        {
            query = bookinborrowcollection.
                whereEqualTo("accepted", false)
                .orderBy("borrow_date", Query.Direction.DESCENDING);
        }
        else if(optionselect == 2 )
        {
            query = bookinborrowcollection.
                    whereEqualTo("accepted", true)
                    .whereEqualTo("returned",false)
                    .orderBy("borrow_date", Query.Direction.DESCENDING);
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        BookInBorrow bookInBorrow = queryDocumentSnapshot.toObject(BookInBorrow.class);
                        bookInBorrows.add(bookInBorrow);
                    }
                    if(optionselect == 2) {
                        rcvAccepted.setAdapter(new BookBorrowAdapter(bookInBorrows, getContext(), R.layout.borrowingbooklib_item));
                        rcvAccepted.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }
                    else if(optionselect == 1) {
                        rcvBookRequesting.setAdapter(new BookBorrowAdapter(bookInBorrows, getContext(), R.layout.borrowingbooklib_item));
                        rcvBookRequesting.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }
                }
            }
        });
    }
}