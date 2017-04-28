package com.jc.bookbrowser.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jc.bookbrowser.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.wangyuwei.particleview.ParticleView;

/**
 * Created by HaohaoChang on 2016/11/28.
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.pv_logo)
    ParticleView pvLogo;
    private View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        pvLogo.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        pvLogo.startAnim();
    }
}
