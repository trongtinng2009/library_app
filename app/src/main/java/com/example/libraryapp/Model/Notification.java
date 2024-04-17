package com.example.libraryapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Notification implements Parcelable {
    private String id,title,maintxt,img;
    private User sender,receiver;
    private boolean received,popup;
    private Timestamp senddate;

    public Notification() {
    }

    public Notification(String id, String title, String maintxt, String img, User sender, User receiver, boolean received, boolean popup, Timestamp senddate) {
        this.id = id;
        this.title = title;
        this.maintxt = maintxt;
        this.img = img;
        this.sender = sender;
        this.receiver = receiver;
        this.received = received;
        this.popup = popup;
        this.senddate = senddate;
    }

    public boolean isPopup() {
        return popup;
    }

    public void setPopup(boolean popup) {
        this.popup = popup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaintxt() {
        return maintxt;
    }

    public void setMaintxt(String maintxt) {
        this.maintxt = maintxt;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public Timestamp getSenddate() {
        return senddate;
    }

    public void setSenddate(Timestamp senddate) {
        this.senddate = senddate;
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
    Notification(Parcel in)
    {
        this.id = in.readString();
        this.title = in.readString();
        this.maintxt = in.readString();
        this.img = in.readString();
        this.sender = in.readParcelable(User.class.getClassLoader());
        this.receiver = in.readParcelable(User.class.getClassLoader());
        this.received = in.readByte() != 0;
        this.popup = in.readByte() != 0;
        this.senddate = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(maintxt);
        dest.writeString(img);
        dest.writeParcelable(sender,flags);
        dest.writeParcelable(receiver,flags);
        dest.writeByte((byte)(received ? 1 : 0));
        dest.writeByte((byte)(popup ? 1 : 0));
        dest.writeParcelable(senddate,flags);
    }
}
