package org.tassemble.weixin.crawler.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.tassemble.weixin.config.AppConfig;
import org.tassemble.weixin.crawler.domain.LinkMark;
import org.tassemble.weixin.crawler.domain.Post;
import org.tassemble.weixin.crawler.service.LinkMarkService;
import org.tassemble.weixin.crawler.service.PostService;
import org.tassemble.weixin.crawler.service.WeixinCrawlerManager;

/**
 * @author CHQ
 * @date Mar 1, 2014
 * @since 1.0
 */

@Component
public class WeixinCrawlerManagerImpl implements WeixinCrawlerManager{

	
	@Autowired
	LinkMarkService linkMarkService;
	@Autowired
	PostService PostService;
	
	@Autowired
	AppConfig appConfig;
	
	
	private static final Logger LOG = LoggerFactory.getLogger(WeixinCrawlerManagerImpl.class);
	@Override
	public void crawleQingNianPostsAndSave(Integer index) {
		//1. crawler url
		//2. crawler content for each url
		//3. saven
		List<LinkMark> marks = linkMarkService.getQingNianLinkMarksByURL("http://www.qingniantuzhai.com/page/" + index);
		if (CollectionUtils.isEmpty(marks)) {
			return;
		}
		Collections.reverse(marks);
		
		for (LinkMark linkMark : marks) {
			try {
				if (StringUtils.isNotBlank(linkMark.getUrl())) {
					List<Post> posts = linkMarkService.getQingNianPostItems(linkMark.getUrl());
					linkMark.setId(linkMarkService.getId());
					
					
					File fold = new File("pictures");
					if (!fold.exists()) {
						fold.mkdirs();
					}
					ExecutorService exec = Executors.newFixedThreadPool(4);
					final CountDownLatch latch = new CountDownLatch(posts.size());
					for (final Post post : posts) {
						try {
							//write picture to file
							if (StringUtils.isNotBlank(post.getPicUrl())) {
								int lastIndex = StringUtils.lastIndexOf(post.getPicUrl(), "/");
								String picName = "";
								if (lastIndex >= 0) {
									picName = post.getPicUrl().substring(lastIndex + 1, post.getPicUrl().length());
								} else {
									continue;
								}
								final String picNameForTask = picName;
								final String originPicUrl = post.getPicUrl();
								Runnable task = new Runnable() {
									public void run() {
										try {
											FileUtils.copyURLToFile(new URL(originPicUrl), new File("pictures" + File.separator + picNameForTask));
											latch.countDown();
										} catch (Exception e) {
											LOG.error(e.getMessage() + " for pic:" + originPicUrl, e);
										}
									}
								};
								exec.execute(task);
								post.setPicUrl(appConfig.getDomain() + "/" + picName);
							}
						} catch (Exception e) {
							LOG.error(e.getMessage(), e);
						}
					}
					exec.shutdown();
					int cnt = 0;
					while(true) {
						if (exec.awaitTermination(1, TimeUnit.SECONDS) == true) {
							break;
						}
						cnt++;
						if (cnt >= 60) {
							List<Runnable> tasksNotExecute = exec.shutdownNow();
							LOG.warn("#warn# there's " + tasksNotExecute.size() + " task left");
						}
						LOG.info("latch: "  + latch.getCount());
					}
					
					if (!CollectionUtils.isEmpty(posts)) {
						for (Post post : posts) {
							post.setLinkId(linkMark.getId());
						}
						PostService.add(posts);
						linkMarkService.add(linkMark);
					} else {
						LOG.warn("fail to get post for url:" + linkMark.getUrl());
					}
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

}
