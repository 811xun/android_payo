package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.FenQiMsgDatas;
import com.home.glx.uwallet.datas.TransationDetailData;
import com.home.glx.uwallet.mvp.CheckPayPassword_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_p;
import com.home.glx.uwallet.mvp.JiHuoFenQi_in;
import com.home.glx.uwallet.mvp.JiHuoFenQi_p;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_p;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.ConfirmSelectRepayDialog;
import com.home.glx.uwallet.selfview.RepayPawDialog;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class InstalmentPendingDetailsActivity extends MainActivity implements CheckPayPassword_in.View,
        View.OnClickListener, JiHuoFenQi_in.View, WhetherOpenInvest_in.View {
    private String id;
    private CheckPayPassword_p checkPayPassword_p;
    private String merchantName;
    private TextView merchantNameView;
    private TextView termNum;
    private List<TransationDetailData.RepayListBean> data = new ArrayList();
    private TextView totalOrder;
    private TextView totalOrderAmount;
    private TextView termsOf;
    private TextView status;
    private TextView statusView;
    private TextView paymentDue;
    private TextView term1Date;
    private TextView money1;
    private TextView term1State;
    private TextView term2Date;
    private TextView money2;
    private TextView term2State;
    private TextView term3Date;
    private TextView money3;
    private TextView term3State;
    private TextView term4Date;
    private TextView money4;
    private TextView term4State;
    private ConstraintLayout term1;
    private ConstraintLayout term2;
    private ConstraintLayout term3;
    private ConstraintLayout term4;
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private ImageView icon4;
    private TextView pay;
    private ImageView back;
    private String chooseAmount = "0";
    private boolean fromOrder = false;
    private List<String> greenList = new ArrayList<>();
    private String feeAmount = "0";
    private ConfirmSelectRepayDialog confirmRepayDialog;
    private JiHuoFenQi_p presentJiHuo;
    private SharePreferenceUtils sharePreferenceUtils;
    private boolean createFlag = true;
    private TextView orderNo;
    private TextView orderDates;
    private String repayId;
    //是否开通个业务
    private WhetherOpenInvest_p openInvestPresent;
    private String selectTerm = "";
    private int repayAll;

    @Override
    public int getLayout() {
        return R.layout.activity_instalment_pending_details;
    }

    @Override
    public void initView() {
        super.initView();
        openInvestPresent = new WhetherOpenInvest_p(this, this);
        sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        presentJiHuo = new JiHuoFenQi_p(this, this);
        checkPayPassword_p = new CheckPayPassword_p(this, this);
        repayAll = getIntent().getIntExtra("repayAll", 0);
        id = getIntent().getStringExtra("id");
        repayId = getIntent().getStringExtra("repayId");
        merchantName = getIntent().getStringExtra("merchantName");
        int fromO = getIntent().getIntExtra("fromOrder", 0);
        if (fromO == 0) {
            greenList = (List<String>) getIntent().getSerializableExtra("greenList");
        } else {
            fromOrder = true;
        }
        merchantNameView = findViewById(R.id.merchant_name);
        termNum = findViewById(R.id.term_num);
        if (!TextUtils.isEmpty(merchantName)) {
            merchantNameView.setText(merchantName);
        }
        totalOrder = findViewById(R.id.total_order);
        totalOrderAmount = findViewById(R.id.total_order_amount);
        termsOf = findViewById(R.id.terms_of);
        status = findViewById(R.id.status);
        statusView = findViewById(R.id.status_view);
        paymentDue = findViewById(R.id.payment_due);
        term1Date = findViewById(R.id.term1_date);
        money1 = findViewById(R.id.money1);
        term1State = findViewById(R.id.term1_state);
        term2Date = findViewById(R.id.term2_date);
        money2 = findViewById(R.id.money2);
        term2State = findViewById(R.id.term2_state);
        term3Date = findViewById(R.id.term3_date);
        money3 = findViewById(R.id.money3);
        term3State = findViewById(R.id.term3_state);
        term4Date = findViewById(R.id.term4_date);
        money4 = findViewById(R.id.money4);
        term4State = findViewById(R.id.term4_state);
        term1 = findViewById(R.id.term1);
        term2 = findViewById(R.id.term2);
        term3 = findViewById(R.id.term3);
        term4 = findViewById(R.id.term4);
        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        icon4 = findViewById(R.id.icon4);
        term1.setOnClickListener(this);
        term2.setOnClickListener(this);
        term3.setOnClickListener(this);
        term4.setOnClickListener(this);

        pay = findViewById(R.id.pay);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = findViewById(R.id.title);
        orderNo = findViewById(R.id.order_no);
        TextView orderDate = findViewById(R.id.order_date);
        orderDates = findViewById(R.id.order_dates);
        TextView statusText = findViewById(R.id.status_text);
        TextView statusTv = findViewById(R.id.status_tv);
        //切换字体
        TypefaceUtil.replaceFont(statusText, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(statusTv, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(orderDates, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(orderDate, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(orderNo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term4State, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(money4, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term4Date, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term3State, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(money3, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term3Date, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term2State, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(money2, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term2Date, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term1State, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(money1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term1Date, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(pay, "fonts/gilroy_semiBold.ttf");

        TypefaceUtil.replaceFont(term1State, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(money1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term1Date, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(merchantNameView, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalOrder, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(totalOrderAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(termsOf, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(termNum, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(status, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(statusView, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(paymentDue, "fonts/gilroy_bold.ttf");

        getRecordDetail(id);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicTools.isFastClick()) {
                    return;
                }
                if (Float.parseFloat(chooseAmount) == 0) {
                    return;
                }
                openInvestPresent.loadOpenInvestStatus("sm");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (createFlag) {
            createFlag = false;
            return;
        }
        presentJiHuo.loadFenQiMsg(new HashMap<String, Object>());
    }

    /**
     * 弹出输入支付（PIN）密码弹窗
     */
    private void showPayPwdDialog() {
        RepayPawDialog repayPawDialog = new RepayPawDialog(this);
        repayPawDialog.setOnRepayPwd(new RepayPawDialog.OnRepayPwd() {
            @Override
            public void inputPwd(String password) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("payPassword", password);
                checkPayPassword_p.checkPayPassword(maps);
            }
        });
        repayPawDialog.show();
    }

    /**
     * 查询卡支付/分期付交易明细
     */
    public void getRecordDetail(final String transId) {

        Map<String, Object> maps = new HashMap<>();
        maps.put("no_user_id", 1);

        RequestUtils requestUtils1 = new RequestUtils(this, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(InstalmentPendingDetailsActivity.this, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("查询卡支付/分期付交易明细参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getRecordDetail(headerMap, transId, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("查询卡支付/分期付交易明细:" + dataStr);
                        Gson gson = new Gson();
                        TransationDetailData transationDetailData = gson.fromJson(dataStr, TransationDetailData.class);
                        orderDates.setText(transationDetailData.getDisplayDate());
                        merchantNameView.setText(transationDetailData.getTradingName());
                        totalOrderAmount.setText("$" + transationDetailData.getPayAmount());
                        orderNo.setText(transationDetailData.getTransNo());
                        data.addAll(transationDetailData.getRepayList());
                        termNum.setText(data.size() + " terms");
                        statusView.setText(transationDetailData.getTransStateStr());
                        if (data.size() > 0) {
                            if (data.size() == 1) {
                                term1.setVisibility(View.VISIBLE);
                                money1.setText("$" + data.get(0).getPayPrincipal());
                                term1Date.setText(data.get(0).getExpectRepayTime());
                                if (data.get(0).getState() == 2 || data.get(0).getState() == 1) {
                                    icon1.setImageResource(R.mipmap.choice_orange_icon);
                                    term1.setTag("NO");
                                    term1State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    if (data.get(0).getState() == 1) {
                                        term1State.setText("Completed");
                                    } else {
                                        term1State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term1.setTag("1");
                                        icon1.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(0).getId())) {
                                            term1.setTag("1");
                                            icon1.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(0).getViolateAmount());
                                        } else {
                                            icon1.setImageResource(R.mipmap.choice_gray_icon);
                                            term1.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(0).getTruelyOverdueDays()) && Float.parseFloat(data.get(0).getTruelyOverdueDays()) > 0) {
                                            term1State.setText("Overdue payment");
                                            term1State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }
                            } else if (data.size() == 2) {
                                term1.setVisibility(View.VISIBLE);
                                term2.setVisibility(View.VISIBLE);
                                money1.setText("$" + data.get(0).getPayPrincipal());
                                term1Date.setText(data.get(0).getExpectRepayTime());
                                if (data.get(0).getState() == 2 || data.get(0).getState() == 1) {
                                    term1State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    icon1.setImageResource(R.mipmap.choice_orange_icon);
                                    term1.setTag("NO");
                                    if (data.get(0).getState() == 1) {
                                        term1State.setText("Completed");
                                    } else {
                                        term1State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term1.setTag("1");
                                        icon1.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(0).getId())) {
                                            term1.setTag("1");
                                            icon1.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(0).getViolateAmount());
                                        } else {
                                            icon1.setImageResource(R.mipmap.choice_gray_icon);
                                            term1.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(0).getTruelyOverdueDays()) && Float.parseFloat(data.get(0).getTruelyOverdueDays()) > 0) {
                                            term1State.setText("Overdue payment");
                                            term1State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }
                                money2.setText("$" + data.get(1).getPayPrincipal());
                                term2Date.setText(data.get(1).getExpectRepayTime());
                                if (data.get(1).getState() == 2 || data.get(1).getState() == 1) {
                                    term2State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    icon2.setImageResource(R.mipmap.choice_orange_icon);
                                    term2.setTag("NO");
                                    if (data.get(1).getState() == 1) {
                                        term2State.setText("Completed");
                                    } else {
                                        term2State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term2.setTag("1");
                                        icon2.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(1).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(1).getId())) {
                                            term2.setTag("1");
                                            icon2.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(1).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(1).getViolateAmount());
                                        } else {
                                            icon2.setImageResource(R.mipmap.choice_gray_icon);
                                            term2.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(1).getTruelyOverdueDays()) && Float.parseFloat(data.get(1).getTruelyOverdueDays()) > 0) {
                                            term2State.setText("Overdue payment");
                                            term2State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }
                            } else if (data.size() == 3) {
                                term1.setVisibility(View.VISIBLE);
                                term2.setVisibility(View.VISIBLE);
                                term3.setVisibility(View.VISIBLE);

                                money1.setText("$" + data.get(0).getPayPrincipal());
                                term1Date.setText(data.get(0).getExpectRepayTime());
                                if (data.get(0).getState() == 2 || data.get(0).getState() == 1) {
                                    term1State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    icon1.setImageResource(R.mipmap.choice_orange_icon);
                                    term1.setTag("NO");
                                    if (data.get(0).getState() == 1) {
                                        term1State.setText("Completed");
                                    } else {
                                        term1State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term1.setTag("1");
                                        icon1.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(0).getId())) {
                                            term1.setTag("1");
                                            icon1.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(0).getViolateAmount());
                                        } else {
                                            icon1.setImageResource(R.mipmap.choice_gray_icon);
                                            term1.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(0).getTruelyOverdueDays()) && Float.parseFloat(data.get(0).getTruelyOverdueDays()) > 0) {
                                            term1State.setText("Overdue payment");
                                            term1State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }
                                money2.setText("$" + data.get(1).getPayPrincipal());
                                term2Date.setText(data.get(1).getExpectRepayTime());
                                if (data.get(1).getState() == 2 || data.get(1).getState() == 1) {
                                    term2State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    icon2.setImageResource(R.mipmap.choice_orange_icon);
                                    term2.setTag("NO");
                                    if (data.get(1).getState() == 1) {
                                        term2State.setText("Completed");
                                    } else {
                                        term2State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term2.setTag("1");
                                        icon2.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(1).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(1).getId())) {
                                            term2.setTag("1");
                                            icon2.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(1).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(1).getViolateAmount());

                                        } else {
                                            icon2.setImageResource(R.mipmap.choice_gray_icon);
                                            term2.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(1).getTruelyOverdueDays()) && Float.parseFloat(data.get(1).getTruelyOverdueDays()) > 0) {
                                            term2State.setText("Overdue payment");
                                            term2State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }

                                money3.setText("$" + data.get(2).getPayPrincipal());
                                term3Date.setText(data.get(2).getExpectRepayTime());
                                if (data.get(2).getState() == 2 || data.get(2).getState() == 1) {
                                    term3State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    icon3.setImageResource(R.mipmap.choice_orange_icon);
                                    term3.setTag("NO");
                                    if (data.get(2).getState() == 1) {
                                        term3State.setText("Completed");
                                    } else {
                                        term3State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term3.setTag("1");
                                        icon3.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(2).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(2).getId())) {
                                            term3.setTag("1");
                                            icon3.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(2).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(2).getViolateAmount());
                                        } else {
                                            icon3.setImageResource(R.mipmap.choice_gray_icon);
                                            term3.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(2).getTruelyOverdueDays()) && Float.parseFloat(data.get(2).getTruelyOverdueDays()) > 0) {
                                            term3State.setText("Overdue payment");
                                            term3State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }
                            } else {
                                term1.setVisibility(View.VISIBLE);
                                term2.setVisibility(View.VISIBLE);
                                term3.setVisibility(View.VISIBLE);
                                term4.setVisibility(View.VISIBLE);

                                money1.setText("$" + data.get(0).getPayPrincipal());
                                term1Date.setText(data.get(0).getExpectRepayTime());
                                if (data.get(0).getState() == 2 || data.get(0).getState() == 1) {
                                    term1State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    icon1.setImageResource(R.mipmap.choice_orange_icon);
                                    term1.setTag("NO");
                                    if (data.get(0).getState() == 1) {
                                        term1State.setText("Completed");
                                    } else {
                                        term1State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term1.setTag("1");
                                        icon1.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(0).getId())) {
                                            term1.setTag("1");
                                            icon1.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(0).getViolateAmount());
                                        } else {
                                            icon1.setImageResource(R.mipmap.choice_gray_icon);
                                            term1.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(0).getTruelyOverdueDays()) && Float.parseFloat(data.get(0).getTruelyOverdueDays()) > 0) {
                                            term1State.setText("Overdue payment");
                                            term1State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }
                                money2.setText("$" + data.get(1).getPayPrincipal());
                                term2Date.setText(data.get(1).getExpectRepayTime());
                                if (data.get(1).getState() == 2 || data.get(1).getState() == 1) {
                                    term2State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    icon2.setImageResource(R.mipmap.choice_orange_icon);
                                    term2.setTag("NO");
                                    if (data.get(1).getState() == 1) {
                                        term2State.setText("Completed");
                                    } else {
                                        term2State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term2.setTag("1");
                                        icon2.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(1).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(1).getId())) {
                                            term2.setTag("1");
                                            icon2.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(1).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(1).getViolateAmount());
                                        } else {
                                            icon2.setImageResource(R.mipmap.choice_gray_icon);
                                            term2.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(1).getTruelyOverdueDays()) && Float.parseFloat(data.get(1).getTruelyOverdueDays()) > 0) {
                                            term2State.setText("Overdue payment");
                                            term2State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }

                                money3.setText("$" + data.get(2).getPayPrincipal());
                                term3Date.setText(data.get(2).getExpectRepayTime());
                                if (data.get(2).getState() == 2 || data.get(2).getState() == 1) {
                                    term3State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    icon3.setImageResource(R.mipmap.choice_orange_icon);
                                    term3.setTag("NO");
                                    if (data.get(2).getState() == 1) {
                                        term3State.setText("Completed");
                                    } else {
                                        term3State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term3.setTag("1");
                                        icon3.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(2).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(2).getId())) {
                                            term3.setTag("1");
                                            icon3.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(2).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(2).getViolateAmount());
                                        } else {
                                            icon3.setImageResource(R.mipmap.choice_gray_icon);
                                            term3.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(2).getTruelyOverdueDays()) && Float.parseFloat(data.get(2).getTruelyOverdueDays()) > 0) {
                                            term3State.setText("Overdue payment");
                                            term3State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }
                                money4.setText("$" + data.get(3).getPayPrincipal());
                                term4Date.setText(data.get(3).getExpectRepayTime());
                                if (data.get(3).getState() == 2 || data.get(3).getState() == 1) {
                                    term4State.setTextColor(getResources().getColor(R.color.colorBackGround));
                                    icon4.setImageResource(R.mipmap.choice_orange_icon);
                                    term4.setTag("NO");
                                    if (data.get(3).getState() == 1) {
                                        term4State.setText("Completed");
                                    } else {
                                        term4State.setText("Processing");
                                    }
                                } else {
                                    if (repayAll == 1) {
                                        term4.setTag("1");
                                        icon4.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(3).getPayPrincipal());
                                    } else {
                                        if (fromOrder || repayId.equals(data.get(3).getId())) {
                                            term4.setTag("1");
                                            icon4.setImageResource(R.mipmap.choice_green_icon);
                                            chooseAmount = CalcTool.add(chooseAmount, data.get(3).getPayPrincipal());
                                            feeAmount = CalcTool.add(feeAmount, data.get(3).getViolateAmount());
                                        } else {
                                            icon4.setImageResource(R.mipmap.choice_gray_icon);
                                            term4.setTag("0");
                                        }
                                        if (!TextUtils.isEmpty(data.get(3).getTruelyOverdueDays()) && Float.parseFloat(data.get(3).getTruelyOverdueDays()) > 0) {
                                            term4State.setText("Overdue payment");
                                            term4State.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }
                            }

                            if (greenList != null && greenList.size() > 0) {
                                for (int i = 0; i < greenList.size(); i++) {
                                    if (greenList.get(i).equals("1")) {
                                        term1.setTag("1");
                                        icon1.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                                        feeAmount = CalcTool.add(feeAmount, data.get(0).getViolateAmount());
                                        continue;
                                    }
                                    if (greenList.get(i).equals("2")) {
                                        term2.setTag("1");
                                        icon2.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(1).getPayPrincipal());
                                        feeAmount = CalcTool.add(feeAmount, data.get(1).getViolateAmount());
                                        continue;
                                    }
                                    if (greenList.get(i).equals("3")) {
                                        term3.setTag("1");
                                        icon3.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(2).getPayPrincipal());
                                        feeAmount = CalcTool.add(feeAmount, data.get(2).getViolateAmount());
                                        continue;
                                    }
                                    if (greenList.get(i).equals("4")) {
                                        term4.setTag("1");
                                        icon4.setImageResource(R.mipmap.choice_green_icon);
                                        chooseAmount = CalcTool.add(chooseAmount, data.get(3).getPayPrincipal());
                                        feeAmount = CalcTool.add(feeAmount, data.get(3).getViolateAmount());
                                        continue;
                                    }
                                }
                            }
                            if (Float.parseFloat(chooseAmount) == 0) {
                                pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                            } else {
                                //pay.setText("Pay all $" + PublicTools.twoend(CalcTool.add(CalcTool.add(chooseAmount, "0.22"), feeAmount)));
                                pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                            }
                        }


                    }


                    @Override
                    public void resError(String error) {

                    }
                });
    }

    @Override
    public void setPasswordStatus(String status) {
        if (status == null) {
            return;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.term1:
                if (term1.getTag().equals("0")) {
                    term1.setTag("1");
                    chooseAmount = CalcTool.add(chooseAmount, data.get(0).getPayPrincipal());
                    feeAmount = CalcTool.add(feeAmount, data.get(0).getViolateAmount());
                    icon1.setImageResource(R.mipmap.choice_green_icon);
                    //pay.setText("Pay all $" + PublicTools.twoend(CalcTool.add(CalcTool.add(chooseAmount, "0.22"), feeAmount)));
                    pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                } else if (term1.getTag().equals("1")) {
                    term1.setTag("0");
                    chooseAmount = CalcTool.sub(chooseAmount, data.get(0).getPayPrincipal());
                    feeAmount = CalcTool.sub(feeAmount, data.get(0).getViolateAmount());
                    icon1.setImageResource(R.mipmap.choice_gray_icon);
                    if (Float.parseFloat(chooseAmount) == 0) {
                        pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                    } else {
                        //pay.setText("Pay all $" + PublicTools.twoend(CalcTool.add(CalcTool.add(chooseAmount, "0.22"), feeAmount)));
                        pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                    }
                }
                break;
            case R.id.term2:
                if (term2.getTag().equals("0")) {
                    term2.setTag("1");
                    icon2.setImageResource(R.mipmap.choice_green_icon);
                    chooseAmount = CalcTool.add(chooseAmount, data.get(1).getPayPrincipal());
                    feeAmount = CalcTool.add(feeAmount, data.get(1).getViolateAmount());
                    //pay.setText("Pay all $" + PublicTools.twoend(CalcTool.add(CalcTool.add(chooseAmount, "0.22"), feeAmount)));
                    pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                } else if (term2.getTag().equals("1")) {
                    term2.setTag("0");
                    icon2.setImageResource(R.mipmap.choice_gray_icon);
                    chooseAmount = CalcTool.sub(chooseAmount, data.get(1).getPayPrincipal());
                    feeAmount = CalcTool.sub(feeAmount, data.get(1).getViolateAmount());
                    if (Float.parseFloat(chooseAmount) == 0) {
                        pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                    } else {
                        //pay.setText("Pay all $" + PublicTools.twoend(CalcTool.add(CalcTool.add(chooseAmount, "0.22"), feeAmount)));
                        pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                    }
                }
                break;
            case R.id.term3:
                if (term3.getTag().equals("0")) {
                    term3.setTag("1");
                    icon3.setImageResource(R.mipmap.choice_green_icon);
                    chooseAmount = CalcTool.add(chooseAmount, data.get(2).getPayPrincipal());
                    feeAmount = CalcTool.add(feeAmount, data.get(2).getViolateAmount());
                    //pay.setText("Pay all $" + PublicTools.twoend(CalcTool.add(CalcTool.add(chooseAmount, "0.22"), feeAmount)));
                    pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                } else if (term3.getTag().equals("1")) {
                    term3.setTag("0");
                    icon3.setImageResource(R.mipmap.choice_gray_icon);
                    chooseAmount = CalcTool.sub(chooseAmount, data.get(2).getPayPrincipal());
                    feeAmount = CalcTool.sub(feeAmount, data.get(2).getViolateAmount());
                    if (Float.parseFloat(chooseAmount) == 0) {
                        pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                    } else {
                        //pay.setText("Pay all $" + PublicTools.twoend(CalcTool.add(CalcTool.add(chooseAmount, "0.22"), feeAmount)));
                        pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                    }
                }
                break;
            case R.id.term4:
                if (term4.getTag().equals("0")) {
                    term4.setTag("1");
                    icon4.setImageResource(R.mipmap.choice_green_icon);
                    chooseAmount = CalcTool.add(chooseAmount, data.get(3).getPayPrincipal());
                    feeAmount = CalcTool.add(feeAmount, data.get(3).getViolateAmount());
                    pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                    //pay.setText("Pay all $" + PublicTools.twoend(CalcTool.add(CalcTool.add(chooseAmount, "0.22"), feeAmount)));
                } else if (term4.getTag().equals("1")) {
                    term4.setTag("0");
                    icon4.setImageResource(R.mipmap.choice_gray_icon);
                    chooseAmount = CalcTool.sub(chooseAmount, data.get(3).getPayPrincipal());
                    feeAmount = CalcTool.sub(feeAmount, data.get(3).getViolateAmount());
                    if (Float.parseFloat(chooseAmount) == 0) {
                        pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                    } else {
                        //pay.setText("Pay all $" + PublicTools.twoend(CalcTool.add(CalcTool.add(chooseAmount, "0.22"), feeAmount)));
                        pay.setText("Next · $" + PublicTools.twoend(chooseAmount));
                    }
                }
                break;
        }
    }

    @Override
    public void setFenQiStatus(String status) {

    }

    @Override
    public void setFenQiMsg(FenQiMsgDatas fenQiMsg) {
        if (fenQiMsg.getBankcardNumber() != null) {
            String card = "****" + fenQiMsg.getBankcardNumber().
                    substring(fenQiMsg.getBankcardNumber().length() - 4, fenQiMsg.getBankcardNumber().length());
            sharePreferenceUtils.put(StaticParament.USER_FQ_ACCOUNT, card);
            sharePreferenceUtils.save();
            if (confirmRepayDialog != null) {
                confirmRepayDialog.changeNum(card);
            }
        }
    }

    @Override
    public void setFenQiLimit(String limit) {

    }

    @Override
    public void setNoLoginFenQiLimit(String limit) {

    }

    @Override
    public void openInvestStatus(String code, String fenqiCard, String zhifu, String fenqi) {
        int termsNum = 0;
        selectTerm = "";
        List<String> repayIds = new ArrayList<>();
        if (term1.getTag().equals("1")) {
            repayIds.add(data.get(0).getId());
            termsNum++;
            selectTerm = selectTerm + " 1st";
        }
        if (term2.getTag().equals("1")) {
            repayIds.add(data.get(1).getId());
            termsNum++;
            selectTerm = selectTerm + " 2nd";
        }
        if (term3.getTag().equals("1")) {
            repayIds.add(data.get(2).getId());
            termsNum++;
            selectTerm = selectTerm + " 3rd";
        }
        if (term4.getTag().equals("1")) {
            repayIds.add(data.get(3).getId());
            termsNum++;
            selectTerm = selectTerm + " 4th";
        }
        int completedNum = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getState() == 2 || data.get(i).getState() == 1) {
                completedNum++;
            }
        }
        if (completedNum + termsNum == data.size()) {
            selectTerm = "Pay total outstanding";
        } else {
            selectTerm = String.format("Paying%s of %s", selectTerm, data.size());
        }
        if (fenqi.equals("1") && fenqiCard.equals("1")) {
            Intent intent = new Intent(this, InstalmentPendingNextActivity.class);
            intent.putExtra("chooseAmount", chooseAmount);
            intent.putExtra("name", merchantNameView.getText().toString());
            intent.putExtra("orderNo", orderNo.getText().toString());
            intent.putExtra("selectTerm", selectTerm);
            intent.putExtra("repayIds", (Serializable) repayIds);
            startActivity(intent);
        } else {
            confirmRepayDialog = new ConfirmSelectRepayDialog(InstalmentPendingDetailsActivity.this, chooseAmount, feeAmount, termsNum, repayIds);
            confirmRepayDialog.setOnitemClick(new ConfirmSelectRepayDialog.OnItemClick() {
                @Override
                public void onClick() {
                }
            });
            confirmRepayDialog.show();
        }
    }
}