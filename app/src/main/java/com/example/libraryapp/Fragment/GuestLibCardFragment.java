package com.example.libraryapp.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestLibCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestLibCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int haveCard;
    private LinearLayout borrowingbtn,borrowedbtn;
    private ConstraintLayout havecard,nothavecard,notlogin;
    private Button signuplibcardbtn;
    private EditText edtfirstname,edtlastname,edtphone,edtaddress
            ,edtday,edtmonth,edtyear;
    private Libcard userlibcard;

    private TextView txtlcfirstname,txtlclastname,
    txtlcaddress,txtlcdob,txtlcexp,txtlcid;
    private ImageView imglibcard;
    private CollectionReference libcardcollection = MainActivity.db.collection("Libcard")
            , usercollection = MainActivity.db.collection("User");
    public GuestLibCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestLibCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestLibCardFragment newInstance(String param1, String param2) {
        GuestLibCardFragment fragment = new GuestLibCardFragment();
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
        return inflater.inflate(R.layout.fragment_guest_lib_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if(MainActivity.user != null)
        {
           setCardState();
        }
        else
        {
            handleNotLogin();
        }
    }
    private void initView(View view)
    {
         havecard = view.findViewById(R.id.fragguestlib_havelibcard);
         nothavecard = view.findViewById(R.id.fragguestlib_nothavelibcard);
         notlogin = view.findViewById(R.id.fragguestlib_notlogin);
         signuplibcardbtn = view.findViewById(R.id.fragguestlib_regbtn);
         edtaddress = view.findViewById(R.id.fragguestlib_address);
         edtday = view.findViewById(R.id.fragguestlib_day);
         edtfirstname = view.findViewById(R.id.fragguestlib_firstname);
         edtlastname = view.findViewById(R.id.fragguestlib_lastname);
         edtmonth = view.findViewById(R.id.fragguestlib_month);
         edtyear = view.findViewById(R.id.fragguestlib_year);
         edtphone = view.findViewById(R.id.fragguestlib_phone);
         borrowingbtn = view.findViewById(R.id.fragguestlib_layoutborrowbook);
         borrowedbtn = view.findViewById(R.id.fragguestlib_layoutborrowed);

         txtlcaddress = view.findViewById(R.id.fragguestlib_libcardaddress);
         txtlcdob = view.findViewById(R.id.fragguestlib_libcarddob);
         txtlcexp = view.findViewById(R.id.fragguestlib_libcardexd);
         txtlcid = view.findViewById(R.id.fragguestlib_libcardid);
         txtlcfirstname = view.findViewById(R.id.fragguestlib_libcardfirstname);
         txtlclastname = view.findViewById(R.id.fragguestlib_libcardlastname);
         imglibcard = view.findViewById(R.id.fragguestlib_libcardimg);
    }
    private void setHaveCard()
    {
        setContentHaveCard();
        borrowingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuestBookBorrowingFragment guestBookBorrowingFragment = new GuestBookBorrowingFragment();
                FragmentTransaction transaction = MainActivity.fragmentManager2.beginTransaction();
                transaction.replace(R.id.mainact_fragmentcontainer,guestBookBorrowingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        borrowedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager2.beginTransaction();
                transaction.replace(R.id.mainact_fragmentcontainer,new GuestBorrowedBookFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void setContentHaveCard()
    {
        Date exp_date = userlibcard.getExp_date().toDate();
        Date dob = userlibcard.getDob().toDate();
       txtlclastname.setText("Họ : " + userlibcard.getLastname());
       txtlcfirstname.setText("Tên : " + userlibcard.getFirstname());
       txtlcid.setText("Mã thẻ : " + userlibcard.getId());
       txtlcexp.setText("Có giá trị đến : " + Utils.dateToString(exp_date));
       txtlcdob.setText("Ngày sinh : " + Utils.dateToString(dob));
       txtlcaddress.setText("Địa chỉ : " + userlibcard.getAddress());
        try {
            byte[] encodeByte = Base64.decode(userlibcard.getImage_url(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            imglibcard.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }

    }
    private void showPopup(int idimg,String titletext,String maintext)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.reg_libcard_popup);

        ImageButton cancelbtn = dialog.findViewById(R.id.reg_libcard_popup_cancel);
        ImageView img = dialog.findViewById(R.id.reg_libcard_popup_img);
        img.setImageResource(idimg);
        TextView title = dialog.findViewById(R.id.reg_libcard_popup_title),txt = dialog.findViewById(R.id.reg_libcard_popup_txt);
        title.setText(titletext);
        txt.setText(maintext);

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button acceptbtn = dialog.findViewById(R.id.reg_libcard_popup_accept);
        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(haveCard == 0)
                {
                 handleNotHaveCard();
                }
                else if(haveCard == 2)
                {
                    handleHaveCard();
                    libcardcollection.document(userlibcard.getId()).
                            update("cardstate",Libcard.CardState.ACCEPTED.value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void setCardState()
    {
        DocumentReference userRef = usercollection.document(MainActivity.user.getId());
        Query query = libcardcollection.whereEqualTo("user",userRef.getPath());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    int size = task.getResult().getDocuments().size();
                    if(size == 1)
                    {
                        Libcard libcard = task.getResult().getDocuments().get(0).toObject(Libcard.class);
                        haveCard = libcard.getCardstate();
                        if(haveCard == 1)
                        {
                            handleNotHaveCard();
                            handleWaitForAccept();
                            Date dob = libcard.getDob().toDate();
                            edtaddress.setText(libcard.getAddress());
                            edtday.setText(Integer.toString(dob.getDate()));
                            edtmonth.setText(Integer.toString(dob.getMonth()+1));
                            edtyear.setText(Integer.toString(dob.getYear()+1900));
                            edtfirstname.setText(libcard.getFirstname());
                            edtlastname.setText(libcard.getLastname());
                            edtphone.setText(libcard.getPhonenum());
                        }
                        else if(haveCard == 2)
                        {
                            userlibcard = libcard;
                            handleHaveCard();
                            setHaveCard();
                            showPopup(getContext().getResources().getIdentifier("messagecute","drawable",
                                    getContext().getPackageName()),"Bạn đã được xét duyệt",
                                    "Yêu cầu đăng ký thẻ thư viện của bạn đã được xét duyệt\n " +
                                            "Xin cảm ơn vì đã sử dụng dịch vụ của chúng tôi!");
                        }
                        else if(haveCard == 3)
                        {
                            userlibcard = libcard;
                            handleHaveCard();
                            setHaveCard();
                        }
                    }
                    else
                    {
                        haveCard = 0;
                        showPopup(getContext().getResources().getIdentifier("libcardpopup",
                                        "drawable",getContext().getPackageName()),
                                "Có vẻ bạn chưa có thẻ thư viện."
                                ,"Tạo thẻ thư viện để dùng được nhiều tính năng hơn nhé" );
                    }
                }
            }
        });
    }
    private void handleNotHaveCard()
    {
        nothavecard.setVisibility(View.VISIBLE);
        havecard.setVisibility(View.GONE);
        notlogin.setVisibility(View.GONE);
        handleSignupLibcard();
    }
    private void handleHaveCard()
    {
        nothavecard.setVisibility(View.GONE);
        havecard.setVisibility(View.VISIBLE);
        notlogin.setVisibility(View.GONE);
        handleSignupLibcard();
    }
    private void handleNotLogin()
    {
        nothavecard.setVisibility(View.GONE);
        havecard.setVisibility(View.GONE);
        notlogin.setVisibility(View.VISIBLE);
        handleSignupLibcard();
    }
    private void handleWaitForAccept()
    {
        edtphone.setFocusable(false);
        edtphone.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.low_grey));

        edtyear.setFocusable(false);
        edtyear.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.low_grey));

        edtmonth.setFocusable(false);
        edtmonth.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.low_grey));

        edtlastname.setFocusable(false);
        edtlastname.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.low_grey));

        edtfirstname.setFocusable(false);
        edtfirstname.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.low_grey));

        edtday.setFocusable(false);
        edtday.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.low_grey));

        edtaddress.setFocusable(false);
        edtaddress.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.low_grey));

        signuplibcardbtn.setEnabled(false);
        signuplibcardbtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.med_grey));
    }
    private void handleSignupLibcard()
    {
        signuplibcardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtaddress.getText() == null || edtday.getText() == null ||
                edtfirstname.getText() == null || edtlastname.getText() == null
               || edtmonth.getText() == null || edtphone.getText() == null
               || edtyear.getText() == null)
                {
                    Toast.makeText(getContext(),"Let's type all the info",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String randomid = Utils.getRandomId("LIB",9998);
                    String userid = MainActivity.user.getId();
                    String userimg = MainActivity.user.getAvatar();
                    DocumentReference userReference = usercollection.document(userid);
                    Query query = libcardcollection.whereEqualTo("id",randomid);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                int docsize = task.getResult().getDocuments().size();
                                if(docsize > 0)
                                {
                                    return;
                                }
                                else
                                {
                                    Timestamp dob = Utils.textToTimestamp(
                                            Integer.parseInt(edtday.getText().toString()),
                                            Integer.parseInt(edtmonth.getText().toString()),
                                            Integer.parseInt(edtyear.getText().toString()));
                                    Timestamp currentdate = Timestamp.now();
                                    Libcard libcard = new Libcard(randomid,edtfirstname.getText().toString()
                                    ,edtlastname.getText().toString(),edtaddress.getText().toString(),
                                            edtphone.getText().toString(),userimg,dob,currentdate,
                                            null,userReference.getPath(),Libcard.CardState.REQUESTING.value,null
                                            );
                                    libcardcollection.document(randomid).set(libcard);
                                    showPopup(getContext().getResources().
                                            getIdentifier("booklibwarning","drawable",getContext().getPackageName())
                                    ,"Gửi yêu cầu thành công",
                                            "Bạn đã gửi yêu cầu đăng ký thẻ thư viện thành công\n " +
                                                    "Vui lòng chờ đợi thủ thư xét duyệt yêu cầu nhé !");
                                    handleWaitForAccept();
                                }
                            }
                            else
                            {
                                Log.i("libcardrequesterror","error");
                            }
                        }
                    });
                }
            }
        });
    }
}