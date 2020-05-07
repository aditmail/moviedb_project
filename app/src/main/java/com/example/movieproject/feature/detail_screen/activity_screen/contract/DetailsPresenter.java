package com.example.movieproject.feature.detail_screen.activity_screen.contract;

import android.content.Context;

import com.example.movieproject.base.ui.BasePresenter;
import com.example.movieproject.feature.detail_screen.fragment_screen.ReviewFragment;
import com.example.movieproject.feature.detail_screen.fragment_screen.VideosFragment;
import com.example.movieproject.utils.TabViewPagerAdapter;

import androidx.viewpager.widget.ViewPager;

public class DetailsPresenter extends BasePresenter<DetailsView> {

    private Context context;

    public DetailsPresenter(DetailsView view, Context context) {
        this.context = context;
        super.attachView(view, context);
    }

    public void setupViewPager(ViewPager viewPager, TabViewPagerAdapter adapter){
        adapter.addFragment(new VideosFragment(), "Videos");
        adapter.addFragment(new ReviewFragment(), "Reviews");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }
}
