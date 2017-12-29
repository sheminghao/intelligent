package com.shemh.intelligent.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shemh.intelligent.R;
import com.shemh.intelligent.utils.ToastUtils;

/**
 * Created by shemh on 2017/12/7.
 */

public class SeatAdapter extends ListBaseAdapter<String> {

    public SeatAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_seat;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView imgSeat = holder.getView(R.id.img_seat);
        TextView tvSeatNum = holder.getView(R.id.tv_seat_num);
        tvSeatNum.setText(mDataList.get(position));

        imgSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(mDataList.get(position));
            }
        });
    }
}
