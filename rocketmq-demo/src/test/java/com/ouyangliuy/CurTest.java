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

import java.util.List;

public class CurTest {




    /**
     * 主要体现在producer方
     */
    @Test
    public void consumerDelay() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("delayconsumer");
        consumer.setNamesrvAddr("192.168.134.11:9876;192.168.134.12:9876;192.168.134.13:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("topic_delay","*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                msgs.forEach(msg-> System.out.println(msg));

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();

    }

    @Test
    public void producerDelay() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("delay_producer");
        producer.setNamesrvAddr("192.168.134.11:9876;192.168.134.12:9876;192.168.134.13:9876");
        producer.start();

        for (int i = 0; i < 10; i++) {

            Message msga = new Message(
                    "topic_delay",
                    "TagD",
                    ("message "+ i).getBytes()

            );
            //延迟是以消息为级别的
            msga.setDelayTimeLevel(i%18);
            SendResult send = producer.send(msga); //没有直接进入目标topic的队列，而是进入延迟队列
            System.out.println(send);
        }

        System.in.read();


    }
}
