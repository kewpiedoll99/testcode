package com.barclayadunn.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: barclaydunn
 * Date: Jun 7, 2010
 * Time: 11:20:02 AM
 */
public class RegexTest {
    public static void main(String[] args) {
//        String[] test = {"a", "-", "--", "a-", "-a", "a-a", "aa", "--A", "--abc-d", "--abc-d-", "abc-d", "abc-d--------"};
//        String[] test = {"_TAG_AGE_1WEEK","_TAG_AGE_2WEEK","_TAG_AGE_3WEEK","_TAG_AGE_4WEEK","_TAG_fs__"};
//        String[] test = {"/offers","/offers/num"};
        String[] test = {"shipmunk03.s.dfw.rtrdc.net","shipmunk04.s.dfw.rtrdc.net","shipmunk03.p.dfw.rtrdc.net","shipmunk04.p.dfw.rtrdc.net"};
//        String[] test = {"_HN_fountain","_HN_lens","_MB_akai","_STYLE_transparent","_THEME_osu_buckeyes","_MB_zoo_york",
//                "_THEME_mickey_mouse", "fountain", "_mb_fountain", "mickey_mouse"};
//        String regexp = "/site-map(/[a-zA-Z\\-/]*)*";
//        String regexp = "^(.+)-([^-]+)";
//        String regexp = "_*([A-Z][A-Z0-9]*)_([A-Za-z0-9_]+)";
//        String regexp = "_([A-Z][A-Z0-9]*)_([[A-Za-z0-9]*_*]+)";
//        String regexp = "(/[a-z]/)+.*\\b";
//        String regexp = "/offers(/[A-Za-z]+)?";
//        String regexp = "_[A-Z]*_.*";
        String regexp = "^shipmunk0(3|4)(\\.s|\\.p)\\.dfw\\.rtrdc\\.net$";
//        String regexp = ".*";

        Pattern p = Pattern.compile(regexp);
        for (String s : test) {
            System.out.print("s: '" + s + "'");
            Matcher m = p.matcher(s);
            if (m.matches()) {
//                String grp1 = m.group(1);
//                String grp2 = m.group(2);
//                String grp2 = m.group(1);
//                System.out.println("s: '" + s + "'; group 1: '" + grp1 + "'; group 2: '" + grp2 + "'");
//                if (grp2 != null) {
//                    if (grp2.endsWith("/"))
//                        grp2 = grp2.substring(0, grp2.length() - 1);
//                    System.out.println("trimmed=" + grp2);
//                }
                System.out.println(" matches");
            } else {
                System.out.println(" does not match");
            }
        }
    }
}
/*
        String[] test = {"/site-map", "/site-map/", "/site-map/blah",
                "/site-map/blah/", "/site-map/blah/de/blah",
                "/site-map/blah/de/blah/", "/site-map/blah/DE/blah",
                "/site-map/blah/DE/blah/", "/site-mapblah"};
*/
