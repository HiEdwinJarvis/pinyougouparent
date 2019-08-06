package com.jarvis.pinyougou.page.service.impl;

import com.jarvis.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @Description:
 * @CreateDate: 2019/8/6 15:09
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/6 15:09
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Component
public class PageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;

        try{

            Long[] goodsIds = (Long[]) objectMessage.getObject();

            System.out.println("监听到的消息是"+goodsIds);

            boolean b = itemPageService.deleteItemHtml(goodsIds);
            System.out.println("网页删除结构"+b);


        }catch (Exception e){


        }
    }
}
