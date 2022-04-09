package com.zxl.sb.redisdemo.clusterdemo.demo.luaJavaApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

/**
 * @program: monitor
 * @description
 * @author: Zhu Xiaolong
 * @create: 2021-11-29 17:30
 **/
@Slf4j
public class ValueOperator {

    private Jedis jedis;

    public void initDevRedis(){
        if(jedis == null) {
            jedis = new Jedis("10.228.147.163", 6379,6000);
            jedis.auth("QDnAb8zg0");
            jedis.select(1);
        }
    }

    public void initTestRedis(){
        if(jedis == null) {
            jedis = new Jedis("10.205.248.40", 6379,3000);
            jedis.auth("iywOoU6LB8");
        }
    }

    public void initProdRedis(){
        if(jedis == null) {
            jedis = new Jedis("10.233.14.163", 6379,3000);
            jedis.auth("redis163");
        }
    }


    public void destoryRedis(){
        if(jedis != null) {
            jedis.close();
            jedis = null;
        }
    }

    /**
     * 参考： https://blog.csdn.net/linshuhe1/article/details/69581584/    https://github.com/luaj/luaj
     *
     * 取值后判断脚本
     * local value_instance = luajava.newInstance('com.eastmoney.analysiscenter.lua.function.ValueOperator')
     * value_instance:initDevRedis()
     * local lastest_value = value_instance:getZsetLastestValue('1410:status')
     * local thirty_avg_value = value_instance:getZsetThirtyAvgValue('1410:status')
     * value_instance:destoryRedis()
     * local result = luajava.newInstance('com.eastmoney.analysiscenter.lua.entity.LuaResult')
     * if (tonumber(lastest_value) ~= tonumber(thirty_avg_value))
     * then
     * result:alterMsg('0','dbsql报警原因最新一条数据和最近30条数据不相等','1') return result
     * else
     * result:alterMsg('1','','0') return result
     * end
     *
     * JK_DOUBLE_CONFIG_PARSE_FIELDS:1410:status  JK_DOUBLE_CONFIG_PARSE_FIELDS:1459:time
     */
    public String getZsetLastestValue(String key){
        if(!StringUtils.isEmpty(key)) {
            key = "JK_DOUBLE_CONFIG_PARSE_FIELDS:" + key;
            Long zcard = jedis.zcard(key);
            List<String> zrange = jedis.zrange(key, zcard - 1, zcard - 1);
            if(!CollectionUtils.isEmpty(zrange) && zrange.size() > 0) {
                String s = (String) zrange.toArray()[0];
                return s.split("_")[1];
            }
        }
        return new String();
    }

    public String getZsetThirtyAvgValue(String key){
        if(!StringUtils.isEmpty(key)) {
            key = "JK_DOUBLE_CONFIG_PARSE_FIELDS:" + key;
            Long zcard = jedis.zcard(key);
            List<String> zrange = jedis.zrange(key, zcard - 31, zcard - 1);
            if(!CollectionUtils.isEmpty(zrange) && zrange.size() > 0) {
                double avg = zrange.stream().mapToDouble(e -> Double.valueOf(((String) e).split("_")[1])).average().orElse(0D);
                return new DecimalFormat(".##").format(avg);
            }
        }
        return new String();
    }



}
