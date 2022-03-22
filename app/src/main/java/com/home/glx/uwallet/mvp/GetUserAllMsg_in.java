package com.home.glx.uwallet.mvp;

import com.home.glx.uwallet.datas.UserAllMsgDatas;

import java.util.Map;

/**
 * 获取用户所有信息
 */
public interface GetUserAllMsg_in {

    interface View {
        void setUserMsg(UserAllMsgDatas userMsg);
    }

    interface Model {
        void getUserAllMsg(Map<String, Object> maps);
    }

    interface Present {
        void loadUserAllMsg(Map<String, Object> maps);

        void resUserAllMsg(String dataStr);
    }

}
