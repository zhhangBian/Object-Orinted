import com.oocourse.elevator2.TimableOutput;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        WaitQueue waitQueue = WaitQueue.getInstance();
        HashMap<Integer, RequestQueue> requestQueueList = new HashMap<>();

        for (int i = 1; i <= 6; i++) {
            RequestQueue requestQueue = new RequestQueue(i);
            requestQueueList.put(i, requestQueue);

            Elevator elevator = new Elevator(i);
            ElevatorThread elevatorThread = new ElevatorThread(elevator, requestQueue);
            elevatorThread.start();
        }

        InputThread inputThread = new InputThread(waitQueue);
        DispatchThread dispatchThread =
            new DispatchThread(waitQueue, requestQueueList);

        inputThread.start();
        dispatchThread.start();
    }
}
