package com.shemh.intelligent.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shemh.intelligent.R;
import com.shemh.intelligent.bean.DeviceInfoBean;

/**
 * Created by shemh on 2017/12/7.
 */

public class SeatAdapter extends ListBaseAdapter<DeviceInfoBean> {

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
        TextView tvRegisterState = holder.getView(R.id.tv_register_state);
        if (TextUtils.isEmpty(mDataList.get(position).getDeviceId())){
            tvRegisterState.setVisibility(View.VISIBLE);
        }else {
            tvRegisterState.setVisibility(View.INVISIBLE);
        }

        tvSeatNum.setText(position+1+"");
        LinearLayout layoutSeat = holder.getView(R.id.layout_seat);
        layoutSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener){
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        if (!TextUtils.isEmpty(mDataList.get(position).getSeatState())){
            if ("00".equals(mDataList.get(position).getSeatState())){
                imgSeat.setImageResource(R.mipmap.zuowei_lv);
            }else if ("01".equals(mDataList.get(position).getSeatState())){
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
