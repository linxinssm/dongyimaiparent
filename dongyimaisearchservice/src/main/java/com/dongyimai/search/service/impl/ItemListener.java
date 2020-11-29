package com.dongyimai.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dongyimai.pojo.TbItem;
import com.dongyimai.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

@Component
public class ItemListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage=(TextMessage)message;
                //把JSON格式的字符串转换成集合
            List<TbItem> itemList = JSON.parseArray(textMessage.getText(),TbItem.class);

            System.out.println("itemList"+itemList.size());

            //每一个item的规格spec都要把json格式转换成字符串,格式化
            for (TbItem tbItem : itemList) {
                Map map = JSON.parseObject(tbItem.getSpec());
                tbItem.setSpecMap(map);
            }

            itemSearchService.importList(itemList);

            System.out.println("更新成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
