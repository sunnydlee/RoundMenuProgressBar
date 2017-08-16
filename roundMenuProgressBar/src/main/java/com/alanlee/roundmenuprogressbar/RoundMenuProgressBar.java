package com.alanlee.roundmenuprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 圆环菜单进度的进度条
 * 核心方法 :getTvProgress
 * @author  lixiao
 *
 */
public class RoundMenuProgressBar extends RelativeLayout {

    private Context mContext;
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    /**
     * 菜单圆 画笔
     */
    private Paint mMenuPaint;
    /**
     * 菜单圆半径(大圆的8分之一)
     */
    private int   menuCircleRadius;

    /**
     * 小圆中功能标签的画笔
     */
    private Paint mTextPaint;
    /**
     * 控制按钮
     */
    private ImageView ivControl;
    /**
     * 居中的进度显示
     */
    private TextView tvProgress;

    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;

    private Bitmap bitmapRed1;
    private Bitmap bitmapRed2;
    private Bitmap bitmapRed3;
    private Bitmap bitmapRed4;
    private Bitmap bitmapRed5;

    /**
     * 控制要绘图的部分
     */
    private Rect mSrcRect;
    /**
     * 控制要绘图的位置与大小
     */
    private Rect mDesRect;

    /**
     * 小圆正方形外框边长
     */
    private int circleWidth ;// = getWidth()/3;





    public RoundMenuProgressBar(Context context) {
        this(context, null);
    }

    public RoundMenuProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundMenuProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setWillNotDraw(false);//解决ViewGroup的onDraw不被调用

