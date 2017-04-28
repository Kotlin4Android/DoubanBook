package com.jc.bookbrowser.model;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public interface IBookListModel {
    void loadBookList(String q,String tag,int start,int count,String fields,RequestListener listener);
    void cancelLoading();
}
