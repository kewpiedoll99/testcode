package com.pronto.pig;

import java.io.IOException;
import java.util.Map;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;

/**
 *
 * From https://gist.github.com/mumrah/5754152
 * Simple UDF to allow modifying an existing map[] datum
 * Usage:
 *   FOREACH relation GENERATE AddToMap(m, "foo", "bar");
 * Multiple key-value pairs can be included (similar to the builtin TOMAP)
 */
public class AddToMap extends EvalFunc<Map> {

    @Override
    public Map exec(Tuple tuple) throws IOException {
        // If tuple is null, has fewer than 3 values, or has an even number of values
        if (tuple == null || tuple.size() < 3 || (tuple.size() % 2 == 0)) {
            throw new IOException("Incorrect number of values.");
        }
        try {
            Map<String, Object> map = (Map<String, Object>) tuple.get(0);
            String key;
            Object value;
            for(int i=1; i<tuple.size(); i+=2) {
                key = (String)tuple.get(i);
                value = (Object)tuple.get(i+1);
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
