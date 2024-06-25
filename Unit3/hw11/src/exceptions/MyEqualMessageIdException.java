package exceptions;

import com.oocourse.spec3.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static int errorCount = 0;
    private static final HashMap<Integer, Integer> idCountMap = new HashMap<>();
    private static String errorInfo = "";

    public MyEqualMessageIdException(int id) {
        if (errorInfo.isEmpty()) {
            errorCount++;
            int idCount = idCountMap.getOrDefault(id, 0) + 1;
            idCountMap.put(id, idCount);
            errorInfo = ("emi-" + errorCount + ", " + id + "-" + idCount);
        }
    }

    @Override
    public void print() {
        System.out.println(errorInfo);
        errorInfo = "";
    }
}
