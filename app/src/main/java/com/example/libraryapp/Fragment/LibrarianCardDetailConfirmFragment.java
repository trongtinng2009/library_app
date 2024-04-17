package com.example.libraryapp.Fragment;

import android.app.Dialog;
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

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.GetUserListener;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;

import java.util.Date;

import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibrarianCardDetailConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarianCardDetailConfirmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView txtid,txtfirstname,txtlastname,
    txtphone,txtdob,txtrequestdate,txtaddress,txtname;
    private ImageView img;
    private Button accbtn,igbtn;
    private Bundle b;
    private Libcard libcard;
    private CollectionReference libcardcollection = MainActivity.db.collection("Libcard")
            , usercollection = MainActivity.db.collection("User");

    public LibrarianCardDetailConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarianCardDetailConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarianCardDetailConfirmFragment newInstance(String param1, String param2) {
        LibrarianCardDetailConfirmFragment fragment = new LibrarianCardDetailConfirmFragment();
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
        return inflater.inflate(R.layout.fragment_librarian_card_detail_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setContentForView();
        handleVerifybtn();
    }
    private void initView(View view)
    {
        b = getArguments();
        libcard = b.getParcelable("libcard");

        txtaddress = view.findViewById(R.id.fraglib_concarddetail_address);
        txtfirstname = view.findViewById(R.id.fraglib_concarddetail_firstname);
        txtdob = view.findViewById(R.id.fraglib_concarddetail_dob);
        txtid = view.findViewById(R.id.fraglib_concarddetail_libid);
        txtphone = view.findViewById(R.id.fraglib_concarddetail_phonenum);
        txtlastname = view.findViewById(R.id.fraglib_concarddetail_lastname);
        txtrequestdate = view.findViewById(R.id.fraglib_concarddetail_requestdate);
        txtname = view.findViewById(R.id.fraglib_concarddetail_name);

        img = view.findViewById(R.id.fraglib_concarddetail_img);

        accbtn = view.findViewById(R.id.fraglib_concarddetail_accbtn);
        igbtn = view.findViewById(R.id.fraglib_concarddetail_igbtn);
    }
    private void setContentForView()
    {
        Date request_date = libcard.getRequest_date().toDate();
        Date dob = libcard.getDob().toDate();
        txtrequestdate.setText(Utils.dateToString(request_date));
        txtlastname.setText(libcard.getLastname());
        txtaddress.setText(libcard.getAddress());
        txtphone.setText(libcard.getPhonenum());
        txtid.setText(libcard.getId());
        txtdob.setText(Utils.dateToString(dob));
        txtfirstname.setText(libcard.getFirstname());
        txtname.setText(libcard.getFirstname() + " " + libcard.getLastname());

        libcard.userobjSet(new GetUserListener() {
            @Override
            public void OnComplete(User user) {
                try {
                    byte[] encodeByte = Base64.decode(user.getAvatar(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                            encodeByte.length);
                    img.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

    }
    private void handleVerifybtn()
    {
        accbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
                showAlertPopup(dialog,getContext().getResources().getIdentifier("kitty","drawable"
                        ,getContext().getPackageName()),"ĐỒNG Ý XÉT DUYỆT",
                        "Bạn đồng ý xét duyệt yêu cầu tạo thẻ thư viện này không"
                        );
            }
        });
    }
    private void handleAccept()
    {
        libcardcollection.document(libcard.getId()).update
                ("cardstate",Libcard.CardState.JUSTACCEPTED.value,
                        "acc_date", Timestamp.now()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    libcard.setAcc_date(Timestamp.now());
                    libcardcollection.document(libcard.getId()).
                            update("exp_date",libcard.calExpdate(2));
                    LibrarianActivity.fragmentManager2.popBackStack();
                }
            }
        });
    }
    private void showAlertPopup(Dialog dialog,int imgid,String title,String maintext)
    {
        TextView txttitle = dialog.findViewById(R.id.alert_popup_title)
                ,txtmain = dialog.findViewById(R.id.alert_popup_maintxt);
        ImageView img = dialog.findViewById(R.id.alert_popup_img);
        txtmain.setText(maintext);
        txttitle.setText(title);
        img.setImageResource(imgid);

        Button accbtn = dialog.findViewById(R.id.alert_popup_accbtn);
        accbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                handleAccept();
            }
        });
        dialog.show();
    }
}