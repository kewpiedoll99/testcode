package com.pronto.pig;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import java.io.IOException;

/**
 * User: barclaydunn
 * Date: 6/27/13
 * Time: 3:27 PM
 */
public class StringToChars extends EvalFunc<Tuple> {

    private TupleFactory tupleFactory = TupleFactory.getInstance();

    @Override
    public Tuple exec(Tuple tuple) throws IOException {
        // If tuple is null
        if (tuple == null) {
            throw new IOException("Null tuple.");
        }
        try {
            String word = (String) tuple.get(0);
            char[] letterArray = word.toCharArray();

            Tuple t = tupleFactory.newTuple(letterArray.length);
            for (int j = 0; j < letterArray.length; j++) {
                t.set(j, Character.toString(letterArray[j]));
            }
            return t;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
