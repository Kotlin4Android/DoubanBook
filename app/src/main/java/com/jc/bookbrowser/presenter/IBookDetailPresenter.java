package com.jc.bookbrowser.presenter;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public interface IBookDetailPresenter {
    void loadComments(String bookId, int start, int count, String fields);

    void loadRecommendedBooks(String bookId, int start, int count, String fields);

    void cancelLoading();
}
