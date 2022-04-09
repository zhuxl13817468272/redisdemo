package com.zxl.sb.redisdemo.clusterdemo.demo.fix;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

/**
 * @program: monitor
 * @description  修复源码中问题
 * @author: Zhu Xiaolong
 * @create: 2021-12-29 20:05
 **/
public class JsePlatformFix extends JsePlatform{

    /**
     * Create a standard set of globals for JSE including all the libraries.
     *
     * @return Table of globals initialized with the standard JSE libraries
     */
    public static Globals standardGlobals() {
        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new CoroutineLib());
        globals.load(new JseMathLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
//        globals.load(new LuajavaLib());
        globals.load(new LuajavaLibFix());
        LoadState.install(globals);
        LuaC.install(globals);
        return globals;
    }

}
