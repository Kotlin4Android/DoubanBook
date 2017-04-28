package com.jc.bookbrowser.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.jc.bookbrowser.R;
import com.jc.bookbrowser.entity.http.BookReviewResponse;
import com.jc.bookbrowser.entity.http.BookReviewsListResponse;
import com.jc.bookbrowser.presenter.impl.BookDetailPresenter;
import com.jc.bookbrowser.view.IBookReviewView;
import com.jc.bookbrowser.view.adapter.BookCommentAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaohaoChang on 2016/12/2.
 */
public class BookCommentActivity extends BaseActivity implements IBookReviewView, SwipeRefreshLayout.OnRefreshListener {

    private static final String COMMENT_FIELDS = "id,rating,author,title,updated,comments,summary,votes,useless";
    private static int count = 20;
    private static int page = 0;
    private static String bookId;
    private static String bookName;
    private boolean isLoadAll = false;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;

    private LinearLayoutManager layoutManager;
    private BookCommentAdapter bookCommentAdapter;
    private BookDetailPresenter bookDetailPresenter;
    private BookReviewsListResponse reviewsListResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_book_comments_layout);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvents() {
        bookDetailPresenter = new BookDetailPresenter(this);
        reviewsListResponse = new BookReviewsListResponse();
        reviewsListResponse.setReviews(new ArrayList<BookReviewResponse>());
        swipeRefreshWidget.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        bookCommentAdapter = new BookCommentAdapter(reviewsListResponse);
        recyclerView.setAdapter(bookCommentAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        bookName = getIntent().getStringExtra("bookName");
        bookId = getIntent().getStringExtra("bookId");
        setTitle(bookName + getString(R.string.comment_of_book));
        toolbar.setNavigationIcon(AppCompatResources.getDrawable(this, R.drawable.ic_action_navigation_arrow_back_inverted));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == bookCommentAdapter.getItemCount()) {
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

    private void onLoadMore() {
        if (isLoadAll) {
            Snackbar.make(toolbar, R.string.no_more_comment, Snackbar.LENGTH_SHORT).show();
        } else {
            bookDetailPresenter.loadComments(bookId, page * count, count, COMMENT_FIELDS);
        }
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
    public void showData(Object data) {
        if (data instanceof BookReviewsListResponse) {
            BookReviewsListResponse response = (BookReviewsListResponse) data;
            if (response.getStart() == 0) {
                reviewsListResponse.getReviews().clear();
            }
            reviewsListResponse.setTotal(response.getTotal());
            reviewsListResponse.getReviews().addAll(response.getReviews());
            bookCommentAdapter.notifyDataSetChanged();

            if (response.getTotal() > page * count) {
                page++;
                isLoadAll = false;
            } else {
                isLoadAll = true;
            }

        }

    }

    @Override
    public void onRefresh() {
        page = 0;
        bookDetailPresenter.loadComments(bookId, page * count, count, COMMENT_FIELDS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
