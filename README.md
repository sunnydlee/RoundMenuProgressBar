# RoundMenuProgressBar
圆盘菜单进度条

[![](https://jitpack.io/v/hackware1993/MagicIndicator.svg)](https://jitpack.io/#hackware1993/MagicIndicator)
![效果图](https://github.com/sunnydlee/RoundMenuProgressBar/blob/master/Screenshot_20170816-114025.png)


# Usage

Simple steps, you can integrate **RoundMenuProgressBar**:

1. checkout out **RoundMenuProgressBar**, which contains source code and demo
2. import module **RoundMenuProgressBar** and add dependency:

  ```
  compile project(':RoundMenuProgressBar')
  ```

 
  **or**
  
  ```
  repositories {
      ...
      maven {
          url "https://jitpack.io"
      }
  }
  
  dependencies {
      ...
      compile 'com.github.hackware1993:MagicIndicator:1.3.1'
  }
  ```
  
  
  3. add **RoundMenuProgressBar** to your layout xml:
  ```
  <com.alanlee.roundmenuprogressbar.RoundMenuProgressBar
        android:id="@+id/roundProgressBar"
        android:layout_width="344dp"
        android:layout_height="400dp"
        android:layout_centerInParent="true"

        app:roundColor="#ffffff"
        app:roundProgressColor="#A5D2FF"
        app:textColor="#9A32CD"
        app:textIsDisplayable="false"
        app:roundWidth="7dip"
        app:textSize="18sp"
        tools:layout_editor_absoluteY="0dp"
        tools:layout_editor_absoluteX="8dp"/>
  ```
  
  
4. find **RoundMenuProgressBar** through code, initialize it:
```

        mProgressBar = (RoundMenuProgressBar) findViewById(R.id.roundProgressBar);
        //设置待执行功能的图
//        mProgressBar.setBitmap1(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        mProgressBar.setBitmap2(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        mProgressBar.setBitmap3(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        mProgressBar.setBitmap4(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        mProgressBar.setBitmap5(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
//        //设置正在执行功能的图片
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


```

#  Core Methods-核心方法

获取控制按钮：
```
mProgressBar.getIvControl();
```

设置进度：
```
mProgressBar.setProgress(progress);
```
获取显示进度TextView：
```
mProgressBar.getIvControl();
```

更换待执行功能的图片：
```
mProgressBar.setBitmap1(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
```

更换正在执行功能的图片：
```
mProgressBar.setBitmapRed1(((BitmapDrawable)getResources().getDrawable(R.mipmap.icon_fish1)).getBitmap());
```



# License

  ```
  MIT License
  
  Copyright (c) 2017 sunnydlee
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
  ```

  
