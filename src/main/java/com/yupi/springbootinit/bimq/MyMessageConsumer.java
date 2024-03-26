package com.yupi.springbootinit.bimq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zrc
 * @date 2024/03/26
 * 这样写好像比注解的方式优先级更高
 */
@Component
@Slf4j
public class MyMessageConsumer {

//    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
//    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
//        log.info("接收到消息为：{}", message);
//        try {
//            channel.basicAck(deliveryTag, false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
