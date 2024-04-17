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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libraryapp.Activity.AdminActivity;
import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Activity.WMActivity;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Notification;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NofDetalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NofDetalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imgsender,img;
    private TextView txtsendername,txtsenderid,txtsenddate,txttitle,txtmain;
    private Button btndel;
    private Notification notification;
    private FirebaseFirestore db;

    public NofDetalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NofDetalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NofDetalFragment newInstance(String param1, String param2) {
        NofDetalFragment fragment = new NofDetalFragment();
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
        return inflater.inflate(R.layout.fragment_nof_detal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setContentForView();
        setBtndel();
    }
    private void setBtndel()
    {
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Notification").document(notification.getId())
                        .delete();
                if(MainActivity.user != null)
                {
                    MainActivity.fragmentManager3.popBackStack();
                }
                else if(LibrarianActivity.user != null)
                {
                    LibrarianActivity.fragmentManager3.popBackStack();
                }
                else if(AdminActivity.user != null)
                {
                    AdminActivity.fragmentManager3.popBackStack();
                }
                else if(WMActivity.user != null)
                {
                    WMActivity.fragmentManager2.popBackStack();
                }
            }
        });
    }
    private void initView(View view)
    {
        notification = getArguments().getParcelable("nof");
        imgsender = view.findViewById(R.id.fragnofdel_senderimg);
        img = view.findViewById(R.id.fragnofdel_img);
        txtmain = view.findViewById(R.id.fragnofdel_maintxt);
        txtsenddate = view.findViewById(R.id.fragnofdel_senddate);
        txtsenderid = view.findViewById(R.id.fragnofdel_senderid);
        txtsendername = view.findViewById(R.id.fragnofdel_sendername);
        txttitle = view.findViewById(R.id.fragnofdel_title);
        btndel = view.findViewById(R.id.fragnofdel_delbtn);
    }
    private void setContentForView()
    {
        txttitle.setText(notification.getTitle());
        txtsendername.setText("Người gửi: "+notification.getSender().getUsername());
        txtsenderid.setText("ID: "+notification.getSender().getId());
        txtsenddate.setText("Gửi vào: "+ Utils.dateToString(notification.getSenddate().toDate()));
        txtmain.setText(notification.getMaintxt());
        try {
            byte[] encodeByte = Base64.decode(notification.getImg(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            byte[] encodeByte = Base64.decode(notification.getSender().getAvatar(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            imgsender.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}