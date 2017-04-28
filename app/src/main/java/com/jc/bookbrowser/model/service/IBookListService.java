package com.jc.bookbrowser.model.service;

import com.jc.bookbrowser.entity.http.BookListResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public interface IBookListService {
    @GET("book/search")
    Observable<Response<BookListResponse>> getBookList(@Query("q") String q, @Query("tag") String tag, @Query("start") int start, @Query("count") int count, @Query("fields") String fields);
}
