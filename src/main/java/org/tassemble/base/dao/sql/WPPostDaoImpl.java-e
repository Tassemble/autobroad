package org.tassemble.base.dao.sql;

import org.springframework.stereotype.Repository;
import org.tassemble.base.WPPost;
import org.tassemble.base.commons.dao.annotation.DomainMetadata;
import org.tassemble.base.commons.dao.sql.BaseDaoSqlImpl;
import org.tassemble.base.dao.WPPostDao;



@DomainMetadata(domainClass = WPPost.class, tableName = "wp_posts", policyIdProperty = "id")
@Repository("wPPostDao")
public class WPPostDaoImpl extends BaseDaoSqlImpl<WPPost> implements WPPostDao {

	
	@Override
	public void testInsert(String sql){
		this.getSqlManager().updateRecord(sql);
	}
}
