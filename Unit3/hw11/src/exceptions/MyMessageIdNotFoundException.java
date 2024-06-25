package exceptions;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static int errorCount = 0;
    private static final HashMap<Integer, Integer> idCountMap = new HashMap<>();
    private static String errorInfo = "";

    public MyMessageIdNotFoundException(int id) {
        if (errorInfo.isEmpty()) {
            errorCount++;
            int idCount = idCountMap.getOrDefault(id, 0) + 1;
            idCountMap.put(id, idCount);
            errorInfo = ("minf-" + errorCount + ", " + id + "-" + idCount);
        }
    }

    @Override
    public void print() {
        System.out.println(errorInfo);
        errorInfo = "";
    }
}
