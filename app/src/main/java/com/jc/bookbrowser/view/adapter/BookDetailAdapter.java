package com.jc.bookbrowser.view.adapter;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hymane.expandtextview.ExpandTextView;
import com.jc.bookbrowser.R;
import com.jc.bookbrowser.entity.http.BookInfoResponse;
import com.jc.bookbrowser.entity.http.BookReviewResponse;
import com.jc.bookbrowser.entity.http.BookReviewsListResponse;
import com.jc.bookbrowser.entity.http.BookSeriesListResponse;
import com.jc.bookbrowser.utils.UIHelper;
import com.jc.bookbrowser.view.activity.BookCommentActivity;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaohaoChang on 2016/11/29.
 */
public class BookDetailAdapter extends RecyclerView.Adapter {
    private static final int TYPE_BOOK_INFO = 0;
    private static final int TYPE_BOOK_BRIEF = 1;
    private static final int TYPE_BOOK_COMMENT = 2;
    private static final int TYPE_BOOK_RECOMMEND = 3;

    public static final int HEADER_COUNT = 2;
    private static final int AVATAR_SIZE_DP = 24;
    private static final int ANIMATION_DURATION = 600;
    //模拟加载时间
    private static final int PROGRESS_DELAY_MIN_TIME = 500;
    private static final int PROGRESS_DELAY_SIZE_TIME = 1000;

    private final BookInfoResponse mBookInfo;
    private final BookReviewsListResponse mReviewsListResponse;
    private final BookSeriesListResponse mSeriesListResponse;

    //图书出版信息是否展开
    private boolean flag;

