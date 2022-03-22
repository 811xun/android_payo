package com.home.glx.uwallet.mvp;

import java.util.Map;

public interface ChangeCardMessage_in {

    interface View {
        void setChangeCardMessage(String dataStr);
    }

    interface Model {
        void reqChangeCardMessage(Map<String, Object> maps);
    }

    interface Present {
        void reqChangeCardMessage(Map<String, Object> maps);

        void resChangeCardMessage(String dataStr);
    }
}
