package org.tassemble.base.dao.sql;

import org.springframework.stereotype.Repository;
import org.tassemble.base.Comment;
import org.tassemble.base.commons.dao.annotation.DomainMetadata;
import org.tassemble.base.commons.dao.sql.BaseDaoSqlImpl;
import org.tassemble.base.dao.CommentDao;


@DomainMetadata(domainClass = Comment.class, tableName = "wp_comments", idColumn="comment_ID", idProperty="commentID")
@Repository("commentDao")
public class CommentDaoImpl extends BaseDaoSqlImpl<Comment> implements CommentDao{

}
