package org.tassemble.weixin.dto;


public class ResponsePicMsgItemDto {
    private String url; //点击图文消息跳转链接
    private String title;
    private String description;
    private String picUrl;//图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80。
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPicUrl() {
        return picUrl;
    }
    
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "ResponsePicMsgItemDto [url=" + url + ", title=" + title + ", description=" + description
               + ", picUrl=" + picUrl + "]";
    }      
}
