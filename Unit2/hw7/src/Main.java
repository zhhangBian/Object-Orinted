import com.oocourse.elevator3.TimableOutput;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        WaitQueue waitQueue = WaitQueue.getInstance();
        HashMap<Integer, RequestQueue> requestQueueListA = new HashMap<>();
        HashMap<Integer, RequestQueue> requestQueueListB = new HashMap<>();

        for (int i = 1; i <= 6; i++) {
            RequestQueue requestQueueA = new RequestQueue(i, "A");
            RequestQueue requestQueueB = new RequestQueue(i, "B");
            requestQueueListA.put(i, requestQueueA);
            requestQueueListB.put(i, requestQueueB);

            Elevator elevator = new Elevator(i, "A");
            ElevatorThread elevatorThread =
                new ElevatorThread(elevator, requestQueueA, requestQueueB);
            elevatorThread.start();
        }

        InputThread inputThread = new InputThread(waitQueue);
        DispatchThread dispatchThread =
            new DispatchThread(waitQueue, requestQueueListA, requestQueueListB);

        inputThread.start();
        dispatchThread.start();
    }
}
