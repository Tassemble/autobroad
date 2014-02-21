package org.tassemble.base.dao.sql;

import org.springframework.stereotype.Repository;
import org.tassemble.base.WPPost;
import org.tassemble.base.WPTermRelationship;
import org.tassemble.base.commons.dao.annotation.DomainMetadata;
import org.tassemble.base.commons.dao.sql.BaseDaoSqlImpl;
import org.tassemble.base.dao.WPTermRelationshipDao;



@DomainMetadata(domainClass = WPPost.class, tableName = "wp_term_relationships")
@Repository("wpTermRelationshipDao")
public class WPTermRelationshipDaoImpl extends BaseDaoSqlImpl<WPTermRelationship> implements WPTermRelationshipDao {

}
