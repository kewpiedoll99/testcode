/*
Hi,
I want a fuzzy logic in pig latin language which should match two string for
Example1
I have two words 'Ramesh' and 'Rahim' I want to check how much percentage of the string are equal
Example2
If the two words are 'Ramesh' and 'Ramesh' .then  it should give 100%.
Kindly provide the solution if available.
Thanks
Harshit Bhargava
*/

register '/Users/barclaydunn/Development/TestCode/PigTests/target/PigTests-1.0-SNAPSHOT.jar';

-- Town	Ceremonial county	Status
A = load '../../../input/englishCities.txt' using PigStorage('\t') as (town:chararray, county:chararray, status:chararray);

--dump A;

--B = foreach A generate com.pronto.pig.StringToChars($1); -- as (letters:tuple);
--C = limit B 10;
--dump C;

D = group A by (town, county);

--dump D;

E = limit D 10;
F = foreach E generate com.pronto.pig.LevenshteinDistance($1);
dump F;
