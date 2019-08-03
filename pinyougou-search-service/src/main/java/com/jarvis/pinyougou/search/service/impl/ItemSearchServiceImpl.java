package com.jarvis.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jarvis.pinyougou.pojo.TbItem;
import com.jarvis.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * 注入redisTemplate对象,数据从缓存中取
     * */
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {

        Map<String,Object> map = new HashMap<>();


        //1.查询列表
        map.putAll(searchList(searchMap));


        //2.根据关键字查询商品分类
        List categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);

        //3.查询品牌和规格列表
        String categoryName=(String)searchMap.get("category");

        if(!"".equals(categoryName)){//如果有分类名称
            map.putAll(searchBrandAndSpecList(categoryName));
        }else{//如果没有分类名称，按照第一个查询
            if(categoryList.size()>0){
                map.putAll(searchBrandAndSpecList((String)categoryList.get(0)));
            }
        }



        return map;
    }

    @Override
    public void importList(List list) {
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    @Override
    public void deleteByGoodsIds(List goodsList) {
        System.out.println("删除商品ID"+goodsList);
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").in(goodsList);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    /**
     * 根据关键字查抄功能方法
     * */

    private Map searchList(Map searchMap) {

        //替换掉搜索关键字中的空格
        String keywords = (String)searchMap.get("keywords");
        searchMap.put("keywords",keywords.replace(" ",""));



        Map map = new HashMap();
        HighlightQuery query = new SimpleHighlightQuery();

        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");//设置高亮域
        highlightOptions.setSimplePrefix("<em style='color:red'>");//高亮效果样式前缀
        highlightOptions.setSimplePostfix("</em>");//高亮显示内容的样式后缀


        query.setHighlightOptions(highlightOptions);//设置高亮选项

        //1.1按关键字查找
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        /**
         * 1.2过滤查询
         * 根据前台选中的各种属性进行过滤查询
         * */

        if(!"".equals(searchMap.get("category"))){

            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery fileterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(fileterQuery);
        }

        /**
         * 1.3品牌过滤
         * */

        if(!"".equals(searchMap.get("brand"))){

            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addCriteria(filterCriteria);
        }

        /**
         * 1.4按规格过滤
         * */
        if(searchMap.get("spec")!=null){
           Map<String,String> specMap = (Map)searchMap.get("spec");

           for(String key:specMap.keySet()){

               Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key) );
               FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
               query.addFilterQuery(filterQuery);

           }

        }

        /**
         * 1.5按价格筛选
         * */

        if(!"".equals(searchMap.get("price"))){
            String[] price = ((String) searchMap.get("price")).split("-");
            if(!price[0].equals("0")){//如果区间起点不等于0
                Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(price[0]);
                FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            if(!price[1].equals("*")){//如果区间终点不等于*
                Criteria filterCriteria=new  Criteria("item_price").lessThanEqual(price[1]);
                FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }



        /**
         * 1.6分页查询
         *
         * */

        Integer pageNo = (Integer)searchMap.get("pageNo");//页码
        if(pageNo==null){

            pageNo=1;//默认第一页
        }
        Integer pageSize = (Integer)searchMap.get("pageSize");
        if(pageSize==null){

            pageSize=20;//默认一页二十条
        }
        query.setOffset((pageNo-1)*pageSize);//从第几条记录开始查询
        query.setRows(pageSize);

        /**
         * 1.7排序
         * */

        String sortValue = (String) searchMap.get("sort");
        String sortField =(String) searchMap.get("sortField");//排序的字段

        if(sortValue!=null && !sortValue.equals("")){

            if(sortValue.equals("ASC")){


                Sort sort = new Sort(Sort.Direction.ASC,"item_"+sortField);
                query.addSort(sort);
            }

            if(sortValue.equals("DESC")){
                Sort sort = new Sort(Sort.Direction.DESC,"item_"+sortField);
                query.addSort(sort);
            }
        }











        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        for(HighlightEntry<TbItem> h:page.getHighlighted()){//循环高亮入口集合
            TbItem item =  h.getEntity();
            if(h.getHighlights().size()>0 && h.getHighlights().get(0).getSnipplets().size()>0){
                item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));//设置高亮的结果


            }

        }


        map.put("rows",page.getContent());

        map.put("totalPages",page.getTotalPages());//返回总页数
        map.put("total",page.getTotalElements());//返回总记录数
        return map;

    }

    /**
     * 查询分类列表
     * */
    private List searchCategoryList(Map searchMap){

        List<String> list = new ArrayList();
        Query query = new SimpleQuery();

        //按照关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        //得到分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query,TbItem.class);


        //根据列等得到分组结果集
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");

        //得到分组结果入口
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();

        //得到分组入口集合
        List<GroupEntry<TbItem>> content = groupEntries.getContent();

        for(GroupEntry<TbItem> entry : content){

            list.add(entry.getGroupValue());//将分组结构的名称分装到返回值中
        }




        return list;



    }

    /**
     * 从redis中取brand列表和spec列表
     * */

    private Map searchBrandAndSpecList(String category){


        Map map = new HashMap();
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);//获取模板ID


        if(typeId!=null){

            //根据模板ID查询品牌列表
            List brandList = (List)redisTemplate.boundHashOps("brandList").get(typeId);

            map.put("brandList",brandList);

            //根据模板ID查询规格列表
            List specList = (List)redisTemplate.boundHashOps("specList").get(typeId);

            map.put("specList",specList);
        }

        return map;


    }




}
