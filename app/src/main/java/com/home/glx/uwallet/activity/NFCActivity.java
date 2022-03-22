package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.nfc.FormatException;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.NfcUtils;
import com.yzq.zxinglibrary.android.CaptureActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public abstract class NFCActivity extends MainActivity {

    @Override
    public void initView() {
        super.initView();
        /*if (NfcUtils.nfcPhone(this)) {
            //nfc初始化设置
            NfcUtils nfcUtils = new NfcUtils(this);
        }*/
    }

    public void checkNfc() {
        if (NfcUtils.nfcPhone(this)) {
            //nfc初始化设置
            NfcUtils nfcUtils = new NfcUtils(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //开启前台调度系统
        if (NfcUtils.nfcPhone(this)) {
            try {
                CaptureActivity.mNfcAdapter.enableForegroundDispatch(this, CaptureActivity.mPendingIntent,
                        CaptureActivity.mIntentFilter, CaptureActivity.mTechList);
            } catch (Exception e) {

            }
        }
    }


    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);

        //当该Activity接收到NFC标签时，运行该方法
        //调用工具方法，读取NFC数据
        String nfc_id = "";
        String nfc_text = "";

        try {
            nfc_id = intent.getStringExtra("nfc_id");
            nfc_text = intent.getStringExtra("nfc_text");
        } catch (Exception e) {

        }

        try {
            //内容
            nfc_text = NfcUtils.readNFCFromTag(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //id
            nfc_id = NfcUtils.readNFCId(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (!nfc_id.equals("ID读取失败")) {
            //index.html?nfcId=043CA07A666781&qrCode=400058698823192577
            String[] list = nfc_text.split("&");
            String canshu = list[/*list.length - 1*/0];
            String[] c_list = canshu.split("=");
            nfc_id = c_list[c_list.length - 1];

            //获取商户id
//        nfc_id = list[list.length - 2];
            L.log("返回接收的nfc_text:" + nfc_text);
            L.log("返回接收的nfc_id:" + nfc_id);
            L.log("返回接收的canshu:" + canshu);

            nfcData(nfc_id, nfc_text);
        }
    }

    /**
     * 写入NFC
     *
     * @param intent
     */
    private void writeToNfc(Intent intent) {
        String text = "";
        try {
            NfcUtils.writeNFCToTag(text, intent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NfcUtils.nfcPhone(this)) {
            //关闭前台调度系统
            if (CaptureActivity.mNfcAdapter != null) {
                CaptureActivity.mNfcAdapter.disableForegroundDispatch(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public abstract void nfcData(String nfcId, String nfcText);

}
