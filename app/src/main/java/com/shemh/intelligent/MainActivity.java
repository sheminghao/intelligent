package com.shemh.intelligent;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shemh.intelligent.adapter.SeatAdapter;
import com.shemh.intelligent.utils.ToastUtils;
import com.shemh.intelligent.utils.ZhuanHuanUtils;
import com.shemh.intelligent.view.SeatTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tw.com.prolific.driver.pl2303.PL2303Driver;

public class MainActivity extends AppCompatActivity {

    List<String> seatList;
    SeatAdapter seatAdapter;
    GridLayoutManager gridLayoutManager;
    public SeatTable seatTableView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.spinner_row)
    Spinner rowSpinner;
    @BindView(R.id.spinner_num)
    Spinner numSpinner;
    @BindView(R.id.spinner_baud_rate)
    Spinner baudRateSpinner;
    @BindView(R.id.et_read)
    EditText etRead;
    @BindView(R.id.et_write)
    EditText etWrite;

    List<Integer> seatNumber = new ArrayList<>();

    private static final boolean SHOW_DEBUG = true;

    private static final String ACTION_USB_PERMISSION = "com.shemh.intelligent.USB_PERMISSION";

    //BaudRate.B4800, DataBits.D8, StopBits.S1, Parity.NONE, FlowControl.RTSCTS
    private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;
    private PL2303Driver.DataBits mDataBits = PL2303Driver.DataBits.D8;
    private PL2303Driver.Parity mParity = PL2303Driver.Parity.NONE;
    private PL2303Driver.StopBits mStopBits = PL2303Driver.StopBits.S1;
    private PL2303Driver.FlowControl mFlowControl = PL2303Driver.FlowControl.OFF;

    PL2303Driver mSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        seatAdapter = new SeatAdapter(this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(seatAdapter);

        seatList = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            seatList.add(i + "");
        }
        seatAdapter.setDataList(seatList);

        final EditText etRow = (EditText) findViewById(R.id.et_row);
        final EditText etNum = (EditText) findViewById(R.id.et_num);
        TextView tvQueding = (TextView) findViewById(R.id.tv_queding);
        tvQueding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row = Integer.parseInt(etRow.getText().toString().trim());
                int num = Integer.parseInt(etNum.getText().toString().trim());
                gridLayoutManager.setSpanCount(row);
                seatList.clear();
                for (int i = 0; i < num; i++) {
                    seatList.add(i + "");
                }
                seatAdapter.setDataList(seatList);
            }
        });


        initSpinner();
        initSeatTableView();

        mSerial = new PL2303Driver((UsbManager) getSystemService(Context.USB_SERVICE),
                this, ACTION_USB_PERMISSION);

        // check USB host function.
        if (!mSerial.PL2303USBFeatureSupported()) {

            Toast.makeText(this, "No Support USB host API", Toast.LENGTH_SHORT)
                    .show();

            Log.d("TAG", "No Support USB host API");

            mSerial = null;

        }

        if( !mSerial.enumerate() ) {
            Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
        }

        try {
            Thread.sleep(1500);
            openUsbSerial();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_open, R.id.btn_read, R.id.btn_write})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_open:
                openUsbSerial();
                break;
            case R.id.btn_read:
                readDataFromSerial();
                break;
            case R.id.btn_write:
                writeDataToSerial();
                break;
        }
    }

    //初始化Spinner
    private void initSpinner(){
        //选择座位行数
        final ArrayAdapter<CharSequence> rowAdapter =
                ArrayAdapter.createFromResource(this, R.array.seat_row, android.R.layout.select_dialog_item);

        rowSpinner.setAdapter(rowAdapter);
        rowSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gridLayoutManager.setSpanCount(Integer.parseInt(rowAdapter.getItem(position).toString()));
                seatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (int i = 4; i < 60; i++) {
            seatNumber.add(i);
        }
        //选择座位个数
        final ArrayAdapter<Integer> numAdapter = new ArrayAdapter<>(this,
                android.R.layout.select_dialog_item, seatNumber);

        numSpinner.setAdapter(numAdapter);
        numSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seatList.clear();
                for (int i = 0; i < numAdapter.getItem(position); i++) {
                    seatList.add(i + "");
                }
                seatAdapter.setDataList(seatList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //选择波特率
        final ArrayAdapter<CharSequence> baudRateAdapter =
                ArrayAdapter.createFromResource(this, R.array.baud_rate, android.R.layout.select_dialog_item);

        baudRateSpinner.setAdapter(baudRateAdapter);
        baudRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(null==mSerial)
                    return;

                if(!mSerial.isConnected())
                    return;

                int baudRate=0;
                String newBaudRate;
                Toast.makeText(parent.getContext(), "newBaudRate is-" + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                newBaudRate= parent.getItemAtPosition(position).toString();

                try	{
                    baudRate= Integer.parseInt(newBaudRate);
                }
                catch (NumberFormatException e)	{
                    System.out.println(" parse int error!!  " + e);
                }

                switch (baudRate) {
                    case 9600:
                        mBaudrate = PL2303Driver.BaudRate.B9600;
                        break;
                    case 19200:
                        mBaudrate =PL2303Driver.BaudRate.B19200;
                        break;
                    case 115200:
                        mBaudrate =PL2303Driver.BaudRate.B115200;
                        break;
                    default:
                        mBaudrate =PL2303Driver.BaudRate.B9600;
                        break;
                }

                int res = 0;
                try {
                    res = mSerial.setup(mBaudrate, mDataBits, mStopBits, mParity, mFlowControl);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if( res<0 ) {
                    Log.d("TAG", "fail to setup");
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //读取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    readDataFromSerial();
                }
            }
        }).start();
    }

    private void writeDataToSerial() {

        Log.d("TAG", "Enter writeDataToSerial");

        if(null==mSerial)
            return;

        if(!mSerial.isConnected())
            return;

        String strWrite = etWrite.getText().toString();
		/*
        //strWrite="012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
       // strWrite = changeLinefeedcode(strWrite);
         strWrite="012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
         if (SHOW_DEBUG) {
            Log.d(TAG, "PL2303Driver Write(" + strWrite.length() + ") : " + strWrite);
        }
        int res = mSerial.write(strWrite.getBytes(), strWrite.length());
		if( res<0 ) {
			Log.d(TAG, "setup: fail to controlTransfer: "+ res);
			return;
		}

		Toast.makeText(this, "Write length: "+strWrite.length()+" bytes", Toast.LENGTH_SHORT).show();
		 */
        // test data: 600 byte
        //strWrite="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        if (SHOW_DEBUG) {
            Log.d("TAG", "PL2303Driver Write 2(" + strWrite.length() + ") : " + strWrite);
        }
        int res = mSerial.write(strWrite.getBytes(), strWrite.length());
        if( res<0 ) {
            Log.d("TAG", "setup2: fail to controlTransfer: "+ res);
            return;
        }

        Toast.makeText(this, "Write length: "+strWrite.length()+" bytes", Toast.LENGTH_SHORT).show();

        Log.d("TAG", "Leave writeDataToSerial");
    }

    private void readDataFromSerial() {

        int len;
        // byte[] rbuf = new byte[4096];
        byte[] rbuf = new byte[20];
        StringBuffer sbHex=new StringBuffer();

        Log.d("TAG", "Enter readDataFromSerial");

        if(null==mSerial)
            return;

        if(!mSerial.isConnected())
            return;

        len = mSerial.read(rbuf);
        if(len<0) {
            Log.d("TAG", "Fail to bulkTransfer(read data)");
            return;
        }

        if (len > 0) {
            if (SHOW_DEBUG) {
                Log.d("TAG", "read len : " + len);
            }
            //rbuf[len] = 0;
            for (int j = 0; j < len; j++) {
                //String temp=Integer.toHexString(rbuf[j]&0x000000FF);
                //Log.i(TAG, "str_rbuf["+j+"]="+temp);
                //int decimal = Integer.parseInt(temp, 16);
                //Log.i(TAG, "dec["+j+"]="+decimal);
                //sbHex.append((char)decimal);
                //sbHex.append(temp);
//                sbHex.append((char) (rbuf[j]&0x000000FF));
                sbHex.append(rbuf[j]);
            }

            byte[] rb = new byte[len];
            for (int i = 0; i < len; i++) {
                rb[i] = rbuf[i];
            }

            String r = ZhuanHuanUtils.byte2HexStr(rb);
            Log.i("TAG", "------rb, " + r);
            etRead.post(new Runnable() {
                @Override
                public void run() {
                    etRead.setText("empty");
                }
            });
//            Toast.makeText(this, "len="+len, Toast.LENGTH_SHORT).show();

            if (len > 8){
                char type = (char)(rbuf[8]&0x000000FF);
                Log.i("TAG", "------type, " + type);
                switch (type){
                    case 0xE4://设置主机恢复出厂设置
                        Log.i("TAG", "------设置主机恢复出厂设置, " + type);
                        break;
                    case 0xEA://设置主机进入注册状态
                        Log.i("TAG", "------设置主机进入注册状态, " + type);
                        break;
                    case 0xEB://设置主机退出注册状态
                        Log.i("TAG", "------设置主机退出注册状态, " + type);
                        break;
                    case 0xE3://获取注册设备数量
                        Log.i("TAG", "------获取注册设备数量, " + type);
                        break;
                    case 0xE2://获取设备ID及状态
                        Log.i("TAG", "------获取设备ID及状态, " + type);
                        break;
                    case 0xE1://清除注册表
                        Log.i("TAG", "------清除注册表, " + type);
                        break;
                }
            }
        }
        else {
            if (SHOW_DEBUG) {
                Log.d("TAG", "read len : 0 ");
            }
            etRead.post(new Runnable() {
                @Override
                public void run() {
                    etRead.setText("empty");
                }
            });
            return;
        }
        try {
            Thread.sleep(90);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "Leave readDataFromSerial");
    }
    private void openUsbSerial() {
        Log.d("TAG", "Enter  openUsbSerial");
        if(mSerial==null) {

            Log.d("TAG", "No mSerial");
            return;

        }
        if (mSerial.isConnected()) {
            if (SHOW_DEBUG) {
                Log.d("TAG", "openUsbSerial : isConnected ");
            }
            String str = baudRateSpinner.getSelectedItem().toString();
            int baudRate= Integer.parseInt(str);
            switch (baudRate) {
                case 9600:
                    mBaudrate = PL2303Driver.BaudRate.B9600;
                    break;
                case 19200:
                    mBaudrate =PL2303Driver.BaudRate.B19200;
                    break;
                case 115200:
                    mBaudrate =PL2303Driver.BaudRate.B115200;
                    break;
                default:
                    mBaudrate =PL2303Driver.BaudRate.B9600;
                    break;
            }
            Log.d("TAG", "baudRate:"+baudRate);
            // if (!mSerial.InitByBaudRate(mBaudrate)) {
            if (!mSerial.InitByBaudRate(mBaudrate,700)) {
                if(!mSerial.PL2303Device_IsHasPermission()) {
                    Toast.makeText(this, "cannot open, maybe no permission", Toast.LENGTH_SHORT).show();
                }
                if(mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
                    Toast.makeText(this, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
                }
            } else {
                Toast.makeText(this, "connected : OK" , Toast.LENGTH_SHORT).show();
                Log.d("TAG", "connected : OK");
                Log.d("TAG", "Exit  openUsbSerial");
            }
        }//isConnected
        else {
            Toast.makeText(this, "Connected failed, Please plug in PL2303 cable again!" , Toast.LENGTH_SHORT).show();
            Log.d("TAG", "connected failed, Please plug in PL2303 cable again!");
        }
    }

    List<String> chuankou = new ArrayList<>();
    //逐个字节读取数据
    private void readDataFromSerial1() {

        int len;
        byte[] rbuf = new byte[1];
        Log.d("TAG", "Enter readDataFromSerial1");

        if(null==mSerial)
            return;

        if(!mSerial.isConnected())
            return;

        len = mSerial.read(rbuf);
        if(len<0) {
            Log.d("TAG", "Fail to bulkTransfer(read data)");
            return;
        }

        if (len > 0) {
            if (SHOW_DEBUG) {
                Log.d("TAG", "read len : " + len);
            }

            if (rbuf[0] == 0xF5) {
                //如果读的字节是0xF5，而且串口列表中有数据，且最后一个为0xFE，则清除上一串数据
                if (chuankou.size() > 0 && "FE".equals(chuankou.get(chuankou.size() - 1))) {
                    chuankou.clear();
                }
            }
            //将串口数据加入list集合中
            chuankou.add(ZhuanHuanUtils.byte2HexStr(rbuf));

            if (rbuf[0] == 0xFE) {
                //如果读的字节是0xFE,而且数据列表的长度与数据长度一致，则数据包发送完成
                if (chuankou.size() >= 2) {
                    int singleCount = Integer.parseInt(ZhuanHuanUtils.hexStr2Str(chuankou.get(1)));
                    if (singleCount + 2 == chuankou.size()) {
                        StringBuffer sbHex = new StringBuffer();
                        for (int i = 0; i < chuankou.size(); i++) {
                            sbHex.append(chuankou.get(i));
                        }
                        if (chuankou.size() > 8){
                            String type = chuankou.get(8);
                            Log.i("TAG", "------type, " + type);
                            if ("E4".equals(type)){
                                Log.i("TAG", "------设置主机恢复出厂设置, " + type);
                            }else if ("EA".equals(type)){
                                Log.i("TAG", "------设置主机进入注册状态, " + type);
                            }else if ("EB".equals(type)){
                                Log.i("TAG", "------设置主机退出注册状态, " + type);
                            }else if ("E3".equals(type)){
                                Log.i("TAG", "------获取注册设备数量, " + type);
                            }else if ("E2".equals(type)){
                                Log.i("TAG", "------获取设备ID及状态, " + type);
                            }else if ("E1".equals(type)){
                                Log.i("TAG", "------清除注册表, " + type);
                            }
                        }
                    }
                }
            }
        }
//            Toast.makeText(this, "len="+len, Toast.LENGTH_SHORT).show();


        else {
            if (SHOW_DEBUG) {
                Log.d("TAG", "read len : 0 ");
            }
            etRead.post(new Runnable() {
                @Override
                public void run() {
                    etRead.setText("empty");
                }
            });
            return;
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("TAG", "Leave readDataFromSerial");
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

    //电影票选座空间（测试）
    private void initSeatTableView(){
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
}
