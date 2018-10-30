package com.barclayadunn.nashorn;

import jdk.nashorn.internal.runtime.Context;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileReader;

/**
 * Created by barclay.dunn on 8/28/15.
 */
public class NashornTest {

    public static void main(String args[]) {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        try {
//            engine.eval("print('Hello, World!');");
            engine.eval(new FileReader("/Users/barclay.dunn/Dropbox/Development/TestCode/misc/helloWorld.js"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
