package com.barclayadunn.menupages;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * User: barclaydunn
 * Date: Apr 4, 2011
 * Time: 10:33:53 AM
 */
public class TidyTest {

    public static void main(String... args) throws Exception {

        String output = tidyStringOfUrl("http://www.menupages.com/restaurants/text/two%20boots/all-areas/all-neighborhoods/all-cuisines/");
        System.out.println(output);
    }

    public static String tidyStringOfUrl(String addr) throws IOException {

        URL url = new URL(addr);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Tidy tidy = new Tidy(); // obtain a new Tidy instance
        boolean xhtml = false;
        tidy.setXHTML(xhtml); // set desired config options using tidy setters
//        ...                           // (equivalent to command line options)

        Document doc = tidy.parseDOM(url.openStream(), output); // run tidy, providing an input and output stream

        // parse doc with xml parsing?


//        IOUtils.copy(url.openStream(), output);
        return output.toString();
    }
}
