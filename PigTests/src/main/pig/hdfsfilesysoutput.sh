hadoop fs -ls -R /hbase/imagestore | grep '\-rw' | awk 'BEGIN { OFS = "\t"; FS = ",[ \t]*|[ \t]+" } { print $6, $7, $8, $9 }'
# | tr '\n' ','

