package com.example.libraryapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Comment implements Parcelable {
    private String id,content;
    private User user;
    private BookOffline bookOffline;
    private Timestamp add_date;

    public static final Parcelable.Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public Comment(String id, String content, User user, BookOffline bookOffline, Timestamp add_date) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.bookOffline = bookOffline;
        this.add_date = add_date;
    }

    Comment(Parcel in)
    {
        this.id = in.readString();
        this.content = in.readString();
        this.user =in.readParcelable(User.class.getClassLoader());
        this.add_date = in.readParcelable(Timestamp.class.getClassLoader());
        this.bookOffline = in.readParcelable(BookOffline.class.getClassLoader());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeParcelable(user,flags);
        dest.writeParcelable(add_date,flags);
        dest.writeParcelable(bookOffline,flags);
    }

    public Comment() {
    }

    public BookOffline getBookOffline() {
        return bookOffline;
    }

    public void setBookOffline(BookOffline bookOffline) {
        this.bookOffline = bookOffline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getAdd_date() {
        return add_date;
    }

    public void setAdd_date(Timestamp add_date) {
        this.add_date = add_date;
    }
}
