package com.ouyangliuy;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.junit.jupiter.api.Test;

import java.util.*;

public class StartApplication {


    @Test
    public void producer() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("ooxx");
        producer.setNamesrvAddr("192.168.208.11:9876");
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setTopic("wula");
            message.setTags("TagA");
            message.setBody(("ooxx"+i).getBytes());
            message.setWaitStoreMsgOK(true);

            SendResult result = producer.send(message);
            System.out.println(result);

            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                }

                @Override
                public void onException(Throwable e) {

                }
            });

            producer.sendOneway(message);


            //这个场景很稀缺，它支持，但是其实有被分布式特征
//            MessageQueue mq =  new MessageQueue("wula");
//            SendResult send = producer.send(message, mq);
//            System.out.println(send);

        }

    }

    @Test
    public void consumerPull() throws Exception {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("xxx1x");
        consumer.setNamesrvAddr("192.168.208.11:9876");
        consumer.start();

        Collection<MessageQueue> mqs = consumer.fetchMessageQueues("wula");

        System.out.println("queues:");
        mqs.forEach(messageQueue -> System.out.println(messageQueue));

        System.out.println("poll....");
//        consumer.assign(mqs);
        Collection<MessageQueue> queue = new ArrayList<>();
        MessageQueue qu = new MessageQueue("wula", "node01", 0);
        queue.add(qu);
        consumer.assign(queue);
        consumer.seek(qu,4);
        List<MessageExt> poll = consumer.poll();
        poll.forEach(s-> System.out.println(s));


        System.in.read();

    }

    @Test
    public void consumerPush() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_ox");
        consumer.setNamesrvAddr("192.168.208.11:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);


        consumer.subscribe("wula","*");


        consumer.registerMessageListener(new MessageListenerConcurrently() {
            //小批量，小集合
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                msgs.forEach(messageExt -> {
//                    System.out.println(messageExt);
                    byte[] body = messageExt.getBody();
                    String msg = new String(body);
                    System.out.println(msg);
                });

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();

        System.in.read();

    }

    @Test
    public void admin() throws Exception {

        DefaultMQAdminExt admin = new DefaultMQAdminExt();
        admin.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876");
        admin.start();

        ClusterInfo clusterInfo = admin.examineBrokerClusterInfo();
        HashMap<String, BrokerData> brokerAddrTable = clusterInfo.getBrokerAddrTable();
        Set<Map.Entry<String, BrokerData>> entries = brokerAddrTable.entrySet();
        Iterator<Map.Entry<String, BrokerData>> iter = entries.iterator();
        while(iter.hasNext()){
            Map.Entry<String, BrokerData> next = iter.next();
            System.out.println(next.getKey()+ " "+ next.getValue());
        }

//        System.out.println("topic list:");
//        TopicList topicList = admin.fetchAllTopicList();
//        Set<String> sets = topicList.getTopicList();
//        sets.forEach(s -> System.out.println(s));
//
//        System.out.println("topic route | info");
//        TopicRouteData wula = admin.examineTopicRouteInfo("wula");
//        System.out.println(wula);



    }
}
