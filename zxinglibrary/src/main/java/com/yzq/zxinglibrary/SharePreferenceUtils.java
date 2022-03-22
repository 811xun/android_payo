package com.yzq.zxinglibrary;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SharePreferenceUtils {

    private Context context;
    private String file_name;

    private SharedPreferences share;
    private SharedPreferences.Editor edit;

    public SharePreferenceUtils(Context context, String file_name) {
        this.context = context;
        this.file_name = file_name;
        share = context.getSharedPreferences(file_name, MODE_PRIVATE);
        edit = share.edit(); //编辑文件
    }


    /**
     * 保存数据
     */
    public void save() {
        edit.commit();
    }

    /**
     * 添加数据
     *
     * @param key
     * @param object
     * @return
     */
    public SharedPreferences.Editor put(String key, Object object) {
        if (object instanceof String) {
            edit.putString(key, (String) object);
        } else if (object instanceof Integer) {
            edit.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            edit.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            edit.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            edit.putLong(key, (Long) object);
        } else {
            edit.putString(key, object.toString());
        }
        return edit;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return share.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return share.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return share.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return share.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return share.getLong(key, (Long) defaultObject);
        }
        return null;
    }


    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String key) {
        edit.remove(key).commit();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        edit.clear().commit();
    }


    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean is_contains(String key) {
        return share.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public Map<String, ?> getAll() {
        return share.getAll();
    }

}
