package com.home.glx.uwallet.mvp;

import java.util.Map;

/**
 * 是否存在支付密码
 */
public interface PresencePayPassword_in {

    interface View {
        void setPresenceStatus(String code, String status);
    }

    interface Model {
        void getPresenceStatus(String code, Map<String, Object> maps);
    }

    interface Present {
        void loadPresenceStatus(String code, Map<String, Object> maps);

        void resPresenceStatus(String code, String dataStr);
    }

}
