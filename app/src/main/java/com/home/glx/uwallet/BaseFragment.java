package com.home.glx.uwallet;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

public abstract class BaseFragment extends Fragment {

    public View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(getLayout(), container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public boolean isLogin() {
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
        if (userId.equals("")) {
//            return true;
            return false;
        }
        return true;
    }

    public abstract int getLayout();

    public abstract void initView();

}
