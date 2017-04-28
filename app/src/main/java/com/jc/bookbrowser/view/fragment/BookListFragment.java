package com.jc.bookbrowser.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.bookbrowser.R;
import com.jc.bookbrowser.entity.http.BookInfoResponse;
import com.jc.bookbrowser.entity.http.BookListResponse;
import com.jc.bookbrowser.presenter.IBookListPresenter;
import com.jc.bookbrowser.presenter.impl.BookListPresenter;
import com.jc.bookbrowser.utils.DensityUtils;
import com.jc.bookbrowser.view.IBookListView;
import com.jc.bookbrowser.view.activity.MainActivity;
import com.jc.bookbrowser.view.adapter.BookListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaohaoChang on 2016/10/24.
 */

public class BookListFragment extends BaseFragment implements IBookListView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;

    private String TAG = BookListFragment.class.getSimpleName();
    private final String SAVE_KEY = "listPosition";
    private int listPosition;
    private String tag = "hot";
    private String fields = "id,title,subtitle,origin_title,rating,author,translator,publisher,pubdate,summary,images,pages,price,binding,isbn13,series";
    private int page = 0;
    private int count = 20;

    private IBookListPresenter bookListPresenter;
    private GridLayoutManager gridLayoutManager;
    private List<BookInfoResponse> bookInfoResponses;
    private BookListAdapter bookListAdapter;

    public static BookListFragment getInstance(String tag) {
        Bundle args = new Bundle();
        args.putString("tag", tag);
        BookListFragment fragment = new BookListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recycler_content, container, false);
        bookListPresenter = new BookListPresenter(this);
        if (savedInstanceState != null) {
            listPosition = savedInstanceState.getInt(SAVE_KEY);
        }

        String result = getArguments().getString("tag");
        if (!TextUtils.isEmpty(result)) {
            tag = result;
        }
    }

    @Override
    protected void initEvents() {
        int spanCount = getResources().getInteger(R.integer.home_span_count);
        bookInfoResponses = new ArrayList<>();
        swipeRefreshWidget.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);

        //布局管理器
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                return bookListAdapter.getItemColumnSpan(position);
            }
        });

        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        //adapter
        bookListAdapter = new BookListAdapter(bookInfoResponses, getActivity(), spanCount,((MainActivity) getActivity()).getFloatingActionBar());
        recyclerView.setAdapter(bookListAdapter);
        recyclerView.smoothScrollToPosition(listPosition);

        //animation
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new RecyclerViewScrollListener());
        swipeRefreshWidget.setOnRefreshListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVE_KEY, gridLayoutManager.findLastVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initData(boolean isSavedNull) {
        if (isSavedNull) {
            onRefresh();
        }

    }

    @Override
    public void showMessage(String msg) {
        showToast(msg);
    }

    @Override
    public void showProgress() {
        swipeRefreshWidget.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshWidget.setRefreshing(true);
            }
        });

    }

    @Override
    public void hideProgress() {
        swipeRefreshWidget.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshWidget.setRefreshing(false);
            }
        });
    }

    @Override
    public void refreshData(Object data) {
        if (data instanceof BookListResponse) {
            bookInfoResponses.clear();
            bookInfoResponses.addAll(((BookListResponse) data).getBooks());
            bookListAdapter.notifyDataSetChanged();
            page = 1;
        }

    }

    @Override
    public void addData(Object data) {
        bookInfoResponses.addAll(((BookListResponse) data).getBooks());
        bookListAdapter.notifyDataSetChanged();
        page++;

    }


    @Override
    public void onRefresh() {
        bookListPresenter.loadBooks(null, tag, 0, count, fields);
    }

    public void onLoadMore() {
        if (!swipeRefreshWidget.isRefreshing()) {
            bookListPresenter.loadBooks(null, tag, page * count, count, fields);
        }
    }

    class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        private int lastVisibleItem;
        private int mScrollThreshold = DensityUtils.dp2px(getActivity(), 1);


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == bookListAdapter.getItemCount()) {
                onLoadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
        }
    }
}
