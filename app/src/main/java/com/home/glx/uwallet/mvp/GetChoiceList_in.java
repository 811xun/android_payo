package com.home.glx.uwallet.mvp;

import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;

import java.util.List;
import java.util.Map;

/**
 * 获取下拉选项
 */
public interface GetChoiceList_in {

    interface View {
        void setChoiceList(GetChoiceDatas getChoiceDatas);
    }


    interface Model {
        void getChoiceList(Map<String, Object> maps);
    }

    interface Present {
        void loadChoiceList(Map<String, Object> maps);

        void resChoiceList(String dataStr);
    }

}
