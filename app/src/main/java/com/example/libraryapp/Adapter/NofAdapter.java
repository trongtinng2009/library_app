package com.example.libraryapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.Activity.AdminActivity;
import com.example.libraryapp.Activity.LibrarianActivity;
import com.example.libraryapp.Activity.WMActivity;
import com.example.libraryapp.Fragment.NofDetalFragment;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.BookOffline;
import com.example.libraryapp.Model.Notification;
import com.example.libraryapp.R;
import com.example.libraryapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NofAdapter extends RecyclerView.Adapter<NofAdapter.NofHolder> implements Filterable {
    ArrayList<Notification> notifications,notificationsold;
    Context context;
    HashMap<String,Integer> checked = new HashMap<>();
    int layoutid;
    private FirebaseFirestore db;
    int searchoption = 1;

    public int getSearchoption() {
        return searchoption;
    }

    public void setSearchoption(int searchoption) {
        this.searchoption = searchoption;
    }

    public NofAdapter(ArrayList<Notification> notifications, Context context, int layoutid) {
        this.notifications = notifications;
        notificationsold = notifications;
        this.context = context;
        this.layoutid = layoutid;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                {
                    notifications = notificationsold;
                }
                else
                {
                    ArrayList<Notification> tempnof = new ArrayList<>();
                    for(Notification notification : notificationsold)
                    {
                        if(searchoption == 1)
                        {
                            if(notification.getSender().getUsername().toLowerCase().contains(search.toLowerCase()))
                                tempnof.add(notification);
                        }
                        else if(searchoption == 2)
                        {
                            if(notification.getSender().getId().toLowerCase().contains(search.toLowerCase()))
                                tempnof.add(notification);
                        }
                    }
                    notifications = tempnof;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = notifications;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifications = (ArrayList<Notification>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public NofHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NofHolder(LayoutInflater.from(context).inflate(layoutid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NofHolder holder, int position) {
        Notification notification = notifications.get(position);
        int pos = position;
        NofHolder nofHolder = (NofHolder) holder;
        nofHolder.txttitle.setText(notification.getTitle());
        nofHolder.txtsender.setText("Người gửi: "+notification.getSender().getUsername());
        nofHolder.txtsendate.setText("Gửi vào: "+ Utils.dateToString(notification.getSenddate().toDate()));
        nofHolder.chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    checked.put(notification.getId(), pos);
                }
                else
                {
                    checked.remove(notification.getId(),(Integer) pos);
                }
            }
        });
        nofHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = null;
                Bundle b = new Bundle();
                NofDetalFragment nofDetalFragment = new NofDetalFragment();
                b.putParcelable("nof",notification);
                nofDetalFragment.setArguments(b);
                if(MainActivity.user != null)
                {
                    transaction = MainActivity.fragmentManager3.beginTransaction();
                    transaction.replace(R.id.mainact_fragmentcontainer,nofDetalFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(LibrarianActivity.user != null)
                {
                    transaction = LibrarianActivity.fragmentManager3.beginTransaction();
                    transaction.replace(R.id.libact_fragmentcontainer,nofDetalFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(AdminActivity.user != null)
                {
                    transaction = AdminActivity.fragmentManager3.beginTransaction();
                    transaction.replace(R.id.adminact_fragmentcontainer,nofDetalFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(WMActivity.user != null)
                {
                    transaction = WMActivity.fragmentManager2.beginTransaction();
                    transaction.replace(R.id.wmact_fragcontainer,nofDetalFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }
    public void setDel()
    {
        if (!checked.isEmpty())
        {
            for (String key : checked.keySet()) {
                notifications.remove((int)checked.get(key));
                db.collection("Notification").document(key).delete();
                checked.remove(key);
                notifyDataSetChanged();
            }
        }
    }
    public void setDelAll()
    {
        if(notifications.size() > 0) {
            String receiverid = notifications.get(0).getReceiver().getId();
            notifications.clear();
            db.collection("Notification").whereEqualTo("receiver.id",receiverid)
                    .whereEqualTo("popup",false)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                {
                                    queryDocumentSnapshot.getReference().delete();
                                }
                                notifyDataSetChanged();
                            }
                        }
                    });
        }
    }
    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NofHolder extends RecyclerView.ViewHolder
    {
        TextView txttitle,txtsender,txtsendate;
        CheckBox chkbox;
        ImageButton del;
        public NofHolder(@NonNull View itemView) {
            super(itemView);
            del = itemView.findViewById(R.id.nofitem_del);
            txtsendate = itemView.findViewById(R.id.nofitem_receiver);
            txtsender = itemView.findViewById(R.id.nofitem_sender);
            txttitle = itemView.findViewById(R.id.nofitem_title);
            chkbox = itemView.findViewById(R.id.nofitem_chkbox);
        }
    }
}
