package com.example.libraryapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class User implements Parcelable {
    private String id;
    private String loginname,username,email,password,role,avatar;
    private boolean isMale;
    private Timestamp add_date;

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    User(Parcel in)
    {
        this.id = in.readString();
        this.loginname = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.role = in.readString();
        this.avatar = in.readString();
        this.isMale = in.readByte() != 0;
        this.add_date = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(loginname);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(role);
        dest.writeString(avatar);
        dest.writeByte((byte)(isMale ? 1 : 0));
        dest.writeParcelable(add_date,flags);
    }

    public User() {
    }

    public User(String username, String role, String avatar) {
        this.username = username;
        this.role = role;
        this.avatar = avatar;
    }
    public User(String id, String loginname, String username, String email,
                String password, String role, String avatar,
                boolean isMale,Timestamp add_date) {
        this.id = id;
        this.loginname = loginname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
        this.isMale = isMale;
        this.add_date = add_date;
    }

    public Timestamp getAdd_date() {
        return add_date;
    }

    public void setAdd_date(Timestamp add_date) {
        this.add_date = add_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean getisMale() {
        return isMale;
    }

    public void setisMale(boolean male) {
        isMale = male;
    }
}
