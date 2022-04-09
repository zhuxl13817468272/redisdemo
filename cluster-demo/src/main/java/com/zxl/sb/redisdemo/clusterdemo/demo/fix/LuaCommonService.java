package com.zxl.sb.redisdemo.clusterdemo.demo.fix;

import lombok.extern.slf4j.Slf4j;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: monitor
 * @description
 * @author: Zhu Xiaolong
 * @create: 2021-06-09 11:15
 **/
@Service
@Slf4j
public class LuaCommonService {
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 在JVM中执行lua script
     * @param luaScript
     * @return
     */
    public String exeLuaScript(String luaScript) {
        if(!StringUtils.isEmpty(luaScript)){
            Globals globals = JsePlatformFix.standardGlobals();
            LuaValue load = globals.load(luaScript);
            LuaValue call = load.call();
            log.info("function result :{}" ,call.toString());
            return call.toString();
        }
        return new String();

    }


    /**
     * 解析lua结果字符串为LuaResult对象
     * @param luaResultStr
     * @return
     */
    public LuaResult parseLuaResult(String luaResultStr){
        LuaResult luaResult = new LuaResult();

        if(StringUtils.isEmpty(luaResultStr)){
            return luaResult;
        }

        Pattern isOkP = Pattern.compile("isOk=([A-Za-z0-9_])*");
        Matcher isOkM = isOkP.matcher(luaResultStr);
        if(isOkM.find()){
            String isOk = isOkM.group().split("=")[1];
            luaResult.setIsOk(isOk);
        }

        Pattern failCauseP = Pattern.compile("failCause=(\\w*[\\u4e00-\\u9fa5]*\\w*)*");
        Matcher failCauseM = failCauseP.matcher(luaResultStr);
        if(failCauseM.find()){
            String failCause = failCauseM.group().split("=")[1];
            luaResult.setFailCause(failCause);
        }

        Pattern isWarnP = Pattern.compile("isWarn=([A-Za-z0-9_])*");
        Matcher isWarnM = isWarnP.matcher(luaResultStr);
        if(isWarnM.find()){
            String isWarn = isWarnM.group().split("=")[1];
            luaResult.setIsWarn(isWarn);
        }

        Pattern warnCauseP = Pattern.compile("warnCause=(\\w*[\\u4e00-\\u9fa5]*\\w*)*");
        Matcher warnCauseM = warnCauseP.matcher(luaResultStr);
        if(warnCauseM.find()){
            String warnCause = warnCauseM.group().split("=")[1];
            luaResult.setWarnCause(warnCause);
        }

        Pattern isSendMsgP = Pattern.compile("isSendMsg=([A-Za-z0-9_])*");
        Matcher isSendMsgM = isSendMsgP.matcher(luaResultStr);
        if(isSendMsgM.find()){
            String isSendMsg = isSendMsgM.group().split("=")[1];
            luaResult.setIsSendMsg(isSendMsg);
        }

        log.info("parseLuaResult :{}",luaResult);
        return luaResult;
    }


}
