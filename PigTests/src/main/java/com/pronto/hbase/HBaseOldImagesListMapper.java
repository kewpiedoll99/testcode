package com.pronto.hbase;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * User: barclaydunn
 * Date: 6/18/13
 * Time: 3:37 PM
 *
 * starting from code by:
 * User: spatel
 * Date: 10/11/12
 * Time: 3:20 PM
 */
public class HBaseOldImagesListMapper extends TableMapper<Text , NullWritable>{

    public static final String FAMILY = "0";
    public static final String COLUMN = "";

    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        String imageKey = Bytes.toString(key.get());
        String imageValue = Bytes.toString(value.getValue(Bytes.toBytes(FAMILY), Bytes.toBytes(COLUMN)));

        String imagePlatform = imageKey.substring(0,2) + "/" +  imageKey.substring(2,4) + "/" + imageKey.substring(4,32) + "_" + imageValue.substring(10)  +".jpg";
        String mysqltable = "product_images_" + imageKey.substring(0,1);
        String mysqlvalue = imageKey.substring(4,32);

        context.write(new Text(imagePlatform + "|" + mysqltable + "|" + mysqlvalue), null);
    }
}
