package com.home.glx.uwallet.datas;

import java.io.Serializable;
import java.util.List;

public class IllionPreLoadData implements Serializable {

    /**
     * name : Email address
     * fieldID : username
     * type : TEXT
     * description :
     * values : [{"keyName":"1","keyValue":"2"},{"keyName":"2","keyValue":"4"}]
     * defaultKey : 1
     * optional : true
     * isMFAField : true
     * validation : {"minLength":0,"maxLength":9999999,"chars":"*"}
     * keyboardType : default
     * src : data:image
     * width : 300
     * height : 60
     * alt :
     */

    private String name;
    private String fieldID;
    private String type;
    private String description;
    private List<ValuesBean> values;
    private String defaultKey;
    private String optional;
    private Boolean isMFAField;
    private ValidationBean validation;
    private String keyboardType;
    private String src;
    private Integer width;
    private Integer height;
    private String alt;

    public static class ValidationBean implements Serializable {
        /**
         * minLength : 0
         * maxLength : 9999999
         * chars : *
         */

        private String minLength;
        private String maxLength;
        private String chars;

        public String getMinLength() {
            return minLength;
        }

        public void setMinLength(String minLength) {
            this.minLength = minLength;
        }

        public String getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(String maxLength) {
            this.maxLength = maxLength;
        }

        public String getChars() {
            return chars;
        }

        public void setChars(String chars) {
            this.chars = chars;
        }
    }

    public static class ValuesBean implements Serializable {
        /**
         * keyName : 1
         * keyValue : 2
         */

        private String keyName;
        private String keyValue;

        public String getKeyName() {
            return keyName;
        }

        public void setKeyName(String keyName) {
            this.keyName = keyName;
        }

        public String getKeyValue() {
            return keyValue;
        }

        public void setKeyValue(String keyValue) {
            this.keyValue = keyValue;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldID() {
        return fieldID;
    }

    public void setFieldID(String fieldID) {
        this.fieldID = fieldID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ValuesBean> getValues() {
        return values;
    }

    public void setValues(List<ValuesBean> values) {
        this.values = values;
    }

    public String getDefaultKey() {
        return defaultKey;
    }

    public void setDefaultKey(String defaultKey) {
        this.defaultKey = defaultKey;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public Boolean getMFAField() {
        return isMFAField;
    }

    public void setMFAField(Boolean MFAField) {
        isMFAField = MFAField;
    }

    public ValidationBean getValidation() {
        return validation;
    }

    public void setValidation(ValidationBean validation) {
        this.validation = validation;
    }

    public String getKeyboardType() {
        return keyboardType;
    }

    public void setKeyboardType(String keyboardType) {
        this.keyboardType = keyboardType;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}
