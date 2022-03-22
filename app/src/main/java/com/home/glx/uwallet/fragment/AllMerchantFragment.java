package com.home.glx.uwallet.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.SearchMerchantActivity;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 商户列表页
 */
public class AllMerchantFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fm;
    private int tabNum = 1;
    private ImageView search;
    private TextView topDeals;
    private TextView newVenues;
    private LinearLayout byDistance;
    private LinearLayout byDiscount;
    private DiscountFragment discountFragment;
    private DistanceFragment distanceFragment;
    private View view;
    private TextView myStreet;
    private ImageView mapImg;

    public static AllMerchantFragment newInstance() {
        Bundle args = new Bundle();
        AllMerchantFragment fragment = new AllMerchantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_merchant, container, false);
        initView();
        return view;
    }

    public void initView() {
        topDeals = (TextView) view.findViewById(R.id.top_deals);
        newVenues = (TextView) view.findViewById(R.id.new_venues);
        byDiscount = (LinearLayout) view.findViewById(R.id.top_ll);
        byDistance = (LinearLayout) view.findViewById(R.id.new_ll);
        search = (ImageView) view.findViewById(R.id.id_select_img);
        mapImg = (ImageView) view.findViewById(R.id.id_map_model);
        myStreet = (TextView) view.findViewById(R.id.street);
        byDiscount.setOnClickListener(this);
        byDistance.setOnClickListener(this);
        search.setOnClickListener(this);
        fm = getChildFragmentManager();
        findCityStInfo();
        ((MainTab) getActivity()).getLocation();
        setTabSelection(1);
        ((MainTab)getActivity()).setOnGetLoactionPermission(new MainTab.OnGetLoactionPermission() {
            @Override
            public void getPermission() {
                findCityStInfo();
                if (tabNum == 0) {
                    if (discountFragment != null) {
                        discountFragment.refresh();
                    }
                } else {
                    if (distanceFragment != null) {
                        distanceFragment.checkLocationPermission(1);
                    }
                }
            }
        });
    }

    private void setTabSelection(int index){
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (index) {
            case 0:
                tabNum = 0;
                topDeals.setBackground(getResources().getDrawable(R.drawable.orange_line));
                topDeals.setTextColor(getResources().getColor(R.color.text_black));
                newVenues.setBackground(null);
                newVenues.setTextColor(getResources().getColor(R.color.share_text_gray));
                if(discountFragment == null){
                    discountFragment = new DiscountFragment();
                    ft.add(R.id.fl, discountFragment);
                }else{
                    ft.show(discountFragment);
                }

                break;

            case 1:
                tabNum = 1;
                newVenues.setBackground(getResources().getDrawable(R.drawable.orange_line));
                newVenues.setTextColor(getResources().getColor(R.color.text_black));
                topDeals.setBackground(null);
                topDeals.setTextColor(getResources().getColor(R.color.share_text_gray));
                if(distanceFragment == null){
                    distanceFragment = new DistanceFragment();
                    ft.add(R.id.fl, distanceFragment);
                } else {
                    ft.show(distanceFragment);
                }
                break;
        }
        ft.commit();
    }

    //用于隐藏fragment
    private void hideFragment(FragmentTransaction ft){
        if(distanceFragment!=null){
            ft.hide(distanceFragment);
        }
        if(discountFragment!=null){
            ft.hide(discountFragment);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.top_ll:
                setTabSelection(0);
                break;
            case R.id.new_ll:
                setTabSelection(1);
                break;
            case R.id.id_select_img:
                Intent intent = new Intent(getContext(), SearchMerchantActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 邀请人列表
     */
    public void findCityStInfo() {
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.APP);
        Map<String, Object> maps = new HashMap<>();
        maps.put("lat", appMsgSharePreferenceUtils.get(StaticParament.LAT, ""));
        maps.put("lng", appMsgSharePreferenceUtils.get(StaticParament.LNG, ""));
        if (maps.get("lat").equals("") && maps.get("lng").equals("")) {
            return;
        }
        maps.put("no_user_id", 1);
        //maps.put("scs", "created_date(desc)");

        RequestUtils requestUtils1 = new RequestUtils(getContext(), maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("地理位置获取城市,街道信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.findCityStInfo(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("地理位置获取城市,街道信息：" + dataStr);
                        Gson gson = new Gson();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(dataStr);
                            String cityName = jsonObject.getString("cityName").trim();
                            String street = jsonObject.getString("street").trim();
                            SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.APP);
                            appMsgSharePreferenceUtils.put(StaticParament.CITY, cityName + "");
                            appMsgSharePreferenceUtils.put(StaticParament.STREET, street + "");
                            appMsgSharePreferenceUtils.save();
                            if (TextUtils.isEmpty(street)) {
                                if (!TextUtils.isEmpty(cityName)) {
                                    myStreet.setText(cityName);
                                    mapImg.setVisibility(View.VISIBLE);
                                } else {
                                    myStreet.setText("");
                                    mapImg.setVisibility(View.GONE);
                                }
                            } else {
                                mapImg.setVisibility(View.VISIBLE);
                                myStreet.setText(street);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void resError(String error) {
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            findCityStInfo();
        }
    }
}
