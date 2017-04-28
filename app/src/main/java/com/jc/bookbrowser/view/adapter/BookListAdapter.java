package com.jc.bookbrowser.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.jc.bookbrowser.R;
import com.jc.bookbrowser.entity.http.BookInfoResponse;
import com.jc.bookbrowser.utils.UIHelper;
import com.jc.bookbrowser.view.activity.BaseActivity;
import com.jc.bookbrowser.view.activity.BookDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaohaoChang on 2016/11/23.
 */
public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_DEFAULT = 1;
    private List<BookInfoResponse> bookInfoResponses;
    private Context context;
    private int columns;
    private FloatingActionButton fab;

    public BookListAdapter(List<BookInfoResponse> bookInfoResponses, Context context, int columns, FloatingActionButton fab) {
        this.bookInfoResponses = bookInfoResponses;
        this.context = context;
        this.columns = columns;
        this.fab = fab;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_DEFAULT) {
            v = LayoutInflater.from(context).inflate(R.layout.item_book_list, parent, false);

            return new BookListViewHolder(v);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.item_empty, parent, false);
            return new EmptyViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (bookInfoResponses == null || bookInfoResponses.isEmpty()) {
            return TYPE_EMPTY;
        } else {
            return TYPE_DEFAULT;
        }
    }

    public int getItemColumnSpan(int position) {
        switch (getItemViewType(position)) {
            case TYPE_DEFAULT:
                return 1;
            default:
                return columns;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BookListViewHolder) {
            final BookInfoResponse bookInfo = bookInfoResponses.get(position);
            Glide.with(context)
                    .load(bookInfo.getImages().getLarge())
                    .into(((BookListViewHolder) holder).ivBookImg);
            ((BookListViewHolder) holder).tvBookTitle.setText(bookInfo.getTitle());
            ((BookListViewHolder) holder).ratingBarHots.setRating(Float.valueOf(bookInfo.getRating().getAverage()) / 2);
            ((BookListViewHolder) holder).tvHotsNum.setText(bookInfo.getRating().getAverage());
            ((BookListViewHolder) holder).tvBookInfo.setText(bookInfo.getInfoString());
            ((BookListViewHolder) holder).tvBookDescription.setText("\u3000" + bookInfo.getSummary());
            ((BookListViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BookInfoResponse.serialVersionName, bookInfo);
                    Bitmap bitmap = null;
                    GlideBitmapDrawable glideBitmapDrawable = (GlideBitmapDrawable) ((BookListViewHolder) holder).ivBookImg.getDrawable();
                    if (glideBitmapDrawable != null) {
                        bitmap = glideBitmapDrawable.getBitmap();
                        bundle.putParcelable("book_img", bitmap);
                    }
                    Intent intent = new Intent(UIHelper.getContext(), BookDetailActivity.class);
                    intent.putExtras(bundle);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (BaseActivity.activity == null) {
                            UIHelper.startActivity(intent);
                            return;
                        }
                        Pair<View,String> imgPair = new Pair<View, String>(((BookListViewHolder) holder).ivBookImg,"book_img");
                        ActivityOptionsCompat optionsCompat = null;
                        if (fab != null) {
                            Pair<View, String> fabPair = new Pair<View, String>(fab, "fab");
                            optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseActivity.activity, imgPair, fabPair);
                        } else {
                            optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseActivity.activity, imgPair);
                        }

                        //optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseActivity.activity, Pair.create((View)((BookListViewHolder) holder).ivBookImg, "book_img"), Pair.create((View)fab,"fab"));
                        BaseActivity.activity.startActivity(intent, optionsCompat.toBundle());
                    } else {
                        UIHelper.startActivity(intent);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if (bookInfoResponses.isEmpty()) {
            return 1;
        }
        return bookInfoResponses.size();
    }

    class BookListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_book_img)
        ImageView ivBookImg;
        @BindView(R.id.tv_book_title)
        TextView tvBookTitle;
        @BindView(R.id.ratingBar_hots)
        AppCompatRatingBar ratingBarHots;
        @BindView(R.id.tv_hots_num)
        TextView tvHotsNum;
        @BindView(R.id.tv_book_info)
        TextView tvBookInfo;
        @BindView(R.id.tv_book_description)
        TextView tvBookDescription;

        BookListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
