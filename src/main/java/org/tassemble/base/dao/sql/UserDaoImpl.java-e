package org.tassemble.base.dao.sql;

import org.springframework.stereotype.Repository;
import org.tassemble.base.User;
import org.tassemble.base.commons.dao.annotation.DomainMetadata;
import org.tassemble.base.commons.dao.sql.BaseDaoSqlImpl;
import org.tassemble.base.dao.UserDao;


@DomainMetadata(domainClass = User.class, tableName = "wp_users", idColumn="ID", idProperty="id")
@Repository("userDao")
public class UserDaoImpl extends BaseDaoSqlImpl<User> implements UserDao{

}
