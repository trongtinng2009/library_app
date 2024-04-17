package com.example.libraryapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.libraryapp.Adapter.CategoryAdapter;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Category;
import com.example.libraryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestAllCateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestAllCateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Category> categories;
    private ImageButton backbtn;
    private RecyclerView rcv;
    private FirebaseFirestore db;

    public GuestAllCateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestAllCateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestAllCateFragment newInstance(String param1, String param2) {
        GuestAllCateFragment fragment = new GuestAllCateFragment();
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
        return inflater.inflate(R.layout.fragment_guest_all_cate, container, false);
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
        backbtn = view.findViewById(R.id.fragguestallcate_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
        rcv = view.findViewById(R.id.fragguestallcate_rcvcate);
        categories = new ArrayList<>();
    }
    private void setRcv()
    {
        db.collection("Category").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful())
                       {
                           for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                           {
                               Category category = queryDocumentSnapshot.toObject(Category.class);
                               categories.add(category);
                           }
                           rcv.setAdapter(new CategoryAdapter(getContext(),categories,R.layout.cate_item2));
                           rcv.setLayoutManager(new GridLayoutManager(getContext(),3));
                       }
                    }
                });
    }
}