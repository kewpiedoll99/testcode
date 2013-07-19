set io.sort.mb 10;         -- avoid java.lang.OutOfMemoryError: Java heap space (execmode: -x local)

A = LOAD '../../../input/file1.txt' using PigStorage(' ') as (aa:chararray, ab:chararray);
B = LOAD '../../../input/file2.txt' using PigStorage(' ') as (ba:chararray, bb:chararray);

C = UNION A, B;
D = COGROUP C by ($0, $1);

F = FOREACH D GENERATE FLATTEN($0), COUNT($1);

G0 = FILTER F BY $2 > 1;   -- any that match
G1 = FILTER F BY $2 < 2;   -- any that don't match

H0 = GROUP G0 BY $0;
H1 = GROUP G1 BY $0;


J0 = FOREACH H0 GENERATE $0, COUNT($1);
J1 = FOREACH H1 GENERATE $0, 0;

K = UNION J0, J1;

DUMP K;
/*
(q2,0)
(q3,0)
(q1,2)
*/




--DUMP A;
--DUMP B;
--DUMP C;
/*
(q1,d1)
(q1,d2)
(q2,d3)
(q2,d1)
(q1,d1)
(q1,d2)
(q3,d3)
*/
--DUMP D;
/*
((q1,d1),{(q1,d1),(q1,d1)})
((q1,d2),{(q1,d2),(q1,d2)})
((q2,d1),{(q2,d1)})
((q2,d3),{(q2,d3)})
((q3,d3),{(q3,d3)})
*/
--DUMP F;
/*
(q1,d1,2)
(q1,d2,2)
(q2,d1,1)
(q2,d3,1)
(q3,d3,1)
*/
--DUMP G0;
/*
(q1,d1,2)
(q1,d2,2)
*/
--DUMP G1;
/*
(q2,d1,1)
(q2,d3,1)
(q3,d3,1)
OR (with addl row in file2.txt)
(q2,d1,1)
(q2,d3,1)
(q2,d4,1)
(q3,d3,1)
*/
--DUMP H0;
/*
(q1,{(q1,d2,2),(q1,d1,2)})
*/






--EX = FOREACH D GENERATE FLATTEN($0), FLATTEN($1);
--DUMP EX;
/*
(q1,d1,q1,d1)
(q1,d1,q1,d1)
(q1,d2,q1,d2)
(q1,d2,q1,d2)
(q2,d1,q2,d1)
(q2,d3,q2,d3)
(q3,d3,q3,d3)
*/
--FX = FOREACH D GENERATE $0, COUNT($1);
--            ^^^
--DUMP FX;
/*
((q1,d1),2)
((q1,d2),2)
((q2,d1),1)
((q2,d3),1)
((q3,d3),1)
*/
--GX = FILTER FX BY $1 > 1;
--DUMP GX;
/*
((q1,d1),2)
((q1,d2),2)
*/
