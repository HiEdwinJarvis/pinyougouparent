package com.jarvis.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.jarvis.pinyougou.dao.mapper.TbItemMapper;
import com.jarvis.pinyougou.pojo.TbItem;
import com.jarvis.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @CreateDate: 2019/7/26 0:15
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/7/26 0:15
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;


    /**
    * 导入商品数据
    * */

    public void importItemData(){

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        System.out.println("=================");
        for(TbItem item:tbItems){

            System.out.println(item.getTitle());
            Map specMap= JSON.parseObject(item.getSpec());//将spec字段中的json字符串转换为map
            item.setSpecMap(specMap);//给带注解的字段赋值


        }


        solrTemplate.saveBeans(tbItems);
        solrTemplate.commit();

        System.out.println("======end=======");



    }

    public static void main(String[] args) {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil)context.getBean("solrUtil");

        solrUtil.importItemData();
    }


}
