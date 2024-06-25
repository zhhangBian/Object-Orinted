import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class InputThread extends Thread {
    private final WaitQueue waitQueue;

    public InputThread(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();

            if (request == null) {
                this.waitQueue.SetEnd();
                return;
            } else {
                Passenger passenger = new Passenger(request.getPersonId(),
                        request.getFromFloor(), request.getToFloor(), request.getElevatorId());
                this.waitQueue.AddPassenger(passenger);
            }
        }
    }
}