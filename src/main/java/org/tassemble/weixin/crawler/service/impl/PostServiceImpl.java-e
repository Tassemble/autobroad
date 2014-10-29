package org.tassemble.weixin.crawler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.tassemble.base.commons.service.impl.BaseServiceImpl;
import org.tassemble.weixin.crawler.dao.PostDao;
import org.tassemble.weixin.crawler.domain.Post;
import org.tassemble.weixin.crawler.service.PostService;


@Service
public class PostServiceImpl extends BaseServiceImpl<PostDao, Post> implements PostService {
	private PostDao dao;

    public PostDao getDao() {
        return dao;
    }

    @Autowired
    public void setPostDao(PostDao dao) {
        super.setBaseDao(dao);
        this.dao = dao;
    }
    
}
