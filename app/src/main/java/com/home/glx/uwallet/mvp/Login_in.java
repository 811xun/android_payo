package com.home.glx.uwallet.mvp;

import com.home.glx.uwallet.datas.LoginDatas;

import java.util.Map;

public interface Login_in {

    interface View {
        void setLoginStatus(String status);
    }

    interface Model {
        void reqLogin(Map<String, Object> maps, boolean regist);
    }

    interface Present {
        void reqLogin(Map<String, Object> maps, boolean regist);

        void resLoginData(LoginDatas loginDatas, boolean regist);
    }

}
