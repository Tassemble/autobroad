package org.tassemble.base.dao;

import org.tassemble.base.WPPost;
import org.tassemble.base.commons.dao.BaseDao;

public interface WPPostDao extends BaseDao<WPPost> {

	void testInsert(String sql);

}
