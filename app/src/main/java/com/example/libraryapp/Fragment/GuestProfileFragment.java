package com.example.libraryapp.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.libraryapp.Activity.AdminActivity;
import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Activity.SignupActivity;
import com.example.libraryapp.Activity.WMActivity;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Libcard;
import com.example.libraryapp.Model.Notification;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean login = false;
    private TextView usernametxt,signuptxt;
    private ImageView useravatar;
    private EditText loginnameedt,loginpassedt;
    private Button loginbtn,logoutbtn;
    private FirebaseFirestore db;
    private RelativeLayout layoutnoaccount;
    private LinearLayout layouthaveaccount,btnfav;
    private CollectionReference usercollection = MainActivity.db.collection("User"),
    libcardcollection = MainActivity.db.collection("Libcard");
    public GuestProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestProfileFragment newInstance(String param1, String param2) {
        GuestProfileFragment fragment = new GuestProfileFragment();
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
        return inflater.inflate(R.layout.fragment_guest_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        if(!login)
         login();
        setFavBtn();
    }
    private void initView(View view)
    {
        btnfav = view.findViewById(R.id.fragguestprofile_favbtn);
        useravatar = view.findViewById(R.id.fragguestprofile_avt);
        usernametxt = view.findViewById(R.id.fragguestprofile_username);
        loginbtn = view.findViewById(R.id.fragguesthome_loginbtn);
        loginnameedt = view.findViewById(R.id.fragguestprofile_loginname);
        loginpassedt = view.findViewById(R.id.fragguestprofile_loginpass);
        layouthaveaccount = view.findViewById(R.id.fragguesthome_haveaccountlayout);
        layoutnoaccount = view.findViewById(R.id.fragguesthome_nothaveaccountlayout);
        logoutbtn = view.findViewById(R.id.fragguestprofile_logout);
        signuptxt = view.findViewById(R.id.fragguestprofile_signuptext);
        signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), SignupActivity.class));
            }
        });
        if(login)
        {
            try {
                byte[] encodeByte = Base64.decode(MainActivity.user.getAvatar(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                        encodeByte.length);
                useravatar.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.getMessage();
            }
            usernametxt.setVisibility(View.VISIBLE);
            usernametxt.setText("Chào " + MainActivity.user.getUsername());
            layouthaveaccount.setVisibility(View.VISIBLE);
            logoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.user = null;
                    MainActivity.libcard = null;
                    getActivity().finish();
                    getContext().startActivity(new Intent(getContext(), MainActivity.class));
                }
            });
            layoutnoaccount.setVisibility(View.GONE);
        }
    }
    private void setFavBtn()
    {
        btnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FragmentTransaction transaction = MainActivity.fragmentManager4.beginTransaction();
               transaction.replace(R.id.mainact_fragmentcontainer,new GuestFavoriteFragment());
               transaction.addToBackStack(null);
               transaction.commit();
            }
        });
    }
    private void login()
    {
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginname = loginnameedt.getText().toString();
                String loginpass = loginpassedt.getText().toString();
              Query query = usercollection.whereEqualTo("loginname",loginname)
                      .whereEqualTo("password",loginpass);
              query.get().addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful())
                      {
                          QuerySnapshot documentSnapshot = task.getResult();
                          if(!documentSnapshot.isEmpty())
                          {
                              User user = documentSnapshot.getDocuments().get(0).toObject(User.class);
                              if(!user.getRole().equals("Guest"))
                              {
                                  getActivity().finish();
                                  Intent i = null;
                                  if(user.getRole().equals("Admin")) {
                                      i = new Intent(getContext(), AdminActivity.class);
                                      i.putExtra("user",user);
                                      getContext().startActivity(i);
                                  }
                                  else if(user.getRole().equals("Librarian"))
                                  {
                                      i = new Intent(getContext(), LibrarianActivity.class);
                                      i.putExtra("user",user);
                                      getContext().startActivity(i);
                                  }
                                  else if(user.getRole().equals("WM"))
                                  {
                                      i = new Intent(getContext(), WMActivity.class);
                                      i.putExtra("user",user);
                                      getContext().startActivity(i);
                                  }
                              }
                              else
                              {
                                  MainActivity.user = user;
                                  DocumentReference userref = usercollection.document(MainActivity.user.getId());
                                  Query query1 = libcardcollection.whereEqualTo("user",userref.getPath());
                                  query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                          if(task.isSuccessful())
                                          {
                                              if(!task.getResult().isEmpty())
                                              {
                                              MainActivity.libcard = task.getResult().getDocuments().get(0).toObject(Libcard.class);}
                                          }
                                      }
                                  });
                                  login = true;
                                  logoutbtn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          getActivity().finish();
                                          getContext().startActivity(new Intent(getContext(), MainActivity.class));
                                      }
                                  });
                                  try {
                                      byte[] encodeByte = Base64.decode(MainActivity.user.getAvatar(), Base64.DEFAULT);
                                      Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                                              encodeByte.length);
                                      useravatar.setImageBitmap(bitmap);
                                  } catch (Exception e) {
                                      e.getMessage();
                                  }
                                  usernametxt.setVisibility(View.VISIBLE);
                                  usernametxt.setText("Hello " + MainActivity.user.getUsername());
                                  layouthaveaccount.setVisibility(View.VISIBLE);
                                  layoutnoaccount.setVisibility(View.GONE);
                                  ScaleAnimation scaleAnimation = new ScaleAnimation(
                                          0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,
                                          0.0f,Animation.RELATIVE_TO_SELF,0.0f
                                  );
                                  scaleAnimation.setDuration(200);
                                  scaleAnimation.setFillAfter(true);
                                  layouthaveaccount.setAnimation(scaleAnimation);
                                  showPopUpNof();
                              }
                          }
                      }
                      else
                      {
                          Log.i("error","error fetch data");
                      }
                  }
              });
            }
        });
    }
    private void showPopUpNof()
    {
        db.collection("Notification").whereEqualTo("receiver.id",MainActivity.user.getId())
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
    @Override
    public void onResume() {
        super.onResume();
    }
}