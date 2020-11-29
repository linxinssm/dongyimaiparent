package com.offcn.util;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component("smsListener")
public class SmsListener implements MessageListener {
    @Autowired
    private SmsUtil smsUtil;

    @Override
    public void onMessage(Message message) {

        if (message instanceof MapMessage) {
            try {

                MapMessage map = (MapMessage) message;

                smsUtil.sendSms(map.getString("mobile"),map.getString("code"));

                System.out.println("短信发送完毕");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
