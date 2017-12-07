package com.shemh.intelligent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.shemh.intelligent.utils.AppUtils;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout layoutLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutLogin = (RelativeLayout) findViewById(R.id.content_login);
        layoutLogin.setPadding(0, AppUtils.getStatusBar(), 0, 0);
    }

}
