package org.tassemble.weixin.dto;


public class ReceiveMsgBaseDto extends MessageBaseDto {
    public ReceiveMsgBaseDto() {
        
    }
    
    public ReceiveMsgBaseDto(String toUserName, String fromUserName, Long createTime, String msgType, Long msgId) {
        super(toUserName, fromUserName, createTime, msgType);
        this.msgId = msgId;
    }
    
    private Long msgId;

    
    public Long getMsgId() {
        return msgId;
    }

    
    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }


    @Override
    public String toString() {
        return "ReceiveMsgBaseDto [msgId=" + msgId + "]";
    }
    
}
