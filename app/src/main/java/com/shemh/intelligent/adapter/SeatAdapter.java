package com.shemh.intelligent.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shemh.intelligent.R;
import com.shemh.intelligent.bean.DeviceInfoBean;
import com.shemh.intelligent.utils.DeviceInfoData;

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

    Handler handler = new Handler();

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView imgSeat = holder.getView(R.id.img_seat);
        final ImageView imgHujiao = holder.getView(R.id.img_hujiao);
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

        imgSeat.setTag(position);
        if (!TextUtils.isEmpty(mDataList.get(position).getSeatState())){
            if ("01".equals(mDataList.get(position).getSeatState())){
                if (((int)imgSeat.getTag()) == position) {
                    imgSeat.setImageResource(R.mipmap.zuowei_lv);
                    if ("01".equals(mDataList.get(position).getAnquandai())) {
                        imgSeat.setImageResource(R.mipmap.zuowei_lan);
                    } else {
                    }
                }
            }else if ("00".equals(mDataList.get(position).getSeatState())){
                if (((int)imgSeat.getTag()) == position) {
                    imgSeat.setImageResource(R.mipmap.zuowei_hong);
                }
            }
        }else {
            if (((int)imgSeat.getTag()) == position) {
                imgSeat.setImageResource(R.mipmap.zuowei_hong);
            }
        }

        imgHujiao.setTag(position);
//        Log.i("TAG", "------kjkk"+((int)imgHujiao.getTag()) + ",, " + position);
        if (!TextUtils.isEmpty(mDataList.get(position).getHujiaoSiji())){
            if ("00".equals(mDataList.get(position).getHujiaoSiji())){
                imgHujiao.setVisibility(View.INVISIBLE);
            }else if ("01".equals(mDataList.get(position).getHujiaoSiji())){
                if (((int)imgHujiao.getTag()) == position) {
                    imgHujiao.setVisibility(View.VISIBLE);
                    mDataList.get(position).setHujiaoSiji("00");
                    DeviceInfoData.saveDeviceInfo(mDataList);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgHujiao.setVisibility(View.INVISIBLE);
                        }
                    }, 5000);
                }
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
