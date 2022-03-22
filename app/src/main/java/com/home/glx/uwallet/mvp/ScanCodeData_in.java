package com.home.glx.uwallet.mvp;

import java.util.Map;

/**
 * 扫码获取用户信息
 */
public interface ScanCodeData_in {

    interface View {
        void setScanData(String type, String data);
    }

    interface Model {
        void getImgCodeData(String code);
        void getNfcCodeData(String code);
    }

    interface Present {
        void loadImgCodeData(String code);

        void loadNfcCodeData(String code);

        void resImgCodeData(String dataStr);
    }

}
