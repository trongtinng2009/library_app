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
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Adapter.BookBorrowAdapter;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.Model.BookOffline;
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
 * Use the {@link LibrarianListGuestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianListGuestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<BookInBorrow> bookInBorrows;
    private FirebaseFirestore db;
    private TextView txttotal;
    private BookBorrowAdapter bookBorrowAdapter;
    private RecyclerView rcv;
    private ImageButton backbtn;
    private BookOffline bookOffline;

    public LibrarianListGuestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianListGuestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianListGuestFragment newInstance(String param1, String param2) {
        LibrarianListGuestFragment fragment = new LibrarianListGuestFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_list_guest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setRcv();
    }
    private void initView(View view)
    {
        backbtn = view.findViewById(R.id.fragliblistguest_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
        bookOffline = getArguments().getParcelable("book");
        bookInBorrows = new ArrayList<>();
        rcv = view.findViewById(R.id.fragliblistguest_rcvlistrequest);
        txttotal = view.findViewById(R.id.fragliblistguest_results);
        bookBorrowAdapter = new BookBorrowAdapter(bookInBorrows,getContext(),R.layout.borrowingbook_item2);
    }
    private void setRcv()
    {
        DocumentReference bookref = db.collection("BookOffline").document(bookOffline.getId());
        db.collection("BookInBorrow").whereEqualTo("returned",false)
                .whereEqualTo("accepted",true)
                .whereEqualTo("book",bookref.getPath())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            txttotal.setText("Tổng: "+Integer.toString(task.getResult().size()));
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                BookInBorrow bookInBorrow = queryDocumentSnapshot.toObject(BookInBorrow.class);
                                if(bookInBorrow.calDayLeft() >0)
                                    bookInBorrows.add(bookInBorrow);
                            }
                            rcv.setAdapter(bookBorrowAdapter);
                            rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                        }
                    }
                });
    }
}