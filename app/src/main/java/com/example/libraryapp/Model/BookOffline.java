package com.example.libraryapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;

public class BookOffline implements Parcelable {
    private String id,author,img_url,name,publisher,summary,img_blob;
    private ArrayList<String> categories;
    private int quantity,remain_quantity,view_point,borrow_point;
    private double rating_point;
    private Timestamp add_date,pub_date;
    private int bookstate;
    public enum BookState
    {
        UnAccepted(1),ACCEPTED(2),UnAvailable(3);
        public int value;
        private BookState(int value)
        {
            this.value = value;
        }
    }

    public static final Parcelable.Creator<BookOffline> CREATOR = new Creator<BookOffline>() {
        @Override
        public BookOffline createFromParcel(Parcel source) {
            return new BookOffline(source);
        }

        @Override
        public BookOffline[] newArray(int size) {
            return new BookOffline[size];
        }
    };
    BookOffline(Parcel in)
    {
        this.id = in.readString();
        this.author = in.readString();
        this.img_url = in.readString();
        this.name = in.readString();
        this.publisher = in.readString();
        this.summary = in.readString();
        this.img_blob = in.readString();
        this.categories = in.createStringArrayList();
        this.quantity = in.readInt();
        this.remain_quantity = in.readInt();
        this.view_point = in.readInt();
        this.borrow_point = in.readInt();
        this.rating_point = in.readDouble();
        this.add_date = in.readParcelable(Timestamp.class.getClassLoader());
        this.pub_date = in.readParcelable(Timestamp.class.getClassLoader());
        this.bookstate = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(img_url);
        dest.writeString(name);
        dest.writeString(publisher);
        dest.writeString(summary);
        dest.writeString(img_blob);
        dest.writeStringList(categories);
        dest.writeInt(quantity);
        dest.writeInt(remain_quantity);
        dest.writeInt(view_point);
        dest.writeInt(borrow_point);
        dest.writeDouble(rating_point);
        dest.writeParcelable(add_date,flags);
        dest.writeParcelable(pub_date,flags);
        dest.writeInt(bookstate);
    }

    public BookOffline() {
    }

    public BookOffline(String id, String author, String img_url, String name, String publisher, String summary,
                       ArrayList<String> categories,
                       int quantity, int remain_quantity, int view_point, int borrow_point,
                       double rating_point, Timestamp add_date, Timestamp pub_date,
                       int bookstate,String img_blob) {
        this.id = id;
        this.author = author;
        this.img_url = img_url;
        this.name = name;
        this.publisher = publisher;
        this.summary = summary;
        this.categories = categories;
        this.quantity = quantity;
        this.remain_quantity = remain_quantity;
        this.view_point = view_point;
        this.borrow_point = borrow_point;
        this.rating_point = rating_point;
        this.add_date = add_date;
        this.pub_date = pub_date;
        this.bookstate = bookstate;
        this.img_blob = img_blob;
    }

    public String getImg_blob() {
        return img_blob;
    }

    public void setImg_blob(String img_blob) {
        this.img_blob = img_blob;
    }

    public int getBookstate() {
        return bookstate;
    }

    public void setBookstate(int bookstate) {
        this.bookstate = bookstate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRemain_quantity() {
        return remain_quantity;
    }

    public void setRemain_quantity(int remain_quantity) {
        this.remain_quantity = remain_quantity;
    }

    public int getView_point() {
        return view_point;
    }

    public void setView_point(int view_point) {
        this.view_point = view_point;
    }

    public int getBorrow_point() {
        return borrow_point;
    }

    public void setBorrow_point(int borrow_point) {
        this.borrow_point = borrow_point;
    }

    public double getRating_point() {
        return rating_point;
    }

    public void setRating_point(double rating_point) {
        this.rating_point = rating_point;
    }

    public Timestamp getAdd_date() {
        return add_date;
    }

    public void setAdd_date(Timestamp add_date) {
        this.add_date = add_date;
    }

    public Timestamp getPub_date() {
        return pub_date;
    }

    public void setPub_date(Timestamp pub_date) {
        this.pub_date = pub_date;
    }
}
