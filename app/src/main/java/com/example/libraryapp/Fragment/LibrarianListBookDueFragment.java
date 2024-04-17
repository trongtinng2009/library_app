package com.example.libraryapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Adapter.BookBorrowAdapter;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianListBookDueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianListBookDueFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<BookInBorrow> bookInBorrows;
    private BookBorrowAdapter bookBorrowAdapter;
    private SearchView searchView;
    private ImageButton backbtn;
    private TextView txttotal;
    private RecyclerView rcv;
    private FirebaseFirestore db;

    public LibrarianListBookDueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianListBookDueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianListBookDueFragment newInstance(String param1, String param2) {
        LibrarianListBookDueFragment fragment = new LibrarianListBookDueFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_list_book_due, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setRcv();
        setSearchView();
    }
    private void initView(View view)
    {
        rcv = view.findViewById(R.id.fragliblistdue_rcvlistrequest);
        txttotal = view.findViewById(R.id.fragliblistdue_results);
        bookInBorrows = new ArrayList<>();
        bookBorrowAdapter = new BookBorrowAdapter(bookInBorrows,getContext(),R.layout.borrowingbook_item2);
        searchView = view.findViewById(R.id.fragliblistdue_searchview);
        backbtn = view.findViewById(R.id.fragliblistdue_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
    }
    private void setRcv()
    {
        db.collection("BookInBorrow").whereEqualTo("returned",false)
                .whereEqualTo("accepted",true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            int bookduetotal = 0;
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                BookInBorrow bookInBorrow = queryDocumentSnapshot.toObject(BookInBorrow.class);
                                if(bookInBorrow.calDayLeft() <=0)
                                {
                                    bookInBorrows.add(bookInBorrow);
                                    bookduetotal += 1;
                                }
                            }
                            txttotal.setText("Tổng: "+ bookduetotal);
                            rcv.setAdapter(bookBorrowAdapter);
                            rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                        }
                    }
                });
    }
    private void setSearchView()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                bookBorrowAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bookBorrowAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}