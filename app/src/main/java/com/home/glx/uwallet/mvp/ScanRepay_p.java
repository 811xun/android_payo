package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.FenQiRepayDatas;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ScanRepay_p implements ScanRepay_in.Present {

    private Context context;
    private ScanRepay_in.View view;
    private ScanRepay_m model;

    public ScanRepay_p(Context context, ScanRepay_in.View view) {
        this.context = context;
        this.view = view;
        model = new ScanRepay_m(context, this);
    }

    @Override
    public void getFenQiLimitData1(Map<String, Object> maps) {
        model.getFenQiLimitData1(maps);
    }

    @Override
    public void getFenQiLimitData(Map<String, Object> maps) {
        model.getFenQiLimitData(maps);
    }


    @Override
    public void getFenQiData(Map<String, Object> maps) {
        model.getFenQiData(maps);
    }

    @Override
    public void resFenQiLimitData(String data) {
        if (data != null) {
            Gson gson = new Gson();
            FenQiRepayDatas fenQiRepayDatas = gson.fromJson(data, FenQiRepayDatas.class);
            view.setFenQiLimitData(fenQiRepayDatas);
        }
    }

    @Override
    public void resFenQiLimitData1(String data) {
        if (data != null) {
            Gson gson = new Gson();
            FenQiRepayDatas fenQiRepayDatas = gson.fromJson(data, FenQiRepayDatas.class);
            view.setFenQiLimitData1(fenQiRepayDatas);
        }
    }

    @Override
    public void resFenQiData(String data) {
        if (data != null) {
            Gson gson = new Gson();
            FenQiRepayDatas fenQiRepayDatas = gson.fromJson(data, FenQiRepayDatas.class);
            view.setFenQiData(fenQiRepayDatas);
        }
    }

    @Override
    public void reqRepqy(Map<String, Object> maps) {
        model.reqRepqy(maps);
    }

    /**
     * 钱包、卡支付返回
     *
     * @param dataStr
     */
    @Override
    public void resRepayData(String dataStr) {
        if (dataStr != null) {
            view.setRepayStatus("1", dataStr);
        }
    }

    /**
     * 提交分期支付
     *
     * @param maps
     */
    @Override
    public void reqFenQiRepqy(Map<String, Object> maps) {
        model.reqFenQiRepqy(maps);
    }

    /**
     * 提交支付
     *
     * @param maps
     */
    @Override
    public void reqQrPqy(Map<String, Object> maps) {
        model.reqQrPay(maps);
    }

    /**
     * 提交支付
     *
     * @param maps
     */
    @Override
    public void reqQrPqyNew(Map<String, Object> maps) {
        model.reqQrPayNew(maps);
    }

    @Override
    public void resFenQiRepayData(String dataStr) {
        if (dataStr != null) {
            view.setFenQiRepayStatus(dataStr);
        }
    }

    @Override
    public void resQrPayData(String dataStr) {
        if (dataStr != null) {
            view.setQrPayStatus(dataStr);
        } else {
            view.setQrPayStatus(null);
        }
    }

    /**
     * 获取为开通分期付的时候折扣
     *
     * @param maps
     */
    @Override
    public void reqUnOpenFQzhekou(Map<String, Object> maps) {
        model.reqUnOpenFQzhekou(maps);
    }

    /**
     * 未开通分期付的时候折扣返回
     */
    @Override
    public void resUnOpenFQzhekou(String dataStr) {
        if (dataStr == null) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(dataStr);
            String rate = jsonObject.getString("rate");
            view.unOpenFQzhekou(rate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
