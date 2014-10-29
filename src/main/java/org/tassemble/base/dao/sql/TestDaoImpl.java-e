package org.tassemble.base.dao.sql;

import org.springframework.stereotype.Repository;
import org.tassemble.base.Test;
import org.tassemble.base.commons.dao.BaseDao;
import org.tassemble.base.commons.dao.annotation.DomainMetadata;
import org.tassemble.base.commons.dao.sql.BaseDaoSqlImpl;
import org.tassemble.base.dao.TestDao;



@DomainMetadata(domainClass = Test.class, tableName = "test", policyIdProperty = "id")
@Repository("testDao")
public class TestDaoImpl extends BaseDaoSqlImpl<Test> implements TestDao {

}
