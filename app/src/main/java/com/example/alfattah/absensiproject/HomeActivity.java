package com.example.alfattah.absensiproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alfattah.absensiproject.Adapter.PageviewAdapter;
import com.example.alfattah.absensiproject.fragment_HomeActivity.LoginFragment;
import com.example.alfattah.absensiproject.fragment_HomeActivity.RegisterFragment;
import com.example.alfattah.absensiproject.fragment_HomeActivity.ResetpassFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/*import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.rigger.Rigger;

@Puppet(containerViewId = R.id.container)*/
public class HomeActivity extends AppCompatActivity {
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private ResetpassFragment resetpassFragment;

    @BindView(R.id.mainframe)
    NonSwipeableViewPager mainPager;

    private PageviewAdapter pageviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        //viewpager
        pageviewAdapter = new PageviewAdapter(getSupportFragmentManager());
        mainPager.setAdapter(pageviewAdapter);
        mainPager.setCurrentItem(0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value =Integer.parseInt(getIntent().getStringExtra("position"));

            mainPager.setCurrentItem(value);
        }

       /* mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

               *//* changeTabs(position);*//*

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


    }
    public void changeTabs(int position) {
        if (position == 0){
            mainPager.setCurrentItem(0);
        }if (position == 1){
            mainPager.setCurrentItem(1);

        }if (position == 2){
            mainPager.setCurrentItem(2);
        }

    }

}
