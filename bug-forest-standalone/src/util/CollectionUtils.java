package util;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author dmutti@gmail.com
 */
public class CollectionUtils {

    public static <K,V> List<V> getEvenIndexedValuesFrom(LinkedHashMap<K, V> arg) {
        if (null == arg || arg.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return getIndexedValuesFrom(arg, 0);
    }

    public static <K,V> List<V> getOddIndexedValuesFrom(LinkedHashMap<K, V> arg) {
        if (null == arg || arg.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return getIndexedValuesFrom(arg, 1);
    }

    private static <K,V> List<V> getIndexedValuesFrom(LinkedHashMap<K, V> arg, int initial) {
        if (null == arg || arg.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<V> indexed = new ArrayList<V>();

        for (Entry<K,V> each : arg.entrySet()) {
            initial++;
            if (initial % 2 == 0) {
                continue;
            }
            indexed.add(each.getValue());
        }
        return indexed;
    }
}
