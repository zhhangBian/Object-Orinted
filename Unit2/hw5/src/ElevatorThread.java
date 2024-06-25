import java.util.ArrayList;

public class ElevatorThread extends Thread {
    private final Elevator elevator;
    private final RequestQueue requestQueue;

    public ElevatorThread(int id, RequestQueue requestQueue) {
        this.elevator = new Elevator(id);
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        while (true) {
            Status nextStatus;
            synchronized (this.requestQueue) {
                nextStatus = Strategy.GetAdvice(this.elevator, this.requestQueue);
            }
            switch (nextStatus) {
                case END:
                    return;
                case IN:
                    this.InPassenger();
                    break;
                case MOVE:
                    this.QuantMove();
                    break;
                case REVERSE:
                    this.Reverse();
                    break;
                case WAIT:
                    synchronized (this.requestQueue) {
                        try {
                            this.requestQueue.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void QuantMove() {
        long beginTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < beginTime + 400) {
            Status nextStatus;
            synchronized (this.requestQueue) {
                this.WaitLeftTime(beginTime);
                nextStatus = Strategy.GetAdvice(this.elevator, this.requestQueue);
            }
            if (nextStatus == Status.IN) {
                return;
            }
        }

        if (System.currentTimeMillis() - beginTime < 400) {
            this.SleepLeftTime(beginTime);
        }
        this.elevator.Move();
    }

    private void Reverse() {
        this.elevator.Reverse();
    }

    private void InPassenger() {
        long openTime = this.elevator.OpenDoor();

        int nowFloor = elevator.GetNowFloor();
        int direction = elevator.GetDirection();
        this.elevator.OutPassenger();

        Status nextStatus;
        synchronized (this.requestQueue) {
            ArrayList<Passenger> passengersIn = requestQueue.
                    GetPassengerInList(nowFloor, direction, elevator.GetLeftCapacity());
            this.elevator.InPassengerList(passengersIn);

            this.WaitLeftTime(openTime);
            nextStatus = Strategy.GetAdvice(this.elevator, this.requestQueue);
        }
        if (nextStatus == Status.IN) {
            return;
        }

        if (System.currentTimeMillis() - openTime < 400) {
            this.SleepLeftTime(openTime);
        }
        this.elevator.CloseDoor();
    }

    private void WaitLeftTime(long beginTime) {
        try {
            this.requestQueue.wait(400 - (System.currentTimeMillis() - beginTime));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void SleepLeftTime(long beginTime) {
        try {
            sleep(400 - (System.currentTimeMillis() - beginTime));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
