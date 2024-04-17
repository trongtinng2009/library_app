package com.example.libraryapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.libraryapp.Activity.AdminActivity;
import com.example.libraryapp.Activity.WMActivity;
import com.example.libraryapp.Adapter.NofAdapter;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.Notification;
import com.example.libraryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WMSendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WMSendFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Notification> notifications;
    private FirebaseFirestore db;
    private TextView total;
    private SearchView searchView;
    private NofAdapter nofAdapter;
    private ImageButton sendbtn;
    private RecyclerView rcv;
    private Button btnsearchname,btnsearchid,btndel,btndelall;
    private int selectop = 1;

    public WMSendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WMSendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WMSendFragment newInstance(String param1, String param2) {
        WMSendFragment fragment = new WMSendFragment();
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
        return inflater.inflate(R.layout.fragment_w_m_send, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        nofAdapter = new NofAdapter(notifications,getContext(),R.layout.notification_item);
        setRcv();
        setSendbtn();
        setSearchView();
        setSearchOp();
        setBtndel();
    }
    private void setSearchOp()
    {
        btnsearchname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectop != 1)
                {
                    selectop = 1;
                    setSearchOptionClick(selectop);
                    nofAdapter.setSearchoption(selectop);
                }
            }
        });
        btnsearchid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectop != 2)
                {
                    selectop = 2;
                    setSearchOptionClick(selectop);
                    nofAdapter.setSearchoption(selectop);
                }
            }
        });
    }
    private void setSearchOptionClick(int select)
    {
        if(select == 1)
        {
            btnsearchid.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
            btnsearchname.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_redbrown));
        }
        else if(select == 2)
        {
            btnsearchid.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_redbrown));
            btnsearchname.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),R.color.pas_cream));
        }
    }
    private void setSendbtn()
    {
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSendNofFragment adminSendNofFragment = new AdminSendNofFragment();
                Bundle b = new Bundle();
                b.putParcelable("user", WMActivity.user);
                adminSendNofFragment.setArguments(b);
                FragmentTransaction transaction= WMActivity.fragmentManager2.beginTransaction();
                transaction.replace(R.id.wmact_fragcontainer,adminSendNofFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void setBtndel()
    {
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               nofAdapter.setDel();
               total.setText("Tổng: " + notifications.size());
            }
        });
        btndelall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nofAdapter.setDelAll();
                total.setText("Tổng: " + notifications.size());
            }
        });
    }
    private void initView(View view)
    {
        btndel = view.findViewById(R.id.fragwmsend_del);
        btndelall = view.findViewById(R.id.fragwmsend_delall);
        notifications = new ArrayList<>();
        rcv = view.findViewById(R.id.fragwmsend_rcv);
        searchView = view.findViewById(R.id.fragwmsend_searchview);
        sendbtn = view.findViewById(R.id.fragwmsend_sendbtn);
        total = view.findViewById(R.id.fragwmsend_total);
        btnsearchid = view.findViewById(R.id.fragwmsend_searchid);
        btnsearchname = view.findViewById(R.id.fragwmsend_searchname);
    }
    private void setRcv()
    {
        db.collection("Notification").
                whereEqualTo("receiver.id", WMActivity.user.getId())
                .whereEqualTo("popup",false)
                .orderBy("senddate", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                Notification notification = queryDocumentSnapshot.toObject(Notification.class);
                                notifications.add(notification);
                            }
                            total.setText("Tổng: "+Integer.toString(task.getResult().size()));
                            rcv.setAdapter(nofAdapter);
                            rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                        }
                    }
                });
    }
    private void setSearchView()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                nofAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                nofAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}