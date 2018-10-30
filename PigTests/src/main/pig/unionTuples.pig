--avoid java.lang.OutOfMemoryError: Java heap space (execmode: -x local)
set io.sort.mb 10;

A = LOAD '../../../input/tuplesSample.txt' using PigStorage(' ') AS (grupo:chararray, charo:chararray);

--DUMP A;

B = GROUP A by grupo;

--DUMP B;

C = FOREACH B GENERATE $0 as grupo, $1.charo as charos;

--DUMP C;

D = FOREACH C GENERATE FLATTEN($1) as charos;

--D = FOREACH C GENERATE $0 as grupo, FLATTEN($1) as charos;

DUMP D;

E = GROUP D ALL;

DUMP E;

--X = FOREACH A GENERATE t1.t1a,t2.$0;

--DUMP X;
