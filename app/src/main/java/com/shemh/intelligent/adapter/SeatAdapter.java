package com.shemh.intelligent.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.shemh.intelligent.R;

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
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView imgSeat = holder.getView(R.id.img_seat);
        TextView tvSeatNum = holder.getView(R.id.tv_seat_num);
        tvSeatNum.setText(mDataList.get(position));
    }
}
