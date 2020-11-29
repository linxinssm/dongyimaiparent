package com.dongyimai.page.service.impl;

import com.dongyimai.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.info4z.club
 * <p>title:com.dongyimai.page.service.impl</p>
 * <p>ClassName:PageDeleteListener</p>
 * <p>Description:TODO(请用一句话描述这个类的作用)</p>
 * <p>Compony:Info4z</p>
 * author:黄色闪光
 * date:2020/10/31
 * version:1.0
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class PageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        try {

            ObjectMessage objectMessage = (ObjectMessage) message;

            Long[] ids = (Long[]) objectMessage.getObject();
            //执行删除
            itemPageService.deleteItemHtml(ids);

            System.out.println("页面删除成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
