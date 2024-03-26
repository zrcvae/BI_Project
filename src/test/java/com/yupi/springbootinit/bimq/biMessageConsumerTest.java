package com.yupi.springbootinit.bimq;

import com.yupi.springbootinit.constant.BiMqConstant;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zrc
 * @date 2024/03/26
 */
@SpringBootTest
class biMessageConsumerTest {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    void receiveMessage() {
        rabbitTemplate.convertAndSend(BiMqConstant.EXCHANGE_NAME, "bi", "测试消息");
    }
}