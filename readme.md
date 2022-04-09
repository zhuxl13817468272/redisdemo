cluster-demo  springboot配置redis集群访问及测试demo
              IndexController -- redis集群测试demo
              SeniorRedisDemo -- 包含redis pipeline、redis cache、Lua script(原生lua脚本)、LuaJ(lua script和java api互调)
curator-demo  springboot配置Zookeeper集群访问及案例
redisson-demo  springboot集成Redisson配置及测试demo
               RedissonDemo  Redisson锁demo
               msg Redis消息推送及订阅demo
sentinel-demo  springboot配置redis sentinel访问及测试demo

附件：《redis高级-消息功能》.pptx  1. 对比redis做消息中间件的优缺点； 2. 对比redis list结构和redis sub/pub做消息中间件的使用场景




ZooKeeper集群需要几台？
    ZK集群机制：超过半数节点才能正常提供服务。
    如果：3个节点的cluster可以挂掉一个节点（leader可以得到2票（超过半数3/2））
          2个节点的cluster就不能挂掉任何一个节点了(leader可以得到2票（超过半数2/2）)

Redis哨兵模式中，需要几台redis,几台sentinel?
    sentinel配置文件中，需要指明当有多少个sentinel认为master失效时（值一般为：sentinel总数/2 + 1），才会从活着的节点中选主。
    如：sentinel monitor mymaster 192.168.0.60 6379 2 #mymsater名字随便取，2表示有2台sentinel认为主失效了，才会从或者的节点中选主。
    形式有：redis一主一从一哨兵  redis高可用（一主一从两哨兵）
    
    Redis哨兵模式中，哨兵对redis的作用：1.监控主；2.选主
                    客户端配置全部哨兵信息，用来避免一台哨兵挂掉的影响。
                    springboot的配置信息如下：
                        server:
                          port: 8080
                        
                        spring:
                          redis:
                            database: 0
                            timeout: 3000
                            sentinel:    #哨兵模式
                              master: mymaster #主服务器所在集群名称
                             nodes: 192.168.0.60:26379,192.168.0.60:26380,192.168.0.60:26381
                           lettuce:
                              pool:
                                max-idle: 50
                                min-idle: 10
                                max-active: 100
                                max-wait: 1000
    