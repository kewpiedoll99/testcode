#!/opt/local/bin/bash
ls -lR ~/Documents/backup | grep '\-rw' | awk 'BEGIN { OFS = "\t"; FS = ",[ \t]*|[ \t]+" } { print $6, $7, $8, $9 }'

