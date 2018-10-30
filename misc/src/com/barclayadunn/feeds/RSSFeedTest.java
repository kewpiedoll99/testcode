package com.barclayadunn.feeds;

/**
 * Created by IntelliJ IDEA.
 * User: barclaydunn
 * Date: Jul 6, 2010
 * Time: 11:55:31 AM
 */
public class RSSFeedTest {
    /*
    private static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    private static final String RSS_2_0 = "rss_2.0";
    private static final String ATOM_1_0 = "atom_1.0";

    // sample html tag to show RSS link in browser location bar
    // <link rel="alternate" type="application/rss+xml" title="RSS Feed" href="{THE RSS URL}" />

    // sample html tags to show RSS link in page
    // <a rel="nofollow" href="{THE RSS URL}">
    //     <img height="14" border="0" width="36" align="absmiddle" alt="XML" src="{THE XML GIF DIRECTORY}/xml.gif">
    // </a>

    public static void main(String[] args) {
        boolean ok = false;
//        if (args.length == 2) {
            try {
//                String feedType = args[0];
//                String fileName = args[1];
//                String feedType = RSS_2_0;
                String feedType = ATOM_1_0;
                String fileName = feedType.replace(".", "_").toLowerCase() + ".xml";
                System.out.println("feedType: " + feedType + "; fileName: " + fileName);

                SyndFeed feed = new SyndFeedImpl();
                feed.setFeedType(feedType);

                feed.setTitle("Sample Feed (created with ROME)");
                feed.setLink("http://rome.dev.java.net");
                feed.setDescription("This feed has been created using ROME (Java syndication utilities");

                List<SyndEntry> entries = new ArrayList<SyndEntry>();
                SyndEntry entry;
                SyndContent description;

                entry = new SyndEntryImpl();
                entry.setTitle("ROME v1.0");
                entry.setLink("http://wiki.java.net/bin/view/Javawsxml/Rome01");
                entry.setPublishedDate(DATE_PARSER.parse("2004-06-08"));
                description = new SyndContentImpl();
                description.setType("text/plain");
                description.setValue("Initial release of ROME");
                entry.setDescription(description);
                entries.add(entry);

                entry = new SyndEntryImpl();
                entry.setTitle("ROME v2.0");
                entry.setLink("http://wiki.java.net/bin/view/Javawsxml/Rome02");
                entry.setPublishedDate(DATE_PARSER.parse("2004-06-16"));
                description = new SyndContentImpl();
                description.setType("text/plain");
                description.setValue("Bug fixes, minor API changes and some new features");
                entry.setDescription(description);
                entries.add(entry);

                entry = new SyndEntryImpl();
                entry.setTitle("ROME v3.0");
                entry.setLink("http://wiki.java.net/bin/view/Javawsxml/Rome03");
                entry.setPublishedDate(DATE_PARSER.parse("2004-07-27"));
                description = new SyndContentImpl();
                description.setType("text/html");
                description.setValue("<p>More Bug fixes, mor API changes, some new features and some Unit testing</p>" +
                        "<p>For details check the <a href=\"http://wiki.java.net/bin/view/Javawsxml/RomeChangesLog#RomeV03\">Changes Log</a></p>");
                entry.setDescription(description);
                entries.add(entry);

                feed.setEntries(entries);

                Writer writer = new FileWriter(fileName);
                SyndFeedOutput output = new SyndFeedOutput();
                output.output(feed, writer);
                writer.close();

                System.out.println("The feed has been written to the file [" + fileName + "]");

                ok = true;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: " + ex.getMessage());
            }
//        }

        if (!ok) {
            System.out.println();
            System.out.println("FeedWriter creates a RSS/Atom feed and writes it to a file.");
            System.out.println("The first parameter must be the syndication format for the feed");
            System.out.println("  (rss_0.90, rss_0.91, rss_0.92, rss_0.93, rss_0.94, rss_1.0 rss_2.0 or atom_0.3)");
            System.out.println("The second parameter must be the file name for the feed");
            System.out.println();
        }
    }
    */
}
