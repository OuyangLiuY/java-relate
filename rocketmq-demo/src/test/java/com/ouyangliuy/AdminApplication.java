package com.ouyangliuy;

import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.common.protocol.route.TopicRouteData;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;

public class AdminApplication {


    @Test
    public void adminExt() throws Exception {
        DefaultMQAdminExt adminExt = new DefaultMQAdminExt();
        adminExt.setNamesrvAddr("192.168.134.11:9876;192.134.150.12:9876;192.168.134.13:9876");
        adminExt.start();
        ClusterInfo clusterInfo = adminExt.examineBrokerClusterInfo();
        HashMap<String, BrokerData> brokers = clusterInfo.getBrokerAddrTable();
        brokers.forEach((k, v) -> {
            System.out.printf("k : %s => v : %s。 %n", k, v);
        });
        System.out.println("topics |  info");
        TopicList topicList = adminExt.fetchAllTopicList();
        Set<String> topics = topicList.getTopicList();
        topics.forEach(System.out::println);


        System.out.println("topic route | info");
        TopicRouteData wula = adminExt.examineTopicRouteInfo("topic_delay");
        System.out.println(wula);
    }
}
