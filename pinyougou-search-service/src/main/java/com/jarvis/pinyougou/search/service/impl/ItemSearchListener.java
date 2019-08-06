package com.jarvis.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.jarvis.pinyougou.pojo.TbItem;
import com.jarvis.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @CreateDate: 2019/8/6 13:53
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/6 13:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Component
public class ItemSearchListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {

        try {
            System.out.println("监听到消息");

            TextMessage textMessage = (TextMessage) message;
            String text  = textMessage.getText();

            List<TbItem> list = JSON.parseArray(text, TbItem.class);
            for(TbItem item:list){

                System.out.println(item.getId()+" "+item.getTitle());
                Map specMap = JSON.parseObject(item.getSpec());

                item.setSpecMap(specMap);
            }
            itemSearchService.importList(list);

            System.out.println("成功导入索引库");

        }catch (Exception e){


        }




    }
}
