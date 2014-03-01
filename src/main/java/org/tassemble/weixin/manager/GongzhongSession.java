package org.tassemble.weixin.manager;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.lf5.util.StreamUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.tassemble.utils.HttpClientUtils;
import org.tassemble.utils.HttpDataProvider;
import org.tassemble.weixin.gongzhong.dto.GongArticle;

import com.netease.framework.dao.sql.annotation.DataProperty;

/**
 * @author CHQ
 * @date Feb 23, 2014
 * @since 1.0
 */
public class GongzhongSession {
	private static final String WECHAT_LOGIN_URL = "http://mp.weixin.qq.com/cgi-bin/login?lang=zh_CN";
	private static final String FILE_UPLOAD_TICKET = "https://mp.weixin.qq.com/cgi-bin/appmsg?t=media/appmsg_edit&action=edit&type=10&isMul=1&isNew=1&lang=zh_CN&token=";
	private static final String FILE_UPLOAD_URL = "https://mp.weixin.qq.com/cgi-bin/filetransfer?action=upload_material&f=json&ticket_id=%s&lang=zh_CN&ticket=%s";
	private final static Logger LOG = LoggerFactory.getLogger(GongzhongSession.class); 
	
	private final String DEFAULT_PIC_URL = "http://img.hb.aicdn.com/4e61532fafc768f467ec9d441611b04badead05e7fc15-5NAFvF_fw658";	
	public static final String INVALID_FILE_ID = "-1";
	
	public static final String INVALID_ARTICLE_ID = "-1";
	public static final String INVALID_RESULT = "-1";
	private HttpClient			client;
	private boolean				login;

	private String				username;
	private String				password;
	private String 				uploadTicket;
	private String 				uploadUsername;
	
	private String 				token;
	

	public GongzhongSession(HttpClient newClient, String username, String passwordWithMD5) {
		this.client = newClient;
		this.username = username;
		this.password = passwordWithMD5;
	}

	
	private boolean getFileUploadTicket() {
		String contentWithUploadTikekct = HttpClientUtils.getHtmlByGetMethod(client, new HttpDataProvider() {
			@Override
			public String getUrl() {
				return FILE_UPLOAD_TICKET + token;
			}
			
			@Override
			public HttpEntity getHttpEntity() {
				return null;
			}
			
			@Override
			public List<Header> getHeaders() {
				List<Header> headerList = new ArrayList<Header>();
				headerList.add(new BasicHeader("Content-Type", "text/html; charset=utf-8"));
				return headerList;
			}
		});
		if (StringUtils.isNotBlank(contentWithUploadTikekct)) {
			this.uploadTicket = getSequenceByKey(contentWithUploadTikekct, "ticket:\"");
			this.uploadUsername = getSequenceByKey(contentWithUploadTikekct, "user_name:\"");
			if (this.uploadTicket.equals(INVALID_RESULT) || this.uploadUsername.equals(INVALID_RESULT)) {
				throw new RuntimeException("获取上传ticket失败");
			}
			return true;
		}
		LOG.error("ticket get failed:" + contentWithUploadTikekct);
		return false;
	}


	private String getSequenceByKey(String contentWithUploadTikekct, String keyword) {
		int ticketIndex = contentWithUploadTikekct.indexOf(keyword);
		if (ticketIndex >= 0) {
			String endPostfix = "\",";
			int ticketIndexEnd = contentWithUploadTikekct.indexOf(endPostfix, ticketIndex + keyword.length());
			if (ticketIndexEnd > 0) {
				return contentWithUploadTikekct.substring(ticketIndex + keyword.length(), ticketIndexEnd);
			}
		}
		return INVALID_RESULT;
	}
	
