package com.home.glx.uwallet.datas;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class IllionMfaData implements Serializable {


    /**
     * id :
     * type : header
     * label : SecurityCode(6digits)
     * value : EnterSecondaryPassword(QWERTYUI)
     * options : [{"keyName":"1","keyValue":"2"},{"keyName":"2","keyValue":"4"}]
     * hint : radio
     * subElements : [{"htmlAttrs":{"size":"1","maxlength":"1","autocomplete":"off","disabled":"disabled","class":"optional"},"active":false,"optional":true,"id":"","type":"password","value":"sdfsdfsdfsd"}]
     * script : functiontabToNextField(field,limit,next,evt){evt=(evt)?evt:
     * htmlAttrs : {"autocomplete":"off"}
     */

    private String id;
    private String type;
    private String label;
    private String value;
    private List<OptionsBean> options;
    private String hint;
    private List<SubElementsBean> subElements;
    private String script;
    private HtmlAttrsBean htmlAttrs;

    public static class HtmlAttrsBean implements Serializable {
        /**
         * autocomplete : off
         */

        private String autocomplete;
    }

    public static class OptionsBean implements Serializable {
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

    public static class SubElementsBean implements Serializable {
        /**
         * htmlAttrs : {"size":"1","maxlength":"1","autocomplete":"off","disabled":"disabled","class":"optional"}
         * active : false
         * optional : true
         * id :
         * type : password
         * value : sdfsdfsdfsd
         */

        private HtmlAttrsBean htmlAttrs;
        private String active;
        private Boolean optional;
        private String id;
        private String type;
        private String value;
        private List<OptionsBean> options;

        public List<OptionsBean> getOptions() {
            return options;
        }

        public void setOptions(List<OptionsBean> options) {
            this.options = options;
        }

        public HtmlAttrsBean getHtmlAttrs() {
            return htmlAttrs;
        }

        public void setHtmlAttrs(HtmlAttrsBean htmlAttrs) {
            this.htmlAttrs = htmlAttrs;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public Boolean getOptional() {
            return optional;
        }

        public void setOptional(Boolean optional) {
            this.optional = optional;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static class HtmlAttrsBean implements Serializable {
            /**
             * size : 1
             * maxlength : 1
             * autocomplete : off
             * disabled : disabled
             * class : optional
             */

            private String size;
            private String maxlength;
            private String autocomplete;
            private String disabled;
            @SerializedName("class")
            private String classX;

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getMaxlength() {
                return maxlength;
            }

            public void setMaxlength(String maxlength) {
                this.maxlength = maxlength;
            }

            public String getAutocomplete() {
                return autocomplete;
            }

            public void setAutocomplete(String autocomplete) {
                this.autocomplete = autocomplete;
            }

            public String getDisabled() {
                return disabled;
            }

            public void setDisabled(String disabled) {
                this.disabled = disabled;
            }

            public String getClassX() {
                return classX;
            }

            public void setClassX(String classX) {
                this.classX = classX;
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<OptionsBean> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsBean> options) {
        this.options = options;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public List<SubElementsBean> getSubElements() {
        return subElements;
    }

    public void setSubElements(List<SubElementsBean> subElements) {
        this.subElements = subElements;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public HtmlAttrsBean getHtmlAttrs() {
        return htmlAttrs;
    }

    public void setHtmlAttrs(HtmlAttrsBean htmlAttrs) {
        this.htmlAttrs = htmlAttrs;
    }
}
