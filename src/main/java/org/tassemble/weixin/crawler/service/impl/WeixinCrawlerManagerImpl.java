package org.tassemble.weixin.crawler.service.impl;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;

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
					for (Post post : posts) {
						//write picture to file
						if (StringUtils.isNotBlank(post.getPicUrl())) {
							int lastIndex = StringUtils.lastIndexOf(post.getPicUrl(), "/");
							String picName = "";
							if (lastIndex >= 0) {
								picName = post.getPicUrl().substring(lastIndex + 1, post.getPicUrl().length());
							} else {
								continue;
							}
							FileUtils.copyURLToFile(new URL(post.getPicUrl()), new File("pictures" + File.separator + picName));
							post.setPicUrl(appConfig.getDomain() + "/pictures/" + picName);
						}
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
