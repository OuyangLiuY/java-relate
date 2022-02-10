package com.ouyangliuy.demo;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleApplication {

    @Test
    public void admin() throws Exception {

        DefaultMQAdminExt admin = new DefaultMQAdminExt();
        admin.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876;192.168.208.13:9876");
        admin.start();

        ClusterInfo clusterInfo = admin.examineBrokerClusterInfo();
        HashMap<String, BrokerData> brokerAddrTable = clusterInfo.getBrokerAddrTable();
        Set<Map.Entry<String, BrokerData>> entries = brokerAddrTable.entrySet();
        Iterator<Map.Entry<String, BrokerData>> iter = entries.iterator();
        while(iter.hasNext()){
            Map.Entry<String, BrokerData> next = iter.next();
            System.out.println(next.getKey()+ " "+ next.getValue());
        }



        TopicList topicList = admin.fetchAllTopicList();
        topicList.getTopicList().forEach(s->{
            System.out.println(s);
        });
    }


    /*

        广播：consumer端自己规划：
            //set to broadcast mode
        consumer.setMessageModel(MessageModel.BROADCASTING);

        延迟队列 (超时)
        1，细粒度
            时间轮：【先自行学习一下】，排序成本，分治概念
        2，粗粒度
        messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            目标topic： ooxx_topic   消息，期望 30s，你的消息是放到  30S——topic，由rocketmq broker

        事务
     */


    //---------------MQ的事务 什么是事务----------------------


    /**
     * 还是producer这一侧的
     */
    @Test
    public void consumerTransaction() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("transaction_consumer2");
        consumer.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("topic_transaction2","*");
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
    public void producerTransaction() throws Exception {

        TransactionMQProducer producer = new TransactionMQProducer("transaction_producer2");

        producer.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876");

        //1,half半消息成功了才能执行本地事务，也需要监听
        //2，半消息的回调要被监听到
        producer.setTransactionListener(new TransactionListener() {
            //本地事务应该干啥事呀？
            //成本的角度来思考
            //三个地方可以传导业务需要的参数
            //1，message 的 body  1，网络带宽，2，污染了body的编码
            //2，userProperty，1，通过网络传递给consumer
            //3，arge方式，local
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                //send ok  half半消息

//                String action1 = new String(msg.getBody());
//                String action = msg.getProperty("action");
                String action = (String)arg;
                String transactionId = msg.getTransactionId();

                System.out.println("transactionID:" + transactionId);
                /**
                 * 状态有2个：
                 * rocketmq的half半消息，这个状态驱动rocketmq 回查producer
                 * service应该是无状态的，那么应该把transactionId  随着本地事务的执行写入事件状态表
                 */
                switch (action){
                    case "0":
                        System.out.println(Thread.currentThread().getName()+"send half：Async api call...action ： 0");
//                        HelloSys.call(ooxx,sdfsdf);
                        return LocalTransactionState.UNKNOW;  //rocketmq 会回调我去check
                    case "1":
                        System.out.println(Thread.currentThread().getName()+"send half：localTransaction faild...action ： 1");
                        /*
                        transaction.begin
                        throw...
                        transaction.rollback
                         */
                        //观察 consumer是消费不到 rollback的message的
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    case "2":
                        System.out.println(Thread.currentThread().getName()+"send half：localTransaction ok...action ： 2");
                        /*
                        transaction.begin
                        throw...
                        transaction.commit..ok
                         */
                        //观察 consumer是肯定消费的到的，只不过，还要验证，中途会不会做check
                        try {
                            Thread.sleep(1000*10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return LocalTransactionState.COMMIT_MESSAGE;
                }


                return null;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                //call back check

                String transactionId = msg.getTransactionId();
                String action = msg.getProperty("action");
                int times = msg.getReconsumeTimes();


                switch (action){
                    case "0" :
                        System.out.println(Thread.currentThread().getName()+"check：action ：0,UNKNOW: "+ times);


                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return LocalTransactionState.COMMIT_MESSAGE;


                    case "1" :
                        System.out.println(Thread.currentThread().getName()+"check：action ：1 ROLLBACK"+ times);
                        //按理来说就是观察事务表
                        return LocalTransactionState.UNKNOW;
                    case "2" :
                        System.out.println(Thread.currentThread().getName()+"check：action ：2 COMMIT"+ times);

                        //check 都是观察事务表的
                        return LocalTransactionState.UNKNOW;
                    //未来你盯着事务表得到结果  COMMIT  ROLLBACK

                    //做了一个长时间的事务：check是要做的
                    //如果produer重启了一下，还能不能响应这个检查

                }


                return null;
            }
        });

        //need thread  ,不是必须要去配置的
        producer.setExecutorService(new ThreadPoolExecutor(
                1,
                Runtime.getRuntime().availableProcessors(),
                2000,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {  //loop worker  -> 消费你提供的queue中的runnable（task）
                        return new Thread(r,"Transaction thread ");
                    }
                }
        ));

        producer.start();

//        for (int i = 0; i < 10; i++) {
//
//            Message msgt =  new Message(
//                    "topic_transaction2",
//                    "TagT",
//                    "key:"+i,
//                    ("message : " + i).getBytes()
//            );
//            msgt.putUserProperty("action",i%3+"");
//
//
//            //发送的是半消息
//            TransactionSendResult res = producer.sendMessageInTransaction(msgt,i%3+"");
//            System.out.println(res);
//        }
        System.in.read();

    }





    //------------example 003 Schedule  |  Delay------------


    /**
     * 主要体现在producer方
     */
    @Test
    public void consumerDelay() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("delayconsumer");
        consumer.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876");
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
        producer.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876");
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




    //--------------example 002------------------------

    /**
     * 保证有序性：
     * 1，producer，其实就是自定义分区器(队列选择器)
     * 2，consumer，MessageListenerOrderly，
     * 感知：
     * 才能实现，生产的时候，有顺序依赖的msg进入一个队列，并且，消费者也会再多线程情况下保证单线程对应的queue能按顺序消费
     * 如果，你只创建一个queue，全局有序
     * @throws Exception
     */
    @Test
    public void orderProducer() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("lllo");
        producer.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876");
        producer.start();


        TransactionMQProducer t = new TransactionMQProducer();
        t.start();


        MyMessageQueueSelector selector = new MyMessageQueueSelector();
        for (int i = 0; i < 10; i++) {

            Message msg = new Message(
                    "order_topic",
                    "TagA",
                    ("message body : " + i + "type: " + i % 3).getBytes()
            );

            //TODO
            SendResult res = producer.send(msg, selector, i % 3);
//            SendResult res = producer.send(msg, new MyMessageQueueSelector(), i % 3);
//            SendResult res = producer.send(msg, new MessageQueueSelector() {   //有没有性能问题？
//                @Override
//                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
//                    Integer index = (Integer) arg;
//                    int target = index % mqs.size();
//                    return mqs.get(target);
////                    MessageQueue target = mqs.get(index);
////                    return target;
////                    /*
//                    不稳定因素
//                    分区器，选择器，路由器，代码逻辑要简单快速
//                     */
////                }
//            }, i % 3);
            System.out.println(res);
        }


    }

    @Test
    public void orderConsumer() throws Exception {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("lllo");
        consumer.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        try {
            consumer.subscribe("order_topic","*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

                msgs.forEach(msge->{
                    System.out.println(Thread.currentThread().getName()+ " : "+new String(msge.getBody()));
                });
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
        System.in.read();
        consumer.shutdown();
    }

    //--------------example 001------------------------


    @Test
    public void producer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("lllo");
        producer.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876");
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
    public void consumer()  {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("xxx6x");
        consumer.setNamesrvAddr("192.168.208.11:9876;192.168.208.12:9876");
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
