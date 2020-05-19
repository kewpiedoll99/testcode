package com.pronto.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * User: barclaydunn
 * Date: 6/13/13
 * Time: 12:03 PM
 */
public class BlogReader {

    public static void main(String[] args) throws Exception {

        HBaseConfiguration conf = new HBaseConfiguration();
        HTable htable = new HTable(conf, "Blog");
        String filename = "blogReaderOutput.txt";

        Scan scan = new Scan();
//        scan.setTimeRange(0, 1371071192905l);

        SimpleTimeZone mdt = new SimpleTimeZone(-7 * 60 * 60 * 1000, TimeZone.getAvailableIDs(-7 * 60 * 60 * 1000)[0]); // mtn time
        Calendar expirationCalendar = new GregorianCalendar(mdt);
        expirationCalendar.setTime(new Date());
        expirationCalendar.add(Calendar.YEAR, -1);
        Long expirationTimestamp = expirationCalendar.getTimeInMillis();
        scan.setTimeRange(0, expirationTimestamp);

        ResultScanner scanner = htable.getScanner(scan);
        Result r;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            while (((r = scanner.next()) != null)) {
                KeyValue keyValue = r.list().get(0);
                long timestamp = keyValue.getTimestamp();
                byte [] key = r.getRow();
                String keyName = Bytes.toString(key);
                byte [] pc = r.getValue(Bytes.toBytes("content"), Bytes.toBytes("post"));
                String postContent = Bytes.toString(pc);

                String outputLine = "key: " + keyName + ",  post content: " + postContent + ", timestamp: " + timestamp;
//                System.out.println(outputLine);
                bw.write(outputLine + "\n");
            }
        } catch (IOException ioie) {
            System.err.println("ERROR: " + ioie.getMessage());
            return;
        } finally {
            if (bw != null) { bw.close(); }
            if (fw != null) { fw.close(); }
        }
        scanner.close();
        htable.close();
    }
}

/*
with no filter on scan:
key: John-005,  post content: Mickey mouse., timestamp: 1371071192904
key: Matt-001,  post content: Do elephants like monkeys?, timestamp: 1371071185301
key: Matt-002,  post content: Do monkeys like elephants?, timestamp: 1371071265275

with a timestamp filter on scan max 1371071192905:
key: John-005,  post content: Mickey mouse., timestamp: 1371071192904
key: Matt-001,  post content: Do elephants like monkeys?, timestamp: 1371071185301
*/
