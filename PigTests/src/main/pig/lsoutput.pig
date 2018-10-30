dummy = load '/tmp/dummy.txt';

files = STREAM dummy THROUGH `ls -lR /tmp | grep "\-rw" | awk 'BEGIN { OFS = "\t"; FS = ",[ \t]*|[ \t]+" } {print $6, $7, $9}'` AS (month:chararray, day:chararray, file:chararray);

dump files;

