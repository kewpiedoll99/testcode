package classloadertest;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class, sp-src/classloadertest.Main, is used to demonstrate ClassCastException
 * (see http://stackoverflow.com/questions/16092141/what-else-can-throw-a-classcastexception-in-java#answer-16127891 )
 * needs to be run separately from src/classloadertest.A and src/classloadertest.MyRunnable
 * these are in two different source dirs, jar'd into two different jars, as in the following:

[~/Development/TestCode/src]------------------------------------------------------[Wed Apr 24 10:21:52 EDT 2013]
$ javac classloadertest/MyRunnable.java
[~/Development/TestCode/src]------------------------------------------------------[Wed Apr 24 10:37:33 EDT 2013]
$ jar cvf ../a.jar classloadertest/A.class classloadertest/MyRunnable.class
added manifest
adding: classloadertest/A.class(in = 511) (out= 328)(deflated 35%)
adding: classloadertest/MyRunnable.class(in = 1377) (out= 657)(deflated 52%)
[~/Development/TestCode/src]------------------------------------------------------[Wed Apr 24 10:37:40 EDT 2013]
$ cd ../sp-src/
[~/Development/TestCode/sp-src]---------------------------------------------------[Wed Apr 24 10:38:03 EDT 2013]
$ javac classloadertest/Main.java
[~/Development/TestCode/sp-src]---------------------------------------------------[Wed Apr 24 10:38:09 EDT 2013]
$ jar cvf ../main.jar classloadertest/Main.class
added manifest
adding: classloadertest/Main.class(in = 1369) (out= 699)(deflated 48%)
[~/Development/TestCode/sp-src]---------------------------------------------------[Wed Apr 24 10:38:14 EDT 2013]
$ cd ..
[~/Development/TestCode]----------------------------------------------------------[Wed Apr 24 10:38:19 EDT 2013]
$ java -cp main.jar classloadertest.Main
Run 1: running
Run 1: cache["key"] = <A value="1">
Run 2: running
Exception in thread "main" java.lang.ClassCastException: classloadertest.A cannot be cast to classloadertest.A
at classloadertest.MyRunnable.run(MyRunnable.java:27)
at classloadertest.Main.run(Main.java:20)
at classloadertest.Main.main(Main.java:28)

 *
 */
public class Main {
    public static void run(String name, ConcurrentHashMap<String, Object> cache) throws Exception {
        // Create a classloader using a.jar as the classpath.
        URLClassLoader classloader = URLClassLoader.newInstance(new URL[] { new File("a.jar").toURI().toURL() });

        // Instantiate MyRunnable from within a.jar and call its run() method.
        Class<?> c = classloader.loadClass("classloadertest.MyRunnable");
        Runnable r = (Runnable)c.getConstructor(String.class, ConcurrentHashMap.class).newInstance(name, cache);
        r.run();
    }

    public static void main(String[] args) throws Exception {
        // Create a shared cache.
        ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

        // each of these run's creates its own classloader and loads classloadertest.MyRunnable via that;
        // MyRunnable uses putIfAbsent to save an instance of class A under key "key";
        // then it retrieves what's in the cache and casts it as class A
        run("1", cache);
        // so when this thread tries to save its instance of class A under key "key" it cannot.
        // trying to retrieve and cast its class, it then generates the ClassCastException as intended.
        run("2", cache);
    }
}
