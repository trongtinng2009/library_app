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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.libraryapp.Activity.AdminActivity;
import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAddStaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddStaffFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView txtloginname,txtusername,txtemail,txtpassword;
    private Spinner spinnergender,spinnerrole;
    private ImageView img;
    private boolean valid;
    private ImageButton backbtn;
    private Button btncancel,btnaccept;

    public AdminAddStaffFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAddStaffFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAddStaffFragment newInstance(String param1, String param2) {
        AdminAddStaffFragment fragment = new AdminAddStaffFragment();
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
        return inflater.inflate(R.layout.fragment_admin_add_staff, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setAcceptBtn();
        setSpinner();
        setPickImg();
        valid = true;
    }
    private void initView(View view)
    {
        txtemail = view.findViewById(R.id.fragadminaddstaff_email);
        txtloginname = view.findViewById(R.id.fragadminaddstaff_loginname);
        txtpassword = view.findViewById(R.id.fragadminaddstaff_password);
        txtusername = view.findViewById(R.id.fragadminaddstaff_username);
        btnaccept = view.findViewById(R.id.fragadminaddstaff_acceptbtn);
        btncancel = view.findViewById(R.id.fragadminaddstaff_cancelbtn);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.fragmentManager.popBackStack();
            }
        });
        backbtn = view.findViewById(R.id.fragadminaddstaff_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.fragmentManager.popBackStack();
            }
        });
        spinnergender = view.findViewById(R.id.fragadminaddstaff_spinnergender);
        spinnerrole = view.findViewById(R.id.fragadminaddstaff_spinnerrole);
        img = view.findViewById(R.id.fragadminaddstaff_img);
    }
    private void setSpinner()
    {
        List<String> gender = Arrays.asList("Male","Female");
        spinnergender.setAdapter(new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,gender));
        List<String> role = Arrays.asList("Librarian","WM");
        spinnerrole.setAdapter(new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,role));
    }
    private void setAcceptBtn()
    {
        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginname = txtloginname.getText().toString().trim();
                if(loginname.isEmpty() || loginname.length() == 0)
                {
                    valid = false;
                    txtloginname.setError("Must have value");
                }
                else
                {
                    MainActivity.db.collection("User").whereEqualTo("loginname",loginname)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        if(task.getResult().size() < 1)
                                        {
                                            String email = txtemail.getText().toString().trim();
                                            if(email.isEmpty() || email.length() == 0)
                                            {
                                                txtemail.setError("Must have value");
                                                valid = false;
                                            }
                                            else
                                            {
                                                Pattern emailregex =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                                                if(!emailregex.matcher(email).matches())
                                                {
                                                    txtemail.setText("Not an email");
                                                    valid = false;
                                                }
                                            }
                                            String username = txtusername.getText().toString().trim();
                                            if (username.isEmpty() || username.length() == 0)
                                            {
                                                valid = false;
                                                txtusername.setError("Must have value");
                                            }
                                            String password = txtpassword.getText().toString().trim();
                                            if(password.isEmpty() || password.length() == 0)
                                            {
                                                valid = false;
                                                txtpassword.setError("Must have value");
                                            }
                                            else
                                            {
                                                if(password.length() < 8) {
                                                    valid = false;
                                                    txtpassword.setError("Must have more than 8 characters");
                                                }
                                            }
                                            if(valid)
                                            {
                                                BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
                                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                                                byte[] hinhAnh = outputStream.toByteArray();
                                                String imgstring = Base64.encodeToString(hinhAnh,Base64.DEFAULT);
                                                boolean isMale;
                                                String gender = spinnerrole.getSelectedItem().toString();
                                                if(gender == "Male")
                                                    isMale = true;
                                                else
                                                    isMale = false;
                                                User user = new User(Utils.getRandomId("user", 9998),
                                                        loginname,username,email,password,spinnerrole.getSelectedItem().toString(),
                                                        imgstring,isMale, Timestamp.now());
                                                MainActivity.db.collection("User")
                                                        .document(user.getId()).set(user);
                                                showSuccessPopup();
                                            }
                                        }
                                        else
                                        {
                                            txtloginname.setError("Existing login name");
                                            valid = false;
                                        }
                                    }
                                }
                            });
                }
                valid = true;
            }
        });
    }
    private void showSuccessPopup()
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.reg_libcard_popup);
        TextView title = dialog.findViewById(R.id.reg_libcard_popup_title),
                maintxt = dialog.findViewById(R.id.reg_libcard_popup_txt);
        Button btnacc = dialog.findViewById(R.id.reg_libcard_popup_accept);
        ImageButton btnig = dialog.findViewById(R.id.reg_libcard_popup_cancel);
        title.setText("THÀNH CÔNG");
        maintxt.setText("Bạn đã tạo tài khoản này thành công !");
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                txtloginname.setText("");
                txtemail.setText("");
                txtpassword.setText("");
                txtusername.setText("");
            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                txtloginname.setText("");
                txtemail.setText("");
                txtpassword.setText("");
                txtusername.setText("");
            }
        });
        dialog.show();
    }
    private void setPickImg()
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