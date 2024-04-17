package com.example.libraryapp.Model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.libraryapp.Utils.GetUserListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Libcard implements Parcelable {
    private String id,firstname,lastname,address,phonenum,image_url;
    private Timestamp dob,request_date,acc_date,exp_date;
    private String user;
    private User userobj;
    private int cardstate;
    public static enum CardState
    {
        REQUESTING(1),JUSTACCEPTED(2),ACCEPTED(3);
        public int value;
        private CardState(int value)
        {
            this.value = value;
        }
    }
    public static final Parcelable.Creator<Libcard> CREATOR = new Creator<Libcard>() {
        @Override
        public Libcard createFromParcel(Parcel source) {
            return new Libcard(source);
        }

        @Override
        public Libcard[] newArray(int size) {
            return new Libcard[size];
        }
    };
    Libcard(Parcel in)
    {
        this.id = in.readString();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.address = in.readString();
        this.phonenum = in.readString();
        this.image_url = in.readString();
        this.dob = in.readParcelable(Timestamp.class.getClassLoader());
        this.request_date = in.readParcelable(Timestamp.class.getClassLoader());
        this.acc_date = in.readParcelable(Timestamp.class.getClassLoader());
        this.exp_date = in.readParcelable(Timestamp.class.getClassLoader());
        this.user = in.readString();
        this.userobj = in.readParcelable(User.class.getClassLoader());
        this.cardstate = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
         dest.writeString(id);
         dest.writeString(firstname);
         dest.writeString(lastname);
         dest.writeString(address);
         dest.writeString(phonenum);
         dest.writeString(image_url);
         dest.writeParcelable(dob,flags);
         dest.writeParcelable(request_date,flags);
         dest.writeParcelable(acc_date,flags);
         dest.writeParcelable(exp_date,flags);
         dest.writeString(user);
         dest.writeParcelable(userobj,flags);
         dest.writeInt(cardstate);
    }

    public Libcard() {
    }

    public Libcard(String id, String firstname, String lastname, String address, String phonenum,
                   String image_url, Timestamp dob, Timestamp request_date, Timestamp acc_date,
                   String user, int cardstate,
    Timestamp exp_date) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phonenum = phonenum;
        this.image_url = image_url;
        this.dob = dob;
        this.request_date = request_date;
        this.acc_date = acc_date;
        this.user = user;
        this.cardstate = cardstate;
        this.exp_date = exp_date;
    }

    public Timestamp calExpdate(int year)
    {
        Date acc_date = getAcc_date().toDate();
        Calendar plusyear = Calendar.getInstance();
        plusyear.setTime(acc_date);
        plusyear.add(Calendar.YEAR,year);
        return new Timestamp(plusyear.getTime());
    }

    public User userobjGet() {
        return userobj;
    }

    public void userobjSet(GetUserListener getUserListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userref = db.document(user);
        userref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    userobj = task.getResult().toObject(User.class);
                    getUserListener.OnComplete(userobj);
                }
            }
        });
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Timestamp getExp_date() {
        return exp_date;
    }

    public void setExp_date(Timestamp exp_date) {
        this.exp_date = exp_date;
    }

    public int getCardstate() {
        return cardstate;
    }

    public void setCardstate(int cardstate) {
        this.cardstate = cardstate;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public Timestamp getRequest_date() {
        return request_date;
    }

    public void setRequest_date(Timestamp request_date) {
        this.request_date = request_date;
    }

    public Timestamp getAcc_date() {
        return acc_date;
    }

    public void setAcc_date(Timestamp acc_date) {
        this.acc_date = acc_date;
    }

}
