package com.jc.bookbrowser.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jc.bookbrowser.R;
import com.jc.bookbrowser.entity.http.BookInfoResponse;
import com.jc.bookbrowser.entity.http.BookReviewsListResponse;
import com.jc.bookbrowser.entity.http.BookSeriesListResponse;
import com.jc.bookbrowser.presenter.impl.BookDetailPresenter;
import com.jc.bookbrowser.utils.Blur;
import com.jc.bookbrowser.utils.UIHelper;
import com.jc.bookbrowser.view.IBookDetailView;
import com.jc.bookbrowser.view.adapter.BookDetailAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaohaoChang on 2016/11/28.
 */
public class BookDetailActivity extends BaseActivity implements IBookDetailView {

    private static final String COMMENT_FIELDS = "id,rating,author,title,updated,comments,summary,votes,useless";
    private static final String SERIES_FIELDS = "id,title,subtitle,origin_title,rating,author,translator,publisher,pubdate,summary,images,pages,price,binding,isbn13,series";
    private static final int REVIEWS_COUNT = 5;
    private static final int SERIES_COUNT = 6;
    private static final int PAGE = 0;

    @BindView(R.id.iv_book_bg)
    ImageView ivBookBg;
    @BindView(R.id.iv_book_img)
    ImageView ivBookImg;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.topCoordinatorLayout)
    CoordinatorLayout topCoordinatorLayout;
    private BookInfoResponse bookInfoResponse;
    private LinearLayoutManager layoutManager;
    private BookDetailAdapter bookDetailAdapter;
    private BookReviewsListResponse reviewsListResponse;
    private BookSeriesListResponse seriesListResponse;
    private BookDetailPresenter bookDetailPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        toolbar.setNavigationIcon(AppCompatResources.getDrawable(this, R.drawable.ic_action_clear));
    }

    @Override
    protected void initEvents() {
        bookDetailPresenter = new BookDetailPresenter(this);
        seriesListResponse = new BookSeriesListResponse();
        reviewsListResponse = new BookReviewsListResponse();
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookInfoResponse = (BookInfoResponse) getIntent().getSerializableExtra(BookInfoResponse.serialVersionName);
        collapsingToolbarLayout.setTitle(bookInfoResponse.getTitle());
        bookDetailAdapter = new BookDetailAdapter(bookInfoResponse, reviewsListResponse, seriesListResponse);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookDetailAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivBookImg.setTransitionName("book_img");
            fab.setTransitionName("fab");
        }

        Bitmap book_img = getIntent().getParcelableExtra("book_img");
        if (book_img != null) {
            ivBookImg.setImageBitmap(book_img);
            ivBookBg.setImageBitmap(Blur.apply(book_img));
            ivBookBg.setAlpha(0.8f);
            Palette.Builder builder = Palette.from(book_img);
            builder.generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getVibrantSwatch();
                    if (swatch != null) {
                        fab.setBackgroundColor(swatch.getTitleTextColor());
                        collapsingToolbarLayout.setBackgroundColor(swatch.getTitleTextColor());
//                        Window window = getWindow();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            window.setStatusBarColor(swatch.getTitleTextColor());
//                            toolbar.setBackgroundColor(swatch.getBodyTextColor());
//                        }

                    }

                }
            });
        } else {
            Glide.with(this)
                    .load(bookInfoResponse.getImages().getLarge())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ivBookImg.setImageBitmap(resource);
                            ivBookBg.setImageBitmap(Blur.apply(resource));
                            ivBookBg.setAlpha(0.8f);
                        }
                    });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Click Fab");
            }
        });
    }

    @Override
    protected void initData() {

        bookDetailPresenter.loadComments(bookInfoResponse.getId(), PAGE * REVIEWS_COUNT, REVIEWS_COUNT, COMMENT_FIELDS);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                StringBuilder sb = new StringBuilder(getString(R.string.your_friend));
                sb.append("给你分享了一本书，名叫《")
                        .append(bookInfoResponse.getTitle())
                        .append("》，快来看看吧");
                UIHelper.share(this,sb.toString(),null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void showMessage(String msg) {

        Snackbar.make(toolbar, msg, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void showProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (fab.getDrawable() instanceof Animatable) {
                ((Animatable) fab.getDrawable()).start();
            }
        }

    }

    @Override
    public void hideProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (fab.getDrawable() instanceof Animatable) {
                ((Animatable) fab.getDrawable()).stop();
            }
        }

    }

    @Override
    public void showData(Object data) {
        if (data instanceof BookReviewsListResponse) {
            BookReviewsListResponse response = (BookReviewsListResponse) data;
            reviewsListResponse.setTotal(response.getTotal());
            reviewsListResponse.getReviews().addAll(response.getReviews());
            bookDetailAdapter.notifyDataSetChanged();
            if (bookInfoResponse.getSeries() != null) {
                bookDetailPresenter.loadRecommendedBooks(bookInfoResponse.getSeries().getId(), PAGE * SERIES_COUNT, 6, SERIES_FIELDS);
            }
        } else if (data instanceof BookSeriesListResponse) {
            BookSeriesListResponse response = (BookSeriesListResponse) data;
            seriesListResponse.setTotal(response.getTotal());
            seriesListResponse.getBooks().addAll(response.getBooks());
            bookDetailAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        bookDetailPresenter.cancelLoading();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (fab.getDrawable() instanceof Animatable) {
                ((Animatable) fab.getDrawable()).stop();
            }
        }
        super.onDestroy();
    }
}
