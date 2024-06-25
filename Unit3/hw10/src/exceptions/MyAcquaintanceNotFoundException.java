package exceptions;

import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;

import java.util.HashMap;

public class MyAcquaintanceNotFoundException extends AcquaintanceNotFoundException {
    private static int errorCount = 0;
    private static final HashMap<Integer, Integer> idCountMap = new HashMap<>();
    private static String errorInfo = "";

    public MyAcquaintanceNotFoundException(int id) {
        if (errorInfo.isEmpty()) {
            errorCount++;
            int idCount = idCountMap.getOrDefault(id, 0) + 1;
            idCountMap.put(id, idCount);
            errorInfo = ("anf-" + errorCount + ", " + id + "-" + idCount);
        }
    }

    @Override
    public void print() {
        System.out.println(errorInfo);
        errorInfo = "";
    }
}
