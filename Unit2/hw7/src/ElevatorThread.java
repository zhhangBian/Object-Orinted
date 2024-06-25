import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ResetRequest;

import java.util.ArrayList;

public class ElevatorThread extends Thread {
    private final Elevator elevator;
    private final RequestQueue requestQueue;
    private final RequestQueue otherRequestQueue;
    private final Object exchangeLock;

    public ElevatorThread(Elevator elevator,
                          RequestQueue requestQueue,
                          RequestQueue otherRequestQueue) {
        this.elevator = elevator;
        this.requestQueue = requestQueue;
        this.otherRequestQueue = otherRequestQueue;
        this.exchangeLock = new Object();
    }

    public ElevatorThread(Elevator elevator,
                          RequestQueue requestQueue,
                          RequestQueue otherRequestQueue,
                          Object exchangeLock) {
        this.elevator = elevator;
        this.requestQueue = requestQueue;
        this.otherRequestQueue = otherRequestQueue;
        this.exchangeLock = exchangeLock;
    }

    @Override
    public void run() {
        while (true) {
            Status nextStatus = Strategy.GetAdvice(
                this.elevator, this.requestQueue, this.otherRequestQueue);
            switch (nextStatus) {
                case END:
                    return;
                case RESET:
                    this.Reset();
                    break;
                case EXCHANGE:
                    this.Exchange();
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

            ShadowArea.getInstance().ElevatorLeaveShadow(
                this.elevator.GetId(),
                this.elevator.GetType(),
                this.elevator.GetNowFloor(),
                this.elevator.GetDirection(),
                this.elevator.GetPassengerNum(),
                this.elevator.ClonePassengerMap(),
                this.requestQueue.CloneRequestMap());
        }
    }

    private void Reset() {
        ResetRequest resetRequest = this.requestQueue.GetResetRequest();
        if (resetRequest instanceof DoubleCarResetRequest) {
            DoubleCarResetRequest request = (DoubleCarResetRequest) resetRequest;
            this.requestQueue.SetDoubleMode(request.getTransferFloor());
            this.otherRequestQueue.SetDoubleMode(request.getTransferFloor());
            ShadowArea.getInstance().ShadowElevatorDoubleReset(
                request.getElevatorId(),
                (int) (request.getSpeed() / 100),
                request.getCapacity(),
                request.getTransferFloor()
            );
        } else {
            NormalResetRequest request = (NormalResetRequest) resetRequest;
            ShadowArea.getInstance().ShadowElevatorNormalReset(
                request.getElevatorId(),
                (int) (request.getSpeed() / 100),
                request.getCapacity()
            );
        }

        this.requestQueue.SetResetBegin();
        this.otherRequestQueue.SetResetBegin();

        ArrayList<PersonRequest> resetPassengerList = new ArrayList<>();
        if (this.elevator.HavePassengerInsideNow()) {
            long openTime = this.elevator.OpenDoor();
            resetPassengerList.addAll(this.elevator.GetResetPassengerOutList());
            this.SleepLeftTime(openTime, 400);
            this.elevator.CloseDoor();
        }

        resetPassengerList.addAll(this.requestQueue.GetResetWaitingPassengerList());

        long resetTime = this.elevator.ResetBegin();
        WaitQueue.getInstance().AddResetPassengerList(resetPassengerList);

        if (resetRequest instanceof DoubleCarResetRequest) {
            this.DoubleCarReset((DoubleCarResetRequest) resetRequest);
        } else if (resetRequest instanceof NormalResetRequest) {
            this.NormalReset((NormalResetRequest) resetRequest);
        }

        this.SleepLeftTime(resetTime, 1200);

        this.elevator.ResetEnd();
        this.requestQueue.SetResetEnd();
        this.otherRequestQueue.SetResetEnd();
        this.requestQueue.AddBufferPassengers();
        this.otherRequestQueue.AddBufferPassengers();
    }

    private void DoubleCarReset(DoubleCarResetRequest resetRequest) {
        this.elevator.ResetDoubleMode((long) (1000 * resetRequest.getSpeed()),
            resetRequest.getCapacity(), resetRequest.getTransferFloor());

        Elevator elevatorDouble = this.elevator.GetDoubleElevator(resetRequest.getTransferFloor());
        ElevatorThread elevatorThreadDouble = new ElevatorThread(elevatorDouble,
            this.otherRequestQueue, this.requestQueue, this.exchangeLock);
        elevatorThreadDouble.start();
    }

    private void NormalReset(NormalResetRequest resetRequest) {
        this.elevator.ResetValue(
            (long) (1000 * resetRequest.getSpeed()), resetRequest.getCapacity());
    }

    private void Exchange() {
        synchronized (this.exchangeLock) {
            try {
                sleep(this.elevator.GetVelocity());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.elevator.Move();
            this.elevator.Reverse();

            this.elevator.OpenDoor();

            String type = this.elevator.GetType();
            int nowFloor = this.elevator.GetNowFloor();
            int direction = this.elevator.GetDirection();

            ArrayList<PersonRequest> exchangePassengerList =
                this.elevator.GetExchangePassengerOutList();
            this.otherRequestQueue.AddExchangePassengerList(exchangePassengerList);

            ArrayList<PersonRequest> passengersIn = this.requestQueue.
                GetPassengerInList(type, nowFloor, direction, this.elevator.GetLeftCapacity());
            this.elevator.InPassengerList(passengersIn);

            try {
                sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.elevator.CloseDoor();

            try {
                sleep(this.elevator.GetVelocity());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.elevator.Move();
            this.exchangeLock.notify();
        }
    }

    private void Open() {
        String type = this.elevator.GetType();
        int nowFloor = this.elevator.GetNowFloor();
        int direction = this.elevator.GetDirection();

        long openTime = this.elevator.OpenDoor();
        this.elevator.OutPassenger();

        Status nextStatus;
        synchronized (this.requestQueue) {
            ArrayList<PersonRequest> passengersIn = this.requestQueue.
                GetPassengerInList(type, nowFloor, direction, this.elevator.GetLeftCapacity());
            this.elevator.InPassengerList(passengersIn);

            this.WaitLeftTime(openTime, 400);
            nextStatus = Strategy.GetAdvice(
                this.elevator, this.requestQueue, this.otherRequestQueue);
            this.requestQueue.notify();
        }
        if (nextStatus == Status.OPEN) {
            return;
        }

        this.SleepLeftTime(openTime, 400);
        this.elevator.CloseDoor();
    }

    private void QuantumMove() {
        long beginTime = System.currentTimeMillis();
        long velocity = this.elevator.GetVelocity();
        while (System.currentTimeMillis() < beginTime + velocity) {
            Status nextStatus;
            synchronized (this.requestQueue) {
                this.WaitLeftTime(beginTime, velocity);
                nextStatus = Strategy.GetAdvice(
                    this.elevator, this.requestQueue, this.otherRequestQueue);
                this.requestQueue.notify();
            }
            if (nextStatus == Status.OPEN) {
                return;
            }
        }

        this.SleepLeftTime(beginTime, velocity);
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

    private void WaitLeftTime(long beginTime, long waitTime) {
        if (System.currentTimeMillis() < beginTime + waitTime) {
            synchronized (this.requestQueue) {
                try {
                    this.requestQueue.wait(waitTime - (System.currentTimeMillis() - beginTime));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void SleepLeftTime(long beginTime, long sleepTime) {
        if (System.currentTimeMillis() < beginTime + sleepTime) {
            try {
                sleep(5 + sleepTime - (System.currentTimeMillis() - beginTime));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
