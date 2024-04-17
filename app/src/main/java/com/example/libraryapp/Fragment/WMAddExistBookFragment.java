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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Activity.WMActivity;
import com.example.libraryapp.Adapter.AutoBookAdapter;
import com.example.libraryapp.Adapter.AutoLibCardAdapter;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.GetBookListener;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WMAddExistBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WMAddExistBookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView autobookid;
    private EditText edtquantity;
    private Button btncancel,btnimport;
    private LinearLayout bookinfo;
    private ImageView img;
    private TextView txtbook;
    private ArrayList<BookOffline> bookOfflines;
    private FirebaseFirestore db;
    private boolean valid;

    public WMAddExistBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WMAddExistBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WMAddExistBookFragment newInstance(String param1, String param2) {
        WMAddExistBookFragment fragment = new WMAddExistBookFragment();
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
        return inflater.inflate(R.layout.fragment_w_m_add_exist_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setBookId();
        setChangeBookId();
        setImportBtn();
    }
    private void initView(View view)
    {
        bookOfflines = new ArrayList<>();
        autobookid = view.findViewById(R.id.fragwmaddexist_bookid);
        edtquantity = view.findViewById(R.id.fragwmaddexist_quantityedt);
        btncancel = view.findViewById(R.id.fragwmaddexist_cancelbtn);
        btnimport = view.findViewById(R.id.fragwmaddexist_importbtn);
        bookinfo = view.findViewById(R.id.fragwmaddexist_bookinfolayout);
        img = view.findViewById(R.id.fragwmaddexist_bookimg);
        txtbook = view.findViewById(R.id.fragwmaddexist_bookname);
    }
    private void setImportBtn()
    {
        btnimport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookid = autobookid.getText().toString().trim();
                if(bookid.isEmpty() || bookid.length() == 0)
                {
                    autobookid.setError("Must have value");
                    valid = false;
                }
                String quantity = edtquantity.getText().toString().trim();
                if(quantity.isEmpty() || quantity.length() == 0)
                {
                    edtquantity.setError("Must have value");
                    valid = false;
                }
                else
                {
                    if(Integer.parseInt(quantity) < 1)
                    {
                        edtquantity.setError("Must greater than 0");
                        valid = false;
                    }
                }
                if(valid)
                {
                    setAlertPopup();
                }
                valid = true;
            }
        });
    }
    private void setSuccessPopUp()
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.reg_libcard_popup);
        TextView title = dialog.findViewById(R.id.reg_libcard_popup_title),
                maintxt = dialog.findViewById(R.id.reg_libcard_popup_txt);
        Button btnacc = dialog.findViewById(R.id.reg_libcard_popup_accept);
        ImageButton btnig = dialog.findViewById(R.id.reg_libcard_popup_cancel);
        title.setText("THÀNH CÔNG");
        maintxt.setText("Bạn đã nhập sách thành công !");
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                WMActivity.fragmentManager.popBackStack();
            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                WMActivity.fragmentManager.popBackStack();
            }
        });
        dialog.show();
    }
    private void setAlertPopup()
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView title = dialog.findViewById(R.id.alert_popup_title),
                maintxt = dialog.findViewById(R.id.alert_popup_maintxt);
        ImageView img = dialog.findViewById(R.id.alert_popup_img);
        Button btnacc = dialog.findViewById(R.id.alert_popup_accbtn),
                btnig = dialog.findViewById(R.id.alert_popup_igbtn);
        title.setText("XÁC NHẬN NHẬP SÁCH");
        maintxt.setText("Bạn xác nhận nhập sách này chứ ?");
        img.setImageResource(getContext().getResources().getIdentifier("kitty3","drawable",getContext().getPackageName()));
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference bookref =
                        db.collection("BookOffline")
                                        .document(autobookid.getText().toString().trim());
                bookref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(task.isSuccessful())
                       {
                           BookOffline bookOffline = task.getResult().toObject(BookOffline.class);
                           bookref.update("quantity",bookOffline.getQuantity() +
                                   Integer.parseInt(edtquantity.getText().toString().trim()),
                                   "remain_quantity",
                                   bookOffline.getRemain_quantity() + Integer.parseInt(edtquantity.getText().toString().trim()));
                       }
                    }
                });
                dialog.cancel();setSuccessPopUp();
            }
        });
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void setBookId()
    {
        db.collection("BookOffline").whereEqualTo("bookstate", BookOffline.BookState.ACCEPTED.value)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().size() > 0)
                            {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                {
                                    BookOffline bookOffline = queryDocumentSnapshot.toObject(BookOffline.class);
                                    bookOfflines.add(bookOffline);
                                }
                                autobookid.setAdapter(new AutoBookAdapter(getContext(),R.layout.book_item_for_auto,bookOfflines));
                            }
                        }
                    }
                });
    }
    private void setChangeBookId()
    {
        autobookid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookinfo.setVisibility(View.VISIBLE);
                BookOffline bookOffline = bookOfflines.get(position);
                if(bookOffline.getImg_blob() == null) {
                    Picasso.get().load(bookOfflines.get(position).getImg_url()).into(img);
                }
                else
                {
                    try {
                        byte[] encodeByte = Base64.decode(bookOffline.getImg_blob(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                                encodeByte.length);
                        img.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                txtbook.setText(bookOfflines.get(position).getName());
            }
        });
    }
}