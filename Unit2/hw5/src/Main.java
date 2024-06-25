import com.oocourse.elevator1.TimableOutput;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        WaitQueue waitQueue = new WaitQueue();
        HashMap<Integer, RequestQueue> requestQueueList = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            RequestQueue requestQueue = new RequestQueue();
            requestQueueList.put(i, requestQueue);
            ElevatorThread elevatorThread = new ElevatorThread(i, requestQueue);
            elevatorThread.start();
        }

        InputThread inputThread = new InputThread(waitQueue);
        DispatchThread dispatchThread = new DispatchThread(waitQueue, requestQueueList);

        inputThread.start();
        dispatchThread.start();
    }
}