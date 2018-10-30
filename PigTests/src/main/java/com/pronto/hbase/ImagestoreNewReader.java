package com.pronto.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: barclaydunn
 * Date: 6/13/13
 * Time: 12:42 PM
 *
 * note: do mvn clean, compile, assembly:single to create PigTests-1.0-SNAPSHOT-jar-with-dependencies.jar
 */
public class ImagestoreNewReader {

    public static final String TABLE = "imagestore-new";
    public static final String FAMILY = "0";
    public static final String COLUMN = "";
//    public static final String STARTROW = "00001000715ce3d569ee256153df31db629b";

    public static final String HBASE_CONF_DIR = "/data/rsync/root/casa/conf/switchable/stage/hadoop/hbase-conf";
//    public static final String HBASE_CONF_DIR = "/Users/barclaydunn/Environment/hbase/conf";

    public static SimpleDateFormat outputFmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static void main(String[] args) throws Exception {

//        HBaseConfiguration conf = new HBaseConfiguration();
        Configuration conf = HBaseConfiguration.create();
        conf.addResource(new Path(HBASE_CONF_DIR + "/hbase-site.xml"));
        HTable htable = new HTable(conf, TABLE);
        String filename = "imagestoreNewReaderOutput.txt";

        Scan scan = new Scan();
//        scan.setTimeRange(0, 1371071192905l);

        SimpleTimeZone mdt = new SimpleTimeZone(-7 * 60 * 60 * 1000, TimeZone.getAvailableIDs(-7 * 60 * 60 * 1000)[0]); // mtn time
        Calendar expirationCalendar = new GregorianCalendar(mdt);
        expirationCalendar.setTime(new Date());
        expirationCalendar.add(Calendar.YEAR, -1);
        Long expirationTimestamp = expirationCalendar.getTimeInMillis();
        scan.setTimeRange(0, expirationTimestamp);

//        scan.setStartRow(STARTROW.getBytes());
//        scan.setStopRow(STARTROW.getBytes());

        ResultScanner scanner = htable.getScanner(scan);
        Result r;
        Calendar outputCal = new GregorianCalendar(mdt);
        FileWriter fw = null;
        BufferedWriter bw = null;
        double count = 0;
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            while (((r = scanner.next()) != null)) {
                count++;
                KeyValue keyValue = r.list().get(0);
                long timestamp = keyValue.getTimestamp();
                outputCal.setTimeInMillis(timestamp);
                String humanReadableDate = outputFmt.format(outputCal.getTime());

                byte [] key = r.getRow();
                String keyName = Bytes.toString(key);
                byte [] rValue = r.getValue(Bytes.toBytes(FAMILY), Bytes.toBytes(COLUMN));
                String columnContent = Bytes.toString(rValue);

                String outputLine = "key: " + keyName + ",  column content: " + columnContent + ", timestamp: " + timestamp + " (" + humanReadableDate + ")";
//                System.out.println(outputLine);
                bw.write(outputLine + "\n");

                if ((count % 10000) == 0) {
                    System.out.println("files found: " + count);
                }
            }
        } catch (IOException ioie) {
            System.err.println("ERROR: " + ioie.getMessage());
            return;
        } finally {
            if (bw != null) { bw.close(); }
            if (fw != null) { fw.close(); }
        }
        System.out.println("files found: " + count);

        scanner.close();
        htable.close();
    }
}
