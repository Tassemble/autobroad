package org.tassemble.weixin.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.tassemble.weixin.constants.WeixinConstants;
import org.tassemble.weixin.dto.MessageBaseDto;
import org.tassemble.weixin.dto.ReceiveEventMsgDto;
import org.tassemble.weixin.dto.ReceiveMsgBaseDto;
import org.tassemble.weixin.dto.ReceiveTextMsgDto;
import org.tassemble.weixin.dto.ResponsePicMsgDto;
import org.tassemble.weixin.dto.ResponsePicMsgItemDto;
import org.tassemble.weixin.dto.ResponseTextMsgDto;

/**
 * @author CHQ
 * @date Mar 12, 2014
 * @since 1.0
 */
public class WeixinXmlParser {

	public static MessageBaseDto parseXml(byte[] xml) throws DocumentException, UnsupportedEncodingException {
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("utf-8");
		Document document = saxReader.read(new ByteArrayInputStream(xml));
		Element rootElt = document.getRootElement();

		String toUserName = rootElt.elementTextTrim("ToUserName");
		String formUserName = rootElt.elementTextTrim("FromUserName");
		Long createTime = Long.valueOf(rootElt.elementTextTrim("CreateTime"));
		String msgType = rootElt.elementTextTrim("MsgType");
		String msgIdStr = rootElt.elementTextTrim("MsgId");
		Long msgId = 0L;
		if (StringUtils.isNotBlank(msgIdStr)) {

			msgId = Long.valueOf(msgIdStr);
		}

		if (WeixinConstants.MSG_TYPE_OF_TEXT.equals(msgType)) {
			ReceiveTextMsgDto text = new ReceiveTextMsgDto(toUserName, formUserName, createTime, msgType, msgId);
			text.setContent(rootElt.elementTextTrim("Content"));
			return text;
		} else if (WeixinConstants.MSG_TYPE_OF_EVENT.equals(msgType)) {
			ReceiveEventMsgDto event = new ReceiveEventMsgDto(toUserName, formUserName, createTime, msgType);
			event.setEvent(rootElt.elementTextTrim("Event"));
			event.setEventKey(rootElt.elementTextTrim("EventKey"));
			return event;
		} else {
			ReceiveMsgBaseDto dto = new ReceiveMsgBaseDto(toUserName, formUserName, createTime, msgType, msgId);
			return dto;
		}
	}

	public static Document makeXml(MessageBaseDto messageDto) {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("utf-8");
		if (messageDto == null) {
			return document;
		}
		Element root = document.addElement("xml");
		Element node = root.addElement("ToUserName");
		node.addCDATA(messageDto.getToUserName());
		node = root.addElement("FromUserName");
		node.addCDATA(messageDto.getFromUserName());
		node = root.addElement("CreateTime");
		node.setText(String.valueOf(System.currentTimeMillis() / 1000));
		node = root.addElement("MsgType");
		node.addCDATA(messageDto.getMsgType());

		if (WeixinConstants.MSG_TYPE_OF_TEXT.equals(messageDto.getMsgType())) {
			node = root.addElement("Content");
			node.addCDATA(((ResponseTextMsgDto) messageDto).getContent());
		} else if (WeixinConstants.MSG_TYPE_OF_NEWS.equals(messageDto.getMsgType())) {
			List<ResponsePicMsgItemDto> itemList = ((ResponsePicMsgDto) messageDto).getItemList();
			if (CollectionUtils.isNotEmpty(itemList)) {
				node = root.addElement("ArticleCount");
				node.setText(String.valueOf(itemList.size()));

				node = root.addElement("Articles");
				Element article = null;
				Element itemElement = null;
				for (ResponsePicMsgItemDto item : itemList) {
					itemElement = node.addElement("item");
					article = itemElement.addElement("Title");
					article.addCDATA(item.getTitle());
					article = itemElement.addElement("Description");
					article.addCDATA(item.getDescription());
					article = itemElement.addElement("PicUrl");
					article.addCDATA(item.getPicUrl());
					article = itemElement.addElement("Url");
					article.addCDATA(item.getUrl());
				}
			}
		}

		return document;
	}

}
