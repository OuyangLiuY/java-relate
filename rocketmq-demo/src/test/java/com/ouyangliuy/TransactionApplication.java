package com.ouyangliuy;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

/**
 * rocketMQ transaction
 */
public class TransactionApplication {



    @Test
    public void producerTransaction() throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer();
        producer.setNamesrvAddr("192.168.134.11:9876;192.168.134.12:9876;192.168.134.13:9876");
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                // 半消息

                return null;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                // check commit/rollback

                return null;
            }
        });
    }
}
