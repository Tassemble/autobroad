package org.tassemble.base;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tassemble.base.dao.BaseTestCase;
import org.tassemble.base.dao.TestDao;

public class HeyTest extends BaseTestCase{

	
	@Autowired
	TestDao testDao;
	
	
	@Test
	public void test() {
		List<org.tassemble.base.Test> records = testDao.getAll();
		System.out.println(records.get(0).getName());
		
		System.out.println(testDao.getFirstOneByCondition("id = ?", 1).getName());
		
	}
	
}
