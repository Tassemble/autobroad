package org.tassemble.weixin.dto;


public class MessageBaseDto {
    public MessageBaseDto() {
        
    }
    
    public MessageBaseDto(String toUserName, String fromUserName, Long createTime, String msgType) {
        this.createTime = createTime;
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.msgType = msgType;
    }
    
    private String toUserName;
    private String fromUserName;
    private Long createTime; //单位s
    private String msgType;
    
    public String getToUserName() {
        return toUserName;
    }
    
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }
    
    public String getFromUserName() {
        return fromUserName;
    }
    
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }
    
    public Long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    
    public String getMsgType() {
        return msgType;
    }
    
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "MessageBaseDto [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", createTime="
               + createTime + ", msgType=" + msgType + "]";
    }
    

}
