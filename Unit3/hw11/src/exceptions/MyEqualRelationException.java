package exceptions;

import com.oocourse.spec3.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static int errorCount = 0;
    private static final HashMap<Integer, Integer> idCountMap = new HashMap<>();
    private static String errorInfo = "";

    public MyEqualRelationException(int id1, int id2) {
        if (errorInfo.isEmpty()) {
            errorCount++;

            if (id1 == id2) {
                int idCount = idCountMap.getOrDefault(id1, 0) + 1;
                idCountMap.put(id1, idCount);
                errorInfo = ("er-" + errorCount + ", " + id1 + "-" + idCount
                    + ", " + id1 + "-" + idCount);
            } else {
                int idMin = Math.min(id1, id2);
                int idMax = Math.max(id1, id2);

                int idCount1 = idCountMap.getOrDefault(idMin, 0) + 1;
                idCountMap.put(idMin, idCount1);
                int idCount2 = idCountMap.getOrDefault(idMax, 0) + 1;
                idCountMap.put(idMax, idCount2);

                errorInfo = ("er-" + errorCount + ", " + idMin + "-" + idCount1
                    + ", " + idMax + "-" + idCount2);
            }
        }
    }

    @Override
    public void print() {
        System.out.println(errorInfo);
        errorInfo = "";
    }
}
