package com.pronto.pig;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * User: barclaydunn
 * Date: 6/10/13
 * Time: 9:22 PM
 *
 * Ported from https://github.com/apache/hbase/blob/trunk/hbase-server/src/main/ruby/hbase/table.rb
 */
public class SomePig {

    private static final String TABLENAME = "imagestore-new";
    public static final String KEY = "00001000715ce3d569ee256153d";

    public static HTable connect(String tableName) {
        Configuration configuration = HBaseConfiguration.create();
        try {
            HBaseAdmin admin = new HBaseAdmin(configuration);
            if (!admin.tableExists(tableName)) {
//                HTableDescriptor [] tableDescriptors = admin.listTables();
                HTable table = new HTable(configuration, tableName);
                return table;
            }
        } catch (MasterNotRunningException e) {
            e.printStackTrace();

        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyValue doGet(String key, HTable table) {
        Get get = new Get(key.getBytes());
        KeyValue keyValue = null;
        try {
            Result result = table.get(get);
            keyValue = result.list().get(0);
//            family = keyValue.getFamily().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyValue;
    }

    public static String getValue(KeyValue keyValue) {
        return Bytes.toStringBinary(keyValue.getValue()); // should be image filename
    }

    public static Long getTimestamp(KeyValue keyValue) {
        return keyValue.getTimestamp();
    }

    /**
     * Fetch the row we previously stored, row1, and convert it back to output
     * @param args asdf
     * @throws Exception
     */
    public static void main(String... args) throws Exception {
        HTable table = connect(TABLENAME);
        KeyValue keyValue = doGet(KEY, table);
        Long timestamp = getTimestamp(keyValue);
        String columnValue = getValue(keyValue);
        System.out.println("Timestamp: " + timestamp + ", Filename: " + columnValue);
    }

    /* *
     * Make a String of the passed kv
     * @param column
     * @param keyValue
     * @param maxLength
     * @return String just made
     * /
    public static String toString(String column, KeyValue keyValue, int maxLength) {
        String val = "";
        if (keyValue.isDelete()) {
            val = "timestamp=#" + keyValue.getTimestamp() + ", type=#" + KeyValue.Type.codeToType(keyValue.getType());
        } else {
            val = "timestamp=#" + keyValue.getTimestamp() + ", type=#" + Bytes.toStringBinary(keyValue.getValue());
        }
        return val;
    }
    */
}
