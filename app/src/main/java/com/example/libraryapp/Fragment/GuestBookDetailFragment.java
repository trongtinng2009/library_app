package com.example.libraryapp.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.libraryapp.Adapter.CommentAdapter;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookInBorrow;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Category;
import com.example.libraryapp.Model.Comment;
import com.example.libraryapp.Model.Favorite;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestBookDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestBookDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private String mParam2;
    private int bookBorrowInWeek = 0,clickinfobtn;
    private Bundle b;
    private BookOffline bookOffline;
    private EditText edtcomment;
    private RecyclerView rcvcmt;
    private ImageButton btncmt;
    private ImageButton backbtn,favbtn;
    private boolean valid = true;
    private ImageView bgimg,bookimg;
    private FirebaseFirestore db;
    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;
    private TextView txtquantity,txtsummary,txttitle,btnborrow,txtauthor,txtauthordel,
            txtview,txtpubdel,txtadddel,txtcatedel;
    private LinearLayout layoutcomment,layoutdetail;
    private TextView btnoverview,btndetail,btncomment;
    private CollectionReference bookinborrowcollection
            = MainActivity.db.collection("BookInBorrow")
            ,libcardcollection = MainActivity.db.collection("Libcard")
            ,bookofflinecollection = MainActivity.db.collection("BookOffline");

    public GuestBookDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestBookDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestBookDetailFragment newInstance(String param1, String param2) {
        GuestBookDetailFragment fragment = new GuestBookDetailFragment();
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
        return inflater.inflate(R.layout.fragment_guest_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        comments = new ArrayList<>();
        initView(view);
        clickinfobtn = 1;
        handleBorrowBookbtn();
        setVisibilityLayoutinfo();
        setClickInfo();
        setRcvcmt();
        setSendCmt();
        if(MainActivity.user != null) {
            setFavColor();
            setFavbtn();
        }
    }
    private void setClickInfo()
    {
        btnoverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickinfobtn != 1)
                {
                    clickinfobtn = 1;
                    btnoverview.setBackgroundTintList(ContextCompat.getColorStateList(
                            getContext(), R.color.pas_medcream));
                    btncomment.setBackgroundTintList(ContextCompat.getColorStateList(
                            getContext(), R.color.pas_redbrown));
                    btndetail.setBackgroundTintList(ContextCompat.getColorStateList(
                            getContext(), R.color.pas_redbrown));
                    setVisibilityLayoutinfo();
                }
            }
        });
        btndetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickinfobtn != 2)
                {
                    clickinfobtn = 2;
                    btnoverview.setBackgroundTintList(ContextCompat.getColorStateList(
                            getContext(), R.color.pas_redbrown));
                    btncomment.setBackgroundTintList(ContextCompat.getColorStateList(
                            getContext(), R.color.pas_redbrown));
                    btndetail.setBackgroundTintList(ContextCompat.getColorStateList(
                            getContext(), R.color.pas_medcream));
                    setVisibilityLayoutinfo();
                }
            }
        });
        btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickinfobtn != 3)
                {
                    clickinfobtn = 3;
                    btnoverview.setBackgroundTintList(ContextCompat.getColorStateList(
                            getContext(), R.color.pas_redbrown));
                    btncomment.setBackgroundTintList(ContextCompat.getColorStateList(
                            getContext(), R.color.pas_medcream));
                    btndetail.setBackgroundTintList(ContextCompat.getColorStateList(
                            getContext(), R.color.pas_redbrown));
                    setVisibilityLayoutinfo();
                }
            }
        });
    }
    private void setVisibilityLayoutinfo()
    {
        if(clickinfobtn == 1)
        {
            txtsummary.setVisibility(View.VISIBLE);
            layoutdetail.setVisibility(View.GONE);
            layoutcomment.setVisibility(View.GONE);
        }
        else if(clickinfobtn == 2)
        {
            txtsummary.setVisibility(View.GONE);
            layoutdetail.setVisibility(View.VISIBLE);
            layoutcomment.setVisibility(View.GONE);
        }
        else if(clickinfobtn == 3)
        {
            txtsummary.setVisibility(View.GONE);
            layoutdetail.setVisibility(View.GONE);
            layoutcomment.setVisibility(View.VISIBLE);
        }
    }
    private void initView(View view)
    {
        b = this.getArguments();
        bookOffline = b.getParcelable("book");
        favbtn = view.findViewById(R.id.fragguestbookdetail_favbtn);
        txtauthor = view.findViewById(R.id.fragguestbookdetail_bookauthor);
        bgimg = view.findViewById(R.id.fragguestbookdetail_bgimg);
        bookimg = view.findViewById(R.id.fragguestbookdetail_bookimg);
        txtquantity = view.findViewById(R.id.fragguestbookdetail_remainquantity);
        txtsummary = view.findViewById(R.id.fragguestbookdetail_summarytxt);
        txttitle = view.findViewById(R.id.fragguestbookdetail_booktitle);
        txtadddel = view.findViewById(R.id.fragguestbookdetail_adddeltxt);
        txtauthordel = view.findViewById(R.id.fragguestbookdetail_authordeltxt);
        edtcomment = view.findViewById(R.id.fragguestbookdetail_edtcomment);
        rcvcmt = view.findViewById(R.id.fragguestbookdetail_cmtrcv);
        btncmt = view.findViewById(R.id.fragguestbookdetail_cmtbtn);
        txtcatedel = view.findViewById(R.id.fragguestbookdetail_catetxt);
        txtpubdel = view.findViewById(R.id.fragguestbookdetail_pubdeltxt);
        btnborrow = view.findViewById(R.id.fragguestbookdetail_btnborrow);
        layoutcomment = view.findViewById(R.id.fragguestbookdetail_commentlayout);
        layoutdetail = view.findViewById(R.id.fragguestbookdetail_detaillayout);
        txtview = view.findViewById(R.id.fragguestbookdetail_viewtxt);
        backbtn = view.findViewById(R.id.fragguestbookdetail_backbtn);
        btnoverview = view.findViewById(R.id.fragguestbookdetail_overview);
        btncomment = view.findViewById(R.id.fragguestbookdetail_comment);
        btndetail = view.findViewById(R.id.fragguestbookdetail_detail);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
        setContentForViews();
    }
    String cate = "";
    private void setContentForViews()
    {
        if(bookOffline.getImg_url() != null)
        {
            Picasso.get().load(bookOffline.getImg_url()).into(bgimg);
            Picasso.get().load(bookOffline.getImg_url()).into(bookimg);
        }
        else if(bookOffline.getImg_blob() != null)
        {
            Picasso.get().load(bookOffline.getImg_blob()).into(bgimg);
            Picasso.get().load(bookOffline.getImg_blob()).into(bookimg);
        }
        txttitle.setText(bookOffline.getName());
        if(bookOffline.getAuthor().length() > 0) {
            txtauthor.setText("Tác giả: " + bookOffline.getAuthor());
            txtauthordel.setText("Tác giả: " + bookOffline.getAuthor());
        }
        else
        {
            txtauthor.setText("Tác giả: Đang cập nhật");
            txtauthordel.setText("Tác giả: Đang cập nhật");
        }
        if(bookOffline.getPublisher().length() > 0)
        {
            txtpubdel.setText("Nhà xuất bản: " + bookOffline.getPublisher());
        }
        else
        {
            txtpubdel.setText("Nhà xuất bản: Đang cập nhật");
        }
        for (String i : bookOffline.getCategories())
        {
            db.document(i).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                  if(task.isSuccessful())
                  {
                      cate += task.getResult().toObject(Category.class).getName() + ", ";
                  }
                  txtcatedel.setText("Thể loại: " + cate);
                }
            });
        }
        txtadddel.setText("Thêm vào: " + Utils.dateToString(bookOffline.getAdd_date().toDate()));
        txtsummary.setText(bookOffline.getSummary());
        txtquantity.setText(Integer.toString(bookOffline.getRemain_quantity()));
        txtview.setText(Integer.toString(bookOffline.getView_point()));
    }
    private void setSendCmt()
    {
        btncmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmt = edtcomment.getText().toString().trim();
                if(MainActivity.user == null)
                {
                    edtcomment.setError("Hãy đăng nhập để bình luận");
                }
                else
                {
                    if (cmt.isEmpty() || cmt.length() < 0) {
                        edtcomment.setError("Phải có ký tự");
                    } else
                    {
                        DocumentReference cmtref = db.collection("Comment").document();
                        Comment comment = new Comment(cmtref.getId(), cmt, MainActivity.user,
                                bookOffline,Timestamp.now());
                        cmtref.set(comment);
                        comments.add(0,comment);
                        commentAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    private void setRcvcmt()
    {
        commentAdapter = new CommentAdapter(getContext(),R.layout.comment_item,comments);
        db.collection("Comment").
                whereEqualTo("bookOffline.id",bookOffline.getId())
                .orderBy("add_date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                           {
                               Comment comment = queryDocumentSnapshot.toObject(Comment.class);
                               comments.add(comment);
                           }
                           rcvcmt.setAdapter(commentAdapter);
                           rcvcmt.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                       }
                    }
                });
    }
    private void setFavColor()
    {
        db.collection("Favorite").
                whereEqualTo("user.id",MainActivity.user.getId())
                .whereEqualTo("bookOffline.id",bookOffline.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().size() > 0)
                            {
                                favbtn.setColorFilter(getResources().getColor(R.color.high_pink));
                            }
                            else
                            {
                                favbtn.setColorFilter(getResources().getColor(R.color.white));
                            }
                        }
                    }
                });
    }
    private void checkFav()
    {
        db.collection("Favorite").whereEqualTo("user.id",MainActivity.user.getId())
                .whereEqualTo("bookOffline.id",bookOffline.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful())
                      {
                          if(task.getResult().size() > 0)
                          {
                              favbtn.setColorFilter(getResources().getColor(R.color.white));
                              task.getResult().getDocuments().get(0).getReference().delete();
                          }
                          else
                          {
                              favbtn.setColorFilter(getResources().getColor(R.color.high_pink));
                              DocumentReference ref =db.collection("Favorite").document();
                              Favorite favorite = new Favorite(ref.getId(),MainActivity.user,bookOffline);
                              ref.set(favorite);
                          }
                      }
                    }
                });
    }
    private void setFavbtn()
    {
        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFav();
            }
        });
    }
    private void handleBorrowBookbtn()
    {
        if(MainActivity.libcard != null && MainActivity.libcard.getCardstate() != Libcard.CardState.REQUESTING.value)
        {
            btnborrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int limit_book = getContext().getResources().getInteger(R.integer.limit_borrow_books_per_week);
                    Timestamp[] currentmonthdate = Utils.getStartToCurrentMonthDate();

                    DocumentReference libcard = libcardcollection.document(MainActivity.libcard.getId()),
                            book = bookofflinecollection.document(bookOffline.getId());
                    BookInBorrow bookInBorrow = new
                            BookInBorrow(Utils.getRandomAlphabeticalId(8),
                            book.getPath(), libcard.getPath(), Timestamp.now(), null,
                            false, false, true, 1);
                    Query querySearchAllBorrowBookInMonth =
                            bookinborrowcollection.whereEqualTo("libcard", libcard.getPath())
                                    .whereGreaterThanOrEqualTo("borrow_date", currentmonthdate[0])
                                    .whereLessThanOrEqualTo("borrow_date", currentmonthdate[1]);

                    querySearchAllBorrowBookInMonth.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                {
                                    BookInBorrow bookInBorrow2 = queryDocumentSnapshot.toObject(BookInBorrow.class);
                                    if (bookInBorrow2.getWeekOfThisBook() == bookInBorrow.getWeekOfThisBook()) {
                                        bookBorrowInWeek++;
                                    }
                                }
                                if (bookBorrowInWeek < limit_book)
                                {
                                    Query querySearchForDuplicateBook =
                                            bookinborrowcollection.
                                                    whereEqualTo("libcard", libcard.getPath())
                                                    .whereEqualTo("book", book.getPath())
                                                    .whereGreaterThanOrEqualTo("borrow_date", currentmonthdate[0])
                                                    .whereLessThanOrEqualTo("borrow_date", currentmonthdate[1]);
                                    querySearchForDuplicateBook.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                if (task.getResult().size() >= 1)
                                                {
                                                    boolean isDuplicate = false;
                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                                    {
                                                        BookInBorrow bookInBorrow1 = queryDocumentSnapshot.
                                                                toObject(BookInBorrow.class);
                                                        if (bookInBorrow1.getWeekOfThisBook() == bookInBorrow.getWeekOfThisBook())
                                                        {
                                                            isDuplicate = true;
                                                            break;
                                                        }
                                                    }
                                                    if (isDuplicate)
                                                    {
                                                        setBookUnavailable("RẤT TIẾC",
                                                                "Bạn đã mượn quyển sách này trong tuần rồi !");
                                                    } else
                                                    {
                                                        bookinborrowcollection.
                                                                whereEqualTo("returned",false)
                                                                .whereEqualTo("accepted",true)
                                                                .whereEqualTo("libcard",libcard.getPath())
                                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                                {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if(task.isSuccessful())
                                                                        {
                                                                            boolean valid = true;
                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                                                            {
                                                                                BookInBorrow bookInBorroww = queryDocumentSnapshot.toObject(BookInBorrow.class);
                                                                                if(bookInBorroww.calDayLeft() <= 0)
                                                                                {
                                                                                    valid = false;
                                                                                    break;
                                                                                }
                                                                            }
                                                                            if(valid)
                                                                            {
                                                                                setBookAvailable(bookBorrowInWeek, bookInBorrow);
                                                                            }
                                                                            else
                                                                            {
                                                                                setBookUnavailable("BẠN CHƯA TRẢ SÁCH",
                                                                                        "Hiện bạn không thể mượn sách do bạn vẫn còn sách chưa trả !");
                                                                            }
                                                                        }
                                                                    }

                                                                });
                                                    }
                                                }
                                                else
                                                {
                                                    bookinborrowcollection.
                                                            whereEqualTo("returned",false)
                                                            .whereEqualTo("accepted",true)
                                                            .whereEqualTo("libcard",libcard.getPath())
                                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                            {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        boolean valid = true;
                                                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                                                        {
                                                                            BookInBorrow bookInBorroww = queryDocumentSnapshot.toObject(BookInBorrow.class);
                                                                            if(bookInBorroww.calDayLeft() <= 0)
                                                                            {
                                                                                valid = false;
                                                                                break;
                                                                            }
                                                                        }
                                                                        if(valid)
                                                                        {
                                                                            setBookAvailable(bookBorrowInWeek, bookInBorrow);
                                                                        }
                                                                        else
                                                                        {
                                                                            setBookUnavailable("BẠN CHƯA TRẢ SÁCH",
                                                                                    "Hiện bạn không thể mượn sách do bạn vẫn còn sách chưa trả !");
                                                                        }
                                                                    }
                                                                }

                                                            });
                                                }
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    setBookUnavailable("RẤT TIẾC",
                                            "Bạn đã hết lượt mượn sách trong tuần này");
                                }
                            }

                        }
                    });
                    bookBorrowInWeek = 0;
                }
            });
        }

        else
        {
            btnborrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBookUnavailable("BẠN CHƯA TẠO THẺ THƯ VIỆN",
                            "Bạn chưa tạo thẻ thư viện hoặc thẻ thư viện của bạn " +
                                    "đang chờ xác nhận !");
                }
            });
        }
    }
    private void setBookAvailable(int borrowedbook,BookInBorrow bookInBorrow)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView txtTitle,txtContent;
        txtTitle = dialog.findViewById(R.id.alert_popup_title);
        txtContent = dialog.findViewById(R.id.alert_popup_maintxt);
        txtTitle.setText("BẠN ĐỒNG Ý MƯỢN QUYỂN SÁCH NÀY KHÔNG");
        int borrow_limit = getContext().getResources().getInteger(R.integer.limit_borrow_books_per_week) - borrowedbook;
        txtContent.setText("Hiện bạn chỉ còn : \n " + borrow_limit + " lần mượn trong tuần này");

        ImageView img = dialog.findViewById(R.id.alert_popup_img);
        img.setImageResource(getContext().getResources().
                getIdentifier("kitty3","drawable",getContext().getPackageName()));
        Button btnacc = dialog.findViewById(R.id.alert_popup_accbtn),
        btnig = dialog.findViewById(R.id.alert_popup_igbtn);
        btnig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 bookinborrowcollection.document(bookInBorrow.getId()).set(bookInBorrow);
                 dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void setBookUnavailable(String title,String maintxt)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.reg_libcard_popup);
        TextView txtTitle,txtContent;
        txtTitle = dialog.findViewById(R.id.reg_libcard_popup_title);
        txtContent = dialog.findViewById(R.id.reg_libcard_popup_txt);
        txtTitle.setText(title);
        txtContent.setText(maintxt);

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
}