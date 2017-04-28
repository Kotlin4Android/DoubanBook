package com.jc.bookbrowser.model;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public interface IBookDetailModel {
    /**
     * get book comments
     * */
    void loadBookComments(String bookID,int start,int count,String fields,RequestListener requestListener);

    /**
     * get recommended books
     * */
    void loadRecommendedBooks(String seriesId, int start, int count, String fields, RequestListener listener);

    /**
     * cancel loading
     * */
    void cancelLoading();

}
