package com.home.glx.uwallet.datas;

/**
 * EventBus 实体类
 */
public class MessageEvent {
    private String message;
    public  MessageEvent(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

