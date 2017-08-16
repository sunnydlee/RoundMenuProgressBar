package com.alanlee.roundmenuprogressbar;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RoundMenuProgressBar mProgressBar;
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mProgressBar = (RoundMenuProgressBar) findViewById(R.id.roundProgressBar);
        //更换待执行功能的图
//        mProgressBar.setBitmap1(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        mProgressBar.setBitmap2(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        mProgressBar.setBitmap3(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        mProgressBar.setBitmap4(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        mProgressBar.setBitmap5(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        //更换正在执行功能的图片
//        mProgressBar.setBitmapRed1(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());

        //获取中间控制按钮
        final ImageView ivControl = mProgressBar.getIvControl();
        //中间进度tv
        final TextView tvProgress =  mProgressBar.getTvProgress();
        ivControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.start();
                ivControl.setVisibility(View.GONE);
                tvProgress.setVisibility(View.VISIBLE);
            }
        });

        //模拟进度
        //30秒倒计时
         timer = new CountDownTimer(30000,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (((30000-millisUntilFinished)/30000f)*100);
                mProgressBar.setProgress(progress);
                tvProgress.setText(progress+"%");
            }

            @Override
            public void onFinish() {
                mProgressBar.setProgress(100);
                tvProgress.setText("100%");
            }
        };


    }
}
