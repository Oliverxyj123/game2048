package com.xyj.game2048;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Dell on 2015/12/19.
 */
public class Card extends FrameLayout {

    private int num = 0;//卡片上的数字
    public TextView label; //显示卡片上的数字;

    public Card(Context context) {
        super(context);
        label = new TextView(getContext());
        label.setGravity(Gravity.CENTER);
        label.setBackgroundColor(0x33ffffff);//设置卡片颜色
        label.setTextSize(32);//设置文本的大小
        LayoutParams lp = new LayoutParams(-1, -1);//填充满父级容器布局参数
        lp.setMargins(10, 10, 0, 0);//设置卡片间的间距
        setNum(0);//默认游戏开始前所有位置为空
        addView(label, lp);
    }

    /**
     * 获得卡片的数字
     *
     * @return
     */
    public int getNum() {
        return num;
    }

    /**
     * 设置卡片的数字
     *
     * @param num
     */
    public void setNum(int num) {
        this.num = num;

        if (num == 0) {
            label.setText("");
        } else {
            label.setText(String.valueOf(num));
        }
    }

    /**
     * 判断两张卡片是否数字相等
     *
     * @param o
     * @return
     */
    public boolean equals(Card o) {
        return getNum() == o.getNum();

    }

    /**
     * 设置卡片的颜色
     *
     * @return
     */
    public int getColor() {

        int r = (int) (Math.random() * 255);
        int g = (int) (Math.random() * 255);
        int b = (int) (Math.random() * 255);
        return Color.argb(0x33, r, g, b);
    }
}
