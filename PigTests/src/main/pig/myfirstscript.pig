define fso `filesysoutput.sh` ship('filesysoutput.sh');

--test = load '/hbase/imagestore' as (name:chararray, type:chararray, size:chararray, replication:chararray, block_size:chararray, modification_time:chararray, permission:chararray, owner:chararray, group:chararray);

--test = LOAD '$files' as (moddate:chararray, modtime:chararray, filename:chararray);

dummy = load '/tmp/dummy.txt' as (f1, f2, f3);
fs -cat /tmp/dummy.txt;

--fs -ls /hbase/imagestore | grep '\-rw' | awk 'BEGIN { OFS = "\t"; FS = "[ \t]*" }{print $6, $7, $8}';

--files = STREAM dummy THROUGH `hadoop fs -ls -R /hbase/imagestore | grep '\-rw' | awk 'BEGIN { OFS="\t"; FS = "[ \t]+" } {print $6, $7, $8}'` AS (moddate:chararray, modtime:chararray, filename:chararray);
--files = STREAM dummy THROUGH `hadoop fs -ls -R / | grep '\\-rw' | awk 'BEGIN { OFS = "\t"; FS = "[ \t]+" }{ print $6, $7, $8 }'` AS (moddate, modtime, filename);
--files = STREAM dummy THROUGH `hadoop fs -ls -R /hbase/imagestore | grep '\-rw'` AS (perms, size, owner, grouper, othersize, moddate, modtime, filename);

--files = STREAM dummy THROUGH `printf "a\tb\tc"` AS (moddate, modtime, filename);

files = STREAM dummy THROUGH `ls -l | grep "\\-rw" | awk 'BEGIN { OFS = "\t"; FS = ",[ \t]*|[ \t]+" } {print $6, $7, $9}'` AS (month:chararray, day:chararray, file:chararray);

--subset = sample test 0.01; 
--oneimg = filter subset by name matches '08.*';
--store oneimg into '/tmp/bdunn/processed3';

--namedate = foreach test generate name, modification_time;

illustrate files;
--illustrate test;
--illustrate subset;
--illustrate oneimg;

--illustrate namedate;


