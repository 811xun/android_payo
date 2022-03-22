package com.home.glx.uwallet.mvp;

import java.util.Map;

/**
 * 是否开启分期付
 */
public interface OpenFenQiFu_in {

    interface View {
        void setFenQiFuStatus(String status, String illingUrl);
    }

    interface Model {
        void getFenQiFuStatus(Map<String, Object> maps);
    }

    interface Present {
        void loadFenQiFuStatus(Map<String, Object> maps);

        void resFenQiFuStatus(String dataStr);
    }
}
