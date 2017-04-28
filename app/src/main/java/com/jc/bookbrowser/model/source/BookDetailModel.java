package com.jc.bookbrowser.model.source;

import com.jc.bookbrowser.common.Constant;
import com.jc.bookbrowser.entity.http.BaseResponse;
import com.jc.bookbrowser.entity.http.BookReviewsListResponse;
import com.jc.bookbrowser.entity.http.BookSeriesListResponse;
import com.jc.bookbrowser.model.DataServiceFactory;
import com.jc.bookbrowser.model.IBookDetailModel;
import com.jc.bookbrowser.model.RequestListener;
import com.jc.bookbrowser.model.service.IBookDetailService;
import com.jc.bookbrowser.model.service.IRecommendedBookService;

import java.net.UnknownHostException;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HaohaoChang on 2016/11/28.
 */
public class BookDetailModel implements IBookDetailModel {
    @Override
    public void loadBookComments(String bookID, int start, int count, String fields, final RequestListener requestListener) {
        IBookDetailService iBookDetailService = DataServiceFactory.createService(Constant.BASE_URL, IBookDetailService.class);
        iBookDetailService.getBookDetail(bookID, start, count, fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<BookReviewsListResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            requestListener.onFailed(null);
                            return;
                        }
                        requestListener.onCompleted(new BaseResponse(404, e.getMessage()));

                    }

                    @Override
                    public void onNext(Response<BookReviewsListResponse> bookReviewsListResponse) {
                        if (bookReviewsListResponse.isSuccessful()) {
                            requestListener.onCompleted(bookReviewsListResponse.body());
                        } else {
                            requestListener.onFailed(new BaseResponse(bookReviewsListResponse.code(), bookReviewsListResponse.message()));
                        }

                    }
                });
    }

    @Override
    public void loadRecommendedBooks(String seriesId, int start, int count, String fields, final RequestListener listener) {
        IRecommendedBookService iRecommendedBookService = DataServiceFactory.createService(Constant.BASE_URL, IRecommendedBookService.class);
        iRecommendedBookService.getBookSeries(seriesId, start, count, fields)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<BookSeriesListResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            listener.onFailed(null);
                            return;
                        }
                        listener.onCompleted(new BaseResponse(404,e.getMessage()));

                    }

                    @Override
                    public void onNext(Response<BookSeriesListResponse> bookSeriesListResponse) {
                        if (bookSeriesListResponse.isSuccessful()) {
                            listener.onCompleted(bookSeriesListResponse.body());
                        } else {
                            listener.onFailed(new BaseResponse(bookSeriesListResponse.code(),bookSeriesListResponse.message()));
                        }

                    }
                });

    }

    @Override
    public void cancelLoading() {

    }
}
