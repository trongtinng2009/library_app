package com.example.libraryapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.libraryapp.Adapter.BookBorrowAdapter;
import com.example.libraryapp.MainActivity;
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
 * Use the {@link GuestBorrowedBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestBorrowedBookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rcv;
    private FirebaseFirestore db;
    ImageButton backbtn;
    private ArrayList<BookInBorrow> bookInBorrows;
    private BookBorrowAdapter bookBorrowAdapter;

    public GuestBorrowedBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestBorrowedBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestBorrowedBookFragment newInstance(String param1, String param2) {
        GuestBorrowedBookFragment fragment = new GuestBorrowedBookFragment();
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
        return inflater.inflate(R.layout.fragment_guest_borrowed_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        bookInBorrows = new ArrayList<>();
        initView(view);
        setRcv();
    }
    private void initView(View view)
    {
        rcv = view.findViewById(R.id.fragguestborrwed_rcv);
        backbtn = view.findViewById(R.id.fragguestborrowed_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager2.popBackStack();
            }
        });
        bookBorrowAdapter = new BookBorrowAdapter(bookInBorrows,getContext(),R.layout.borrowingbook_item);
    }
    private void setRcv()
    {
        DocumentReference libref = db.collection("Libcard").document(MainActivity.libcard.getId());
        db.collection("BookInBorrow").whereEqualTo("returned",true)
                .whereEqualTo("libcard",libref.getPath())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if(task.isSuccessful())
                         {
                             for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                             {
                                 BookInBorrow bookInBorrow = queryDocumentSnapshot.toObject(BookInBorrow.class);
                                 bookInBorrows.add(bookInBorrow);
                             }
                             rcv.setAdapter(bookBorrowAdapter);
                             rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                         }
                    }
                });
    }
}