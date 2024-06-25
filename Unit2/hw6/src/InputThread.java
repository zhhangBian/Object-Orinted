import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.Request;

public class InputThread extends Thread {
    private final WaitQueue waitQueue;

    public InputThread(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                this.waitQueue.SetInputEnd();
                return;
            } else {
                this.waitQueue.AddRequest(request);
            }
        }
    }
}
