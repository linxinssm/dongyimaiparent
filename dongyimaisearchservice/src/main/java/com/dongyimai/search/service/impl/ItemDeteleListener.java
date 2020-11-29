package com.dongyimai.search.service.impl;

import com.dongyimai.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * All rights Reserved, Designed By www.info4z.club
 * <p>title:com.dongyimai.search.service.impl</p>
 * <p>ClassName:ItemDeteleListener</p>
 * <p>Description:TODO(请用一句话描述这个类的作用)</p>
 * <p>Compony:Info4z</p>
 * author:黄色闪光
 * date:2020/10/31
 * version:1.0
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class ItemDeteleListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;


            Long[] ids = (Long[]) objectMessage.getObject();

            itemSearchService.deleteByGoodsIds(ids);

            System.out.println("删除成功");

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
