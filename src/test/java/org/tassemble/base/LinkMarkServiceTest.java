package org.tassemble.base;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tassemble.base.dao.BaseTestCase;
import org.tassemble.utils.GsonUtils;
import org.tassemble.utils.HttpClientUtils;
import org.tassemble.weixin.crawler.domain.Post;
import org.tassemble.weixin.crawler.service.LinkMarkService;
import org.tassemble.weixin.crawler.service.WeixinCrawlerManager;
import org.tassemble.weixin.manager.GongZhongManager;
import org.tassemble.weixin.manager.GongzhongSession;

/**
 * @author CHQ
 * @date Feb 22, 2014
 * @since 1.0
 */
public class LinkMarkServiceTest extends BaseTestCase{

	@Autowired
	LinkMarkService linkMarkService;
	
	
	@Autowired
	HttpClientUtils httpClientUtils;
	
	
	@Autowired
	WeixinCrawlerManager WeixinCrawlerManager;
	
	
	@Autowired
	GongZhongManager GongZhongManager;
	
	@Test
	public void uploadFile() throws Exception {
		GongzhongSession session = new GongzhongSession(httpClientUtils.getCommonHttpManager(), "luanlexi@163.com", "b1efe3fe1c322c3107dc7c605beafa02");
		
		if (session.login()) {
			GsonUtils.printJson(session.uploadFile("http://e.hiphotos.baidu.com/image/w%3D2048/sign=d5c6221ddf54564ee565e33987e69d82/738b4710b912c8fc5f5ce8c3fe039245d6882114.jpg"));
		} else {
			System.out.println("login in failed");
		}
		
	}
	@Test
	public void testGetData() {
		GsonUtils.printJson(linkMarkService.getQingNianLinkMarksByURL("http://www.qingniantuzhai.com/"));
	}
	
	

	@Test
	public void testGetDataDetail() {
		GsonUtils.printJson(linkMarkService.getQingNianPostItems("http://www.qingniantuzhai.com/6887.html"));
	}
	
	
	@Test
	public void testSaveArticles() {
			WeixinCrawlerManager.crawleQingNianPostsAndSave(1);
	}
	
	
	@Test
	public void testCreateArticles() {
		GongzhongSession session = GongZhongManager.createSesson("luanlexi@163.com", "b1efe3fe1c322c3107dc7c605beafa02");
		String result = GongZhongManager.createArticle(session, GongZhongManager.fetchSomeArticlesByTimes(Post.POST_TYPE_SMALL_FUNNY, 1));
		GsonUtils.printJson(result);
	}
	
	@Test
	public void getSendArticle() {
		GongzhongSession session = new GongzhongSession(httpClientUtils.getCommonHttpManager(), "luanlexi@163.com", "b1efe3fe1c322c3107dc7c605beafa02");
		session.login();
//		System.out.println(session.getTopArticleId());
		session.doPostArticle();
	}
	
	
	@Test
	public void createArticleAndSend() {
		GongzhongSession session = GongZhongManager.createSesson("luanlexi@163.com", "b1efe3fe1c322c3107dc7c605beafa02");
		if (session.isLogin()) {
			String result = GongZhongManager.createArticle(session, GongZhongManager.fetchSomeArticlesByTimes(Post.POST_TYPE_SMALL_FUNNY, 1));
			if (!GongzhongSession.INVALID_ARTICLE_ID.equals(result)) {
				session.doPostArticle();
			}
		}
	}
	
}
