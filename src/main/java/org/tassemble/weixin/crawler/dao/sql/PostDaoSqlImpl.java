package org.tassemble.weixin.crawler.dao.sql;

import org.springframework.stereotype.Component;
import org.tassemble.base.commons.dao.annotation.DomainMetadata;
import org.tassemble.base.commons.dao.sql.BaseDaoSqlImpl;
import org.tassemble.weixin.crawler.domain.Post;
import org.tassemble.weixin.crawler.dao.PostDao;


@Component
@DomainMetadata(domainClass= Post.class)
public class PostDaoSqlImpl extends BaseDaoSqlImpl<Post> implements PostDao {

}
