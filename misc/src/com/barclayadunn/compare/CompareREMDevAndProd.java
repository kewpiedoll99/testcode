package com.barclayadunn.compare;

import com.barclayadunn.util.DateUtil;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: barclayadunn
 * Date: 8/22/14
 * Time: 12:03 PM
 *
 * Gets data for yesterday
 */
public class CompareREMDevAndProd {

    private static Log log = LogFactory.getLog(CompareREMDevAndProd.class);

    private static final DateFormat shortMysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    static final String dbDevURI = "jdbc:mysql://va-dev-dbrw101:3306/mexp";
    static final String dbProdURI = "jdbc:mysql://va-dbrw101:3306/mexp";
    static final String dbUser = "rc";
    static final String dbPass = "shop";
    static final String dbName = "mexp";

    static final String sql = "select date_format(timestamp2, '%H') as reportdate, count(*) " +
            "from redirect_event_mexp " +
            "where productsourceid = 15 " +
            "and timestamp2 >= DATE_SUB(?, INTERVAL 1 DAY) and timestamp2 < ? " +
            "group by reportdate";

    static final String sql2 = "select date_format(clickdate, '%H') as reportdate, count(*) " +
            "from report_product_performance " +
            "where clickdate >= DATE_SUB(?, INTERVAL 1 DAY) and clickdate < ? " +
//            "and productsourceid = 15 " +
            "group by reportdate";

    public static void main(String [] args) throws Exception {
        MysqlDataSource devDataSource = new MysqlDataSource();
        devDataSource.setURL(dbDevURI);
        devDataSource.setUser(dbUser);
        devDataSource.setPassword(dbPass);
        devDataSource.setDatabaseName(dbName);

        MysqlDataSource prodDataSource = new MysqlDataSource();
        prodDataSource.setURL(dbProdURI);
        prodDataSource.setUser(dbUser);
        prodDataSource.setPassword(dbPass);
        prodDataSource.setDatabaseName(dbName);

        Date yesterday = DateUtils.addDays(new Date(), -1);
        yesterday = DateUtils.setHours(yesterday, 0);
        yesterday = DateUtils.setMinutes(yesterday, 0);
        yesterday = DateUtils.setSeconds(yesterday, 0);
        yesterday = DateUtils.setMilliseconds(yesterday, 0);
        String yesterdayString = DateUtil.getShortMySqlFormattedDate(yesterday);

        TreeSet<ClickRow> prodClickRowTreeSet = null;
        Map<String, ClickRow> devClickRowMap = null;

        // Connection reference
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // dev contains data from Hive - getting from redirect_event_mexp
            conn = devDataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(yesterday.getTime()));
            stmt.setDate(2, new java.sql.Date(yesterday.getTime()));
            rs = stmt.executeQuery();

            List<ClickRow> devClickRows = new ArrayList<ClickRow>();
            devClickRowMap = new HashMap<String, ClickRow>();
            while (rs.next()) {
                devClickRowMap.put(rs.getString(1), new ClickRow(yesterday, rs.getString(1), rs.getInt(2)));
            }

            // prod contains data from DW - getting from redirect_event_mexp
            conn = prodDataSource.getConnection();
            stmt = conn.prepareStatement(sql2);
            stmt.setDate(1, new java.sql.Date(yesterday.getTime()));
            stmt.setDate(2, new java.sql.Date(yesterday.getTime()));
            rs = stmt.executeQuery();

            prodClickRowTreeSet = new TreeSet<ClickRow>(devClickRows);
            while (rs.next()) {
                prodClickRowTreeSet.add(new ClickRow(yesterday, rs.getString(1), rs.getInt(2)));
            }
        } catch (SQLException e) {
                    System.out.println(e.getMessage());
        } finally {
            // release resources in a finally block in reverse creation order
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { } // ignore
                rs = null;
            }

            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { } // ignore
                stmt = null;
            }

            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { } // ignore
                conn = null;
            }
        }

        StringBuilder mainBodyBuilder = new StringBuilder();
        int prodTotal = 0, devTotal = 0;
        if (prodClickRowTreeSet != null && prodClickRowTreeSet.size() > 0) {
            for (ClickRow prodClickRow : prodClickRowTreeSet) {
                int devCount = 0;
                if (devClickRowMap != null && devClickRowMap.get(prodClickRow.getHour()) != null) {
                    devCount = devClickRowMap.get(prodClickRow.getHour()).getCount();
                    devTotal += devCount;
                }

                int prodCount = prodClickRow.getCount();
                prodTotal += prodCount;

                mainBodyBuilder.append(prodClickRow.getHour()).append(" | ").append(devCount).append(" | ").append(prodCount)
                        .append(" | ").append(prodCount - devCount).append("\n");
            }
        }

        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append(yesterdayString + "\n");
        headBuilder.append("Hour | Dev Leads (Hive) | Prod Leads (DW) | Lead Diff\n");
        headBuilder.append("Total | ").append(devTotal).append(" | ").append(prodTotal).append(" | ")
                .append(prodTotal - devTotal).append("\n");

        sendEmail(headBuilder.append(mainBodyBuilder).toString(), yesterdayString);
        System.out.println(headBuilder.append(mainBodyBuilder).toString());
    }

    private static void sendEmail(String emailMessage, String date) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("merchantsupport@pronto.com", "MEXP Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("barclay.dunn@pronto.com", "B.A. Baracus"));
            msg.setSubject("Clicks: prod vs Double Check - " + date);
            msg.setText(emailMessage);

            Transport tr = session.getTransport("smtp");
            tr.connect("appmail.cluster", "", "");
            tr.sendMessage(msg, msg.getAllRecipients());
            tr.close();
        } catch (UnsupportedEncodingException uoe) {
            log.error("UnsupportedEncodingException: " + uoe.getMessage());
        } catch (AddressException ae) {
            log.error("AddressException: " + ae.getMessage());
        } catch (MessagingException me) {
            log.error("MessagingException: " + me.getMessage());
        }
    }
}
