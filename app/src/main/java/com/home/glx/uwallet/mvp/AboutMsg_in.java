package com.home.glx.uwallet.mvp;

import com.home.glx.uwallet.datas.AboutDatas;

import java.util.Map;

/**
 * 契约类
 */
public interface AboutMsg_in {

    interface View {
        void setAboutMsg(AboutDatas aboutMsg);
    }

    interface Model {
        void getAboutMsg(Map<String, Object> map);
    }

    interface Present {
        void getAboutMsg(Map<String, Object> map);

        void resAboutMsg(String dataStr);
    }

}
