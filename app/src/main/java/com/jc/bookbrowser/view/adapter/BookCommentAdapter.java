package com.jc.bookbrowser.view.adapter;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jc.bookbrowser.R;
import com.jc.bookbrowser.entity.http.BookReviewResponse;
import com.jc.bookbrowser.entity.http.BookReviewsListResponse;
import com.jc.bookbrowser.utils.UIHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaohaoChang on 2016/12/2.
 */
public class BookCommentAdapter extends RecyclerView.Adapter {
    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_DEFAULT = 1;
    private BookReviewsListResponse reviewsListResponse;

    public BookCommentAdapter(BookReviewsListResponse reviewsListResponse) {
        this.reviewsListResponse = reviewsListResponse;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_EMPTY) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new EmptyViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_comment, parent, false);
            return new BookCommentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BookCommentViewHolder) {
            List<BookReviewResponse> reviews = reviewsListResponse.getReviews();
            if (reviews.isEmpty()) {
                ((BookCommentViewHolder) holder).itemView.setVisibility(View.GONE);
            }
            Glide.with(UIHelper.getContext())
                    .load(reviews.get(position).getAuthor().getAvatar())
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(UIHelper.getContext().getResources(), resource);
                            drawable.setCircular(true);
                            ((BookCommentViewHolder) holder).ivAvatar.setImageDrawable(drawable);
                        }
                    });
            ((BookCommentViewHolder) holder).tvUserName.setText(reviews.get(position).getAuthor().getName());
            if (reviews.get(position).getRating() != null) {
                ((BookCommentViewHolder) holder).ratingBarHots.setRating(Float.valueOf(reviews.get(position).getRating().getValue()));
            }
            ((BookCommentViewHolder) holder).tvCommentContent.setText(reviews.get(position).getSummary());
            ((BookCommentViewHolder) holder).tvFavoriteNum.setText(reviews.get(position).getVotes() + "");
            ((BookCommentViewHolder) holder).tvUpdateTime.setText(reviews.get(position).getUpdated().split(" ")[0]);

        }

    }

    @Override
    public int getItemCount() {
        int count = 1;
        if (reviewsListResponse != null && !reviewsListResponse.getReviews().isEmpty()) {
            count = reviewsListResponse.getReviews().size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (reviewsListResponse == null || reviewsListResponse.getReviews().isEmpty()) {
            return TYPE_EMPTY;
        } else {
            return TYPE_DEFAULT;
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }


    class BookCommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_comment_title)
        TextView tvCommentTitle;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.ratingBar_hots)
        AppCompatRatingBar ratingBarHots;
        @BindView(R.id.tv_comment_content)
        TextView tvCommentContent;
        @BindView(R.id.iv_favorite)
        ImageView ivFavorite;
        @BindView(R.id.tv_favorite_num)
        TextView tvFavoriteNum;
        @BindView(R.id.tv_update_time)
        TextView tvUpdateTime;
        @BindView(R.id.ll_comment)
        LinearLayout llComment;
        @BindView(R.id.tv_more_comment)
        TextView tvMoreComment;

        BookCommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
