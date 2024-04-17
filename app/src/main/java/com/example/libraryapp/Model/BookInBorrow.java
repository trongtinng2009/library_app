package com.example.libraryapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.libraryapp.Utils.GetBookListener;
import com.example.libraryapp.Utils.GetLibcardListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class BookInBorrow implements Parcelable {
    private String id;
    private String book,libcard;
    private BookOffline bookOfflineobj;
    private Libcard libcardobj;
    private Timestamp borrow_date,due_date,return_date;
    private boolean isReturned,isAccepted,isBookOffline;

    private int quantity,weekOfThisBook;


    public static final Parcelable.Creator<BookInBorrow> CREATOR = new Creator<BookInBorrow>() {
        @Override
        public BookInBorrow createFromParcel(Parcel source) {
            return new BookInBorrow(source);
        }

        @Override
        public BookInBorrow[] newArray(int size) {
            return new BookInBorrow[size];
        }
    };
    BookInBorrow(Parcel in)
    {
        this.id = in.readString();
        this.book = in.readString();
        this.libcard = in.readString();
        this.bookOfflineobj = in.readParcelable(BookOffline.class.getClassLoader());
        this.libcardobj = in.readParcelable(Libcard.class.getClassLoader());
        this.borrow_date = in.readParcelable(Timestamp.class.getClassLoader());
        this.due_date = in.readParcelable(Timestamp.class.getClassLoader());
        this.return_date = in.readParcelable(Timestamp.class.getClassLoader());
        this.isReturned = in.readByte() != 0;
        this.isAccepted = in.readByte() !=0;
        this.isBookOffline = in.readByte() != 0;
        this.quantity = in.readInt();
        this.weekOfThisBook = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(book);
        dest.writeString(libcard);
        dest.writeParcelable(bookOfflineobj,flags);
        dest.writeParcelable(libcardobj,flags);
        dest.writeParcelable(borrow_date,flags);
        dest.writeParcelable(due_date,flags);
        dest.writeParcelable(return_date,flags);
        dest.writeByte((byte) (isReturned ? 1 : 0));
        dest.writeByte((byte) (isAccepted ? 1 : 0));
        dest.writeByte((byte) (isBookOffline ? 1 : 0));
        dest.writeInt(quantity);
        dest.writeInt(weekOfThisBook);
    }

    public BookInBorrow() {
    }

    public BookInBorrow(String id, String book,
                        String user, Timestamp borrow_date, Timestamp due_date, boolean isReturned,
                        boolean isAccepted, boolean isBookOffline, int quantity) {
        this.id = id;
        this.book = book;
        this.libcard = user;
        this.borrow_date = borrow_date;
        this.due_date = due_date;
        this.isReturned = isReturned;
        this.isAccepted = isAccepted;
        this.isBookOffline = isBookOffline;
        this.quantity = quantity;
    }

    public BookOffline bookofflineobjGet() {
        return bookOfflineobj;
    }

    public void bookOfflineobjSet(GetBookListener getBookListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference bookref = db.document(book);
        bookref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    bookOfflineobj = task.getResult().toObject(BookOffline.class);
                    getBookListener.OnComplete(bookOfflineobj);
                }
            }
        });
    }

    public Timestamp calDuedate(int due_day)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrow_date.toDate());
        calendar.add(Calendar.DATE,due_day);
        return new Timestamp(calendar.getTime());
    }
    public int calDayLeft()
    {
        Calendar calendar = Calendar.getInstance();
        Calendar due_calendar = Calendar.getInstance();
        due_calendar.setTime(due_date.toDate());
        int duedate = due_calendar.get(Calendar.DAY_OF_YEAR);
        int currentdate = calendar.get(Calendar.DAY_OF_YEAR);
        return duedate - currentdate;
    }
    public Libcard libcardobjGet() {
        return libcardobj;
    }

    public void libcardobjSet(GetLibcardListener getLibcardListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(libcard).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful())
               {
                   libcardobj = task.getResult().toObject(Libcard.class);
                   getLibcardListener.OnComplete(libcardobj);
               }
            }
        });
    }

    public Timestamp getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Timestamp return_date) {
        this.return_date = return_date;
    }

    public int getWeekOfThisBook()
    {
        Date date = borrow_date.toDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public void setWeekOfThisBook(int weekOfThisBook) {
        this.weekOfThisBook = weekOfThisBook;
    }

    public boolean isBookOffline() {
        return isBookOffline;
    }

    public void setBookOffline(boolean bookOffline) {
        isBookOffline = bookOffline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getLibcard() {
        return libcard;
    }

    public void setLibcard(String user) {
        this.libcard = user;
    }

    public Timestamp getBorrow_date() {
        return borrow_date;
    }

    public void setBorrow_date(Timestamp borrow_date) {
        this.borrow_date = borrow_date;
    }

    public Timestamp getDue_date() {
        return due_date;
    }

    public void setDue_date(Timestamp due_date) {
        this.due_date = due_date;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
