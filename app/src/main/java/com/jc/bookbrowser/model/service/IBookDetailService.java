package com.jc.bookbrowser.model.service;

import com.jc.bookbrowser.entity.http.BookReviewsListResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public interface IBookDetailService {

    @GET("book/{bookId}/reviews")
    Observable<Response<BookReviewsListResponse>> getBookDetail(@Path("bookId") String bookId, @Query("start") int start, @Query("count") int count, @Query("fields") String fields);


}
