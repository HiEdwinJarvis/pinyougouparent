package com.jarvis.pinyougou.page.service.impl;

import com.jarvis.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Description:
 * @CreateDate: 2019/8/6 14:45
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/6 14:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Component
public class PageListener implements MessageListener {

    @Autowired
    ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage)message;


        try{
            String text = textMessage.getText();
            System.out.println("接受到的消息是"+text);
            boolean b = itemPageService.genItemHtml(Long.parseLong(text));

        }catch(Exception e){


        }


    }
}
