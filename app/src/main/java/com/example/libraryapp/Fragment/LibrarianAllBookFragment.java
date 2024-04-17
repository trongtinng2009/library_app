package com.example.libraryapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Adapter.BookAdapter;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianAllBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianAllBookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rcv;
    private TextView totaltxt;
    private SearchView searchView;
    private BookAdapter bookAdapter;
    private ArrayList<BookOffline> bookOfflines;
    private FirebaseFirestore db;
    private ImageButton backbtn;
    private Button searchidbtn,searchnamebtn,searchauthorbtn;
    private int select;

    public LibrarianAllBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianAllBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianAllBookFragment newInstance(String param1, String param2) {
        LibrarianAllBookFragment fragment = new LibrarianAllBookFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_all_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        select = 1;
        initView(view);
        setRcv();
        setSearchView();
        setOnClickOption();
    }
    private void initView(View view)
    {
        searchauthorbtn = view.findViewById(R.id.fragliballbook_searchauthor);
        searchidbtn = view.findViewById(R.id.fragliballbook_searchid);
        searchnamebtn = view.findViewById(R.id.fragliballbook_searchname);
        bookOfflines = new ArrayList<>();
        backbtn = view.findViewById(R.id.fragguestallbook_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
        totaltxt = view.findViewById(R.id.fragliballbook_total);
        rcv = view.findViewById(R.id.fragliballbook_rcv);
        searchView = view.findViewById(R.id.fragliballbook_searchview);
        bookAdapter = new BookAdapter(bookOfflines,getContext(),R.layout.book_item2,1);
        bookAdapter.setGuest(false);
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
    private void setRcv()
    {
        db.collection("BookOffline")
                .whereIn("bookstate", Arrays.asList(BookOffline.BookState.ACCEPTED.value,
                        BookOffline.BookState.UnAvailable.value))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            totaltxt.setText("Tổng: " +task.getResult().size());
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                BookOffline bookOffline = queryDocumentSnapshot.toObject(BookOffline.class);
                                bookOfflines.add(bookOffline);
                            }
                            rcv.setAdapter(bookAdapter);
                            rcv.setLayoutManager(new GridLayoutManager(getContext(),2));
                        }
                    }
                });
    }
    private void setOnClickOption()
    {
        searchnamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select != 1)
                {
                    select = 1;
                    setClickOption(select);
                    bookAdapter.setSearchoption(select);
                }
            }
        });
        searchauthorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select != 2)
                {
                    select = 2;
                    setClickOption(select);
                    bookAdapter.setSearchoption(select);
                }
            }
        });
        searchidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select != 3)
                {
                    select = 3;
                    setClickOption(select);
                    bookAdapter.setSearchoption(select);
                }
            }
        });
    }
    private void setClickOption(int select)
    {
        if(select == 1)
        {
            searchidbtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
            searchauthorbtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
            searchnamebtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_redbrown));
        }
        else if(select == 2)
        {
            searchauthorbtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_redbrown));
            searchnamebtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
            searchidbtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
        }
        else if(select == 3)
        {
            searchauthorbtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
            searchnamebtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
            searchidbtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_redbrown));
        }
    }
}