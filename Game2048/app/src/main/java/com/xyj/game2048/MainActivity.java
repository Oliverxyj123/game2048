package com.xyj.game2048;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    private static MainActivity mainActivity = null;
    private int score;

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textViewScore);
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        textView.setText(String.valueOf(score));
    }

    public void addScore(int s) {
        score += s;
        showScore();
    }


}
