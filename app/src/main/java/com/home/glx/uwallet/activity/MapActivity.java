package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.VisibleRegion;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.fragment.FindMapFragment;
import com.home.glx.uwallet.fragment.HuaWeiMapFragment;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.tools.TypefaceUtil;

import static com.home.glx.uwallet.tools.SystemUtils.getDeviceBrand;

public class MapActivity extends MainActivity {
    FragmentManager fm;
    private FindMapFragment googleMap;
    private EditText search;
    private HuaWeiMapFragment huaWeiMap;
    private TextView openList;
    private ImageView back;
    private MerchantListener merchantListener;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//必须要写
        String id = getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(id)) {
            if (googleMap != null) {
                googleMap.checkId(id);
            }
            if (huaWeiMap != null) {
                huaWeiMap.checkId(id);
            }
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_map;
    }

    @Override
    public void initView() {
        super.initView();
        merchantListener = new MerchantModel(this);
        back = (ImageView) findViewById(R.id.id_back);
        openList = (TextView) findViewById(R.id.open_list);
        search = (EditText) findViewById(R.id.search);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(search.getText().toString())) {
                        Intent intent = new Intent(MapActivity.this, MapListActivity.class);
                        intent.putExtra("searchText", search.getText().toString());
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });
        TextView title = findViewById(R.id.title);
        //切换字体
        TypefaceUtil.replaceFont(openList, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(search, "fonts/acumin_regularPro.ttf");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        String mobileCompany = getDeviceBrand();
        if (mobileCompany.equals("HONOR") || mobileCompany.equals("HUAWEI")) {
            if (huaWeiMap == null) {
                huaWeiMap = new HuaWeiMapFragment();
                ft.add(R.id.fl, huaWeiMap);
            }
        } else {
            if (googleMap == null) {
                googleMap = new FindMapFragment();
                ft.add(R.id.fl, googleMap);
            }
        }
        ft.commit();
        openList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    VisibleRegion visibleRegion = googleMap.getFourPoint();

                    Intent intent = new Intent(MapActivity.this, MapListActivity.class);
                    if (visibleRegion != null) {
                        if (visibleRegion.farLeft != null) {
                            intent.putExtra("leftUpLng", String.valueOf(visibleRegion.farLeft.longitude));
                            intent.putExtra("leftUpLat", String.valueOf(visibleRegion.farLeft.latitude));
                            intent.putExtra("rightUpLat", String.valueOf(visibleRegion.farLeft.latitude));
                        }
                        if (visibleRegion.farRight != null) {
                            intent.putExtra("rightUpLng", String.valueOf(visibleRegion.farRight.longitude));
                        }
                        if (visibleRegion.nearLeft != null) {
                            intent.putExtra("leftDownLng", String.valueOf(visibleRegion.nearLeft.longitude));
                            intent.putExtra("leftDownLat", String.valueOf(visibleRegion.nearLeft.latitude));
                        }
                        if (visibleRegion.nearRight != null) {
                            intent.putExtra("rightDownLng", String.valueOf(visibleRegion.nearRight.longitude));
                            intent.putExtra("rightDownLat", String.valueOf(visibleRegion.nearRight.latitude));
                        }
                    }
                    startActivity(intent);
                } else if (huaWeiMap != null) {
                    com.huawei.hms.maps.model.VisibleRegion visibleRegion = huaWeiMap.getFourPoint();
                    if (visibleRegion != null) {
                        Intent intent = new Intent(MapActivity.this, MapListActivity.class);
                        intent.putExtra("leftUpLng", String.valueOf(visibleRegion.farLeft.longitude));
                        intent.putExtra("leftUpLat", String.valueOf(visibleRegion.farLeft.latitude));
                        intent.putExtra("rightUpLng", String.valueOf(visibleRegion.farRight.longitude));
                        intent.putExtra("rightUpLat", String.valueOf(visibleRegion.farLeft.latitude));
                        intent.putExtra("leftDownLng", String.valueOf(visibleRegion.nearLeft.longitude));
                        intent.putExtra("leftDownLat", String.valueOf(visibleRegion.nearLeft.latitude));
                        intent.putExtra("rightDownLng", String.valueOf(visibleRegion.nearRight.longitude));
                        intent.putExtra("rightDownLat", String.valueOf(visibleRegion.nearRight.latitude));
                        startActivity(intent);
                    }
                }
            }
        });
    }
}