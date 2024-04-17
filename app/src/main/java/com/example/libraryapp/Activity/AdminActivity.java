package com.example.libraryapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.libraryapp.Fragment.AdminProfileFragment;
import com.example.libraryapp.Fragment.AdminSendFragment;
import com.example.libraryapp.Fragment.AdminStatisticFragment;
import com.example.libraryapp.Fragment.AdminUserDetailFragment;
import com.example.libraryapp.Fragment.GuestHomeFragment;
import com.example.libraryapp.Fragment.ManageAccountFragment;
import com.example.libraryapp.MainActivity;
import com.example.libraryapp.Model.User;
import com.example.libraryapp.R;

public class AdminActivity extends AppCompatActivity {

    enum SelectedBtn
    {
        SUPERVISE(1),STATISTIC(2),SEND(3),PROFILE(4);
        private int value;
        private SelectedBtn(int value)
        {
            this.value = value;
        }
    }
    public static User user;
    private ManageAccountFragment manageAccountFragment;
    private AdminProfileFragment adminProfileFragment;
    private AdminSendFragment adminSendFragment;
    private AdminStatisticFragment adminStatisticFragment;
    private int selectedbtn = SelectedBtn.SUPERVISE.value;
    private LinearLayout layoutsuper,layoutsta,layoutsend,layoutprofile;
    private ImageView imgsuper,imgsta,imgsend,imgprofile;
    private TextView txtsuper,txtsta,txtsend,txtprofile;
    public static FragmentManager fragmentManager,fragmentManager2,fragmentManager3,fragmentManager4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        fragmentManager = getSupportFragmentManager();
        fragmentManager2 = getSupportFragmentManager();
        fragmentManager3 = getSupportFragmentManager();
        fragmentManager4 = getSupportFragmentManager();
        initView();
        handleSelected();
        setFragment(fragmentManager,manageAccountFragment);
    }
    private void initView()
    {
        layoutsuper = findViewById(R.id.adminact_linearlayoutsupervise);
        layoutprofile = findViewById(R.id.adminact_linearlayoutprofile);
        layoutsta = findViewById(R.id.adminact_linearlayoutstatistic);
        layoutsend = findViewById(R.id.adminact_linearlayoutsend);

        imgsuper = findViewById(R.id.adminact_imgsupervise);
        imgprofile = findViewById(R.id.adminact_imgprofile);
        imgsend = findViewById(R.id.adminact_imgsend);
        imgsta = findViewById(R.id.adminact_imgstatistic);

        txtprofile = findViewById(R.id.adminact_textprofile);
        txtsend = findViewById(R.id.adminact_textsend);
        txtsta = findViewById(R.id.adminact_textstatistic);
        txtsuper = findViewById(R.id.adminact_textsupervise);
        manageAccountFragment = new ManageAccountFragment();
        adminProfileFragment = new AdminProfileFragment();
        adminSendFragment = new AdminSendFragment();
        adminStatisticFragment = new AdminStatisticFragment();
        user = getIntent().getParcelableExtra("user");
    }
    private void setFragment(FragmentManager fragmentManager,Fragment fragment)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_left);
        transaction.replace(R.id.adminact_fragmentcontainer,fragment);
        transaction.commit();
    }
    private void handleSelected()
    {
        TextView[][] textViews = {{txtsta,txtsend,txtprofile,txtsuper},
                {txtsuper,txtsend,txtprofile,txtsta},
                {txtsuper,txtsta,txtprofile,txtsend},
                {txtsuper,txtsend,txtsta,txtprofile}};
        ImageView[][] imageViews = {{imgsta,imgsend,imgprofile,imgsuper},
                {imgsuper,imgsend,imgprofile,imgsta},
                {imgsuper,imgsta,imgprofile,imgsend},
                {imgsuper,imgsend,imgsta,imgprofile}};
        LinearLayout[][] linearLayouts = {{layoutsta,layoutsend,layoutprofile,layoutsuper},
                {layoutsuper,layoutsend,layoutprofile,layoutsta},
                {layoutsuper,layoutsta,layoutprofile,layoutsend},
                {layoutsuper,layoutsend,layoutsta,layoutprofile}};
        int[][] drawables = {{R.drawable.statistic,R.drawable.send,R.drawable.person,R.drawable.supervisor_account_selected},
                {R.drawable.supervisor_account,R.drawable.send,R.drawable.person,R.drawable.statistic_selected},
                {R.drawable.supervisor_account,R.drawable.statistic,R.drawable.person,R.drawable.send_selected},
                {R.drawable.supervisor_account,R.drawable.send,R.drawable.statistic,R.drawable.person_selected}};
        int[] bgcolor = {R.drawable.round_home,R.drawable.round_locallib,R.drawable.round_nof,R.drawable.round_profile};

        layoutsuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if(selectedbtn != SelectedBtn.SUPERVISE.value)
                  {
                      setFragment(fragmentManager,manageAccountFragment);
                      idenBtn(textViews[0],imageViews[0],linearLayouts[0],drawables[0],bgcolor[0],
                              SelectedBtn.SUPERVISE.value);
                  }
            }
        });
        layoutsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedbtn != SelectedBtn.STATISTIC.value)
                {
                    setFragment(fragmentManager2,adminStatisticFragment);
                    idenBtn(textViews[1],imageViews[1],linearLayouts[1],drawables[1],bgcolor[1],
                            SelectedBtn.STATISTIC.value);
                }
            }
        });
        layoutsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedbtn != SelectedBtn.SEND.value)
                {
                    setFragment(fragmentManager3,adminSendFragment);
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
                    setFragment(fragmentManager4,adminProfileFragment);
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