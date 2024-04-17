package com.example.libraryapp.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;

import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout layoutviewcard,layoutcrecard,layoutconcard;
    private LibrarianConfirmCardFragment librarianConfirmCardFragment;

    public LibrarianCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianCardFragment newInstance(String param1, String param2) {
        LibrarianCardFragment fragment = new LibrarianCardFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        handleClick();
    }
    private void initView(View view)
    {
        layoutviewcard = view.findViewById(R.id.fraglibcard_layoutviewcard);
        layoutconcard = view.findViewById(R.id.fraglibcard_layoutconcard);
        librarianConfirmCardFragment = new LibrarianConfirmCardFragment();
    }
    private void handleClick()
    {
        layoutviewcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = LibrarianActivity.fragmentManager2.beginTransaction();
                trans.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_left);
                trans.replace(R.id.libact_fragmentcontainer,new LibrarianViewCardFragment());
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        layoutconcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = LibrarianActivity.fragmentManager2.beginTransaction();
                trans.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_left);
                trans.replace(R.id.libact_fragmentcontainer,librarianConfirmCardFragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }
}