    public BookDetailAdapter(BookInfoResponse mBookInfo, BookReviewsListResponse mReviewsListResponse, BookSeriesListResponse mSeriesListResponse) {
        this.mBookInfo = mBookInfo;
        this.mReviewsListResponse = mReviewsListResponse;
        this.mSeriesListResponse = mSeriesListResponse;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_BOOK_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_info, parent, false);
                viewHolder = new BookInfoHolder(view);
                break;
            case TYPE_BOOK_BRIEF:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_brief, parent, false);
                viewHolder = new BookBriefHolder(view);
                break;
            case TYPE_BOOK_COMMENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_comment, parent, false);
                viewHolder = new BookCommentHolder(view);
                break;
            case TYPE_BOOK_RECOMMEND:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_series, parent, false);
                viewHolder = new BookSeriesHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BookInfoHolder) {
            ((BookInfoHolder) holder).ratingBarHots.setRating(Float.valueOf(mBookInfo.getRating().getAverage()) / 2);
            ((BookInfoHolder) holder).tvHotsNum.setText(mBookInfo.getRating().getAverage());
            ((BookInfoHolder) holder).tvCommentNum.setText(mBookInfo.getRating().getNumRaters() + UIHelper.getContext().getString(R.string.comment_num));
            ((BookInfoHolder) holder).tvBookInfo.setText(mBookInfo.getInfoString());
            ((BookInfoHolder) holder).rlMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag) {
                        ObjectAnimator.ofFloat(((BookInfoHolder) holder).ivMoreInfo, "rotation", 90, 0).start();
                        ((BookInfoHolder) holder).progressBar.setVisibility(View.GONE);
                        ((BookInfoHolder) holder).llPublishInfo.setVisibility(View.GONE);
                        flag = false;
                    } else {
                        ObjectAnimator.ofFloat(((BookInfoHolder) holder).ivMoreInfo, "rotation", 0, 90).start();
                        ((BookInfoHolder) holder).progressBar.setVisibility(View.VISIBLE);
                        new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if (flag) {
                                    ((BookInfoHolder) holder).progressBar.setVisibility(View.GONE);
                                    ((BookInfoHolder) holder).llPublishInfo.setVisibility(View.VISIBLE);
                                }
                            }
                        }.sendEmptyMessageDelayed(0, getDelayTime());
                        flag = true;
                    }
                }
            });
            if (mBookInfo.getAuthor().length > 0) {
                ((BookInfoHolder) holder).tvAuthor.setText("作者:" + mBookInfo.getAuthor()[0]);
            }
            ((BookInfoHolder) holder).tvPublisher.setText("出版社:" + mBookInfo.getPublisher());
            if (mBookInfo.getSubtitle().isEmpty()) {
                ((BookInfoHolder) holder).tvSubtitle.setVisibility(View.GONE);
            }
            ((BookInfoHolder) holder).tvSubtitle.setText("副标题:" + mBookInfo.getSubtitle());
            if (mBookInfo.getOrigin_title().isEmpty()) {
                ((BookInfoHolder) holder).tvOriginTitle.setVisibility(View.GONE);
            }
            ((BookInfoHolder) holder).tvOriginTitle.setText("原作名:" + mBookInfo.getOrigin_title());
            if (mBookInfo.getTranslator().length > 0) {
                ((BookInfoHolder) holder).tvTranslator.setText("译者:" + mBookInfo.getTranslator()[0]);
            } else {
                ((BookInfoHolder) holder).tvTranslator.setVisibility(View.GONE);
            }
            ((BookInfoHolder) holder).tvPublishDate.setText("出版年:" + mBookInfo.getPubdate());
            ((BookInfoHolder) holder).tvPages.setText("页数:" + mBookInfo.getPages());
            ((BookInfoHolder) holder).tvPrice.setText("定价:" + mBookInfo.getPrice());
            ((BookInfoHolder) holder).tvBinding.setText("装帧:" + mBookInfo.getBinding());
            ((BookInfoHolder) holder).tvIsbn.setText("isbn:" + mBookInfo.getIsbn13());
        } else if (holder instanceof BookBriefHolder) {
            if (!mBookInfo.getSummary().isEmpty()) {
                ((BookBriefHolder) holder).etvBrief.setContent(mBookInfo.getSummary());
            } else {
                ((BookBriefHolder) holder).etvBrief.setContent(UIHelper.getContext().getString(R.string.no_brief));
            }
        } else if (holder instanceof BookCommentHolder) {
            List<BookReviewResponse> reviews = mReviewsListResponse.getReviews();
            if (reviews.isEmpty()) {
                ((BookCommentHolder) holder).itemView.setVisibility(View.GONE);
            } else if (position == HEADER_COUNT) {
                ((BookCommentHolder) holder).tvCommentTitle.setVisibility(View.VISIBLE);
            } else if (position == reviews.size() + 1) {
                ((BookCommentHolder) holder).tvMoreComment.setVisibility(View.VISIBLE);
                ((BookCommentHolder) holder).tvMoreComment.setText(UIHelper.getContext().getString(R.string.more_brief) + mReviewsListResponse.getTotal() + "条");
                ((BookCommentHolder) holder).tvMoreComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UIHelper.getContext(), BookCommentActivity.class);
                        intent.putExtra("bookId", mBookInfo.getId());
                        intent.putExtra("bookName", mBookInfo.getTitle());
                        UIHelper.startActivity(intent);
                    }
                });
            }
            Glide.with(UIHelper.getContext())
                    .load(reviews.get(position - HEADER_COUNT).getAuthor().getAvatar())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(((BookCommentHolder) holder).ivAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(UIHelper.getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ((BookCommentHolder) holder).ivAvatar.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            ((BookCommentHolder) holder).tvUserName.setText(reviews.get(position - HEADER_COUNT).getAuthor().getName());
            if (reviews.get(position - HEADER_COUNT).getRating() != null) {
                ((BookCommentHolder) holder).ratingBarHots.setRating(Float.valueOf(reviews.get(position - HEADER_COUNT).getRating().getValue()));
            }
            ((BookCommentHolder) holder).tvCommentContent.setText(reviews.get(position - HEADER_COUNT).getSummary());
            ((BookCommentHolder) holder).tvFavoriteNum.setText(reviews.get(position - HEADER_COUNT).getVotes() + "");
            ((BookCommentHolder) holder).tvUpdateTime.setText(reviews.get(position - HEADER_COUNT).getUpdated().split(" ")[0]);
        } else if (holder instanceof BookSeriesHolder) {
            List<BookInfoResponse> seriesBooks = mSeriesListResponse.getBooks();
            if (seriesBooks.isEmpty()) {
                ((BookSeriesHolder) holder).itemView.setVisibility(View.GONE);
            } else {
                BookSeriesCeilHolder ceilHolder;
                ((BookSeriesHolder) holder).llSeriesContent.removeAllViews();
                for (int i = 0;i<seriesBooks.size();i++) {
                    ceilHolder = new BookSeriesCeilHolder(seriesBooks.get(i));
                    ((BookSeriesHolder) holder).llSeriesContent.addView(ceilHolder.getContentView());
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        int count = HEADER_COUNT;
        if (mReviewsListResponse != null) {
            count += mReviewsListResponse.getReviews().size();
        }
        if (mSeriesListResponse != null && !mSeriesListResponse.getBooks().isEmpty()) {
            count += 1;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BOOK_INFO;
        } else if (position == 1) {
            return TYPE_BOOK_BRIEF;
        } else if (position > 1 && position < (mReviewsListResponse == null ? HEADER_COUNT : (mReviewsListResponse.getReviews().size() + HEADER_COUNT))) {
            return TYPE_BOOK_COMMENT;
        } else {
            return TYPE_BOOK_RECOMMEND;
        }
    }

    static class BookInfoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ratingBar_hots)
        AppCompatRatingBar ratingBarHots;
        @BindView(R.id.tv_hots_num)
        TextView tvHotsNum;
        @BindView(R.id.tv_comment_num)
        TextView tvCommentNum;
        @BindView(R.id.tv_book_info)
        TextView tvBookInfo;
        @BindView(R.id.iv_more_info)
        ImageView ivMoreInfo;
        @BindView(R.id.rl_more_info)
        RelativeLayout rlMoreInfo;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_publisher)
        TextView tvPublisher;
        @BindView(R.id.tv_subtitle)
        TextView tvSubtitle;
        @BindView(R.id.tv_origin_title)
        TextView tvOriginTitle;
        @BindView(R.id.tv_translator)
        TextView tvTranslator;
        @BindView(R.id.tv_publish_date)
        TextView tvPublishDate;
        @BindView(R.id.tv_pages)
        TextView tvPages;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_binding)
        TextView tvBinding;
        @BindView(R.id.tv_isbn)
        TextView tvIsbn;
        @BindView(R.id.ll_publish_info)
        LinearLayout llPublishInfo;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        BookInfoHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class BookBriefHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etv_brief)
        ExpandTextView etvBrief;

        BookBriefHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class BookCommentHolder extends RecyclerView.ViewHolder {
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

        BookCommentHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class BookSeriesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_comment_title)
        TextView tvCommentTitle;
        @BindView(R.id.ll_series_content)
        LinearLayout llSeriesContent;
        @BindView(R.id.hsv_series)
        HorizontalScrollView hsvSeries;

        BookSeriesHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private int getDelayTime() {
        return new Random().nextInt(PROGRESS_DELAY_SIZE_TIME) + PROGRESS_DELAY_MIN_TIME;
    }
}
