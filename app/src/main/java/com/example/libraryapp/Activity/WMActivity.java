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

import com.example.libraryapp.Fragment.WMBookFragment;
import com.example.libraryapp.Fragment.WMProfileFragment;
import com.example.libraryapp.Fragment.WMSendFragment;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;

public class WMActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager,fragmentManager2,fragmentManager3;
    private WMBookFragment wmBookFragment;
    private WMProfileFragment wmProfileFragment;
    private WMSendFragment wmSendFragment;
    public static User user;
    enum SelectBtn
    {
        BOOK(1),SEND(2),PROFILE(3);
        private int value;
        private SelectBtn(int value)
        {
            this.value = value;
        }
    }
    int selectedbtn = SelectBtn.BOOK.value;
    LinearLayout layoutbook,layoutsend,layoutprofile;
    ImageView imgbook,imgsend,imgprofile;
    TextView txtbook,txtsend,txtprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmactivity);
        fragmentManager = getSupportFragmentManager();
        fragmentManager2 = getSupportFragmentManager();
        fragmentManager3 = getSupportFragmentManager();
        wmBookFragment = new WMBookFragment();
        wmProfileFragment = new WMProfileFragment();
        wmSendFragment = new WMSendFragment();
        initView();
        handleSelect();
        setFragment(fragmentManager,wmBookFragment);
    }
    private void initView()
    {
        layoutbook = findViewById(R.id.wmact_linearlayoutbook);
        layoutprofile = findViewById(R.id.wmact_linearlayoutprofile);
        layoutsend = findViewById(R.id.wmact_linearlayoutsend);
        imgbook = findViewById(R.id.wmact_imgbook);
        imgprofile = findViewById(R.id.wmact_imgprofile);
        imgsend = findViewById(R.id.wmact_imgsend);
        txtbook = findViewById(R.id.wmact_textbook);
        txtprofile = findViewById(R.id.wmact_textprofile);
        txtsend = findViewById(R.id.wmact_textsend);
        user = getIntent().getParcelableExtra("user");
    }
    private void setFragment(FragmentManager fragmentManager, Fragment fragment)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_left);
        transaction.replace(R.id.wmact_fragcontainer,fragment);
        transaction.commit();
    }
    private void handleSelect()
    {
        TextView[][] textViews = {{txtsend,txtprofile,txtbook},
                {txtbook,txtprofile,txtsend},
                {txtbook,txtsend,txtprofile}};
        ImageView[][] imageViews = {{imgsend,imgprofile,imgbook},
                {imgbook,imgprofile,imgsend},
                {imgbook,imgsend,imgprofile}};
        LinearLayout[][] linearLayouts = {{layoutsend,layoutprofile,layoutbook},
                {layoutbook,layoutprofile,layoutsend},
                {layoutbook,layoutsend,layoutprofile}};
        int[][] drawables = {{R.drawable.send,R.drawable.person,R.drawable.menu_book_selected},
                {R.drawable.menu_book,R.drawable.person,R.drawable.send_selected},
                {R.drawable.menu_book,R.drawable.send,R.drawable.person_selected}};
        int[] bgcolor = {R.drawable.round_home,R.drawable.round_nof,R.drawable.round_profile};

        layoutbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedbtn != SelectBtn.BOOK.value)
                {
                    idenBtn(textViews[0], imageViews[0], linearLayouts[0],drawables[0],bgcolor[0],SelectBtn.BOOK.value);
                    setFragment(fragmentManager,wmBookFragment);
                }
            }
        });
        layoutsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedbtn != SelectBtn.SEND.value)
                {
                    idenBtn(textViews[1],imageViews[1],linearLayouts[1],drawables[1],bgcolor[1],SelectBtn.SEND.value);
                    setFragment(fragmentManager2,wmSendFragment);
                }
            }
        });
        layoutprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedbtn != SelectBtn.PROFILE.value)
                {
                    idenBtn(textViews[2],imageViews[2],linearLayouts[2],drawables[2],bgcolor[2],SelectBtn.PROFILE.value);
                    setFragment(fragmentManager3,wmProfileFragment);
                }
            }
        });
    }
    private void idenBtn(TextView[] textViews,
                         ImageView[] imageViews, LinearLayout[] linearLayouts,
                         int[] drawables, int bgcolor, int btn)
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