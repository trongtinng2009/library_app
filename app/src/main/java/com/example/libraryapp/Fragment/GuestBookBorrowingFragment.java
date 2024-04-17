package com.example.libraryapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.libraryapp.Adapter.BookAdapter;
import com.example.libraryapp.Adapter.BookBorrowAdapter;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestBookBorrowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestBookBorrowingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvbookborrowing;
    private LinearLayout havebook,nothavebook;
    ImageButton backbtn;
    private ArrayList<BookInBorrow> bookInBorrows;
    private CollectionReference bookinborrowcollection = MainActivity.db.collection("BookInBorrow")
            ,libcardcollection = MainActivity.db.collection("Libcard");

    public GuestBookBorrowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestBookBorrowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestBookBorrowingFragment newInstance(String param1, String param2) {
        GuestBookBorrowingFragment fragment = new GuestBookBorrowingFragment();
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
        return inflater.inflate(R.layout.fragment_guest_book_borrowing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }
    private void initView(View view)
    {
        rcvbookborrowing = view.findViewById(R.id.fragguestbookborrowing_rcv);
        rcvbookborrowing.setNestedScrollingEnabled(false);
        havebook = view.findViewById(R.id.fragguestbookborrowing_havebook);
        backbtn = view.findViewById(R.id.fragguestbookborrowing_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager2.popBackStack();
            }
        });
        nothavebook = view.findViewById(R.id.fragguestbookborrowing_nothavebook);
        bookInBorrows = new ArrayList<>();
        setDataforBookBorrow();
    }
    private void setDataforBookBorrow()
    {
        DocumentReference libcard = libcardcollection.document(MainActivity.libcard.getId());
        Query query = bookinborrowcollection.whereEqualTo("libcard",libcard.getPath())
                .whereEqualTo("returned",false);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().size() > 0) {
                        setHavebook();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                        {
                            BookInBorrow bookInBorrow = queryDocumentSnapshot.toObject(BookInBorrow.class);
                            bookInBorrows.add(bookInBorrow);
                        }
                        BookBorrowAdapter bookBorrowAdapter = new BookBorrowAdapter(bookInBorrows,getContext(),R.layout.borrowingbook_item);
                        rcvbookborrowing.setAdapter(bookBorrowAdapter);
                        rcvbookborrowing.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    }
                    else
                    {
                        setNothavebook();
                    }
                }
                else
                {

                }
            }
        });
    }
    private void setHavebook()
    {
        havebook.setVisibility(View.VISIBLE);
        nothavebook.setVisibility(View.GONE);
    }
    private void setNothavebook()
    {
        nothavebook.setVisibility(View.VISIBLE);
        havebook.setVisibility(View.GONE);
    }
}