package org.tassemble.weixin.crawler.service;
/**
 * @author CHQ
 * @date Feb 26, 2014
 * @since 1.0
 */
public interface WeixinCrawlerManager {
	
	
	/**
	 * 
	 * 抓取青年图摘入口 同时，将结果保存到数据库中
	 * <p>
	 *  调用者信息:(由调用方来添加以下信息：包括前端，移动端，以及有可能QA)
	 * <p>用途<span>web端</span>xxx</p>
	 * </p>
	 * @author CHQ,如果有更新者在这里添加
	 * @version 1.0(每次更新者更新版本号，1.1,1,2等)
	 * @date  Mar 1, 2014 8:12:08 AM
	 */
	void crawleQingNianPostsAndSave(Integer index);
	
	
	
	
}
