package com.noahedu.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.noahedu.demo.R;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：GameActivity$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/5$ 10:14$
 */
public class GameActivity extends Activity {
    private GameView gV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gV = (GameView) findViewById(R.id.game);
    }

    public void rePay(View v){
        gV.repeat();
    }
    public void newPay(View v){
        gV.play();

    }
}
