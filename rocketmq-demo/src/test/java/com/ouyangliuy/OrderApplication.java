package com.ouyangliuy;

import io.netty.channel.DefaultChannelId;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * rocketMQ 顺序队列
 */
public class OrderApplication {


    @Test
    public void orderProducer() throws Exception {
        //
        DefaultMQProducer producer = new DefaultMQProducer("order_producer");
        producer.setNamesrvAddr("192.168.134.11:9876;192.168.134.12:9876;192.168.134.13");
        producer.start();


        for (int i = 0; i < 10; i++) {
            Message msg = new Message(
                    "order_topic",
                    "TagA",
                    ("message body : " + i + "type: " + i % 3).getBytes()
            );
            //TODO
            //SendResult res = producer.send(msg, selector, i % 3);
            // SendResult res = producer.send(msg, new MyMessageQueueSelector(), i % 3);
            SendResult res = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer index = (Integer) arg;
                    int target = index % mqs.size();
                    return mqs.get(target);
                }
            }, i % 3);
            System.out.println(res);
            Thread.sleep(3000);
            producer.shutdown();
        }
    }

    @Test
    public void orderConsumer() throws Exception {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer");
        // Specify name server addresses.
        consumer.setNamesrvAddr("192.168.134.11:9876;192.134.150.12:9876;192.168.134.13:9876");


        // Subscribe one more more topics to consume.
        consumer.subscribe("order_topic", "*");
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext context) {
                list.forEach(msge -> {
                    System.out.println(Thread.currentThread().getName() + " : " + new String(msge.getBody()));
                });
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        DefaultChannelId.newInstance();
        //Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");

        System.in.read();

    }
}
