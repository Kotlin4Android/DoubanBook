package com.jc.bookbrowser.model;

import com.jc.bookbrowser.entity.http.BaseResponse;

/**
 * Created by HaohaoChang on 2016/9/18.
 */
public interface RequestListener {
    void onCompleted(Object result);
    void onFailed(BaseResponse message);
}
