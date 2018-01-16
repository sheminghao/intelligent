package com.shemh.intelligent.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.shemh.intelligent.R;

/**
 * Created by shemh on 2018/1/16.
 */

public class JingbaoDialog extends Dialog {

    private Context context;

    public JingbaoDialog(Context context) {
        super(context);
        this.context = context;
    }

    public JingbaoDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_jingbao, null);
        setContentView(view);
    }

    @Override
    public void show() {
        super.show();
    }
}
