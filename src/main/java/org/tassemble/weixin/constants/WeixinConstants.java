package org.tassemble.weixin.constants;
/**
 * @author CHQ
 * @date Mar 12, 2014
 * @since 1.0
 */
public interface WeixinConstants {
    public static final String TOKEN = "netease_edu_901";
    public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create";
    public static final String GET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get";
    public static final String DLETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete";
    
    public static final Long TEN_MINUTE_SEC = 10*60L;//10分钟的秒数
    
    public static final String MSG_TYPE_OF_TEXT = "text";
    public static final String MSG_TYPE_OF_EVENT = "event";
    public static final String MSG_TYPE_OF_NEWS = "news";
    
    public static final String EVENT_TYPE_OF_CLICK = "CLICK";
    public static final String EVENT_TYPE_OF_SUBSCRIBE = "subscribe";

    
    public static final String MSG_TYPE_TEXT = "text";
    public static final String MSG_TYPE_PIC = "image";
    public static final String MSG_TYPE_VOICE = "voice";
    public static final String MSG_TYPE_VIDEO = "video";
    public static final String MSG_TYPE_LOCATION = "location";
    public static final String MSG_TYPE_LINK = "link";
    public static final String MSG_TYPE_EVENT = "event";
    
    
}
