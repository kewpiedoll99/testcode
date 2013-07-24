package classloadertest;

/**
 * Used to demonstrate ClassLoadingException
 * http://stackoverflow.com/questions/16092141/what-else-can-throw-a-classcastexception-in-java#answer-16127891
 *
 * See sp-src/classloadertest.Main for much more detail
 */
public class A {
    private String value;

    public A(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "<A value=\"" + value + "\">";
    }
}
