package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.ScanImgDatas;
import com.home.glx.uwallet.datas.ScanUserImgDatas;
import com.home.glx.uwallet.tools.L;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanCodeData_p implements ScanCodeData_in.Present {

    private Context context;
    private ScanCodeData_in.View view;
    private ScanCodeData_m model;
    private int scanNum = 0;

    public ScanCodeData_p(Context context, ScanCodeData_in.View view) {
        this.context = context;
        this.view = view;
        model = new ScanCodeData_m(context, this);
    }


    @Override
    public void loadImgCodeData(String code) {
        this.scanNum = 0;
        model.getImgCodeData(code);
    }

    @Override
    public void loadNfcCodeData(String code) {
        this.scanNum = 0;
        model.getNfcCodeData(code);
    }

    @Override
    public void resImgCodeData(String dataStr) {
        scanNum ++;
        if (dataStr != null) {
            String userType = "";
            String routeDTOS = "";
            try {
                JSONObject jsonObject = new JSONObject(dataStr);
                userType = jsonObject.getString("userType");
                routeDTOS = jsonObject.getString("routeDTOS");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
//            ScanUserImgDatas scanUserImgDatas = gson.fromJson(dataStr, ScanUserImgDatas.class);
            if (scanNum == 1) {
                if (userType.equals("10")) {
                    //用户
                    ScanUserImgDatas scanUserImgDatas = gson.fromJson(dataStr, ScanUserImgDatas.class);
                    view.setScanData("10", gson.toJson(scanUserImgDatas));
                } else {
                    ScanImgDatas scanImgDatas = gson.fromJson(dataStr, ScanImgDatas.class);
                    view.setScanData("20", gson.toJson(scanImgDatas));
                }
            }
        }
    }
}
