package com.shemh.intelligent;

import android.app.Application;
import android.widget.ImageView;

/**
 * Created by shemh on 2017/2/17.
 */

public class APP extends Application {

    public static APP instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized APP getInstance() {
        return instance;
    }
}
