-- Subject: 	Re: comparing two files using pig
-- Date: 	Thu, 20 Jun 2013 09:07:16 -0500
-- From: 	Jacob Perkins <jacob.a.perkins@gmail.com>
-- Reply-To: 	user@pig.apache.org
-- To: 	user@pig.apache.org

--avoid java.lang.OutOfMemoryError: Java heap space (execmode: -x local)
set io.sort.mb 10;

A = load '../../../input/file1.txt' using PigStorage(' ') as (q:chararray, d:chararray);
B = load '../../../input/file2.txt' using PigStorage(' ') as (q:chararray, d:chararray);

/*
C = cogroup A by q, B by q;
dump C;
*/

/*
(q1,{(q1,d2),(q1,d1)},{(q1,d2),(q1,d1)})
(q2,{(q2,d1),(q2,d3)},{})
(q3,{},{(q3,d3)})
*/

/*
D = foreach C generate $0, COUNT($1), COUNT($2);
DUMP D;
*/

counts = foreach (cogroup A by q, B by q) {
                num_matches = MIN(TOBAG(COUNT(A), COUNT(B)));
                generate
                  group       as q,
                  num_matches as num_matches;
             };
dump counts;
/*
(q1,2)
(q2,0)
(q3,0)
*/
