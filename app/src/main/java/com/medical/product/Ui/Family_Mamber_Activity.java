package com.medical.product.Ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.medical.product.R;
import com.medical.product.helpingFile.Pager;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Family_Mamber_Activity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    //This is our tablayout
    String mamberid="";
    private TabLayout tabLayout;
    Intent intent;
    //This is our viewPager
    private ViewPager viewPager;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    Pager adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_mamber);



        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Member Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        mamberid=intent.getStringExtra("memberid");
        if(!(mamberid.equals("")||mamberid.equals(null)))
        {
            setMyString(mamberid);
        }
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Mamber Detail"));
        tabLayout.addTab(tabLayout.newTab().setText("Document"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
         adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    public  String getMyString(){
        return mamberid;
    }
    public void setMyString(String memberidis){
         mamberid=memberidis;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
