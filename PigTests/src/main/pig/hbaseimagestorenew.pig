register '/tmp/bdunn/PigTests-1.0-SNAPSHOT.jar';

--files = LOAD 'hbase://imagestore-new'
--USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('0:', '-loadKey true -limit 500')
--AS (id:bytearray, thingy:chararray);

--oldimages = filter files by com.pronto.pig.TooOld();

--store oldimages into 'hbaseout';

files = LOAD 'hbase://imagestore-new' USING com.pronto.pig.SomePig AS (id, thingy);

illustrate files;

