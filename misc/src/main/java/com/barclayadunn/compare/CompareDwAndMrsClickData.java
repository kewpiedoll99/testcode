package com.barclayadunn.compare;

import com.barclayadunn.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

/**
 * User: barclaydunn
 * Date: 8/21/14
 * Time: 4:36 PM
 */
public class CompareDwAndMrsClickData {

    private static Log log = LogFactory.getLog(CompareDwAndMrsClickData.class);

    private static final String endpointUrl = "http://va-mpds-dev-svc101:8080/redirectEventMexpsAndDoubleChecks?date=";
//    private static final String endpointUrl = "http://va-mpds-stage-svc101:8080/redirectEventMexpsAndDoubleChecks?date=";

    public static void main(String [] args) throws Exception {

        String todaysDate = DateUtil.getShortMySqlFormattedDate(new Date());
        String json = stringOfUrl(endpointUrl + todaysDate);

        // Now do the Gson magic.
        Type regularType = new TypeToken<List<RegularClickRow>>(){}.getType();
        Type doubleCheckType = new TypeToken<List<DoubleCheckClickRow>>(){}.getType();

        List<RegularClickRow> regularClickRowList = new Gson().fromJson(json, regularType);
        TreeSet<ClickRow> regularClickRowTreeSet = new TreeSet<ClickRow>(regularClickRowList);

        List<RegularClickRow> doubleCheckClickRowList = new Gson().fromJson(json, doubleCheckType);
        TreeSet<ClickRow> doubleCheckClickRowTreeSet = new TreeSet<ClickRow>(doubleCheckClickRowList);

        Map<String, ClickRow> regularClickRowMap = new HashMap<String, ClickRow>();

        for (ClickRow cr : regularClickRowTreeSet) {
            regularClickRowMap.put(cr.getHour(), cr);
        }

        StringBuilder mainBodyBuilder = new StringBuilder();
        int regularTotal = 0, doubleCheckTotal = 0;
        for (ClickRow cr : doubleCheckClickRowTreeSet) {
            int regularCount = regularClickRowMap.get(cr.getHour()).getCount();
            regularTotal += regularCount;

            int doubleCheckCount = cr.getCount();
            doubleCheckTotal += doubleCheckCount;

            mainBodyBuilder.append(cr.getHour()).append(" | ").append(regularCount).append(" | ").append(doubleCheckCount).append(" | ")
                    .append(regularCount - doubleCheckCount);
        }

        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append(todaysDate + "\n");
        headBuilder.append("Hour | Hive Leads | MEXP Leads | Lead Diff\n");

        headBuilder.append("Total | ").append(regularTotal).append(" | ").append(doubleCheckTotal).append(" | ")
                .append(regularTotal - doubleCheckTotal);

        sendEmail(headBuilder.append(mainBodyBuilder).toString(), todaysDate);
    }

    public static String stringOfUrl(String addr) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        URL url = new URL(addr);
        IOUtils.copy(url.openStream(), output);
        return output.toString();
    }

    private static void sendEmail(String emailMessage, String date) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("merchantsupport@pronto.com", "MEXP Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("barclay.dunn@pronto.com", "B.A. Baracus"));
            msg.setSubject("Clicks: Regular vs Double Check - " + date);
            msg.setText(emailMessage);
            Transport.send(msg);
        } catch (UnsupportedEncodingException uoe) {
            log.error("UnsupportedEncodingException: " + uoe.getMessage());
        } catch (AddressException ae) {
            log.error("AddressException: " + ae.getMessage());
        } catch (MessagingException me) {
            log.error("MessagingException: " + me.getMessage());
        }
    }
}

class RegularClickRow extends ClickRow {}

class DoubleCheckClickRow extends ClickRow {}

