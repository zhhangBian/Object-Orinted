import java.util.HashMap;

public class DispatchThread extends Thread {
    private final WaitQueue waitQueue;
    private final HashMap<Integer, RequestQueue> requestQueueMap;

    public DispatchThread(WaitQueue waitQueue, HashMap<Integer, RequestQueue> requestQueueMap) {
        this.waitQueue = waitQueue;
        this.requestQueueMap = requestQueueMap;
    }

    @Override
    public void run() {
        while (true) {
            if (this.waitQueue.IfIsEmpty() && this.waitQueue.IfIsEnd()) {
                this.requestQueueMap.forEach((key, value) -> value.SetEnd());
                return;
            }

            Passenger passenger = this.waitQueue.GetOnePassenger();
            if (passenger != null) {
                this.requestQueueMap.get(passenger.GetElevatorId()).AddPassenger(passenger);
            }
        }
    }
}
