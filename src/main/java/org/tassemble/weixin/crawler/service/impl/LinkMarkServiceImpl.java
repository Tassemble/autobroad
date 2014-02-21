package org.tassemble.weixin.crawler.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.tassemble.base.commons.service.impl.BaseServiceImpl;
import org.tassemble.base.commons.utils.collection.PropertyExtractUtils;
import org.tassemble.base.commons.utils.sql.SqlBuilder;
import org.tassemble.utils.HttpClientUtils;
import org.tassemble.weixin.crawler.dao.LinkMarkDao;
import org.tassemble.weixin.crawler.domain.LinkMark;
import org.tassemble.weixin.crawler.domain.Post;
import org.tassemble.weixin.crawler.service.LinkMarkService;


@Service
public class LinkMarkServiceImpl extends BaseServiceImpl<LinkMarkDao, LinkMark> implements LinkMarkService {
	private LinkMarkDao dao;

	
	@Autowired
	HttpClientUtils httpClientUtils;
	
	
	private Logger LOG = LoggerFactory.getLogger(LinkMarkServiceImpl.class);
	
    public LinkMarkDao getDao() {
        return dao;
    }

    @Autowired
    public void setLinkMarkDao(LinkMarkDao dao) {
        super.setBaseDao(dao);
        this.dao = dao;
    }

	@Override
	public List<LinkMark> getQianNianLinkMarksByURL(String url) {
		List<LinkMark> links = new ArrayList<LinkMark>();
		String html = HttpClientUtils.getHtmlByGetMethod(httpClientUtils.getCommonHttpManager(), url);
		
		
		Document document = Jsoup.parse(html);
		Elements contents = document.select(".post > .post-img");
		if (!(contents != null && contents.size() > 0)) {
			return links;
		}
		
		Pattern picPattern = Pattern.compile("background:url\\('(.*?)'\\)");
		long now = System.currentTimeMillis();
		for (Element element : contents) {
			try {
				LinkMark link = new LinkMark();
				
				String stringContainPicture = element.attr("style");
				if (StringUtils.isNotBlank(stringContainPicture)) {
					Matcher matcher = picPattern.matcher(stringContainPicture);
					if (matcher.find()) {
						link.setPic(matcher.group(1));
					}
				}
				Elements subElements = element.select(".png");
				if (!(subElements != null && subElements.size() > 0)) {
					continue;
				}
				
				link.setTitle(subElements.get(0).text());
				link.setUrl(subElements.get(0).attr("href"));
				
				
				if (StringUtils.isBlank(link.getPic())) {
					continue;
				}
				link.setGmtCreate(now);
				link.setGmtModified(now);
				link.setUrlHash(DigestUtils.md5Hex(link.getPic()));
				links.add(link);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			
			List<LinkMark> marks = this.getByCondition(SqlBuilder.inSql("url_hash", 
					PropertyExtractUtils.getByPropertyValue(links, "urlHash", String.class)));
			if (CollectionUtils.isEmpty(marks)) {
				return links;
			}
			
			for (Iterator iter = links.iterator(); iter.hasNext();) {
				LinkMark item = (LinkMark) iter.next();
				for (LinkMark exist : marks) {
					if (exist.getUrlHash().equals(item.getUrlHash())) {
						iter.remove();
					}
				}
			}			
		}
		return links;
	}

	@Override
	public List<Post> getQianNianPostItems(String url) {
		List<Post> posts = new ArrayList<Post>();
		
		String html = HttpClientUtils.getHtmlByGetMethod(httpClientUtils.getCommonHttpManager(), url);
		
		Document document = Jsoup.parse(html);
		Elements contents = document.select("#mainbox > .post-content > .content-c > p");
		
		
		if (!(contents != null && contents.size() > 0)) {
			return posts;
		}
		
		long now = System.currentTimeMillis();
		StringBuilder sb = null;
		for (int index = 0; index < contents.size(); index++) {
			try {
				if (isItemStart(index)) {
					sb = new StringBuilder();
				}
				sb.append("<p>");
				String text = contents.get(index).html();
				
				if (StringUtils.isNotBlank(text) && text.contains("搜狗")) {
					index += 2;
					continue;
				}
				if (StringUtils.isNotBlank(text)) {
					sb.append(filterContent(text));
				}
				sb.append("</p>");
				if (isItemEnd(index)) {
					Post post = new Post();
					post.setContent(sb.toString());
					post.setPostType(Post.POST_TYPE);
					post.setGmtCreate(now);
					post.setGmtModified(now);
					posts.add(post);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			
		}
		
		return posts;
	}
	
	
	private String filterContent(String content) {
		
		if (StringUtils.isBlank(content)) {
			return content;
		}
		
		int start = content.indexOf("【");
		if (start >= 0) {
			int end = content.indexOf("】");
			if (end >= 0) {
				return content.substring(end + 1, content.length());
			}
		}
		
		return content;
	}

	private boolean isItemStart(int index) {
		if (index % 3 == 0) {
			return true;
		}
		return false;
	}
	
	private boolean isItemEnd(int index) {
		if ((index + 1) % 3 == 0) {
			return true;
		}
		
		return false;
	}
	
	
	
    
}
