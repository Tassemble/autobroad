package org.tassemble.base;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tassemble.base.dao.BaseTestCase;
import org.tassemble.utils.GsonUtils;
import org.tassemble.weixin.crawler.service.LinkMarkService;

/**
 * @author CHQ
 * @date Feb 22, 2014
 * @since 1.0
 */
public class LinkMarkServiceTest extends BaseTestCase{

	@Autowired
	LinkMarkService linkMarkService;
	
	@Test
	public void testGetData() {
		GsonUtils.printJson(linkMarkService.getQianNianLinkMarksByURL("http://www.qingniantuzhai.com/"));
	}
	
	

	@Test
	public void testGetDataDetail() {
		GsonUtils.printJson(linkMarkService.getQianNianPostItems("http://www.qingniantuzhai.com/6860.html"));
	}
	
	
}
