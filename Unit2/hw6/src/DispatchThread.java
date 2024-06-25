import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.ResetRequest;

import java.util.HashMap;

public class DispatchThread extends Thread {
    private final WaitQueue waitQueue;
    private final HashMap<Integer, RequestQueue> requestQueueMap;

    public DispatchThread(WaitQueue waitQueue, HashMap<Integer,
        RequestQueue> requestQueueMap) {
        this.waitQueue = waitQueue;
        this.requestQueueMap = requestQueueMap;
    }

    @Override
    public void run() {
        while (true) {
            if (this.waitQueue.IfDispatchEnd()) {
                this.requestQueueMap.forEach((key, value) -> value.SetRequestEnd());
                return;
            }

            if (this.waitQueue.IfIsEmpty()) {
                synchronized (this.waitQueue) {
                    try {
                        this.waitQueue.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            Request request = this.waitQueue.GetOneRequest();
            if (request != null) {
                int elevatorId = (request instanceof PersonRequest) ?
                    ShadowArea.getInstance().GetShadowEstimateId((PersonRequest) request) :
                    ((ResetRequest) request).getElevatorId();
                this.requestQueueMap.get(elevatorId).AddRequest(request);
            }
        }
    }
}
