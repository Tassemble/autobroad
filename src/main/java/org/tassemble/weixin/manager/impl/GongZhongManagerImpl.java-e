package org.tassemble.weixin.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.tassemble.utils.HttpClientUtils;
import org.tassemble.weixin.crawler.domain.LinkMark;
import org.tassemble.weixin.crawler.domain.Post;
import org.tassemble.weixin.crawler.service.LinkMarkService;
import org.tassemble.weixin.gongzhong.dto.GongArticle;
import org.tassemble.weixin.manager.GongZhongManager;
import org.tassemble.weixin.manager.GongzhongSession;

/**
 * @author CHQ
 * @date Mar 1, 2014
 * @since 1.0
 */
@Component
public class GongZhongManagerImpl implements GongZhongManager{

	
	@Autowired
	HttpClientUtils httpClientUtils;
	
	@Autowired
	LinkMarkService linkMarkService;
	
	
	@Override
	public String createArticle(GongzhongSession session, List<GongArticle> articles) {
		if (session != null && session.isLogin()) {
			return session.createArticle(articles);
		}
		return GongzhongSession.INVALID_ARTICLE_ID;
	}

	
	// DigestUtils.md5Hex("xxxx"));
	@Override
	public GongzhongSession createSesson(String username, String md5Password) {
		GongzhongSession session = new GongzhongSession(httpClientUtils.getCommonHttpManager(), username,md5Password);
		session.login();
		return session;
	}


	@Override
	public List<GongArticle> fetchSomeArticlesByTimes(String postType, int articleNum) {
		int retryTime = 3;
		List<GongArticle> articles = new ArrayList<GongArticle>();
		for (int i = 0; i < articleNum; i++) {
			GongArticle article = getOneArticle(postType);
			if (article == null) {
				if (retryTime > 0) {
					i--;
				}
				retryTime--;
				continue;
			}
			articles.add(article);
		}
		return articles;
	}


	private GongArticle getOneArticle(String postType) {
		int number = 35;
		if (Post.POST_TYPE_LARGE_FUNNY.equals(postType)) {
			number = 1;
		}
		List<Post> posts = linkMarkService.getPosts(postType, number, LinkMarkService.CHOOSE_TYPE_TIME_DESC);
		if (CollectionUtils.isEmpty(posts)) {
			return null;
		}
		
		Long linkId = null;
		for (Post post : posts) {
			if (post.getLinkId() != null) {
				linkId = post.getLinkId();
				break;
			}
		}
		LinkMark linkMark = linkMarkService.getById(linkId);
		if (linkMark == null) {
			return null;
		}
		GongArticle article = linkMarkService.makeArticles(linkMark, posts);
		return article;
	}
	
}
