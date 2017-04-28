package com.jc.bookbrowser.presenter.impl;

import android.util.Log;

import com.jc.bookbrowser.R;
import com.jc.bookbrowser.entity.http.BaseResponse;
import com.jc.bookbrowser.model.IBookDetailModel;
import com.jc.bookbrowser.model.RequestListener;
import com.jc.bookbrowser.model.source.BookDetailModel;
import com.jc.bookbrowser.presenter.IBookDetailPresenter;
import com.jc.bookbrowser.utils.NetworkUtils;
import com.jc.bookbrowser.utils.UIHelper;
import com.jc.bookbrowser.view.IBookDetailView;

/**
 * Created by HaohaoChang on 2016/11/28.
 */
public class BookDetailPresenter implements IBookDetailPresenter, RequestListener {
    private String TAG = BookDetailPresenter.class.getSimpleName();
    private IBookDetailModel iBookDetailModel;
    private IBookDetailView iBookDetailView;

    public BookDetailPresenter(IBookDetailView iBookDetailView) {
        this.iBookDetailView = iBookDetailView;
        this.iBookDetailModel = new BookDetailModel();
    }

    @Override
    public void loadComments(String bookId, int start, int count, String fields) {
        if (!NetworkUtils.isConnected(UIHelper.getContext())) {
            iBookDetailView.showMessage(UIHelper.getContext().getString(R.string.fail_to_connect_to_network));
            iBookDetailView.hideProgress();
        }
        iBookDetailView.showProgress();
        iBookDetailModel.loadBookComments(bookId, start, count, fields, this);

    }

    @Override
    public void loadRecommendedBooks(String bookId, int start, int count, String fields) {
        if (!NetworkUtils.isConnected(UIHelper.getContext())) {
            iBookDetailView.showMessage(UIHelper.getContext().getString(R.string.fail_to_connect_to_network));
        }
        iBookDetailModel.loadRecommendedBooks(bookId, start, count, fields, this);

    }

    @Override
    public void cancelLoading() {
        iBookDetailModel.cancelLoading();

    }

    @Override
    public void onCompleted(Object result) {
        Log.i(TAG,"onCompleted");
        iBookDetailView.showData(result);
        iBookDetailView.hideProgress();

    }

    @Override
    public void onFailed(BaseResponse message) {
        Log.i(TAG,"onFailed");
        iBookDetailView.hideProgress();
        if (message != null) {
            iBookDetailView.showMessage(message.getMsg());
        }
    }
}
