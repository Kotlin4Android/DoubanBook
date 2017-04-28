package com.jc.bookbrowser.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.bookbrowser.R;
import com.jc.bookbrowser.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaohaoChang on 2016/10/24.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private List<BaseFragment> fragments;
    private String[] titles;

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected void initRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData(boolean isSavedNull) {
        titles = new String[]{"热门", "新书", "小说", "科幻", "文学", "其他"};
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialData();
        ((MainActivity) getActivity()).setToolBar(toolbar);
        ((MainActivity) getActivity()).setFloatingActionBtn(fab);
    }

    private void initialData() {
        fragments = new ArrayList<>();
        for (String tag : titles) {
            fragments.add(BookListFragment.getInstance(tag));
        }
        viewPager.setAdapter(new HomePagerAdapter(getChildFragmentManager(), fragments, titles));
        viewPager.setOffscreenPageLimit(5);
        viewPager.setCurrentItem(2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class HomePagerAdapter extends FragmentStatePagerAdapter {
        private List<BaseFragment> fragments;
        private String[] titles;

        public HomePagerAdapter(FragmentManager fm, List<BaseFragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
