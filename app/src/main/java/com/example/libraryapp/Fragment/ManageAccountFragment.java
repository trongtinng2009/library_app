package com.example.libraryapp.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libraryapp.Activity.AdminActivity;
import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Adapter.UserAdapter;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Notification;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rcvacc;
    private ArrayList<User> users;
    private FirebaseFirestore db;
    TextView txttotal;
    private ImageButton addstaffbtn;

    public ManageAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageAccountFragment newInstance(String param1, String param2) {
        ManageAccountFragment fragment = new ManageAccountFragment();
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
        return inflater.inflate(R.layout.fragment_manage_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setDataRcv();
        setAddstaffbtn();
        showPopUpNof();
    }

    private void initView(View view)
    {
        rcvacc = view.findViewById(R.id.fragadminmngacc_rcvacc);
        txttotal = view.findViewById(R.id.fragadminmngacc_listacctxt);
        addstaffbtn = view.findViewById(R.id.fragadminmngacc_addstaffbtn);
        users = new ArrayList<User>();
    }
    private void setAddstaffbtn()
    {
        addstaffbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = AdminActivity.fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_left);
                transaction.replace(R.id.adminact_fragmentcontainer,new AdminAddStaffFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void setDataRcv()
    {
        MainActivity.db.collection("User")
                .whereNotEqualTo("role","Admin")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            txttotal.setText("Tổng: " + task.getResult().size());
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                User user = queryDocumentSnapshot.toObject(User.class);
                                users.add(user);
                            }
                            rcvacc.setAdapter(new UserAdapter(users,getContext(),R.layout.account_item));
                            rcvacc.setLayoutManager(new GridLayoutManager(getContext(),2));
                        }
                    }
                });
    }
    private void showPopUpNof()
    {
        db.collection("Notification").whereEqualTo("receiver.id", AdminActivity.user.getId())
                .whereEqualTo("popup",true)
                .whereEqualTo("received",false)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                Notification notification = queryDocumentSnapshot.toObject(Notification.class);
                                showPopUp(notification);
                            }
                        }
                    }
                });
    }
    private void showPopUp(Notification notification)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.reg_libcard_popup);
        TextView title = dialog.findViewById(R.id.reg_libcard_popup_title),
                main = dialog.findViewById(R.id.reg_libcard_popup_txt);
        ImageView img = dialog.findViewById(R.id.reg_libcard_popup_img);
        Button btn = dialog.findViewById(R.id.reg_libcard_popup_accept);
        title.setText(notification.getTitle());
        main.setText(notification.getMaintxt());
        try {
            byte[] encodeByte = Base64.decode(notification.getImg(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Notification").document(notification.getId())
                        .update("received",true);
                dialog.cancel();
            }
        });
        dialog.show();
    }
}