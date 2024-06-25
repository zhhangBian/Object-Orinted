import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.ResetRequest;

import java.util.ArrayList;

public class ElevatorThread extends Thread {
    private final Elevator elevator;
    private final RequestQueue requestQueue;

    public ElevatorThread(Elevator elevator, RequestQueue requestQueue) {
        this.elevator = elevator;
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
                case RESET:
                    this.Reset();
                    break;
                case OPEN:
                    this.Open();
                    break;
                case MOVE:
                    this.QuantumMove();
                    break;
                case REVERSE:
                    this.Reverse();
                    break;
                case WAIT:
                    this.Wait();
                    break;
                default:
                    break;
            }

            ShadowArea.getInstance().ElevatorLeaveShadow(elevator.GetId(),
                (int) elevator.GetVelocity(), elevator.GetCapacity(),
                elevator.GetNowFloor(), elevator.GetDirection(),
                (elevator.GetCapacity() - elevator.GetLeftCapacity()),
                elevator.ClonePassengerMap(), requestQueue.CloneRequestMap());
        }
    }

    private void Reset() {
        ShadowArea.getInstance().ShadowElevatorSetReset(this.elevator.GetId());
        this.requestQueue.SetReset();

        ArrayList<PersonRequest> resetPassengerList = new ArrayList<>();
        if (this.elevator.HavePassengerInsideNow()) {
            long openTime = this.elevator.OpenDoor();
            resetPassengerList.addAll(this.elevator.ResetPassengerOutList());
            this.SleepLeftTime(openTime, 400);
            this.elevator.CloseDoor();
        }

        resetPassengerList.addAll(this.requestQueue.GetResetWaitingPassengerList());
        long beginTime = this.elevator.ResetBegin();

        WaitQueue.getInstance().AddResetPassengerList(resetPassengerList);
        ResetRequest reset = this.requestQueue.GetResetRequest();
        this.elevator.ResetValue((long) (1000 * reset.getSpeed()), reset.getCapacity());

        this.SleepLeftTime(beginTime, 1200);
        this.elevator.ResetEnd();

        this.requestQueue.FinishReset();
    }

    private void Open() {
        int nowFloor = this.elevator.GetNowFloor();
        int direction = this.elevator.GetDirection();

        long openTime = this.elevator.OpenDoor();
        this.elevator.OutPassenger();

        Status nextStatus;
        synchronized (this.requestQueue) {
            ArrayList<PersonRequest> passengersIn = requestQueue.
                GetPassengerInList(nowFloor, direction, elevator.GetLeftCapacity());
            this.elevator.InPassengerList(passengersIn);

            this.WaitLeftTime(openTime, 400);
            nextStatus = Strategy.GetAdvice(this.elevator, this.requestQueue);
            this.requestQueue.notify();
        }
        if (nextStatus == Status.OPEN) {
            return;
        }

        if (System.currentTimeMillis() < openTime + 400) {
            this.SleepLeftTime(openTime, 400);
        }
        this.elevator.CloseDoor();
    }

    private void QuantumMove() {
        long beginTime = System.currentTimeMillis();
        long velocity = this.elevator.GetVelocity();
        while (System.currentTimeMillis() < beginTime + velocity) {
            Status nextStatus;
            synchronized (this.requestQueue) {
                this.WaitLeftTime(beginTime, velocity);
                nextStatus = Strategy.GetAdvice(this.elevator, this.requestQueue);
                this.requestQueue.notify();
            }
            if (nextStatus == Status.OPEN) {
                return;
            }
        }

        if (System.currentTimeMillis() < beginTime + velocity) {
            this.SleepLeftTime(beginTime, velocity);
        }
        this.elevator.Move();
    }

    private void Reverse() {
        this.elevator.Reverse();
    }

    private void Wait() {
        synchronized (this.requestQueue) {
            try {
                this.requestQueue.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void WaitLeftTime(long beginTime, long velocity) {
        try {
            this.requestQueue.wait(5 + velocity - (System.currentTimeMillis() - beginTime));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void SleepLeftTime(long beginTime, long sleepTime) {
        try {
            sleep(5 + sleepTime - (System.currentTimeMillis() - beginTime));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
