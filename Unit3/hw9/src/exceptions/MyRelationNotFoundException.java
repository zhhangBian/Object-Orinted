package exceptions;

import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int errorCount = 0;
    private static final HashMap<Integer, Integer> idCountMap = new HashMap<>();
    private static String errorInfo = "";

    public MyRelationNotFoundException(int id1, int id2) {
        if (errorInfo.isEmpty()) {
            errorCount++;

            int idMin = Math.min(id1, id2);
            int idMax = Math.max(id1, id2);

            int idCount1 = idCountMap.getOrDefault(idMin, 0) + 1;
            idCountMap.put(idMin, idCount1);
            int idCount2 = idCountMap.getOrDefault(idMax, 0) + 1;
            idCountMap.put(idMax, idCount2);

            errorInfo = ("rnf-" + errorCount + ", " + idMin + "-" + idCount1
                + ", " + idMax + "-" + idCount2);
        }
    }

    @Override
    public void print() {
        System.out.println(errorInfo);
        errorInfo = "";
    }
}
