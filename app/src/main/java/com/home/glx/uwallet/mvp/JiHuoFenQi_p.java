package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.FenQiMsgDatas;

import java.util.Map;

public class JiHuoFenQi_p implements JiHuoFenQi_in.Present {

    private Context context;
    private JiHuoFenQi_in.View view;
    private JiHuoFenQi_in.InitFenQiView initFenQiView;
    private JiHuoFenQi_in.ActivationInstallmentView activationInstallmentView;
    private JiHuoFenQi_in.bankStatementsIsResultView bankStatementsIsResultView;
    private JiHuoFenQi_m model;

    public JiHuoFenQi_p(Context context, JiHuoFenQi_in.View view) {
        this.context = context;
        this.view = view;
        model = new JiHuoFenQi_m(context, this);
    }

    public JiHuoFenQi_p(Context context, JiHuoFenQi_in.InitFenQiView view) {
        this.context = context;
        this.initFenQiView = view;
        model = new JiHuoFenQi_m(context, this);
    }

    public JiHuoFenQi_p(Context context, JiHuoFenQi_in.ActivationInstallmentView view) {
        this.context = context;
        this.activationInstallmentView = view;
        model = new JiHuoFenQi_m(context, this);
    }

    public JiHuoFenQi_p(Context context, JiHuoFenQi_in.bankStatementsIsResultView bankStatementsIsResultView) {
        this.context = context;
        this.bankStatementsIsResultView = bankStatementsIsResultView;
        model = new JiHuoFenQi_m(context, this);
    }

    @Override
    public void reqJiHuo(Map<String, Object> maps) {
        model.reqJiHuo(maps);
    }

    @Override
    public void resJiHuoStatus(String dataStr) {
        if (dataStr != null) {
            view.setFenQiStatus("1");
        } else {
            view.setFenQiStatus("0");
        }
    }

    @Override
    public void loadFenQiMsg(Map<String, Object> maps) {
        model.getFenQiMsg(maps);
    }

    @Override
    public void loadFenQiLimitMsg(Map<String, Object> maps) {
        model.getFenQiLimit(maps);
    }

    @Override
    public void loadNoLoginFenQiLimitMsg(Map<String, Object> maps) {
        model.getNoLoginFenQiLimit(maps);
    }

    @Override
    public void resFenQiMsg(String dataStr) {
        if (dataStr != null) {
            Gson gson = new Gson();
            FenQiMsgDatas fenQiMsgDatas = gson.fromJson(dataStr, FenQiMsgDatas.class);
            view.setFenQiMsg(fenQiMsgDatas);
        }
    }

    @Override
    public void resFenQiLimit(String limit) {
        view.setFenQiLimit(limit);
    }

    @Override
    public void resNoLoginFenQiLimit(String limit) {
        view.setNoLoginFenQiLimit(limit);
    }

    @Override
    public void initFenQi(Map<String, Object> maps) {
        model.initFenQi(maps);
    }

    @Override
    public void resFenQi(String dataStr) {
        initFenQiView.setStatus("1");
    }

    @Override
    public void reqActivationInstallment(Map<String, Object> maps) {
        model.activationInstallment(maps);
    }

    /**
     * 触发风控返回
     *
     * @param status
     */
    @Override
    public void resActivationInstallment(String status, String rejectMessage) {
        if (status != null) {
            activationInstallmentView.setFKStatus(status, rejectMessage);
        }
    }

    /**
     * 查看illion报告是否返回
     */
    @Override
    public void reqBankStatementsIsResult(Map<String, Object> maps) {
        model.bankStatementsIsResult(maps);
    }

    @Override
    public void resBankStatementsIsResult(String status) {
        if (status != null) {
            bankStatementsIsResultView.setStatus(status);
        }
    }
}
