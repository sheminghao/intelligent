package com.shemh.intelligent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.shemh.intelligent.adapter.SeatAdapter;
import com.shemh.intelligent.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        SeatAdapter seatAdapter = new SeatAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(seatAdapter);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            list.add(i + "");
        }
        seatAdapter.setDataList(list);
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                ToastUtils.showToast(R.string.press_again_to_quit_app);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                AppManager.getAppManager().AppExit(MainActivity.this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
