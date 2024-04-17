package com.example.libraryapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.libraryapp.Adapter.BookAdapter;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Category;
import com.example.libraryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestCategoryViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestCategoryViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView txttitle;
    private ImageButton btnback;
    private BookAdapter bookAdapter;
    private Button btnsearchname,btnsearchauthor;
    private SearchView searchView;
    private RecyclerView rcv;
    private int selectbtn;
    private ArrayList<BookOffline> bookOfflines;
    private Category category;
    private FirebaseFirestore db;
    public GuestCategoryViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestCategoryViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestCategoryViewFragment newInstance(String param1, String param2) {
        GuestCategoryViewFragment fragment = new GuestCategoryViewFragment();
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
        return inflater.inflate(R.layout.fragment_guest_category_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        bookAdapter = new BookAdapter(bookOfflines,getContext(),R.layout.book_item2,1);
        setRcv();
        setSearchView();
        setSearchOption();
    }
    private void initView(View view)
    {
        searchView = view.findViewById(R.id.fragguestcate_searchview);
        category = (Category) getArguments().getSerializable("cate");
        txttitle = view.findViewById(R.id.fragguestcate_titletxt);
        txttitle.setText(category.getName().toUpperCase() + " BOOK");
        btnback = view.findViewById(R.id.fragguestcate_backbtn);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
        btnsearchauthor = view.findViewById(R.id.fragguestcate_searchauthor);
        btnsearchname = view.findViewById(R.id.fragguestcate_searchname);
        rcv = view.findViewById(R.id.fragguestcate_rcv);
        bookOfflines = new ArrayList<>();
    }
    private void setRcv()
    {
        DocumentReference cateref = db.collection("Category").document(category.getId());
        db.collection("BookOffline")
                .whereArrayContains("categories",cateref.getPath())
                .whereEqualTo("bookstate",BookOffline.BookState.ACCEPTED.value)
                .orderBy("add_date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
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
    private void setSearchOption()
    {
        btnsearchname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectbtn != 1)
                {
                    selectbtn = 1;
                    setSearchOptionClick(selectbtn);
                    bookAdapter.setSearchoption(selectbtn);
                }
            }
        });
        btnsearchauthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectbtn != 2)
                {
                    selectbtn = 2;
                    setSearchOptionClick(selectbtn);
                    bookAdapter.setSearchoption(selectbtn);
                }
            }
        });
    }

    private void setSearchOptionClick(int selectbtn)
    {
        if(selectbtn == 1)
        {
            btnsearchauthor.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
            btnsearchname.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_redbrown));
        }
        else if(selectbtn == 2)
        {
            btnsearchauthor.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_redbrown));
            btnsearchname.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
        }
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
}