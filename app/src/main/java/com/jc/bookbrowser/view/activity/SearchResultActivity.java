package com.jc.bookbrowser.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.jc.bookbrowser.R;
import com.jc.bookbrowser.entity.http.BookInfoResponse;
import com.jc.bookbrowser.entity.http.BookListResponse;
import com.jc.bookbrowser.presenter.impl.BookListPresenter;
import com.jc.bookbrowser.view.IBookListView;
import com.jc.bookbrowser.view.adapter.BookListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaohaoChang on 2016/12/2.
 */
public class SearchResultActivity extends BaseActivity implements IBookListView, SwipeRefreshLayout.OnRefreshListener {

    private static final String fields = "id,title,subtitle,origin_title,rating,author,translator,publisher,pubdate,summary,images,pages,price,binding,isbn13";
    private static int count = 20;
    private static int page = 0;
    private static String queryKey;
    private boolean isLoadAll = false;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;

    private GridLayoutManager layoutManager;
    private BookListAdapter bookListAdapter;
    private List<BookInfoResponse> bookInfoResponseList;
    private BookListPresenter bookListPresenter;
    private int spanCount = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            isLoadAll = savedInstanceState.getBoolean("isLoadAll");
        }

        setContentView(R.layout.activity_search_result_layout);
        ButterKnife.bind(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvents() {
        queryKey = getIntent().getStringExtra("query");
        setTitle(queryKey);
        toolbar.setNavigationIcon(AppCompatResources.getDrawable(this, R.drawable.ic_action_navigation_arrow_back_inverted));
        spanCount = (int) getResources().getInteger(R.integer.home_span_count);
        bookListPresenter = new BookListPresenter(this);
        bookInfoResponseList = new ArrayList<>();

        swipeRefreshWidget.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);
        layoutManager = new GridLayoutManager(SearchResultActivity.this, spanCount);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        bookListAdapter = new BookListAdapter(bookInfoResponseList, this, spanCount, null);
        recyclerView.setAdapter(bookListAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

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
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        swipeRefreshWidget.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void showMessage(String msg) {

        Snackbar.make(toolbar, msg, Snackbar.LENGTH_SHORT).show();

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
            bookInfoResponseList.clear();
            bookInfoResponseList.addAll(((BookListResponse) data).getBooks());
            bookListAdapter.notifyDataSetChanged();
            if (((BookListResponse) data).getTotal() > page * count) {
                isLoadAll = false;
            } else {
                isLoadAll = true;
            }
            page = 1;
        }

    }

    @Override
    public void addData(Object data) {
        bookInfoResponseList.addAll(((BookListResponse) data).getBooks());
        bookListAdapter.notifyDataSetChanged();
        if (((BookListResponse) data).getTotal() > page * count) {
            page++;
            isLoadAll = false;
        } else {
            isLoadAll = true;
        }
    }


    private void onLoadMore() {
        if (!isLoadAll) {
            if (!swipeRefreshWidget.isRefreshing()) {
                bookListPresenter.loadBooks(queryKey, null, page * count, count, fields);
            }
        } else {
            Snackbar.make(toolbar, getResources().getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {

        bookListPresenter.loadBooks(queryKey, null, 0, count, fields);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isLoadAll", isLoadAll);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        bookListPresenter.cancelLoading();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
