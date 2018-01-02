package com.shemh.intelligent.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        tvSeatNum.setText(position+"");
        LinearLayout layoutSeat = holder.getView(R.id.layout_seat);
        layoutSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener){
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        if (mDataList.get(position).length() > 10){
            if ("00".equals(mDataList.get(position).substring(20, 22))){
                imgSeat.setImageResource(R.mipmap.zuowei_lv);
            }else if ("01".equals(mDataList.get(position).substring(20, 22))){
                imgSeat.setImageResource(R.mipmap.zuowei_hong);
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }
}
