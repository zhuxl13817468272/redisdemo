package com.zxl.sb.redisdemo.clusterdemo.demo.fix;

import org.luaj.vm2.lib.jse.LuajavaLib;

/**
 * @program: monitor
 * @description  修复源码中问题
 * @author: Zhu Xiaolong
 * @create: 2021-12-29 19:54
 **/
public class LuajavaLibFix extends LuajavaLib {

    public LuajavaLibFix() {
    }


    // load classes using app loader to allow luaj to be used as an extension
    @Override
    protected Class classForName(String name) throws ClassNotFoundException {
        return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
    }

}
