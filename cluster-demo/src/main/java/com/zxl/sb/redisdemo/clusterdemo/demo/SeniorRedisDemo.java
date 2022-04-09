package com.zxl.sb.redisdemo.clusterdemo.demo;

import lombok.extern.slf4j.Slf4j;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.function.*;

@RestController
@Slf4j
public class SeniorRedisDemo {
    @Autowired
    private RedisTemplate redisTemplate;

    //@todo redis消息订阅： https://www.zhihu.com/question/20795043   https://www.cnblogs.com/telwanggs/p/12325395.html




    /**
     * 测试Redis管道Pipelined，特性：如果其中有插入错误，不影响前后的插入
     * @return  [ 1, true, 2, true, 3, true, 4, true, 5, true, 6, true, 7, true, 8, true, 9, true ]
     */
    @GetMapping(value = "testRedisPipelined")
    public List<Object> testRedisPipelined(){

        List list = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for (int i = 0; i < 9; i++) {
                    redisConnection.incr("pipelinekey".getBytes());
                    redisConnection.set(("zxl" + i).getBytes(), "zxl".getBytes());
                }
                return null;
            }
        });

        return list;
    }

    /**
     * 课题：java 使用lua脚本，拓展redis的灵活性：
     * 1. 支持原生lua script执行。测试Redis的Lua script,特性：原子性，同时成功或同时失败（exeOriginalLuaToRedis）
     * 2. 支持luaj。lua脚本中调用java api，并在java代码中执行lua脚本（testExeLuaScript）。其中被调用的java api为luaJavaApi/ValueOperator类中方法（查询redis zset数据并对比）
     * @return
     *
     *      参考文档：https://vimsky.com/examples/detail/java-class-org.springframework.data.redis.core.script.DefaultRedisScript.html 最后两个例子，学习大的使用形式
     *               https://blog.csdn.net/liubenlong007/article/details/53816087
     *              Redis使用Lua脚本保证zset的删除和插入的原子性   https://blog.csdn.net/riemann_/article/details/109684365
     *              lua实现split的简易方法   https://blog.51cto.com/zhaiku/1163077
     *
     */
    @GetMapping(value = "exeOriginalLuaToRedis")
    public String exeOriginalLuaToRedis(){
        redisTemplate.opsForValue().set("product_stock_10016",15); // value值必须为数值型
        String script = "   local count = redis.call( 'get' , KEYS[1] )    " +
                        "   local a = tonumber(count)   " +
                        "   local b = tonumber(ARGV[1]) " +
                        "   local c = tonumber(ARGV[2]) " +
                        "   if a >= b + c then  " +
                        "   redis.call( 'set' , KEYS[1], a-b-c) " +
//                        "   bb == 0 " +  //语法错误
                        "   return 1    " +
                        "   end " +
                        "   return 0    ";
        //最好指定ReturnType为Object.class，具体返回值类型看结果决定输出类型
        DefaultRedisScript<Object> redisScript = new DefaultRedisScript<>(script, Object.class);
        //execute 为 ArrayList
        Object execute = redisTemplate.execute(redisScript, Arrays.asList("product_stock_10016"), 11,3);  //参数必须为数值型
        System.out.println("========================================result:" + execute);

        System.out.println("=====================================================Lua 执行结果：" + ((ArrayList) execute).get(0));
        if((Long)((ArrayList) execute).get(0) == 1){
            Object rest = redisTemplate.opsForValue().get("product_stock_10016");
            System.out.println("================================================product_stock_10016库存：" + String.valueOf(rest));
            return String.valueOf(rest);
        }

        return "-1";

    }



    /**
     * 课题：java 使用lua脚本，拓展redis的灵活性：
     * 1. 支持原生lua script执行。测试Redis的Lua script,特性：原子性，同时成功或同时失败（exeOriginalLuaToRedis）
     * 2. 支持luaj。lua脚本中调用java api，并在java代码中执行lua脚本（testExeLuaScript）。其中被调用的java api为luaJavaApi/ValueOperator类中方法（查询redis zset数据并对比）。
     * @return
     *
     * 参考： https://blog.csdn.net/linshuhe1/article/details/69581584/
     *        https://github.com/luaj/luaj
     *        https://www.runoob.com/lua/lua-miscellaneous-operator.html
     *
     *      LuaJ官网： http://www.luaj.org   http://luaj.org/luaj/3.0/api/index.html    http://luaj.org/luaj/3.0/api/org/luaj/vm2/LuaValue.html
     *      Java调用lua脚本-LuaJ:  https://www.jianshu.com/p/cfac7ff0d8d7
     *      Luaj学习笔记(一) - 快速入门小Demo:  https://blog.csdn.net/lgj123xj/article/details/81592978
     *      Java与Lua互相调用:  https://blog.csdn.net/linshuhe1/article/details/69581584/
     *
     * @param luaScript  脚本在luaJavaApi/ValueOperator类中getZsetLastestValue方法的注解上（查询redis zset数据并比对）
     *
     * 后记，这种写法只能再本地idea中运行，一旦在正式线上tomcat运行时会报class not found的错误，修复方法建fix目录。
     */
    @GetMapping(value = "testExeLuaScript")
    public void testExeLuaScript(String luaScript){
        Globals globals = JsePlatform.standardGlobals();
        LuaValue load = globals.load(luaScript);
        LuaValue call = load.call();
        System.out.println("lua result ------------------" + call);
    }


    /**
     * 缓存测试方法延时两秒
     *      Redis中 key为：cache_test::genus_expire_66 value: \xAC\xED\x00\x05t\x00\x09success66
     * @param i
     * @return
     */
    @Cacheable(value = "cache_test",key = "'genus_expire_'+#i")

    @GetMapping(value = "cacheFunction")
    public String cacheFunction(String i){
        System.out.println("参数i = " + i);
        try {
            long time = 2000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        return "success"+ i;
    }


    public static void main(String[] args) {

    }

}
