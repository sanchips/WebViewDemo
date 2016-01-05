package com.zrp.webviewdemo.web;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 用于状态切换的布局，只显示1个状态
 * <p/>
 * Created by zijunna on 15/1/26.
 */
public class CustomFrameLayout extends FrameLayout {
    private int[] list;

    public CustomFrameLayout(Context context) {
        super(context);
        initView();
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * 设置子面板id数组
     *
     * @param list
     */
    public void setList(int[] list) {
        this.list = list;
        show(0);
    }

    /**
     * 显示某个面板
     *
     * @param id
     */
    public void show(int id) {
        if (list == null) {
            for (int i = 0; i < getChildCount(); ++i) {
                View view = getChildAt(i);

                if (id == view.getId()) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }
            return;
        }

        for (int aList : list) {
            View item = findViewById(aList);
            if (item == null) {
                continue;
            }
            if (aList == id) {
                item.setVisibility(View.VISIBLE);
            } else {
                item.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 隐藏所有面板
     */
    public void GoneAll() {
        if (list == null) {
            for (int i = 0; i < getChildCount(); ++i) {
                View view = getChildAt(i);
                view.setVisibility(View.GONE);
            }
            return;
        }

        for (int aList : list) {
            View item = findViewById(aList);
            if (item == null) {
                continue;
            }
            item.setVisibility(View.GONE);
        }
    }

    /**
     * 切换。
     *
     * @param index 布局在fram中的index
     */
    public void showOfIndex(int index) {
        for (int i = 0; i < getChildCount(); ++i) {
            View view = getChildAt(i);

            if (index == i) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public void initView() {
    }
}
