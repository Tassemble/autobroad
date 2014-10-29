package org.tassemble.weixin.manager;

import java.util.List;

import org.tassemble.weixin.gongzhong.dto.GongArticle;

/**
 * @author CHQ
 * @date Feb 23, 2014
 * @since 1.0
 */
public interface GongZhongManager {
	
	
	public String createArticle(GongzhongSession session, List<GongArticle> articles);
	
	
	public GongzhongSession createSesson(String username, String md5Password);
	
	

	List<GongArticle> fetchSomeArticlesByTimes(String postType, int articleNum);
}
