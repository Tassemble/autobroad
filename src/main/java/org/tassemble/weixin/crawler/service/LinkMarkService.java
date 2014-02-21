package org.tassemble.weixin.crawler.service;

import java.util.List;

import org.tassemble.base.commons.service.BaseService;
import org.tassemble.weixin.crawler.domain.LinkMark;
import org.tassemble.weixin.crawler.domain.Post;
import org.tassemble.weixin.crawler.domain.Post;

public interface LinkMarkService extends BaseService<LinkMark> {

	
	
	List<LinkMark> getQianNianLinkMarksByURL(String url);
	
	
	
	List<Post> getQianNianPostItems(String url);
}
