package com.jc.bookbrowser;

import android.app.Application;
import android.content.Context;

import com.jc.bookbrowser.view.activity.BaseActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by HaohaoChang on 2016/9/14.
 */
public class BookBrowserApplication extends Application {

    private static BookBrowserApplication application;
    private static List<BaseActivity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        activities = new LinkedList<>();
        application = this;
    }

    public static Context getApplication() {
        return application;
    }

    public void addActivity(BaseActivity activity) {

        activities.add(activity);

    }

    public void removeActivity(BaseActivity activity) {
        activities.remove(activity);
    }

    public static void clearActivities() {
        ListIterator<BaseActivity> iterator = activities.listIterator();
        BaseActivity activity;
        while (iterator.hasNext()) {
            activity = iterator.next();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    public static void quiteApp() {
        clearActivities();
        System.exit(0);
    }

}
