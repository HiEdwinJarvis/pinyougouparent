package com.jarvis.pinyougou.page.service.impl;

//import com.alibaba.dubbo.config.annotation.Service;
import com.jarvis.pinyougou.dao.mapper.TbGoodsDescMapper;
import com.jarvis.pinyougou.dao.mapper.TbGoodsMapper;
import com.jarvis.pinyougou.dao.mapper.TbItemCatMapper;
import com.jarvis.pinyougou.dao.mapper.TbItemMapper;
import com.jarvis.pinyougou.page.service.ItemPageService;
import com.jarvis.pinyougou.pojo.TbGoods;
import com.jarvis.pinyougou.pojo.TbGoodsDesc;
import com.jarvis.pinyougou.pojo.TbItem;
import com.jarvis.pinyougou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @CreateDate: 2019/8/4 12:55
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/4 12:55
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Value("${pagedir}")
    private String pagedir;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public boolean genItemHtml(Long goodsId) {

        try {
            Configuration configuration = freeMarkerConfig.getConfiguration();

            Template template = configuration.getTemplate("item.ftl");



            Map dataModel =new HashMap<>();

            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);

            //1,加载商品表数据
            dataModel.put("goods",goods);

            //2,加载商品扩展表数据
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);

            dataModel.put("goodsDesc",goodsDesc);

            //3.读取商品分类

            String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            dataModel.put("itemCat1", itemCat1);
            dataModel.put("itemCat2", itemCat2);
            dataModel.put("itemCat3", itemCat3);

            //4.读取SKU列表
            TbItemExample example=new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(goodsId);//SPU ID
            criteria.andStatusEqualTo("1");//状态有效
            example.setOrderByClause("is_default desc");//按是否默认字段进行降序排序，目的是返回的结果第一条为默认SKU

            List<TbItem> itemList = itemMapper.selectByExample(example);
            dataModel.put("itemList", itemList);


            /**
             *
             * new OutputStreamWriter(new FileOutputStream(new File(pagedir+goodsId+".html")),"UTF-8")
             *
             * 解决生成的魔板模板中文乱码问题
             * */
            Writer out = new OutputStreamWriter(new FileOutputStream(new File(pagedir+goodsId+".html")),"UTF-8");

            template.process(dataModel,out);

            out.close();

             return true;





        }catch (Exception e){

            return false;
        }




    }

    @Override
    public boolean deleteItemHtml(Long[] goodsIds) {

        try{

            for(Long goodsId:goodsIds){

                new File(pagedir+goodsId+".html").delete();
            }

            return true;
        }catch (Exception e){

            return false;
        }

    }
}
