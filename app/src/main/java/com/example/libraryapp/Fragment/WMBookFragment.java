package com.example.libraryapp.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Activity.WMActivity;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Notification;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WMBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WMBookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button addimgbtn,addimgurlbtn,btnsubmit,btncancel,btnaddexistbook;
    private ImageView imgbook;
    private FirebaseFirestore db;
    private EditText imgurledt,edtbookid,edtbookname,edtauthorname,edtquantity,edtpublisher,edtadddate;
    private int selectimgbtn;
    private boolean valid;
    FirebaseStorage fs;
    ProgressDialog progressDialog;
    StorageReference sr;
    private CollectionReference bookcollection = MainActivity.db.collection("BookOffline");

    public WMBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WMBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WMBookFragment newInstance(String param1, String param2) {
        WMBookFragment fragment = new WMBookFragment();
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
        return inflater.inflate(R.layout.fragment_w_m_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectimgbtn = 0;
        valid = true;
        db = FirebaseFirestore.getInstance();
        fs = FirebaseStorage.getInstance();
        initView(view);
        setAddimgbtn();
        setAddimgurlbtn();
        setBtnsubmit();
        setBtnaddexistbook();
        showPopUpNof();
    }
    private void initView(View view)
    {
        addimgbtn = view.findViewById(R.id.fragwmbook_addimgbtn);
        addimgurlbtn = view.findViewById(R.id.fragwmbook_addimgurlbtn);
        imgbook = view.findViewById(R.id.fragwmbook_bookimg);
        imgurledt = view.findViewById(R.id.fragwmbook_imgurledt);
        edtadddate = view.findViewById(R.id.fragwmbook_adddateedt);
        edtauthorname = view.findViewById(R.id.fragwmbook_authoredt);
        edtbookid = view.findViewById(R.id.fragwmbook_bookidedt);
        edtbookname = view.findViewById(R.id.fragwmbook_booknameedt);
        edtpublisher = view.findViewById(R.id.fragwmbook_publisheredt);
        edtquantity = view.findViewById(R.id.fragwmbook_quantityedt);
        btncancel = view.findViewById(R.id.fragwmbook_btncancel);
        btnsubmit = view.findViewById(R.id.fragwmbook_btncreate);
        btnaddexistbook = view.findViewById(R.id.fragwmbook_btnaddexistbook);
    }
    private void setBtnaddexistbook()
    {
        btnaddexistbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = WMActivity.fragmentManager.beginTransaction();
                transaction.replace(R.id.wmact_fragcontainer,new WMAddExistBookFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void setBtnsubmit()
    {
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WMActivity.fragmentManager.popBackStack();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookid = edtbookid.getText().toString().trim();
                if(bookid.isEmpty() || bookid.length() == 0)
                {
                    edtbookid.setError("Must have value");
                    valid = false;
                }
                else
                {
                    if(bookid.length() < 7)
                    {
                        edtbookid.setError("Must have more than 7 characters");
                        valid = false;
                    }
                    else
                    {
                        bookcollection.whereEqualTo("id", bookid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() > 0) {
                                        edtbookid.setError("Already have this id");
                                        valid = false;
                                    }
                                    else
                                    {
                                        String bookname = edtbookname.getText().toString().trim();
                                        if(bookname.isEmpty() || bookname.length() == 0)
                                        {
                                            edtbookname.setError("Must have value");
                                            valid = false;
                                        }
                                        else
                                        {
                                            if(bookname.length() < 3)
                                            {
                                                valid = false;
                                                edtbookname.setError("Must have more than 3 character");
                                            }
                                        }
                                        String authorname = edtauthorname.getText().toString().trim();
                                        String publisher = edtpublisher.getText().toString().trim();
                                        String quantity = edtquantity.getText().toString().trim();
                                        if(quantity.isEmpty() || quantity.length() == 0)
                                        {
                                            edtquantity.setError("Must have value");
                                            valid = false;
                                        }
                                        edtadddate.setText(Utils.dateToString(Timestamp.now().toDate()));
                                        if(valid)
                                        {
                                            showProgressDialog();
                                            String img_url = null;
                                            byte[] hinhAnh = null;
                                            if(!imgurledt.getText().toString().trim().isEmpty() || imgurledt.getText().toString().trim().length() > 0)
                                            {
                                                img_url = imgurledt.getText().toString().trim();
                                                BookOffline bookOffline = new BookOffline(bookid,authorname,img_url,
                                                        bookname.toUpperCase(),
                                                        publisher,null,null,Integer.parseInt(quantity),Integer.parseInt(quantity),
                                                        0,0,0,Timestamp.now(),Timestamp.now()
                                                        ,BookOffline.BookState.UnAccepted.value, null);
                                                setPopupAccept(bookOffline);
                                            }
                                            else
                                            {
                                                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgbook.getDrawable();
                                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                                                hinhAnh = outputStream.toByteArray();
                                                String path = "libraryapp/" + UUID.randomUUID() + ".png";
                                                sr = fs.getReference(path);
                                                sr.putBytes(hinhAnh).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        sr.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task)
                                                            {
                                                                BookOffline bookOffline = new BookOffline(bookid,authorname,null,
                                                                        bookname.toUpperCase(),
                                                                        publisher,null,null,Integer.parseInt(quantity),Integer.parseInt(quantity),
                                                                        0,0,0,Timestamp.now(),Timestamp.now()
                                                                        ,BookOffline.BookState.UnAccepted.value, task.getResult().toString());
                                                                setPopupAccept(bookOffline);
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                        valid = true;
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    private void showProgressDialog()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
    private void setPopupAccept(BookOffline bookOffline)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView title = dialog.findViewById(R.id.alert_popup_title)
                ,maintxt = dialog.findViewById(R.id.alert_popup_maintxt);
        ImageView img = dialog.findViewById(R.id.alert_popup_img);
        Button btnacc = dialog.findViewById(R.id.alert_popup_accbtn),
                btnig = dialog.findViewById(R.id.alert_popup_igbtn);
        title.setText("XÁC NHẬN NHẬP SÁCH");
        maintxt.setText("Bạn xác nhận nhập sách này không ?");
        img.setImageResource(getContext().getResources().getIdentifier("kitty2","drawable",getContext().getPackageName()));
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookcollection.document(bookOffline.getId()).set(bookOffline);
                edtbookid.setText("");
                edtbookname.setText("");
                edtquantity.setText("");
                edtauthorname.setText("");
                edtpublisher.setText("");
                imgurledt.setText("");
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private void setAddimgbtn()
    {
        addimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectimgbtn != 1 || imgurledt.getVisibility() == View.GONE) {
                    imgbook.setVisibility(View.VISIBLE);
                    imgurledt.setVisibility(View.GONE);
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startPickImg.launch(i);
                    selectimgbtn = 1;
                }
            }
        });
    }
    private void setAddimgurlbtn()
    {
        addimgurlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectimgbtn !=2)
                {
                    selectimgbtn = 2;
                    imgbook.setVisibility(View.GONE);
                    imgurledt.setVisibility(View.VISIBLE);
                }
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
                            imgbook.setImageBitmap(selectedImgBit);
                        }
                    }
                }
            });
    private void showPopUpNof()
    {
        db.collection("Notification").whereEqualTo("receiver.id", WMActivity.user.getId())
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