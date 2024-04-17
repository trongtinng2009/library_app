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
import com.example.libraryapp.Adapter.LibcardAdapter;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianViewCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianViewCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    private ImageButton backbtn;
    private ArrayList<Libcard> libcards;
    private RecyclerView rcvcard;
    TextView txttotal;


    public LibrarianViewCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianViewCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianViewCardFragment newInstance(String param1, String param2) {
        LibrarianViewCardFragment fragment = new LibrarianViewCardFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_view_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setRcvcard();
    }
    private void initView(View view)
    {
        rcvcard = view.findViewById(R.id.fraglibviewcard_rcvcard);
        txttotal = view.findViewById(R.id.fraglibviewcard_totalbtn);
        backbtn = view.findViewById(R.id.fraglibviewcard_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager2.popBackStack();
            }
        });
        libcards = new ArrayList<>();
    }
    private void setRcvcard()
    {
        db.collection("Libcard").whereNotEqualTo("cardstate",Libcard.CardState.REQUESTING.value)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    txttotal.setText("Tổng: " + task.getResult().size());
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        Libcard libcard = queryDocumentSnapshot.toObject(Libcard.class);
                        libcards.add(libcard);
                    }
                    rcvcard.setAdapter(new LibcardAdapter(getContext(),R.layout.libcard_item,libcards));
                    rcvcard.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }
            }
        });
    }
}