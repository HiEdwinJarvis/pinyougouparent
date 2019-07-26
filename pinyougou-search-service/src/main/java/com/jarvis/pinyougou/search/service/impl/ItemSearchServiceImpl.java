package com.jarvis.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jarvis.pinyougou.pojo.TbItem;
import com.jarvis.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @CreateDate: 2019/7/26 1:08
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/7/26 1:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {


    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {

        Map<String,Object> map = new HashMap<>();
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        map.put("rows",page.getContent());
        return map;
    }
}