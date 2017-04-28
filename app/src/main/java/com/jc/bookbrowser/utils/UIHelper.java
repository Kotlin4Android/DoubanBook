package com.jc.bookbrowser.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jc.bookbrowser.BookBrowserApplication;
import com.jc.bookbrowser.R;
import com.jc.bookbrowser.view.activity.BaseActivity;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public class UIHelper {

    private static final String WEICHAT_KEY = "Kdescription";
    public static Context getContext() {
        return BookBrowserApplication.getApplication();
    }

    public static void startActivity(Intent intent) {
        if (BaseActivity.activity == null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } else {
            BaseActivity.activity.startActivity(intent);
        }

    }

    public static void share(Context context, String content, Uri uri) {

        Intent shareIntent = new Intent();
        if (uri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            shareIntent.putExtra("sms_body", content);
        } else {
            shareIntent.setType("text/plain");
        }
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,content);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(WEICHAT_KEY, content);
        context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_dialog_title) ));

    }

}
