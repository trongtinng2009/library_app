package com.example.libraryapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Favorite implements Parcelable {
    String id;
    User user;
    BookOffline bookOffline;

    public Favorite() {
    }

    public Favorite(String id, User user, BookOffline bookOffline) {
        this.id = id;
        this.user = user;
        this.bookOffline = bookOffline;
    }
    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel source) {
            return new Favorite(source);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
    Favorite(Parcel in)
    {
        this.id = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.bookOffline = in.readParcelable(BookOffline.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(user,flags);
        dest.writeParcelable(bookOffline,flags);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookOffline getBookOffline() {
        return bookOffline;
    }

    public void setBookOffline(BookOffline bookOffline) {
        this.bookOffline = bookOffline;
    }
}
