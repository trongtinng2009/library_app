package com.example.libraryapp.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libraryapp.Activity.AdminActivity;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminUserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminUserDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView txtname,txtrole,txtuserid,txtusername,txtloginname
            ,txtgender,txtadddate,txtemail;
    private ImageView img;
    private ImageButton backbtn;
    private User userReceived;
    public AdminUserDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminUserDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminUserDetailFragment newInstance(String param1, String param2) {
        AdminUserDetailFragment fragment = new AdminUserDetailFragment();
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
        return inflater.inflate(R.layout.fragment_admin_user_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setDataForView();
    }

    private void initView(View view)
    {
        txtadddate = view.findViewById(R.id.fragadminuserdel_adddate);
        txtemail = view.findViewById(R.id.fragadminuserdel_useremail);
        txtgender = view.findViewById(R.id.fragadminuserdel_gender);
        txtname = view.findViewById(R.id.fragadminuserdel_name);
        txtrole = view.findViewById(R.id.fragadminuserdel_role);
        txtloginname = view.findViewById(R.id.fragadminuserdel_loginname);
        txtuserid = view.findViewById(R.id.fragadminuserdel_userid);
        txtusername = view.findViewById(R.id.fragadminuserdel_username);
        img = view.findViewById(R.id.fragadminuserdel_img);
        backbtn = view.findViewById(R.id.fragadminuserdel_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.fragmentManager.popBackStack();
            }
        });
    }
    private void setDataForView()
    {
        userReceived = getArguments().getParcelable("user");
        txtusername.setText(userReceived.getUsername());
        txtuserid.setText(userReceived.getId());
        txtloginname.setText(userReceived.getLoginname());
        txtrole.setText(userReceived.getRole().toUpperCase());
        txtname.setText(userReceived.getUsername());
        txtemail.setText(userReceived.getEmail());
        String gender;
        if(userReceived.getisMale())
            gender = "Male";
        else
            gender = "Female";
        txtgender.setText(gender);
        txtadddate.setText(Utils.dateToString(userReceived.getAdd_date().toDate()));
        try {
            byte[] encodeByte = Base64.decode(userReceived.getAvatar(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}