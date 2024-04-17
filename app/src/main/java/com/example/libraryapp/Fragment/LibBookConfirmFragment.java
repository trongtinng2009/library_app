package com.example.libraryapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Adapter.BookAdapter;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibBookConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibBookConfirmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvBookconfirm;
    private ArrayList<BookOffline> bookOfflines;
    private SearchView searchView;
    private ImageButton backbtn;
    private BookAdapter bookAdapter;
    private TextView txttotal;

    public LibBookConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibBookConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibBookConfirmFragment newInstance(String param1, String param2) {
        LibBookConfirmFragment fragment = new LibBookConfirmFragment();
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
        return inflater.inflate(R.layout.fragment_lib_book_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bookAdapter = new BookAdapter(bookOfflines,getContext(),R.layout.book_item_confirm,1);
        setData();
        setSearchView();
    }
    private void setSearchView()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                bookAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bookAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private void initView(View view)
    {
        rcvBookconfirm = view.findViewById(R.id.fraglibconbook_rcvlistrequest);
        backbtn = view.findViewById(R.id.fraglibconbook_backbtn);
        txttotal = view.findViewById(R.id.fraglibconbook_total);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
        searchView = view.findViewById(R.id.fraglibconbook_searchview);
        bookOfflines = new ArrayList<>();
    }
    private void setData()
    {
        MainActivity.db.collection("BookOffline").
                whereEqualTo("bookstate",BookOffline.BookState.UnAccepted.value)
                .orderBy("add_date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           txttotal.setText("Tổng: " + task.getResult().size());
                           if(task.getResult().size() > 0)
                           {
                               for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                               {
                                   BookOffline bookOffline = queryDocumentSnapshot.toObject(BookOffline.class);
                                   bookOfflines.add(bookOffline);
                               }
                               rcvBookconfirm.setAdapter(bookAdapter);
                               rcvBookconfirm.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                           }
                       }
                    }
                });
    }
}