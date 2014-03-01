package org.tassemble.weixin.crawler.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
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
import org.tassemble.weixin.crawler.service.PostService;
import org.tassemble.weixin.gongzhong.dto.GongArticle;


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
	public List<LinkMark> getQingNianLinkMarksByURL(String url) {
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
				
				link.setTitle(filterExtraWords(subElements.get(0).text()));
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
		return links;
	}

	private String filterExtraWords(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
		
		return text.replaceAll("青年[0-9]+，", "");
	}

	@Override
	public List<Post> getQingNianPostItems(String url) {
		List<Post> posts = new ArrayList<Post>();
		
		String html = HttpClientUtils.getHtmlByGetMethod(httpClientUtils.getCommonHttpManager(), url);
		
		Document document = Jsoup.parse(html);
//		Elements contents = document.select("#mainbox > .post-content > .content-c > p");
		Elements contents = document.select("span[style=color: #339966;]");
		
		if (!(contents != null && contents.size() > 0)) {
			return posts;
		}
		
		long now = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		String title = "";
		String picUrl = null;
		for (int index = 0; index < contents.size(); index++) {
			
			try {
				String text = contents.get(index).html();
				if (isItemStart(text)) {
					if (StringUtils.isNotBlank(sb.toString())) {
						sb.append("</p>");//为了区分图片和首位分隔符
						Post post = new Post();
						post.setPicUrl(picUrl);
						post.setTitle(title);
						post.setContent(sb.toString());
						post.setPostType(Post.POST_TYPE_SMALL_FUNNY);
						post.setGmtCreate(now);
						post.setGmtModified(now);
						posts.add(post);

						title = "";
						picUrl = "";
					}
					sb = new StringBuilder();

					// 标题
					title = filterContent(contents.get(index).text());

				} else {
					sb.append("<p>");
				}
				if (StringUtils.isNotBlank(text) && text.contains("搜狗")) {
					index += 1;
					continue;
				}
				if (StringUtils.isNotBlank(text)) {
					sb.append(filterContent(text));
				}
				// 图片

				Document picHtml = Jsoup.parse(text);
				Elements elements = picHtml.select("img");

				if (elements != null && elements.size() > 0)
					picUrl = elements.get(0).attr("src");
				
				
				
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			
		}
		
		
		if (CollectionUtils.isEmpty(posts)) {
			// get all content
			Elements allContents = document.select("#mainbox > .post-content > .content-c > p");
			if (allContents != null && allContents.size() > 0) {
				Elements pics = allContents.select("img");
				
				Post post = new Post();
				if (pics != null && pics.size() > 0) {
					post.setPicUrl(pics.attr("src"));
				}
				post.setTitle("美图欣赏");
				
				StringBuilder allContent = new StringBuilder();
				for (int i = 0; i < allContents.size(); i++) {
					allContent.append("<p>").append(allContents.get(0).html()).append("</p>").append("<br/>");
				}
				post.setContent(allContent.toString());
				post.setPostType(Post.POST_TYPE_LARGE_FUNNY);
				post.setGmtCreate(now);
				post.setGmtModified(now);
				posts.add(post);
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

	private boolean isItemStart(String text) {
		Pattern seqPattern = Pattern.compile("(【[0-9]+】)");
		Matcher matcher = seqPattern.matcher(text);
		if (matcher.find()) {
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
	

	@Override
	public void addCrawlerResults(LinkMark originUrl, List<Post> posts) {
		long now = System.currentTimeMillis();
		originUrl.setGmtCreate(now);
		originUrl.setGmtModified(now);
		originUrl.setId(this.getId());
		add(originUrl);
		for (Post post : posts) {
			post.setGmtCreate(now);
			post.setGmtModified(now);
			post.setLinkId(originUrl.getId());
		}
		postService.add(posts);
	}

	
	@Autowired
	PostService postService;
	
	@Override
	public List<Post> getPosts(String postType, Integer number, Integer chosenType) {
		if (StringUtils.isBlank(postType)) {
			postType = Post.POST_TYPE_SMALL_FUNNY;
		}
		if (number == null || number <= 0) {
			if (Post.POST_TYPE_SMALL_FUNNY.equals(postType)) {
				number = 40;
			} else {
				throw new RuntimeException("post type 不能没有");
			}
		}
		List<Post> result = new ArrayList<Post>();
		if (chosenType == 1) {//随机
			List<Post> posts = postService.getByCondition("post_type = ? limit ?", postType, number * 10);
			int cnt = 0;
			
			while(cnt < number && !CollectionUtils.isEmpty(posts)) {
				cnt++;
				Integer randInt = RandomUtils.nextInt(posts.size());
				result.add(posts.get(randInt));
				posts.remove(randInt);
			}
			return result;
		} else { //按照时间
			return postService.getByCondition("post_type = ? order by gmt_create desc limit ?", postType, number);
		}
	}

	@Override
	public GongArticle makeArticles(LinkMark link, List<Post> posts) {
		if (CollectionUtils.isEmpty(posts)) {
			throw new RuntimeException("文章不能为空");
		}
 		GongArticle article = new GongArticle();
 		if (link != null && StringUtils.isNotBlank(link.getPic())) {
 			article.setPicUrl(link.getPic());
 			article.setTitle(link.getTitle());
 		} else {
 			article.setPicUrl(posts.get(0).getPicUrl());
 			article.setTitle(posts.get(0).getTitle());
 		}
		String click = "<p style=\"white-space: normal; \"><span style=\"color: rgb(63, 63, 63); \"><img data-src=\"http://mmbiz.qpic.cn/mmbiz/U5Kia6Wuqh6sGku1hLGy8CSoDaS89GMG3mLiaRfiaUWrEKY4jQBFNZd6CVT4N98Td0VpZ8kJ2euz53zVULDoCpdXw/0\" style=\"border: 0px; width: 700px; color: rgb(34, 34, 34); font-family: 宋体; font-size: medium; -webkit-text-size-adjust: none; background-color: rgb(255, 255, 255); \" src=\"http://mmbiz.qpic.cn/mmbiz/U5Kia6Wuqh6sGku1hLGy8CSoDaS89GMG3mLiaRfiaUWrEKY4jQBFNZd6CVT4N98Td0VpZ8kJ2euz53zVULDoCpdXw/0\"></span></p>";
		StringBuilder sb = new StringBuilder(click);
		int cnt = 1;
		for (Post post : posts) {//<img alt="" src="http://ww1.sinaimg.cn/mw690/6b3904catw1edsejaodcej20k00qogmt.jpg" />
			sb.append("(" + (cnt++) + ").").append(" ").append(post.getTitle()).append("<br/><img alt=\"\" src=\"").append(post.getPicUrl()).append("\" /><br/>").append("<p></p>");
		}
		article.setContent(sb.toString());
		return article;
	}

	
	
	
    
}
