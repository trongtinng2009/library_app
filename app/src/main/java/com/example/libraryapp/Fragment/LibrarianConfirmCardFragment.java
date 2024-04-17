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
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.Model.User;
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
 * Use the {@link LibrarianConfirmCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianConfirmCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvlistrequest;
    private ArrayList<Libcard> libcards;
    ImageButton backbtn;
    TextView totaltxt;
    private CollectionReference libcardcollection = MainActivity.db.collection("Libcard")
            , usercollection = MainActivity.db.collection("User");

    public LibrarianConfirmCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianConfirmCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianConfirmCardFragment newInstance(String param1, String param2) {
        LibrarianConfirmCardFragment fragment = new LibrarianConfirmCardFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_confirm_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setListRequest();
    }

    private void initView(View view)
    {
        rcvlistrequest = view.findViewById(R.id.fraglibconcard_rcvlistrequest);
        backbtn = view.findViewById(R.id.fraglibconcard_backbtn);
        totaltxt = view.findViewById(R.id.fraglibconcard_totalbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager2.popBackStack();
            }
        });
        libcards = new ArrayList<Libcard>();
    }
    private void setListRequest()
    {
        Query query = libcardcollection.whereEqualTo("cardstate",Libcard.CardState.REQUESTING.value);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    totaltxt.setText("Tổng: " + task.getResult().size());
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                    {
                        Libcard libcard = documentSnapshot.toObject(Libcard.class);
                        libcards.add(libcard);
                    }
                    rcvlistrequest.setAdapter(new LibcardAdapter(getContext(),R.layout.libcard_request_item,libcards));
                    rcvlistrequest.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }
            }
        });
    }
}