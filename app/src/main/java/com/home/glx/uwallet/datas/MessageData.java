package com.home.glx.uwallet.datas;

public class MessageData {


    /**
     * id : 4
     * createdBy : 4
     * createdDate : 01-01-1970 08:00:00
     * modifiedBy : 4
     * modifiedDate : 01-01-1970 08:00:00
     * status : 1
     * ip :
     * title : 4
     * content : 4
     * type : 4
     */

    private String id;
    private String createdDate;
    private String modifiedDate;
    private String title;
    private String content;
    private int isRead;
    private String type;
    private String createTimes;

    public String getCreateTimes() {
        return createTimes;
    }

    public void setCreateTimes(String createTimes) {
        this.createTimes = createTimes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
