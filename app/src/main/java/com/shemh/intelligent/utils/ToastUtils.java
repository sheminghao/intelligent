package com.shemh.intelligent.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shemh.intelligent.APP;

/**
 * ToastUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-9
 */
public class ToastUtils {
    private static Toast toast = null; //Toast的对象！

    public static void showToast(String str) {
//        if (toast == null) {
//            toast = Toast.makeText(APP.getInstance(), id, Toast.LENGTH_SHORT);
//        }else {
//            toast.setText(id);
//        }
        toast = Toast.makeText(APP.getInstance(), str, Toast.LENGTH_SHORT);
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.removeAllViews();
        toastView.setBackgroundColor(Color.parseColor("#b2000000"));
        toastView.setPadding(38, 26, 38, 26);
        TextView tvCodeProject = new TextView(APP.getInstance());
        tvCodeProject.setTextSize(12);
        tvCodeProject.setText(str);
        toastView.addView(tvCodeProject, 0);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(Context mContext, String id) {
        if (toast == null) {
            toast = Toast.makeText(mContext, id, Toast.LENGTH_SHORT);
        }
        else {
            toast.setText(id);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(int id) {
//        if (toast == null) {
//            toast = Toast.makeText(APP.getInstance(), id, Toast.LENGTH_SHORT);
//        }else {
//            toast.setText(id);
//        }
        toast = Toast.makeText(APP.getInstance(), id, Toast.LENGTH_SHORT);
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.removeAllViews();
        toastView.setBackgroundColor(Color.parseColor("#b2000000"));
        toastView.setPadding(38, 26, 38, 26);
        TextView tvCodeProject = new TextView(APP.getInstance());
        tvCodeProject.setTextSize(12);
        tvCodeProject.setText(id);
        toastView.addView(tvCodeProject, 0);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
