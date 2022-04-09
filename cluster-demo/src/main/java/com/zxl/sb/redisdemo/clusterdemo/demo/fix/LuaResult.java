package com.zxl.sb.redisdemo.clusterdemo.demo.fix;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: monitor
 * @description
 * @author: Zhu Xiaolong
 * @create: 2021-12-20 14:09
 **/
@Data
public class LuaResult implements Serializable {

    /**
     * 是否正常，不正常则报警  0-不正常，报警；1-正常，不报警
     */
    private String isOk;
    /**
     * 报警原因
     */
    private String failCause;

    /**
     * 是否提醒，提醒则发提醒短信  0-不提醒；1-提醒
     */
    private String isWarn;
    /**
     * 提醒原因
     */
    private String warnCause;

    /**
     * 是否发送短信 0-不发短信；1-发送短信
     */
    private String isSendMsg;

    public void alterMsg(String isOk, String failCause) {
        this.isOk = isOk;
        this.failCause = failCause;
    }

    public void alterMsg(String isOk, String failCause, String isSendMsg) {
        this.isOk = isOk;
        this.failCause = failCause;
        this.isSendMsg = isSendMsg;
    }

    public void warnMsg(String isWarn, String warnCause) {
        this.isWarn = isWarn;
        this.warnCause = warnCause;
    }

    public void warnMsg(String isWarn, String warnCause, String isSendMsg) {
        this.isWarn = isWarn;
        this.warnCause = warnCause;
        this.isSendMsg = isSendMsg;
    }


}
