package org.tassemble.weixin.dto;

import java.util.List;

import org.tassemble.weixin.constants.WeixinConstants;


public class ResponsePicMsgDto extends MessageBaseDto {
    public ResponsePicMsgDto() {
        
    }
    
    public ResponsePicMsgDto(String toUserName, String fromUserName, Long createTime) {
        super(toUserName, fromUserName, createTime, WeixinConstants.MSG_TYPE_PIC);
    }
    
    private List<ResponsePicMsgItemDto> itemList;    
     
    public List<ResponsePicMsgItemDto> getItemList() {
        return itemList;
    }
    
    public void setItemList(List<ResponsePicMsgItemDto> itemList) {
        this.itemList = itemList;
    }
}
