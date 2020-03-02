package com.barclayadunn.pronto.myFirstMapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * User: barclaydunn
 * Date: 5/8/13
 * Time: 4:11 PM
 *
 * http://blogs.microsoft.co.il/blogs/roadan/archive/2012/06/04/hadoop-on-azure-creating-and-running-a-simple-java-mapreduce.aspx
 */
public class TokenizingMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static final IntWritable one = new IntWritable(1);

    protected void map(LongWritable offset, Text value, Context context)
            throws IOException, InterruptedException {

        StringTokenizer tok = new StringTokenizer(value.toString());
        while (tok.hasMoreTokens()) {
            Text word = new Text(tok.nextToken());
            context.write(word, one);
        }
    }
}