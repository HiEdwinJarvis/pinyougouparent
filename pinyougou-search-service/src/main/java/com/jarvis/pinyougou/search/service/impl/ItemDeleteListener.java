package com.jarvis.pinyougou.search.service.impl;

import com.jarvis.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Arrays;

/**
 * @Description:
 * @CreateDate: 2019/8/6 14:26
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/6 14:26
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ItemDeleteListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    
    @Override
    public void onMessage(Message message) {

        try{
            ObjectMessage objectMessage =(ObjectMessage) message;

            Long[] goodsIds = (Long[]) objectMessage.getObject();

            System.out.println("我监听到的消息是"+goodsIds);

            itemSearchService.deleteByGoodsIds(Arrays.asList(goodsIds));

            System.out.println("成功删除索引库中的记录");


        }catch (Exception e){


        }



    }
}
