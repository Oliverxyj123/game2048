package com.xyj.game2048;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2015/12/19.
 */
public class GameView extends GridLayout {

    private Card[][] cards = new Card[4][4]; //卡片数组用来保存卡片
    private List<Point> emptyPoints = new ArrayList<>();
    private final static int COLOR = 0x33ffffff;

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    /**
     * 初始化视图
     */
    private void initGameView() {
        setBackgroundColor(0xffbbada0);

        /**
         * 设置触摸屏幕的监听器
         */
        setOnTouchListener(new OnTouchListener() {
            float startX;
            float startY;
            float offsetX;
            float offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /**
                 * 确定用户意图,根据用户按下和离开的位置确定
                 */
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if ((Math.abs(offsetX) - Math.abs(offsetY)) > 0) {
                            if (offsetX < -5) {
                                swipeLeft();
                            } else if (offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                            } else if (offsetY > 5) {
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 视图大小发生改变
     *
     * @param w    当前屏幕宽度
     * @param h    当前屏幕高度
     * @param oldw 上一次屏幕宽度
     * @param oldh 上一次屏幕高度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //确定卡片的宽高
        int cardWidth = (Math.min(w, h) - 10) / 4;
        addCard(cardWidth, cardWidth);

        startGame();//开始游戏
    }

    /**
     * 添加卡片
     *
     * @param cardWidth
     * @param cardHeight
     */
    private void addCard(int cardWidth, int cardHeight) {
        Card c;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                cards[x][y] = c;//将卡片保存到数组中
                c.setNum(0);//初始化所有位置都为空
                c.label.setBackgroundColor(COLOR);
                addView(c, cardWidth, cardHeight);//将卡片添加到游戏视图中
            }
        }
    }

    /**
     * 开始游戏
     */
    private void startGame() {

        MainActivity.getMainActivity().clearScore();

        //开始游戏前给数组所有位置赋空
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cards[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }

    /**
     * 添加卡片随机数
     */
    private void addRandomNum() {

        //先清空空点链表
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cards[x][y].getNum() == 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        //从空点链表中随机删除一个位置的点
        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        //给当前删除位置赋上随机数
        cards[p.x][p.y].setNum(Math.random() >= 0.1 ? 2 : 4);

    }

    /**
     * 处理向左滑动
     */
    private void swipeLeft() {

        boolean isAddNewCard = false;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (cards[x1][y].getNum() != 0) {
                        if (cards[x][y].getNum() == 0) {
                            cards[x][y].setNum(cards[x1][y].getNum());
                            if (cards[x][y].getNum() != 2) {

                                cards[x][y].label.setBackgroundColor(cards[x][y].getColor());
                            }
                            cards[x1][y].setNum(0);
                            cards[x1][y].label.setBackgroundColor(COLOR);
                            isAddNewCard = true;
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            x--;
                            break;
                        } else if (cards[x][y].equals(cards[x1][y])) {
                            cards[x][y].setNum(cards[x1][y].getNum() * 2);
                            cards[x][y].label.setBackgroundColor(cards[x][y].getColor());
                            cards[x1][y].setNum(0);
                            cards[x1][y].label.setBackgroundColor(COLOR);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            isAddNewCard = true;
                            break;
                        }
                    }
                }
            }
        }
        if (isAddNewCard) {
            addRandomNum();
            gameOver();
        }
    }

    /**
     * 处理向右滑动
     */
    private void swipeRight() {
        boolean isAddNewCard = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cards[x1][y].getNum() != 0) {
                        if (cards[x][y].getNum() == 0) {
                            cards[x][y].setNum(cards[x1][y].getNum());
                            if (cards[x][y].getNum() != 2) {

                                cards[x][y].label.setBackgroundColor(cards[x][y].getColor());
                            }
                            cards[x1][y].setNum(0);
                            cards[x1][y].label.setBackgroundColor(COLOR);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            x++;
                            isAddNewCard = true;
                            break;
                        } else if (cards[x][y].equals(cards[x1][y])) {
                            cards[x][y].setNum(cards[x1][y].getNum() * 2);
                            cards[x][y].label.setBackgroundColor(cards[x][y].getColor());
                            cards[x1][y].setNum(0);
                            cards[x1][y].label.setBackgroundColor(COLOR);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            isAddNewCard = true;
                            break;
                        }
                    }
                }
            }
        }
        if (isAddNewCard) {
            addRandomNum();
            gameOver();
        }
    }

    /**
     * 处理向上滑动
     */
    private void swipeUp() {

        boolean isAddNewCard = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (cards[x][y1].getNum() != 0) {
                        if (cards[x][y].getNum() == 0) {
                            cards[x][y].setNum(cards[x][y1].getNum());
                            if (cards[x][y].getNum() != 2) {

                                cards[x][y].label.setBackgroundColor(cards[x][y].getColor());
                            }
                            cards[x][y1].setNum(0);
                            cards[x][y1].label.setBackgroundColor(COLOR);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            y--;
                            isAddNewCard = true;
                            break;
                        } else if (cards[x][y].equals(cards[x][y1])) {
                            cards[x][y].setNum(cards[x][y1].getNum() * 2);
                            cards[x][y].label.setBackgroundColor(cards[x][y].getColor());
                            cards[x][y1].setNum(0);
                            cards[x][y1].label.setBackgroundColor(COLOR);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            isAddNewCard = true;
                            break;
                        }
                    }
                }
            }
        }
        if (isAddNewCard) {
            addRandomNum();
            gameOver();
        }
    }

    /**
     * 处理向下滑动
     */
    private void swipeDown() {

        boolean isAddNewCard = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cards[x][y1].getNum() != 0) {
                        if (cards[x][y].getNum() == 0) {
                            cards[x][y].setNum(cards[x][y1].getNum());
                            if (cards[x][y].getNum() != 2) {

                                cards[x][y].label.setBackgroundColor(cards[x][y].getColor());
                            }
                            cards[x][y1].setNum(0);
                            cards[x][y1].label.setBackgroundColor(COLOR);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            y++;
                            isAddNewCard = true;
                            break;
                        } else if (cards[x][y].equals(cards[x][y1])) {
                            cards[x][y].setNum(cards[x][y1].getNum() * 2);
                            cards[x][y].label.setBackgroundColor(cards[x][y].getColor());
                            cards[x][y1].setNum(0);
                            cards[x][y1].label.setBackgroundColor(COLOR);
                            isAddNewCard = true;
                            break;
                        }
                    }
                }
            }
        }

        if (isAddNewCard) {
            addRandomNum();
            gameOver();
        }
    }

    /**
     * 游戏结束判断
     * 当前所有位置卡片已经填充满且每个卡片的数字不相等
     */
    private void gameOver() {

        boolean isOver = true;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (cards[x][y].getNum() == 0
                        || (x > 0 && cards[x][y].equals(cards[x - 1][y]))
                        || (x < 3 && cards[x][y].equals(cards[x + 1][y]))
                        || (y > 0 && cards[x][y].equals(cards[x][y - 1]))
                        || (y < 3 && cards[x][y].equals(cards[x][y + 1]))) {

                    isOver = false;
                    break;
                }
            }
        }

        if (isOver) {
            new AlertDialog.Builder(getContext()).
                    setMessage("游戏结束").
                    setCancelable(false).
                    setPositiveButton("重来", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }
    }

}
