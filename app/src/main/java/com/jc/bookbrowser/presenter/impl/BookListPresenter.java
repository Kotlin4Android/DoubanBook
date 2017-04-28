package com.jc.bookbrowser.presenter.impl;

import com.jc.bookbrowser.BookBrowserApplication;
import com.jc.bookbrowser.R;
import com.jc.bookbrowser.entity.http.BaseResponse;
import com.jc.bookbrowser.entity.http.BookListResponse;
import com.jc.bookbrowser.model.IBookListModel;
import com.jc.bookbrowser.model.RequestListener;
import com.jc.bookbrowser.model.source.BookListModel;
import com.jc.bookbrowser.presenter.IBookListPresenter;
import com.jc.bookbrowser.utils.NetworkUtils;
import com.jc.bookbrowser.view.IBookListView;

/**
 * Created by HaohaoChang on 2016/11/23.
 */
public class BookListPresenter implements IBookListPresenter, RequestListener {
    private IBookListView bookListView;
    private IBookListModel bookListModel;

    public BookListPresenter(IBookListView bookListView) {
        this.bookListView = bookListView;
        bookListModel = new BookListModel();
    }

    @Override
    public void loadBooks(String q, String tag, int start, int count, String fields) {
        if (!NetworkUtils.isConnected(BookBrowserApplication.getApplication())) {
            bookListView.showMessage(BookBrowserApplication.getApplication().getString(R.string.fail_to_connect_to_network));
            bookListView.hideProgress();
        }
        bookListView.showProgress();
        bookListModel.loadBookList(q,tag,start,count,fields,this);
    }

    @Override
    public void cancelLoading() {
        bookListModel.cancelLoading();

    }

    @Override
    public void onCompleted(Object result) {
        if (result instanceof BookListResponse) {
            int index = ((BookListResponse)result).getStart();
            if (index == 0) {
                bookListView.refreshData(result);
            } else {
                bookListView.addData(result);
            }
            bookListView.hideProgress();
        }

    }

    @Override
    public void onFailed(BaseResponse message) {
        bookListView.hideProgress();
        if (message == null) {
            return;
        }
        bookListView.showMessage(message.getMsg());

    }
}
