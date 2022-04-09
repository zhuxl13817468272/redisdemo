//package com.zxl.sb.redisdemo.curatordemo.zookeeperConfig;
//
//import org.apache.curator.RetryPolicy;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Slogan 致敬大师，致敬未来的你
// */
//@Configuration
//public class CuratorCfg {
//
//    @Bean(initMethod = "start")  // initMethod初始化对象后，执行的方法。start对应CuratorFramework类内部的start()方法
//    public CuratorFramework curatorFramework(){
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.32.129:2181", retryPolicy);
//        return client;
//    }
//
//}
