package org.tassemble.weixin.crawler.service;

import java.util.List;

import org.tassemble.base.commons.service.BaseService;
import org.tassemble.weixin.crawler.domain.LinkMark;
import org.tassemble.weixin.crawler.domain.Post;
import org.tassemble.weixin.crawler.domain.Post;
import org.tassemble.weixin.gongzhong.dto.GongArticle;

public interface LinkMarkService extends BaseService<LinkMark> {

	
	
	
	public static final Integer CHOOSE_TYPE_TIME_DESC = 2;
	public static final Integer CHOOSE_TYPE_RANDOM = 1;
	/**
	 * 
	 * 获得青年图摘网站主页下各个帖子的链接
	 * url:使用http://www.qingniantuzhai.com/
	 * <p>
	 *  调用者信息:(由调用方来添加以下信息：包括前端，移动端，以及有可能QA)
	 * <p>用途<span>web端</span>xxx</p>
	 * </p>
	 * @param url
	 * @return
	 * @author CHQ,如果有更新者在这里添加
	 * @version 1.0(每次更新者更新版本号，1.1,1,2等)
	 * @date  Mar 1, 2014 7:37:57 AM
	 */
	List<LinkMark> getQingNianLinkMarksByURL(String url);
	
	
	/**
	 * 在执行抓取直接，先检测是否已经抓取过了 ，如果已经抓取过了，就放弃这个抓取
	 * 获取青年图摘各个帖子的内容
	 * <p>
	 *  调用者信息:(由调用方来添加以下信息：包括前端，移动端，以及有可能QA)
	 * <p>用途<span>web端</span>xxx</p>
	 * </p>
	 * @param url
	 * @return
	 * @author CHQ,如果有更新者在这里添加
	 * @version 1.0(每次更新者更新版本号，1.1,1,2等)
	 * @date  Mar 1, 2014 7:39:19 AM
	 */
	List<Post> getQingNianPostItems(String url);
	
	
	
	/**
	 * 1. 存入数据库，记录是否爬虫已经爬过，第二，保存爬取的内容
	 * 2. 构建文章 title content pic ，文章的选择是一种类型，
	 * 		加一个数量，获得的方式可以是随机从数据库中读取，也可以是按照最新的时间
	 * 3. 上传文章，图片上传失败需要使用默认的图片上传
	 * 4. 发送文章 如果是多个内容，就选择多篇文章发送，如果是单个内容，就选择单个内容发送
	 */
	
	/**
	 * 
	 * <p>
	 * 保存到数据库中，同时，记录是否爬虫已经爬过，第二，保存爬取的内容
	 * </p>
	 * <p>
	 *  调用者信息:(由调用方来添加以下信息：包括前端，移动端，以及有可能QA)
	 * <p>用途<span>web端</span>xxx</p>
	 * </p>
	 * @param originUrl 文章的来源网站，需要记录以便不会重复抓取
	 * @param posts  
	 * @author CHQ,如果有更新者在这里添加
	 * @version 1.0(每次更新者更新版本号，1.1,1,2等)
	 * @date  Mar 1, 2014 8:03:28 AM
	 */
	public void addCrawlerResults(LinkMark originUrl, List<Post> posts);
	
	
	
	/**
	 * 从数据库中获取1到几篇文章
	 * 
	 * 
	 * @param postType 参考Post的post type
	 * @param number   要选择多少篇文章
	 * @param chosenType 1是随机 2是按照时间
	 * @return
	 * @author CHQ,如果有更新者在这里添加
	 * @version 1.0(每次更新者更新版本号，1.1,1,2等)
	 * @date  Mar 1, 2014 7:58:21 AM
	 */
	public List<Post> getPosts(String postType, Integer number, Integer chosenType);
	
	
	/**
	 * 重新组织文章，同时，增加广告等样式可以在这里调整
	 * @param posts
	 * @return
	 * @author CHQ,如果有更新者在这里添加
	 * @version 1.0(每次更新者更新版本号，1.1,1,2等)
	 * @date  Mar 1, 2014 8:00:51 AM
	 */
	public GongArticle makeArticles(LinkMark link, List<Post> posts);
	
	
}
