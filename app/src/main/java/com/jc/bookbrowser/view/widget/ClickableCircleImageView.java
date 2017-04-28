package com.jc.bookbrowser.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.jc.bookbrowser.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HaohaoChang on 2016/9/17.
 */
public class ClickableCircleImageView extends CircleImageView {
    public ClickableCircleImageView(Context context) {
        super(context);
    }

    public ClickableCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (pressed) {
            setBorderColor(getResources().getColor(R.color.colorAccent));
        } else {
            setBorderColor(Color.WHITE);
        }
    }
}
