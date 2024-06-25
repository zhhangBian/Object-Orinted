package exceptions;

import com.oocourse.spec3.exceptions.TagIdNotFoundException;

import java.util.HashMap;

public class MyTagIdNotFoundException extends TagIdNotFoundException {
    private static int errorCount = 0;
    private static final HashMap<Integer, Integer> idCountMap = new HashMap<>();
    private static String errorInfo = "";

    public MyTagIdNotFoundException(int id) {
        if (errorInfo.isEmpty()) {
            errorCount++;
            int idCount = idCountMap.getOrDefault(id, 0) + 1;
            idCountMap.put(id, idCount);
            errorInfo = ("tinf-" + errorCount + ", " + id + "-" + idCount);
        }
    }

    @Override
    public void print() {
        System.out.println(errorInfo);
        errorInfo = "";
    }
}
