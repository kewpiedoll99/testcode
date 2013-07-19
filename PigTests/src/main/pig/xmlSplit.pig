register /Users/barclaydunn/Development/pig/contrib/piggybank/java/piggybank.jar;

A = load '../../../input/blahblah.xml' using org.apache.pig.piggybank.storage.XMLLoader as (name:chararray, exp:bag{});

dump A;
