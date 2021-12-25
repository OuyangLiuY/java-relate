package com.ouyangliuy;

import io.netty.channel.DefaultChannelId;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SimpleApplication {


    @Test
    public void syncProducer() throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer("producer");
        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.134.11:9876;192.134.150.12:9876;192.168.134.13:9876");
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 10; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA-sync" /* Tag */,
                    ("Hello RocketMQ, I am syncProducer" +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        System.in.read();
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }

    @Test
    public void asyncProducer() throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("producer");
        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.134.11:9876;192.134.150.12:9876;192.168.134.13:9876");
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        for (int i = 0; i < messageCount; i++) {
            try {
                final int index = i;
                Message msg = new Message("TopicTest",
                        "TagA-async",
                        "OrderID188",
                        "Hello world, I am asyncProducer".getBytes(RemotingHelper.DEFAULT_CHARSET));
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d Exception %s %n", index, e);
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        System.in.read();
        producer.shutdown();
    }

    @Test
    public void onewayProducer() throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("producer");
        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.134.11:9876;192.134.150.12:9876;192.168.134.13:9876");
//        DefaultChannelId.newInstance();
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 10; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA-oneway" /* Tag */,
                    ("Hello RocketMQ I am onewayProducer " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            producer.sendOneway(msg);
        }
        System.out.println("发送完毕...");
        //Wait for sending to complete
//        Thread.sleep(5000);
        System.in.read();
        producer.shutdown();
    }

    @Test
    public void consumer() throws Exception {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("producer");
        // Specify name server addresses.
        consumer.setNamesrvAddr("192.168.134.11:9876;192.134.150.12:9876;192.168.134.13:9876");


        // Subscribe one more more topics to consume.
        consumer.subscribe("TopicTest", "*");
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msg);
                    String res = new String(msg.getBody());
                    System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), res);
                }
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        DefaultChannelId.newInstance();
        //Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");

        System.in.read();

    }




    @Test
    public void producer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ooxx");
        producer.setNamesrvAddr("192.168.134.11:9876;192.168.134.12:9876");
        producer.start();

        ArrayList<Message> msgs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            msgs.add(new Message(
                    "bala",
                    "TagA",
                    "key"+i,//全局唯一
                    ("message:"+i).getBytes()

            ));
        }

        SendResult res = producer.send(msgs);
        System.out.println(res);
    }


    /**
     * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     * 18个延迟级别
     * 1：
     * ConsumeConcurrentlyStatus.RECONSUME_LATER
     * consumer.setMaxReconsumeTimes(2);
     * %RETRY%xxx3x
     * %DLQ%xxx3x
     * 总结：rocketmq：重试队列，死信队列：以topic形式，且：以consumerGroup
     * 补充一下:kafka，客户端自己维护逻辑
     *
     * 2:延迟队列的场景
     */
    @Test
    public void consumer0()  {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("xxx6x");
        consumer.setNamesrvAddr("192.168.134.11:9876;192.168.134.12:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMaxReconsumeTimes(2);
        //TODO
//        consumer.setConsumeMessageBatchMaxSize(1);   //交付给消费逻辑的数量
        consumer.setPullBatchSize(1);  //从broker拉取消息的数量
        try {
            consumer.subscribe("bala","*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                MessageExt msge = msgs.get(0);
                try {
                    System.out.println(new String(msge.getBody()));
                    if(msge.getKeys().equals("key1")){
                        int a =  1/0;
                    }
                }catch (Exception e ){
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        /*
        new MessageListenerOrderly()  //有序消息处理
        new MessageListenerConcurrently()
        消息顺序的依赖性

         */

        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        consumer.shutdown();

    }
}