	public String uploadFile(final String picUrl) {
		return uploadFile(picUrl, 2);
	}
	
	
	public String uploadFile(final String picUrl, int retry) {
		if (retry <= 0) {
			return INVALID_FILE_ID;
		}
		retry--;
		
		final String token = this.token;
		final String ticket = this.uploadTicket;
		final String uploadUsername = this.uploadUsername;
		String result;
		try {
			result = HttpClientUtils.getHtmlByPostMethod(client, new HttpDataProvider() {
				
				@Override
				public String getUrl() {
					return String.format(FILE_UPLOAD_URL, uploadUsername,  ticket);
				}
				
				@Override
				public HttpEntity getHttpEntity()  {
					try {
						InputStream is = new URL(picUrl).openStream();
						MultipartEntity entity = new MultipartEntity();
						entity.addPart("file", new ByteArrayBody(StreamUtils.getBytes(is), "filename.jpg"));
						is.close();
						return entity;
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
					return null;
				}
				
				@Override
				public List<Header> getHeaders() {
					List<Header> headers = new ArrayList<Header>();
					BasicHeader header = new BasicHeader("referer", FILE_UPLOAD_TICKET + token );
					headers.add(header);
					return headers;
				}
			});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return INVALID_FILE_ID;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, Object> hash = mapper.readValue(result, HashMap.class);
			if (hash.get("base_resp") != null) {
				HashMap<String, Object> innerMap = (HashMap)hash.get("base_resp");
				Integer successCode = 0;
				if(successCode.equals(innerMap.get("ret"))) {
					LOG.info("file id:" + hash.get("content").toString());
					return hash.get("content").toString();
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return INVALID_FILE_ID;
		}
		
		//try default picture
		LOG.error("upload file failed, result:" + result);
		return uploadFile(DEFAULT_PIC_URL, retry);
	}


	public boolean login() {

		String result = null;
		try {
			result = HttpClientUtils.getHtmlByPostMethod(client, new HttpDataProvider() {
				@Override
				public String getUrl() {
					return WECHAT_LOGIN_URL;
				}

				@Override
				public HttpEntity getHttpEntity() {
					try {
						ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("username", username));
						params.add(new BasicNameValuePair("pwd", password));
						params.add(new BasicNameValuePair("imgcode", ""));
						params.add(new BasicNameValuePair("f", ""));
						
						return new UrlEncodedFormEntity(params, "UTF-8");
					} catch (UnsupportedEncodingException e) {
					}
					return null;
				}

				@Override
				public List<Header> getHeaders() {
					
					List<Header> headerList = new ArrayList<Header>();
			        headerList.add(new BasicHeader("Referer", "https://mp.weixin.qq.com/cgi-bin/loginpage?t=wxm-login&lang=zh_CN"));
			        headerList.add(new BasicHeader("Content-Type", "text/html; charset=utf-8"));
			        
					return headerList;
				}
			});
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, Object> hash = mapper.readValue(result, HashMap.class);
			
			if (hash.get("ErrCode").equals(0)) {
				this.setLogin(true);
				String msg = (String)hash.get("ErrMsg");
				if (StringUtils.isNotBlank(msg)) {
					String tokenPrefxi = "token=";
					int idx = msg.indexOf(tokenPrefxi);
					if (idx >= 0) {
						this.setToken(msg.substring(idx + tokenPrefxi.length(), msg.length()));
						getFileUploadTicket();
						return true;
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		LOG.error("login in failed, result:" + result);

		return false;
	}


	private String createMultiArticles(List<GongArticle> articles) {
		try {
			final StringBuilder sb = new StringBuilder();
			List<String> fileIds = new ArrayList<String>();
			for (GongArticle gongArticle : articles) {
				String fileId = uploadFile(gongArticle.getPicUrl());
				if (!INVALID_FILE_ID.equals(fileId)) {
					fileIds.add(fileId);
				} else {
					throw new RuntimeException("上传文件失败");
				}
			}
				
			int order = 0;
			boolean isFirstOne = true;
			for (GongArticle gongArticle : articles) {
				if (!isFirstOne) {
					sb.append("&");
				} else {
					isFirstOne = false;
				}
				sb.append("title").append(order).append("=").append(URLEncoder.encode(gongArticle.getTitle(), "UTF-8"));
				sb.append("&content").append(order).append("=").append(URLEncoder.encode(gongArticle.getContent(), "UTF-8"));
				sb.append("&digest").append(order).append("=").append(URLEncoder.encode(gongArticle.getTitle(), "UTF-8"));
				sb.append("&author").append(order).append("=");
				sb.append("&fileid").append(order).append("=").append(fileIds.get(order));
				sb.append("&show_cover_pic").append(order).append("=0");
				sb.append("&sourceurl").append(order).append("=");
				order++;
			}
			
			//''.join([str(postString.getvalue()),'&token=',self.sessionToken.token,'&AppMsgId=&count=', str(index), '&ajax=1&lang=zh_CN&random=0.9744154384825379&f=json&t=ajax-response&sub=create&type=10']);
			sb.append("&token=").append(this.token).append("&AppMsgId=&count=").append(articles.size())
			.append("&ajax=1&lang=zh_CN&random=0.9744154384825379&f=json&t=ajax-response&sub=create&type=10");
			//
			final String token = this.token;
			final int isMulti = (articles.size() > 1 ? 1 : 0);
			
			return HttpClientUtils.getHtmlByPostMethod(client, new HttpDataProvider() {
				
				@Override
				public String getUrl() {
					return "https://mp.weixin.qq.com/cgi-bin/operate_appmsg";
				}
				
				@Override
				public HttpEntity getHttpEntity() {
					return new StringEntity(sb.toString(), ContentType.TEXT_PLAIN);
				}
				
				@Override
				public List<Header> getHeaders() {
					List<Header> headerList = new ArrayList<Header>();
					headerList.add(new BasicHeader("Referer", "https://mp.weixin.qq.com/cgi-bin/appmsg?t=media/appmsg_edit&action=edit&type=10&isMul=" + isMulti+ "&isNew=1&lang=zh_CN&token=" + token));
					headerList.add(new BasicHeader("Content-Type", "text/html; charset=utf-8"));
					return headerList;
				}
			});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return INVALID_ARTICLE_ID;
		}
	}
	
	public String createArticle(List<GongArticle> articles) {
		if (CollectionUtils.isEmpty(articles)) {
			return "-1";
		}
		
		return createMultiArticles(articles);
	}
	
	
	public String getTopArticleId() {
		String result = HttpClientUtils.getHtmlByGetMethod(client, new HttpDataProvider() {
			@Override
			public String getUrl() {
				return String.format("https://mp.weixin.qq.com/cgi-bin/appmsg?token=%s&lang=zh_CN&random=0.5771591463126242&f=json&ajax=1&type=10&action=list&begin=0&count=10", token);
			}

			@Override
			public HttpEntity getHttpEntity() {
				return null;
			}

			@Override
			public List<Header> getHeaders() {
				List<Header> headerList = new ArrayList<Header>();
				headerList.add(new BasicHeader("Referer", String.format("https://mp.weixin.qq.com/cgi-bin/masssendpage?t=mass/send&token=%s&lang=zh_CN", token)));
				headerList.add(new BasicHeader("Content-Type", "text/html; charset=utf-8"));
				return headerList;
			}
			
		});
		
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, Object> hash = mapper.readValue(result, HashMap.class);
			if (hash.get("base_resp") != null) {
				HashMap<String, Object> innerMap = (HashMap)hash.get("base_resp");
				Integer successCode = 0;
				if(successCode.equals(innerMap.get("ret"))) {
					if (hash.get("app_msg_info") != null) {
						HashMap<String, Object>  msgMap = (HashMap)hash.get("app_msg_info");
						String firstJson = JSONObject.fromObject(hash.get("app_msg_info")).getJSONArray("item").get(0).toString();
						return JSONObject.fromObject(firstJson).getString("app_id");
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return INVALID_FILE_ID;
	}
	
	public boolean doPostArticle() {
		final String id = getTopArticleId();
		final String token = this.token;
		//
		//Referer:
		try {
			String result = HttpClientUtils.getHtmlByPostMethod(client, new HttpDataProvider() {

				@Override
				public String getUrl() {
					return "https://mp.weixin.qq.com/cgi-bin/masssend";
				}

				@Override
				public HttpEntity getHttpEntity() {
					try {
						ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("type", "10"));
						params.add(new BasicNameValuePair("appmsgid", id));
						params.add(new BasicNameValuePair("sex", "0"));
						params.add(new BasicNameValuePair("groupid", "-1"));
						params.add(new BasicNameValuePair("synctxweibo", "0"));
						params.add(new BasicNameValuePair("synctxnews", "0"));
						params.add(new BasicNameValuePair("country", ""));
						params.add(new BasicNameValuePair("province", ""));
						params.add(new BasicNameValuePair("city", ""));
						params.add(new BasicNameValuePair("imgcode", ""));
						params.add(new BasicNameValuePair("token", token));
						params.add(new BasicNameValuePair("lang", "zh_CN"));
						params.add(new BasicNameValuePair("random", "0.5487554285209626"));
						params.add(new BasicNameValuePair("f", "json"));
						params.add(new BasicNameValuePair("ajax", "1"));
						params.add(new BasicNameValuePair("t", "ajax-response"));
						return new UrlEncodedFormEntity(params, "UTF-8");
					} catch (UnsupportedEncodingException e) {
					}
					return null;
				}

				@Override
				public List<Header> getHeaders() {
					List<Header> headerList = new ArrayList<Header>();
					headerList.add(new BasicHeader("Referer", String.format("https://mp.weixin.qq.com/cgi-bin/masssendpage?t=mass/send&token=%s&lang=zh_CN", token)));
					headerList.add(new BasicHeader("Content-Type", "text/html; charset=utf-8"));
					return headerList;
				}
			});
			return true;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		
		return false;
	}

	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	


	public boolean isLogin() {
		return login;
	}


	public void setLogin(boolean login) {
		this.login = login;
	}


	public String getUploadTicket() {
		return uploadTicket;
	}


	public void setUploadTicket(String uploadTicket) {
		this.uploadTicket = uploadTicket;
	}


	public String getUploadUsername() {
		return uploadUsername;
	}


	public void setUploadUsername(String uploadUsername) {
		this.uploadUsername = uploadUsername;
	}

}
