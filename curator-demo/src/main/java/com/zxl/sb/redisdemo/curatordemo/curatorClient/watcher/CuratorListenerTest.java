package com.zxl.sb.redisdemo.curatordemo.curatorClient.watcher;

import com.zxl.sb.redisdemo.curatordemo.curatorClient.CuratorBaseOperations;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.junit.Test;

@Slf4j
public class CuratorListenerTest extends CuratorBaseOperations {

    @Test
    public void testCuratorListener() throws Exception {

        CuratorFramework curatorFramework = getCuratorFramework();
        curatorFramework.getCuratorListenable().addListener((CuratorFramework client, CuratorEvent event)->{
            log.info(" things changed1: event: {},  {} ",event.getType().name(),event);
        });

        String path = "/test";
        createIfNeed(path);
        log.info("start to change data for path :{}" ,path);
        curatorFramework.setData().inBackground().forPath(path,"xxx1".getBytes());
        log.info("start to change data for path :{} again" ,path);
        curatorFramework.setData().inBackground().forPath(path,"xxx2".getBytes());
        curatorFramework.create().inBackground().forPath("/test456","test456".getBytes());
    }




}
