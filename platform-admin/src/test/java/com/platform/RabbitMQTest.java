/*
 * 项目名称:platform-plus
 * 类名称:RabbitMQTest.java
 * 包名称:com.platform
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/9/21 10:42            liuqianru    初版做成
 *
 */
package com.platform;

import com.platform.modules.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQTest
 *
 * @author liuqianru
 * @date 2020/9/21 10:42
 */

public class RabbitMQTest {
    //队列名称
    private static final String QUEUE_NAME = "test_simple_queue";

    @Test
    public static void main(String[] args)
    {
        try
        {
            //获取连接
            Connection connection = RabbitMQUtil.getConnection();
            //从连接中获取一个通道
            Channel channel = connection.createChannel();
            //声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "This is simple queue";
            //发送消息
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("utf-8"));
            System.out.println("[send]：" + message);
            channel.close();
            connection.close();
        }
        catch (IOException | TimeoutException e)
        {
            e.printStackTrace();
        }
    }
}
