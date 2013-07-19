package com.pronto.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * User: barclaydunn
 * Date: 6/18/13
 * Time: 4:11 PM
 *
 * starting from code by:
 * User: shashank
 * Date: 10/10/12
 * Time: 11:26 AM
 */
public class HBaseOldImagesList {

    public static final String FAMILY = "0";
    public static final String STARTROW = "00001000715ce3d569ee256153df31db629b";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: HBaseOldImagesList <hbase table to scan> <output path>");
            System.exit(-1);
        }

        String input = args[0];
        String output = args[1];

        Configuration configuration = HBaseConfiguration.create();

        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(FAMILY));

        SimpleTimeZone mdt = new SimpleTimeZone(-7 * 60 * 60 * 1000, TimeZone.getAvailableIDs(-7 * 60 * 60 * 1000)[0]); // mtn time
        Calendar todayLastYear = Calendar.getInstance(mdt);
        todayLastYear.add(Calendar.YEAR, -1);
        try {
            scan.setTimeRange(0l, todayLastYear.getTimeInMillis());

            scan.setStartRow(STARTROW.getBytes());
            scan.setStopRow(STARTROW.getBytes());

            // deal with hadoop's rule that it won't run if the output dir already exists
            if (FileSystem.get(configuration).exists(new Path(output))){
                FileSystem.get(configuration).delete(new Path(output), true);
            }

            Job job = new Job(configuration, HBaseOldImagesList.class.getName());
            job.setJarByClass(HBaseOldImagesList.class);
            job.setJobName("old images");

            FileInputFormat.addInputPath(job, new Path(input));
            FileOutputFormat.setOutputPath(job, new Path(output));

            TableMapReduceUtil.initTableMapperJob(input, scan, HBaseOldImagesListMapper.class, Text.class, NullWritable.class, job);
            job.setNumReduceTasks(0);
            job.setOutputFormatClass(TextOutputFormat.class);
            TextOutputFormat.setOutputPath(job , new Path(output));
            job.waitForCompletion(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
