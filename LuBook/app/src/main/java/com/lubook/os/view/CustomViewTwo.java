package com.lubook.os.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.lubook.os.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by wowo on 2017/9/22.
 */

public class CustomViewTwo extends View {

    private String textvalue;
    private int textcolor;
    private int textsize;
    private Paint mPaint;
    private Rect mBound;

    public CustomViewTwo(Context context) {
        super(context);
    }

    public CustomViewTwo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewTwo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomViewTwo, defStyleAttr, 0);
        int length = typedArray.getIndexCount();

        for (int i = 0; i < length; i++) {
            int attr = typedArray.getIndex(i);//自定义属性的值
            switch (attr) {
                case R.styleable.CustomTitleView_titleTextColor:
                    //获取文本颜色
                    textcolor = typedArray.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    //getDimensionPixelSize 获取attr的尺寸大小，如果attr没有值，则使用右侧默认的值
                    /*
                      TypedValue.applyDimension是一个将各种单位的值转换为像素的方法
                      三个参数，单位 TypedValue.COMPLEX_UNIT_SP  数值 16
                     */
                    textsize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    //获取文本大小
                    break;
                case R.styleable.CustomTitleView_titleText:
                    textvalue = typedArray.getString(attr);
                    //获取文本内容
                    break;
                default:
                    break;
            }
        }//end for

        typedArray.recycle();


        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(textsize);
        mBound = new Rect();
        mPaint.getTextBounds(textvalue, 0, textvalue.length(), mBound); // 没看懂它的作用是啥。。。。

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textvalue = randomText();
                postInvalidate();// 刷新
            }
        });
    }

    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append("" + i);
        }

        return sb.toString();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;

        int widthmode = MeasureSpec.getMode(widthMeasureSpec);
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        int heightsize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthmode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                break;
        }

        switch (heightmode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                break;
        }

        setMeasuredDimension(width, height);
        //
    }*/


}
