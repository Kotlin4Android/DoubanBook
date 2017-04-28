package com.jc.bookbrowser.presenter;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public interface IBookListPresenter {

    void loadBooks(String q, String tag, int start, int count, String fields);

    void cancelLoading();

}
