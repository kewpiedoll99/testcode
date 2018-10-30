--register 'PigTests-1.0-SNAPSHOT.jar';
register '/Users/barclaydunn/.m2/repository/com/pronto/PigTests/1.0-SNAPSHOT/PigTests-1.0-SNAPSHOT.jar';

dummy = load 'dummy.txt' AS (month:chararray, day:chararray, timeoryear:chararray, file:chararray);

files = STREAM dummy THROUGH `localfilesysoutput.sh` AS (month:chararray, day:chararray, timeoryear:chararray, file:chararray);

--oldfiles = filter files by timeoryear matches '201*' and not timeoryear matches '2012' or timeoryear matches '2012' and not month matches 'Jul|Aug|Sep|Oct|Nov|Dec';

youngfiles = filter files by not com.pronto.pig.TooOld();

--store files into 'localout';
--store oldfiles into 'localout';
store youngfiles into 'useUdfOut';
