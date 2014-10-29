package org.tassemble.base.commons.utils.collection;

import java.util.List;

/*
 * @author 
 * @date 2013-3-22
 */
public interface BatchQueryerDataProvider<T, D> {

    /**
     * 对一个长数组queryParams进行分组查询，结果集再汇总到List<T>
     * 
     * @param queryParams
     * @return
     * @author 
     * @date 2013-3-29
     */
    List<T> getByQueryParams(List<D> queryParams);
}
