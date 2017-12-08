package com.shemh.intelligent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shemh.intelligent.adapter.SeatAdapter;
import com.shemh.intelligent.utils.ToastUtils;
import com.shemh.intelligent.view.SeatTable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> list;
    SeatAdapter seatAdapter;
    GridLayoutManager gridLayoutManager;
    public SeatTable seatTableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        seatAdapter = new SeatAdapter(this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(seatAdapter);

        list = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            list.add(i + "");
        }
        seatAdapter.setDataList(list);

        final EditText etRow = (EditText) findViewById(R.id.et_row);
        final EditText etNum = (EditText) findViewById(R.id.et_num);
        TextView tvQueding = (TextView) findViewById(R.id.tv_queding);
        tvQueding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row = Integer.parseInt(etRow.getText().toString().trim());
                int num = Integer.parseInt(etNum.getText().toString().trim());
                gridLayoutManager.setSpanCount(row);
                list.clear();
                for (int i = 0; i < num; i++) {
                    list.add(i + "");
                }
                seatAdapter.setDataList(list);
            }
        });


        seatTableView = (SeatTable) findViewById(R.id.seatView);
        seatTableView.setScreenName("8号厅荧幕");//设置屏幕名称
        seatTableView.setMaxSelected(3);//设置最多选中

        seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                if(column==2) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                if(row==6&&column==6){
                    return true;
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seatTableView.setData(10,15);
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
