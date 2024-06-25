import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.HashMap;

public class DispatchThread extends Thread {
    private final WaitQueue waitQueue;
    private final HashMap<Integer, RequestQueue> requestQueueMapA;
    private final HashMap<Integer, RequestQueue> requestQueueMapB;

    public DispatchThread(WaitQueue waitQueue,
                          HashMap<Integer, RequestQueue> requestQueueMapA,
                          HashMap<Integer, RequestQueue> requestQueueMapB) {
        this.waitQueue = waitQueue;
        this.requestQueueMapA = requestQueueMapA;
        this.requestQueueMapB = requestQueueMapB;
    }

    @Override
    public void run() {
        while (true) {
            if (this.waitQueue.IfDispatchEnd()) {
                this.requestQueueMapA.forEach((key, value) -> value.SetRequestEnd());
                this.requestQueueMapB.forEach((key, value) -> value.SetRequestEnd());
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
            if (request == null) {
                continue;
            }

            if (request instanceof NormalResetRequest) {
                NormalResetRequest normalResetRequest = (NormalResetRequest) request;
                this.requestQueueMapA.get(normalResetRequest.getElevatorId()).
                    AddRequest(normalResetRequest);
            } else if (request instanceof DoubleCarResetRequest) {
                DoubleCarResetRequest doubleCarResetRequest = (DoubleCarResetRequest) request;
                this.requestQueueMapA.get(doubleCarResetRequest.getElevatorId()).
                    AddRequest(doubleCarResetRequest);
            } else {
                PersonRequest personRequest = (PersonRequest) request;
                int elevatorId = ShadowArea.getInstance().GetShadowEstimateId(personRequest);

                RequestQueue requestQueueA = this.requestQueueMapA.get(elevatorId);
                if (requestQueueA.IfInDoubleMode()) {
                    int exchangeFloor = requestQueueA.GetExchangeFloor();
                    int fromFloor = personRequest.getFromFloor();
                    int toFloor = personRequest.getToFloor();

                    if (fromFloor < exchangeFloor ||
                        (fromFloor == exchangeFloor && toFloor < exchangeFloor)) {
                        requestQueueA.AddRequest(personRequest);
                    } else {
                        this.requestQueueMapB.get(elevatorId).AddRequest(personRequest);
                    }
                } else {
                    requestQueueA.AddRequest(personRequest);
                }
            }
        }
    }
}
