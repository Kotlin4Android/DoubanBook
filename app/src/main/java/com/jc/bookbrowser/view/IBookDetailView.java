package com.jc.bookbrowser.view;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public interface IBookDetailView {
    void showMessage(String msg);

    void showProgress();

    void hideProgress();

    void showData(Object data);
}
