package com.example.libraryapp.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

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
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private EditText loginedt,nameedt,emailedt,passwordedt,repassedt;
    private ImageView img;
    private Button signupbtn;
    private RadioButton male,female;
    ImageButton backbtn;
    private boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        setSignupbtn();
        valid = true;
    }
    private void initView()
    {
        loginedt = findViewById(R.id.signupact_loginname);
        nameedt = findViewById(R.id.signupact_username);
        emailedt = findViewById(R.id.signupact_email);
        passwordedt = findViewById(R.id.signupact_password);
        repassedt = findViewById(R.id.signupact_repassword);
        male = findViewById(R.id.signupact_male);
        backbtn = findViewById(R.id.signupact_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        female = findViewById(R.id.signupact_female);
        img = findViewById(R.id.signupact_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startPickImg.launch(i);
            }
        });
        signupbtn = findViewById(R.id.signupact_signupbtn);
    }
    private void setSignupbtn()
    {
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginname = loginedt.getText().toString().trim();
                if(loginname.isEmpty() || loginname.length() == 0)
                {
                    valid = false;
                    loginedt.setError("Must have value");
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
                                            String email = emailedt.getText().toString().trim();
                                            if(email.isEmpty() || email.length() == 0)
                                            {
                                                emailedt.setError("Must have value");
                                                valid = false;
                                            }
                                            else
                                            {
                                                Pattern emailregex =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                                                if(!emailregex.matcher(email).matches())
                                                {
                                                    emailedt.setText("Not an email");
                                                    valid = false;
                                                }
                                            }
                                            String username = nameedt.getText().toString().trim();
                                            if (username.isEmpty() || username.length() == 0)
                                            {
                                                valid = false;
                                                nameedt.setError("Must have value");
                                            }
                                            String password = passwordedt.getText().toString().trim();
                                            if(password.isEmpty() || password.length() == 0)
                                            {
                                                valid = false;
                                                passwordedt.setError("Must have value");
                                            }
                                            else
                                            {
                                                if(password.length() < 8) {
                                                    valid = false;
                                                    passwordedt.setError("Must have more than 8 characters");
                                                }
                                            }
                                            String repass = repassedt.getText().toString().trim();
                                            if(repass.isEmpty() || repass.length() == 0)
                                            {
                                                valid = false;
                                                repassedt.setError("Must have value");
                                            }
                                            else
                                            {
                                                if(!repass.equals(password))
                                                {
                                                    valid = false;
                                                    repassedt.setError("Not equal to password");
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
                                                if(male.isChecked())
                                                    isMale = true;
                                                else
                                                    isMale = false;
                                                User user = new User(Utils.getRandomId("user", 9998),
                                                        loginname,username,email,password,"Guest",
                                                        imgstring,isMale, Timestamp.now());
                                                MainActivity.db.collection("User")
                                                        .document(user.getId()).set(user);
                                                showSuccessPopup();
                                            }
                                        }
                                        else
                                        {
                                            loginedt.setError("Existing login name");
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
        Dialog dialog = Utils.getPopup(this,R.layout.reg_libcard_popup);
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
                finish();
            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
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
                                        SignupActivity.this.getContentResolver(),selectedImg
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