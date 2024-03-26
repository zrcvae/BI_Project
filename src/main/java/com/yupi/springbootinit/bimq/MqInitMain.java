package com.yupi.springbootinit.bimq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yupi.springbootinit.constant.BiMqConstant;

import java.io.IOException;
import java.util.concurrent.TimeoutException;



/**
 * @author zrc
 * @date 2024/03/26
 * 用于创建队列和交换机，程序启动前执行
 * 目前使用这种方式会报错，无法连接
 */
public class MqInitMain {
//    public static void main(String[] args) {
//        try {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("124.221.7.140");
//        // 创建连接
//        Connection connection = factory.newConnection();
//        // 创建通道
//        Channel channel = connection.createChannel();
//        channel.exchangeDeclare("test_exchange", "direct");
//
//        // 绑定交换机和队列
//        channel.queueBind("test_queue", "test_exchange", "bi");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