        paint = new Paint();
        mMenuPaint = new Paint();
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#666666"));
        mTextPaint.setTextSize(DensityUtil.dip2px(context,13));
        mTextPaint.setAntiAlias(true);


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundMenuProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundMenuProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundMenuProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundMenuProgressBar_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.RoundMenuProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundMenuProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundMenuProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundMenuProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundMenuProgressBar_style, 0);

        mTypedArray.recycle();

        //添加控制按钮
        ivControl = new ImageView(mContext);
        LayoutParams params = new LayoutParams(DensityUtil.dip2px(context,50),DensityUtil.dip2px(context,50));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        ivControl.setLayoutParams(params);
        ivControl.setPadding(DensityUtil.dip2px(context,7),0,0,0);
        ivControl.setImageResource(R.mipmap.icon_play3);
        ivControl.setClickable(true);
        addView(ivControl,params);

        //添加进度显示
        tvProgress = new TextView(mContext);
        LayoutParams params2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvProgress.setTextSize(35);
        tvProgress.setTextColor(roundColor);
        addView(tvProgress,params2);
        tvProgress.setVisibility(GONE);

        initBitmap();

    }


    /**
     * 获取进度显示
     * @return
     */
    public TextView getTvProgress() {
        return tvProgress;
    }

    /**
     * 获取控制按钮
     * 使用方法：1、设置该按钮点击监听
     *          2、在点击后，隐藏该按钮 ivControl.setVisibility(View.GONE);
     *          3、显示进度 #tvProgress.setVisibility(View.VISIBLE); tvProgress.setText(progress+"%");
     * @return
     */
    public ImageView getIvControl() {
        return ivControl;
    }

    /**
     * 设置控制按钮图片
     * @param resId
     */
    public void setIvControlImageResource(int resId){
        if(ivControl !=null){
            ivControl.setImageResource(resId);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }


    private void initBitmap(){
        bitmap1 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle1)).getBitmap();
        bitmap2 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle2)).getBitmap();
        bitmap3 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle3)).getBitmap();
        bitmap4 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle4)).getBitmap();
        bitmap5 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle5)).getBitmap();

        bitmapRed1 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle1_doing)).getBitmap();
        bitmapRed2 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle2_doing)).getBitmap();
        bitmapRed3 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle3_doing)).getBitmap();
        bitmapRed4 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle4_doing)).getBitmap();
        bitmapRed5 = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.icon_test_circle5_doing)).getBitmap();


        mSrcRect = new Rect(0,0,bitmap1.getWidth(),bitmap1.getHeight());


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int roundCenterX = getWidth()/2; //获取圆心的x坐标
        int roundCenterY = getHeight()/2; //获取圆心的Y坐标
        int radius = (int) (roundCenterX - roundWidth/2)-getWidth()/10; //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(roundCenterX, roundCenterY, radius, paint); //画出圆环

        Log.e("log", roundCenterX + "");






        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        int percent = (int)(((float)progress / (float)max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        if(textIsDisplayable && percent != 0 && style == STROKE){
            canvas.drawText(percent + "%", roundCenterX - textWidth / 2, roundCenterX + textSize/2, paint); //画出进度百分比
        }



        /**
         * 画圆弧 ，画圆环的进度
         */

        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(roundCenterX - radius, roundCenterY - radius, roundCenterX
                + radius, roundCenterY + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE:{
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0-90, 360 * progress / max, false, paint);  //根据进度画圆弧
                break;
            }
            case FILL:{
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(progress !=0)
                    canvas.drawArc(oval, 0-90, 360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }



        //画小圆-默认背景
        //上圆
        menuCircleRadius = getWidth()/10;//小圆半径（原版，不用）
        circleWidth = getWidth()/3-20;//正方形边长（新，自定义）
        mMenuPaint.setColor(roundColor);
        mMenuPaint.setStyle(Paint.Style.FILL);
        mMenuPaint.setAntiAlias(true);
        int centerX =getWidth()/2;
        int centerY = (int) (roundCenterY - radius+roundWidth/2);
//        canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
//        Log.i("TAG", "onDraw: sin54:"+Math.sin(Math.toRadians(54d)));
//        Log.i("TAG", "onDraw: cos54:"+Math.cos(Math.toRadians(54d)));
        int offsetBX = centerX-circleWidth/2;
        int offsetBY = centerY-circleWidth/2;
        mDesRect = new Rect(offsetBX,offsetBY,circleWidth+offsetBX,circleWidth+offsetBY);
        if(progress > 0){
            canvas.drawBitmap(bitmapRed1,mSrcRect,mDesRect,null);
        }else{
            canvas.drawBitmap(bitmap1,mSrcRect,mDesRect,null);
        }

        //右圆
        centerX =(int) (radius*Math.sin(Math.toRadians(72d))+getWidth()/2);
        centerY = (int) (roundCenterY-radius*Math.cos(Math.toRadians(72d)));
//        canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
        offsetBX = centerX-circleWidth/2-(25);
        offsetBY = centerY-circleWidth/2+(30);
        mDesRect = new Rect(offsetBX,offsetBY,circleWidth+offsetBX,circleWidth+offsetBY);
        if(progress >(20-50/10f) ){
            canvas.drawBitmap(bitmapRed2,mSrcRect,mDesRect,null);
        }else{
            canvas.drawBitmap(bitmap2,mSrcRect,mDesRect,null);
        }

        //右下圆
        centerX =(int) (radius*Math.cos(Math.toRadians(54d))+getWidth()/2);
        centerY = (int) (radius*Math.sin(Math.toRadians(54d))+roundCenterY);
//        canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
        offsetBX = centerX-circleWidth/2-(0);
        offsetBY = centerY-circleWidth/2+(0);
        mDesRect = new Rect(offsetBX,offsetBY,circleWidth+offsetBX,circleWidth+offsetBY);
        if(progress > (40-50/10f)){
            canvas.drawBitmap(bitmapRed3,mSrcRect,mDesRect,null);
        }else{
            canvas.drawBitmap(bitmap3,mSrcRect,mDesRect,null);
        }
        //左下圆
        centerX =(int) (getWidth()/2-radius*Math.cos(Math.toRadians(54d)));
        centerY = (int) (radius*Math.sin(Math.toRadians(54d))+roundCenterY);
//        canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
        offsetBX = centerX-circleWidth/2-(0);
        offsetBY = centerY-circleWidth/2+(0);
        mDesRect = new Rect(offsetBX,offsetBY,circleWidth+offsetBX,circleWidth+offsetBY);
        if(progress > (60-50/10f)){
            canvas.drawBitmap(bitmapRed4,mSrcRect,mDesRect,null);
        }else{
            canvas.drawBitmap(bitmap4,mSrcRect,mDesRect,null);
        }
        //左圆
        centerX =(int) (getWidth()/2-radius*Math.sin(Math.toRadians(72d)));
        centerY = (int) (roundCenterY-radius*Math.cos(Math.toRadians(72d)));
//        canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
        offsetBX = centerX-circleWidth/2+(38);
        offsetBY = centerY-circleWidth/2+(30);
        mDesRect = new Rect(offsetBX,offsetBY,circleWidth+offsetBX,circleWidth+offsetBY);
        if(progress > (80-50/10f)){
            canvas.drawBitmap(bitmapRed5,mSrcRect,mDesRect,null);
        }else{
            canvas.drawBitmap(bitmap5,mSrcRect,mDesRect,null);
        }


//        //画小圆-按进度
//        mMenuPaint.setColor(roundProgressColor);//进度颜色
//        //上圆
//        if(progress > 0){
//            centerX =getWidth()/2;
//            centerY = (int) (roundCenterY - radius+roundWidth/2);
//            canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
//        }
//        //右圆
//        if(progress > (20-50/10f) ){
//            centerX =(int) (radius*Math.sin(Math.toRadians(72d))+getWidth()/2);
//            centerY = (int) (roundCenterY-radius*Math.cos(Math.toRadians(72d)));
//            canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
//        }
//        //右下圆
//        if(progress > (40-50/10f)){
//            centerX =(int) (radius*Math.cos(Math.toRadians(54d))+getWidth()/2);
//            centerY = (int) (radius*Math.sin(Math.toRadians(54d))+roundCenterY);
//            canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
//        }
//        //左下圆
//        if(progress > (60-50/10f)){
//            centerX =(int) (getWidth()/2-radius*Math.cos(Math.toRadians(54d)));
//            centerY = (int) (radius*Math.sin(Math.toRadians(54d))+roundCenterY);
//            canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
//        }
//        //左圆
//        if(progress > (80-50/10f)){
//            centerX =(int) (getWidth()/2-radius*Math.sin(Math.toRadians(72d)));
//            centerY = (int) (roundCenterY-radius*Math.cos(Math.toRadians(72d)));
//            canvas.drawCircle(centerX,centerY,menuCircleRadius,mMenuPaint);
//        }

//        //绘制-功能标题
//        int offsetX = (int) (-menuCircleRadius*0.80f);
//        int offsetY = 10;
//        //上
//        if(progress > 0){
//            mTextPaint.setColor(getResources().getColor(R.color.orange_explain));
//        }
//        centerX =getWidth()/2+offsetX;
//        centerY = (int) (roundCenterY - radius+roundWidth/2)+offsetY;
//        canvas.drawText("同频分析",centerX,centerY,mTextPaint);
//
//        //右
//        if(progress > (20-50/10f) ){
//            mTextPaint.setColor(getResources().getColor(R.color.orange_explain));
//        }else{
//            mTextPaint.setColor(Color.parseColor("#666666"));
//        }
//        centerX =(int) (radius*Math.sin(Math.toRadians(72d))+getWidth()/2)+offsetX;
//        centerY = (int) (roundCenterY-radius*Math.cos(Math.toRadians(72d)))+offsetY;
//        canvas.drawText("网络监测",centerX,centerY,mTextPaint);
//
//        //右下
//        if(progress > (40-50/10f)){
//            mTextPaint.setColor(getResources().getColor(R.color.orange_explain));
//        }else{
//            mTextPaint.setColor(Color.parseColor("#666666"));
//        }
//        centerX =(int) (radius*Math.cos(Math.toRadians(54d))+getWidth()/2)+offsetX;
//        centerY = (int) (radius*Math.sin(Math.toRadians(54d))+roundCenterY)+offsetY;
//        canvas.drawText("领域分析",centerX,centerY,mTextPaint);
//
//        //左下
//        if(progress > (60-50/10f)){
//            mTextPaint.setColor(getResources().getColor(R.color.orange_explain));
//        }else{
//            mTextPaint.setColor(Color.parseColor("#666666"));
//        }
//        centerX =(int) (getWidth()/2-radius*Math.cos(Math.toRadians(54d)))+offsetX;
//        centerY = (int) (radius*Math.sin(Math.toRadians(54d))+roundCenterY)+offsetY;
//        canvas.drawText("WIFI信息",centerX,centerY,mTextPaint);
//        //左
//        if(progress > (80-50/10f)){
//            mTextPaint.setColor(getResources().getColor(R.color.orange_explain));
//        }else{
//            mTextPaint.setColor(Color.parseColor("#666666"));
//        }
//        centerX =(int) (getWidth()/2-radius*Math.sin(Math.toRadians(72d)))+offsetX;
//        centerY = (int) (roundCenterY-radius*Math.cos(Math.toRadians(72d)))+offsetY;
//        canvas.drawText("周边干扰",centerX,centerY,mTextPaint);


    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     * @param max
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }

    }


    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }


    public Bitmap getBitmap1() {
        return bitmap1;
    }

    public void setBitmap1(Bitmap bitmap1) {
        this.bitmap1 = bitmap1;
    }

    public Bitmap getBitmap2() {
        return bitmap2;
    }

    public void setBitmap2(Bitmap bitmap2) {
        this.bitmap2 = bitmap2;
    }

    public Bitmap getBitmap3() {
        return bitmap3;
    }

    public void setBitmap3(Bitmap bitmap3) {
        this.bitmap3 = bitmap3;
    }

    public Bitmap getBitmap4() {
        return bitmap4;
    }

    public void setBitmap4(Bitmap bitmap4) {
        this.bitmap4 = bitmap4;
    }

    public Bitmap getBitmap5() {
        return bitmap5;
    }

    public void setBitmap5(Bitmap bitmap5) {
        this.bitmap5 = bitmap5;
    }

    public Bitmap getBitmapRed1() {
        return bitmapRed1;
    }

    public void setBitmapRed1(Bitmap bitmapRed1) {
        this.bitmapRed1 = bitmapRed1;
    }

    public Bitmap getBitmapRed2() {
        return bitmapRed2;
    }

    public void setBitmapRed2(Bitmap bitmapRed2) {
        this.bitmapRed2 = bitmapRed2;
    }

    public Bitmap getBitmapRed3() {
        return bitmapRed3;
    }

    public void setBitmapRed3(Bitmap bitmapRed3) {
        this.bitmapRed3 = bitmapRed3;
    }

    public Bitmap getBitmapRed4() {
        return bitmapRed4;
    }

    public void setBitmapRed4(Bitmap bitmapRed4) {
        this.bitmapRed4 = bitmapRed4;
    }

    public Bitmap getBitmapRed5() {
        return bitmapRed5;
    }

    public void setBitmapRed5(Bitmap bitmapRed5) {
        this.bitmapRed5 = bitmapRed5;
    }

    public Rect getSrcRect() {
        return mSrcRect;
    }

    public void setSrcRect(Rect srcRect) {
        mSrcRect = srcRect;
    }
}
