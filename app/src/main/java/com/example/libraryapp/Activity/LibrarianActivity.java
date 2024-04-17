package com.example.libraryapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.libraryapp.Fragment.AdminProfileFragment;
import com.example.libraryapp.Fragment.LibrarianBookFragment;
import com.example.libraryapp.Fragment.LibrarianCardFragment;
import com.example.libraryapp.Fragment.LibrarianProfileFragment;
import com.example.libraryapp.Fragment.LibrarianSendFragment;
import com.example.libraryapp.Fragment.ManageAccountFragment;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;

public class LibrarianActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager,fragmentManager2,fragmentManager3,fragmentManager4;
    private LibrarianBookFragment librarianBookFragment;
    private LibrarianCardFragment librarianCardFragment;
    public static User user;
    private LibrarianProfileFragment librarianProfileFragment;
    private LibrarianSendFragment librarianSendFragment;
    enum SelectedBtn
    {
        BOOK(1),LIBCARD(2),SEND(3),PROFILE(4);
        private int value;
        private SelectedBtn(int value)
        {
            this.value = value;
        }
    }
    int selectedbtn = SelectedBtn.BOOK.value;
    LinearLayout layoutbook,layoutcard,layoutsend,layoutprofile;
    ImageView imgbook,imgcard,imgsend,imgprofile;
    TextView txtbook,txtcard,txtsend,txtprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian);
        fragmentManager = getSupportFragmentManager();
        fragmentManager2 = getSupportFragmentManager();
        fragmentManager3 = getSupportFragmentManager();
        fragmentManager4 = getSupportFragmentManager();
        initView();
        handleSelected();
        setFragment(fragmentManager,librarianBookFragment);
    }
    private void initView()
    {
        layoutbook = findViewById(R.id.libact_linearlayoutbook);
        layoutcard = findViewById(R.id.libact_linearlayoutlibcard);
        layoutsend = findViewById(R.id.libact_linearlayoutsend);
        layoutprofile = findViewById(R.id.libact_linearlayoutprofile);

        imgbook = findViewById(R.id.libact_imgbook);
        imgcard = findViewById(R.id.libact_imglibcard);
        imgsend = findViewById(R.id.libact_imgsend);
        imgprofile = findViewById(R.id.libact_imgprofile);

        txtbook = findViewById(R.id.libact_textbook);
        txtcard = findViewById(R.id.libact_textlibcard);
        txtsend = findViewById(R.id.libact_textsend);
        txtprofile = findViewById(R.id.libact_textprofile);

        librarianBookFragment = new LibrarianBookFragment();
        librarianCardFragment = new LibrarianCardFragment();
        librarianProfileFragment = new LibrarianProfileFragment();
        librarianSendFragment = new LibrarianSendFragment();
        user = getIntent().getParcelableExtra("user");
    }
    private void setFragment(FragmentManager fragmentManager,Fragment fragment)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_left);
        transaction.replace(R.id.libact_fragmentcontainer,fragment);
        transaction.commit();
    }
    private void handleSelected()
    {
        TextView[][] textViews = {{txtcard,txtsend,txtprofile,txtbook},
                {txtbook,txtsend,txtprofile,txtcard},
                {txtbook,txtcard,txtprofile,txtsend},
                {txtbook,txtsend,txtcard,txtprofile}};
        ImageView[][] imageViews = {{imgcard,imgsend,imgprofile,imgbook},
                {imgbook,imgsend,imgprofile,imgcard},
                {imgbook,imgcard,imgprofile,imgsend},
                {imgbook,imgsend,imgcard,imgprofile}};
        LinearLayout[][] linearLayouts = {{layoutcard,layoutsend,layoutprofile,layoutbook},
                {layoutbook,layoutsend,layoutprofile,layoutcard},
                {layoutbook,layoutcard,layoutprofile,layoutsend},
                {layoutbook,layoutsend,layoutcard,layoutprofile}};
        int[][] drawables = {{R.drawable.local_library,R.drawable.send,R.drawable.person,R.drawable.menu_book_selected},
                {R.drawable.menu_book,R.drawable.send,R.drawable.person,R.drawable.local_library_selected},
                {R.drawable.menu_book,R.drawable.local_library,R.drawable.person,R.drawable.send_selected},
                {R.drawable.menu_book,R.drawable.send,R.drawable.local_library,R.drawable.person_selected}};
        int[] bgcolor = {R.drawable.round_home,R.drawable.round_locallib,R.drawable.round_nof,R.drawable.round_profile};

        layoutbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedbtn != SelectedBtn.BOOK.value)
                {
                    setFragment(fragmentManager,librarianBookFragment);
                    idenBtn(textViews[0],imageViews[0],linearLayouts[0],drawables[0],bgcolor[0],
                            SelectedBtn.BOOK.value);
                }
            }
        });
        layoutcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedbtn != SelectedBtn.LIBCARD.value)
                {
                    setFragment(fragmentManager2,librarianCardFragment);
                    idenBtn(textViews[1],imageViews[1],linearLayouts[1],drawables[1],bgcolor[1],
                            SelectedBtn.LIBCARD.value);
                }
            }
        });
        layoutsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedbtn != SelectedBtn.SEND.value)
                {
                    setFragment(fragmentManager3,librarianSendFragment);
                    idenBtn(textViews[2],imageViews[2],linearLayouts[2],drawables[2],bgcolor[2],
                            SelectedBtn.SEND.value);
                }
            }
        });
        layoutprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedbtn != SelectedBtn.PROFILE.value)
                {
                    setFragment(fragmentManager4,librarianProfileFragment);
                    idenBtn(textViews[3],imageViews[3],linearLayouts[3],drawables[3],bgcolor[3],
                            SelectedBtn.PROFILE.value);
                }
            }
        });
    }
    private void idenBtn(TextView[] textViews,
                         ImageView[] imageViews,LinearLayout[] linearLayouts,
                         int[] drawables,int bgcolor,int btn)
    {
        for(int i = 0;i<textViews.length-1;i++)
        {
            textViews[i].setVisibility(View.GONE);
            imageViews[i].setImageResource(drawables[i]);
            linearLayouts[i].setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        textViews[textViews.length -1].setVisibility(View.VISIBLE);
        imageViews[imageViews.length - 1].setImageResource(drawables[drawables.length-1]);
        linearLayouts[linearLayouts.length-1].setBackgroundResource(bgcolor);
        selectedbtn = btn;

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,
                0.0f,Animation.RELATIVE_TO_SELF,0.0f
        );
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        linearLayouts[linearLayouts.length-1].setAnimation(scaleAnimation);
    }
}