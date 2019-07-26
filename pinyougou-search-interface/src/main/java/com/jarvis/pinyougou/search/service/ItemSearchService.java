package com.jarvis.pinyougou.search.service;

import java.util.Map;

/**
 * @Description:
 * @CreateDate: 2019/7/26 0:57
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/7/26 0:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface ItemSearchService {

    /**
     * 搜索
     * */
    public Map<String,Object> search(Map searchMap);


}
