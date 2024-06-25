package exceptions;

import com.oocourse.spec2.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static int errorCount = 0;
    private static final HashMap<Integer, Integer> idCountMap = new HashMap<>();
    private static String errorInfo = "";

    public MyEqualPersonIdException(int id) {
        if (errorInfo.isEmpty()) {
            errorCount++;
            int idCount = idCountMap.getOrDefault(id, 0) + 1;
            idCountMap.put(id, idCount);
            errorInfo = ("epi-" + errorCount + ", " + id + "-" + idCount);
        }
    }

    @Override
    public void print() {
        System.out.println(errorInfo);
        errorInfo = "";
    }
}
