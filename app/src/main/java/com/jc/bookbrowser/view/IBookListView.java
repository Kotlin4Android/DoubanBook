package com.jc.bookbrowser.view;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public interface IBookListView {
    void showMessage(String msg);

    void showProgress();

    void hideProgress();

    void refreshData(Object data);

    void addData(Object data);
}
