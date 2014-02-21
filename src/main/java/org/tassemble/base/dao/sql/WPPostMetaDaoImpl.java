package org.tassemble.base.dao.sql;

import org.springframework.stereotype.Repository;
import org.tassemble.base.WPPost;
import org.tassemble.base.WPPostMeta;
import org.tassemble.base.commons.dao.annotation.DomainMetadata;
import org.tassemble.base.commons.dao.sql.BaseDaoSqlImpl;
import org.tassemble.base.dao.WPPostMetaDao;



@DomainMetadata(idColumn="meta_id", domainClass = WPPostMeta.class, tableName = "wp_postmeta", idProperty = "metaId")
@Repository("wpPostMetaDao")
public class WPPostMetaDaoImpl extends BaseDaoSqlImpl<WPPostMeta> implements WPPostMetaDao {

}
