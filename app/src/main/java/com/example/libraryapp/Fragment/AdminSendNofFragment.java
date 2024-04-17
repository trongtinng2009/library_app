package com.example.libraryapp.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.libraryapp.Activity.AdminActivity;
import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Activity.WMActivity;
import com.example.libraryapp.Adapter.AutoUserAdapter;
import com.example.libraryapp.Model.Notification;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminSendNofFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminSendNofFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView receiverid;
    private EditText edtmain,edttitle;
    private ImageView img;
    private Button btncancel,btnsend;
    private RadioButton notibtn,popupbtn;
    private ImageButton backbtn;
    private FirebaseFirestore db;
    private ArrayList<User> users;
    private boolean valid = true;
    private User sender;
    public AdminSendNofFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminSendNofFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminSendNofFragment newInstance(String param1, String param2) {
        AdminSendNofFragment fragment = new AdminSendNofFragment();
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
        return inflater.inflate(R.layout.fragment_admin_send_nof, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setAutoUser();
        setAddimgbtn();
        setBtn();
    }
    private void initView(View view)
    {
        sender = getArguments().getParcelable("user");
        users = new ArrayList<>();
        receiverid = view.findViewById(R.id.fragadminsend_receiver);
        edtmain = view.findViewById(R.id.fragadminsend_mainedt);
        edttitle = view.findViewById(R.id.fragadminsend_titleedt);
        img = view.findViewById(R.id.fragadminsend_img);
        btncancel = view.findViewById(R.id.fragadminsend_cancelbtn);
        btnsend = view.findViewById(R.id.fragadminsend_sendbtn);
        notibtn = view.findViewById(R.id.fragadminsend_notibtn);
        popupbtn = view.findViewById(R.id.fragadminsend_popupbtn);
        backbtn = view.findViewById(R.id.fragadminsend_backbtn);
    }
    private void setBtn()
    {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sender.getRole().equals("Admin"))
                    AdminActivity.fragmentManager3.popBackStack();
                else if(sender.getRole().equals("Librarian"))
                    LibrarianActivity.fragmentManager3.popBackStack();
                else if(sender.getRole().equals("WM"))
                    WMActivity.fragmentManager2.popBackStack();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sender.getRole().equals("Admin"))
                    AdminActivity.fragmentManager3.popBackStack();
                else if(sender.getRole().equals("Librarian"))
                    LibrarianActivity.fragmentManager3.popBackStack();
                else if(sender.getRole().equals("WM"))
                    WMActivity.fragmentManager2.popBackStack();
            }
        });
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receivertxt = receiverid.getText().toString().trim();
                if(receivertxt.isEmpty() || receivertxt.length() == 0)
                {
                    valid = false;
                    receiverid.setError("Must have value");
                }
                String title = edttitle.getText().toString().trim();
                if(title.isEmpty() || title.length() == 0)
                {
                    valid = false;
                    edttitle.setText("Must have value");
                }
                String main = edtmain.getText().toString().trim();
                if(main.isEmpty() || main.length() == 0)
                {
                    valid = false;
                    edtmain.setText("Must have value");
                }
                if(valid ==true)
                {
                    boolean popup = false;
                    if(popupbtn.isChecked())
                    {
                        popup = true;
                    }
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    byte[] hinhAnh = outputStream.toByteArray();
                    String imgblob = Base64.encodeToString(hinhAnh,Base64.DEFAULT);
                    Notification notification = new Notification(null,title,main,imgblob,sender,null,false,popup,
                            Timestamp.now());
                    setPopup(notification);
                }
            }
        });
    }
    private void setPopup(Notification notification)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView title = dialog.findViewById(R.id.alert_popup_title)
                ,main = dialog.findViewById(R.id.alert_popup_maintxt);
        title.setText("XÁC NHẬN GỬI");
        main.setText("Bạn xác nhận gửi thông báo không ?");
        Button accbtn = dialog.findViewById(R.id.alert_popup_accbtn),
                igbtn = dialog.findViewById(R.id.alert_popup_igbtn);
        igbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        accbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference doc = db.collection("Notification")
                                .document();
                notification.setId(doc.getId());
                db.collection("User")
                                .document(receiverid.getText().toString().trim()).
                        get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    notification.setReceiver(task.getResult().toObject(User.class));
                                    doc.set(notification);
                                    setSuccesspopup();
                                    dialog.cancel();
                                }
                            }
                        });
            }
        });
        dialog.show();
    }
    private void setSuccesspopup()
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.reg_libcard_popup);
        TextView txtTitle,txtContent;
        txtTitle = dialog.findViewById(R.id.reg_libcard_popup_title);
        txtContent = dialog.findViewById(R.id.reg_libcard_popup_txt);
        txtTitle.setText("THÀNH CÔNG");
        txtContent.setText("Bạn đã gửi thông báo thành công");

        ImageView img = dialog.findViewById(R.id.reg_libcard_popup_img);
        img.setImageResource(getContext().getResources().
                getIdentifier("caution","drawable",getContext().getPackageName()));
        Button btnacc = dialog.findViewById(R.id.reg_libcard_popup_accept);
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void setAutoUser()
    {
        db.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
              if(task.isSuccessful())
              {
                  for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                  {
                      User user = queryDocumentSnapshot.toObject(User.class);
                      users.add(user);
                  }
                  receiverid.setAdapter(new AutoUserAdapter(getContext(),R.layout.book_item_for_auto,users));
              }
            }
        });
    }
    private void setAddimgbtn()
    {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startPickImg.launch(i);
            }
        });
    }
    ActivityResultLauncher<Intent> startPickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if(data != null && data.getData() != null)
                        {
                            Uri selectedImg = data.getData();
                            Bitmap selectedImgBit = null;
                            try {
                                selectedImgBit = MediaStore.Images.Media.getBitmap(
                                        getContext().getContentResolver(),selectedImg
                                );
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            img.setImageBitmap(selectedImgBit);
                        }
                    }
                }
            });
}