package com.home.glx.uwallet.mvp;

import java.util.Map;

/**
 * 校验支付密码
 */
public interface CheckPayPassword_in {

    interface View {
        void setPasswordStatus(String status);
    }

    interface Model {
        void checkPayPassword(Map<String, Object> maps);
    }

    interface Present {
        void checkPayPassword(Map<String, Object> maps);

        void resPasswordSrarus(String data);
    }

}
