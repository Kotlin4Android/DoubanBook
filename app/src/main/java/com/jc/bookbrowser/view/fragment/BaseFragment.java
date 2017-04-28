package com.jc.bookbrowser.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by HaohaoChang on 2016/10/24.
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initRootView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        initEvents();
        initData(savedInstanceState == null);
        return rootView;
    }

    protected abstract void initRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract void initEvents();

    protected abstract void initData(boolean isSavedNull);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }
}
