import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ShadowElevator {
    private int velocity;
    private int capacity;
    private int nowFloor;
    private int direction;
    private int passengerNum;
    private HashMap<Integer, ArrayList<PersonRequest>> passengerMap;
    private HashMap<Integer, ArrayList<Request>> requestMap;
    private int time;
    private int count;

    public ShadowElevator() {
        this.velocity = 400;
        this.capacity = 6;
        this.nowFloor = 1;
        this.direction = 1;
        this.passengerNum = 0;
        this.passengerMap = new HashMap<>();
        this.requestMap = new HashMap<>();
        this.time = 0;
        this.count = 0;

        for (int i = 0; i < 12; i++) {
            this.passengerMap.put(i, new ArrayList<>());
            this.requestMap.put(i, new ArrayList<>());
        }
    }

    public void LeaveShadow(
        int velocity, int capacity, int nowFloor, int direction, int passengerNum,
        HashMap<Integer, ArrayList<PersonRequest>> passengerMap,
        HashMap<Integer, ArrayList<Request>> requestMap) {
        this.velocity = velocity;
        this.capacity = capacity;
        this.nowFloor = nowFloor;
        this.direction = direction;
        this.passengerNum = passengerNum;
        this.passengerMap = passengerMap;
        this.requestMap = requestMap;
        this.time = 0;
        this.count = 0;
    }

    public void SetReset() {
        this.time += 1200;
    }

    public void AddPassengerToRequestQueue(PersonRequest passenger) {
        this.requestMap.get(passenger.getFromFloor()).add(passenger);
    }

    public int GetEstimatePerformance(PersonRequest personRequest) {
        this.requestMap.get(personRequest.getFromFloor()).add(personRequest);
        while (true) {
            Status nextStatus = GetAdvice();
            if (nextStatus == Status.END) {
                break;
            } else if (nextStatus == Status.RESET) {
                break;
            } else if (nextStatus == Status.OPEN) {
                this.Open();
            } else if (nextStatus == Status.MOVE) {
                this.Move();
            } else if (nextStatus == Status.REVERSE) {
                this.Reverse();
            } else {
                break;
            }
        }
        return this.time / 100 * this.count;
    }

    private void Open() {
        this.time += 400;
        this.count += 2;
        this.OutPassenger();
        this.InPassenger();
    }

    private void OutPassenger() {
        ArrayList<PersonRequest> passengersOutThisFloor = this.passengerMap.get(this.nowFloor);
        this.passengerNum -= passengersOutThisFloor.size();
        passengersOutThisFloor.clear();
    }

    private void InPassenger() {
        ArrayList<Request> passengersWantInNow = this.requestMap.get(this.nowFloor);

        Iterator<Request> iterator = passengersWantInNow.iterator();
        while (iterator.hasNext() && this.passengerNum < this.capacity) {
            PersonRequest passenger = (PersonRequest) iterator.next();
            if ((passenger.getToFloor() - this.nowFloor) * this.direction > 0) {
                this.passengerMap.get(passenger.getToFloor()).add(passenger);
                iterator.remove();
                this.passengerNum++;
            }
        }
    }

    private void Move() {
        this.time += this.velocity;
        this.count += 4;
        this.nowFloor += this.direction;
    }

    private void Reverse() {
        this.direction = -this.direction;
    }

    private Status GetAdvice() {
        if (this.HavePassengerOutNow()) {
            return Status.OPEN;
        }

        if (this.HavePassengerSameDirectionNow() && this.passengerNum < this.capacity) {
            return Status.OPEN;
        }

        if (this.HavePassengerInLater()) {
            return Status.MOVE;
        }

        if (this.HavePassengerInsideNow()) {
            return Status.MOVE;
        }

        if (this.IsEmpty()) {
            return Status.END;
        }

        return Status.REVERSE;
    }

    private boolean HavePassengerOutNow() {
        return !this.passengerMap.get(this.nowFloor).isEmpty();
    }

    private boolean HavePassengerSameDirectionNow() {
        ArrayList<Request> passengersThisFloor = this.requestMap.get(this.nowFloor);
        for (Request passenger : passengersThisFloor) {
            if ((((PersonRequest) passenger).getToFloor() - this.nowFloor) * this.direction > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean HavePassengerInLater() {
        for (int i = this.nowFloor + this.direction; i >= 1 && i <= 11; i += this.direction) {
            if (!this.requestMap.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean HavePassengerInsideNow() {
        return this.passengerNum > 0;
    }

    private boolean IsEmpty() {
        boolean isEmpty = true;
        for (int i = 1; i <= 11; i++) {
            isEmpty = isEmpty && this.requestMap.get(i).isEmpty();
        }
        return isEmpty;
    }

    public ShadowElevator clone() {
        ShadowElevator newShadowElevator = new ShadowElevator();

        newShadowElevator.velocity = this.velocity;
        newShadowElevator.capacity = this.capacity;
        newShadowElevator.nowFloor = this.nowFloor;
        newShadowElevator.direction = this.direction;
        newShadowElevator.passengerNum = this.passengerNum;
        newShadowElevator.time = this.time;

        newShadowElevator.passengerMap = new HashMap<>();
        newShadowElevator.requestMap = new HashMap<>();

        for (int i = 1; i <= 11; i++) {
            newShadowElevator.passengerMap.put(i, new ArrayList<>(this.passengerMap.get(i)));
            newShadowElevator.requestMap.put(i, new ArrayList<>(this.requestMap.get(i)));
        }

        return newShadowElevator;
    }
}
