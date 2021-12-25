package com.ouyangliuy;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import javax.sound.midi.Soundbank;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExampleApplication {

    /**
     * 延迟队列
     */
    @Test
    public void delayProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("delay-producer");
        producer.setNamesrvAddr("192.168.134.11:9876;192.168.134.12:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("topic_delay", "tags",
                    " Hello ! I am delayProducer,我来了".getBytes(StandardCharsets.UTF_8));
            msg.setDelayTimeLevel(1);
            SendResult res = producer.send(msg);
            System.out.println("I am delayProducer,I send result = " + res);
        }
        System.in.read();
    }


    @Test
    public void delayConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("delay-consumer");
        consumer.setNamesrvAddr("192.168.134.11:9876;192.168.134.12:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("topic_delay", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msg,
                                                            ConsumeConcurrentlyContext context) {
                msg.forEach(System.out::println);
                System.out.println("消息收到了一条了...");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("consumer started.");
        System.in.read();
    }
}
