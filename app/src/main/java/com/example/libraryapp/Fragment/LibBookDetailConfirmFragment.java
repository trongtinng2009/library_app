package com.example.libraryapp.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Adapter.AutoCategoryAdapter;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Category;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibBookDetailConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibBookDetailConfirmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText edtname,edtauthor,edtpublisher,edtsum;
    private MultiAutoCompleteTextView edtcate;
    private TextView txtbookid,txtquantity,txtaddate;
    private Button btnacc,btnig;
    private ImageView imgbook;
    private ImageButton edittoggle;
    private BookOffline bookReceived;
    private boolean canEdit,valid;
    private ArrayList<Category> categories;

    public LibBookDetailConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibBookDetailConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibBookDetailConfirmFragment newInstance(String param1, String param2) {
        LibBookDetailConfirmFragment fragment = new LibBookDetailConfirmFragment();
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
        return inflater.inflate(R.layout.fragment_lib_book_detail_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setDataForView();
        setMultiAutoTxt();
        setBtn();
        valid = true;
    }

    private void initView(View view) {
        bookReceived = getArguments().getParcelable("book");
        edtcate = view.findViewById(R.id.fraglibbookdelconfirm_cateedt);
        edtauthor = view.findViewById(R.id.fraglibbookdelconfirm_authoredt);
        edtname = view.findViewById(R.id.fraglibbookdelconfirm_booknameedt);
        edtsum = view.findViewById(R.id.fraglibbookdelconfirm_sumedt);
        edtpublisher = view.findViewById(R.id.fraglibbookdelconfirm_publisheredt);
        txtbookid = view.findViewById(R.id.fraglibbookdelconfirm_bookid);
        txtaddate = view.findViewById(R.id.fraglibbookdelconfirm_adddate);
        txtquantity = view.findViewById(R.id.fraglibbookdelconfirm_quantity);
        btnacc = view.findViewById(R.id.fraglibbookdelconfirm_btnaccept);
        btnig = view.findViewById(R.id.fraglibbookdelconfirm_btncancel);
        imgbook = view.findViewById(R.id.fraglibbookdelconfirm_imgbook);
        edittoggle = view.findViewById(R.id.fraglibbookdelconfirm_edittoggle);
        canEdit = false;
    }
    private void setDataForView()
    {
        edtpublisher.setText(bookReceived.getPublisher().toString());
        edtname.setText(bookReceived.getName().toString());
        edtauthor.setText(bookReceived.getAuthor().toString());
        txtbookid.setText("Book ID: "+ bookReceived.getId());
        txtquantity.setText("Số lượng: "+Integer.toString(bookReceived.getQuantity()));
        txtaddate.setText("Thêm vào: " +Utils.dateToString(bookReceived.getAdd_date().toDate()));
        if(bookReceived.getImg_url() != null)
        {
            Picasso.get().load(bookReceived.getImg_url()).into(imgbook);
        }
        else if(bookReceived.getImg_blob() != null)
        {
            Picasso.get().load(bookReceived.getImg_blob()).into(imgbook);
        }
        edittoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToggleEdit();
            }
        });
    }
    private void setMultiAutoTxt()
    {
        categories = new ArrayList<>();
        MainActivity.db.collection("Category")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                           {
                               Category category = queryDocumentSnapshot.toObject(Category.class);
                               categories.add(category);
                           }
                           edtcate.setAdapter(new AutoCategoryAdapter(getContext(),R.layout.cate_item_auto,categories));
                           edtcate.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                       }
                    }
                });
    }
    private void setBtn()
    {
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String summary = edtsum.getText().toString().trim();
                if(summary.isEmpty() || summary.length() == 0)
                {
                    edtsum.setError("Must have value");
                    valid = false;
                }
                else
                {
                    if(summary.length() < 50)
                    {
                        edtsum.setError("Must have more than 50 characters");
                        valid = false;
                    }
                }
                String cates = edtcate.getText().toString().trim();
                Map<String,String> categories = new HashMap<>();
                if(cates.length() == 0)
                {
                    valid = false;
                    edtcate.setError("Must have at least 1 category");
                }
                else
                {
                    String[] cate = cates.split("\\s*,\\s*");
                    for(String i : cate)
                    {
                        if(!categories.containsKey(i))
                        {
                            categories.put(i,i);
                        }
                    }
                }
                String bookname = edtname.getText().toString().trim();
                if(bookname.isEmpty() || bookname.length() == 0)
                {
                    edtname.setError("Must have value");
                    valid = false;
                }
                else
                {
                    if(bookname.length() <= 3)
                        edtname.setError("Must have more than 3 characters");
                }
                if(valid)
                {
                    CollectionReference categorycollection = MainActivity.db.collection("Category");
                    ArrayList<String> cateref = new ArrayList<>();
                  for(String i : categories.values())
                  {
                     DocumentReference documentReference = categorycollection.document(i);
                     cateref.add(documentReference.getPath());
                  }
                  bookReceived.setAdd_date(Timestamp.now());
                  bookReceived.setAuthor(edtauthor.getText().toString().trim());
                  bookReceived.setPublisher(edtpublisher.getText().toString().trim());
                  bookReceived.setCategories(cateref);
                  bookReceived.setName(bookname);
                  bookReceived.setSummary(summary);
                  bookReceived.setBookstate(BookOffline.BookState.ACCEPTED.value);
                  showAcceptPopup(bookReceived);
                }
                valid = true;
            }
        });
    }
    private void showAcceptPopup(BookOffline bookOffline)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView title = dialog.findViewById(R.id.alert_popup_title),
                maintxt = dialog.findViewById(R.id.alert_popup_maintxt);
        ImageView img = dialog.findViewById(R.id.alert_popup_img);
        Button btnacc = dialog.findViewById(R.id.alert_popup_accbtn),
                btnig = dialog.findViewById(R.id.alert_popup_igbtn);
        title.setText("XÁC NHẬN TẠO PHIẾU MƯỢN SÁCH");
        maintxt.setText("Bạn xác nhận duyệt sách này chứ ?");
        img.setImageResource(getContext().getResources().getIdentifier("kitty3","drawable",getContext().getPackageName()));
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MainActivity.db.collection("BookOffline")
                       .document(bookOffline.getId()).
                       update("add_date",bookOffline.getAdd_date()
                       ,"author",bookOffline.getAuthor()
                       ,"bookstate",bookOffline.getBookstate(),"categories",bookOffline.getCategories()
                       ,"name",bookOffline.getName(),"publisher",bookOffline.getPublisher()
                       ,"summary",bookOffline.getSummary());
               dialog.cancel();
               showSuccessPopup();
            }
        });
        dialog.show();
    }
    private void showSuccessPopup()
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.reg_libcard_popup);
        TextView title = dialog.findViewById(R.id.reg_libcard_popup_title),
                maintxt = dialog.findViewById(R.id.reg_libcard_popup_txt);
        Button btnacc = dialog.findViewById(R.id.reg_libcard_popup_accept);
        ImageButton btnig = dialog.findViewById(R.id.reg_libcard_popup_cancel);
        title.setText("THÀNH CÔNG");
        maintxt.setText("Bạn đã tạo xét duyệt sách thành công !");
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LibrarianActivity.fragmentManager.popBackStack();
            }
        });
        dialog.show();
    }
    private void setToggleEdit()
    {
        if(canEdit)
        {
            canEdit = false;
            edittoggle.setColorFilter(getResources().getColor(R.color.black));
            edtauthor.setEnabled(false);
            edtname.setEnabled(false);
            edtpublisher.setEnabled(false);
        }
        else
        {
            canEdit = true;
            edittoggle.setColorFilter(getResources().getColor(R.color.high_pink));
            edtauthor.setEnabled(true);
            edtname.setEnabled(true);
            edtpublisher.setEnabled(true);
        }
    }
}