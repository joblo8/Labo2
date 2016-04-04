package com.example.jonathan.labo2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Vector;

public class MainActivity extends ActionBarActivity {
    private PagerAdapter pagerAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.viewpager);

        List<Fragment> listFragments = new Vector<Fragment>();
        listFragments.add(Fragment.instantiate(this, Recherche.class.getName()));
        listFragments.add(Fragment.instantiate(this, Liste.class.getName()));
        listFragments.add(Fragment.instantiate(this, Nouveau.class.getName()));

        this.pagerAdaper = new MonPagerAdapter(super.getSupportFragmentManager(),
                listFragments);
        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
        pager.setAdapter(this.pagerAdaper);
    }
}
