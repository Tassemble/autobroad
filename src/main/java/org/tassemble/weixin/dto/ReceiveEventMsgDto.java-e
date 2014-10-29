package org.tassemble.weixin.dto;


public class ReceiveEventMsgDto extends MessageBaseDto {
    public ReceiveEventMsgDto() {
        
    }
    
    public ReceiveEventMsgDto(String toUserName, String fromUserName, Long createTime, String msgType) {
        super(toUserName, fromUserName, createTime, msgType);
    }
    
    private String event;
    private String eventKey;
    
    public String getEvent() {
        return event;
    }
    
    public void setEvent(String event) {
        this.event = event;
    }
    
    public String getEventKey() {
        return eventKey;
    }
    
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    @Override
    public String toString() {
        return "ReceiveEventMsgDto [event=" + event + ", eventKey=" + eventKey + "]";
    }
    

}
