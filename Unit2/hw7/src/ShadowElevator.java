import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ShadowElevator {
    private boolean inDoubleMode;
    private int exchangeFloor;
    private int velocity;
    private int capacity;

    private int time;
    private double count;

    private int waitTimeA;
    private int waitTimeB;
    private int stageA;
    private int stageB;

    private int nowFloorA;
    private int nowFloorB;
    private int directionA;
    private int directionB;
    private int passengerNumA;
    private int passengerNumB;
    private HashMap<Integer, ArrayList<PersonRequest>> passengerMapA;
    private HashMap<Integer, ArrayList<PersonRequest>> passengerMapB;
    private HashMap<Integer, ArrayList<PersonRequest>> requestMapA;
    private HashMap<Integer, ArrayList<PersonRequest>> requestMapB;

    public ShadowElevator() {
        this.inDoubleMode = false;
        this.exchangeFloor = 0;
        this.velocity = 4;
        this.capacity = 6;

        this.time = 0;
        this.count = 0;

        this.waitTimeA = 0;
        this.waitTimeB = 0;
        this.stageA = 0;
        this.stageB = 0;

        this.nowFloorA = 1;
        this.nowFloorB = 1;

        this.directionA = 1;
        this.directionB = 1;

        this.passengerNumA = 0;
        this.passengerNumB = 0;

        this.passengerMapA = new HashMap<>();
        this.passengerMapB = new HashMap<>();
        this.requestMapA = new HashMap<>();
        this.requestMapB = new HashMap<>();

        for (int i = 0; i < 12; i++) {
            this.passengerMapA.put(i, new ArrayList<>());
            this.passengerMapB.put(i, new ArrayList<>());
            this.requestMapA.put(i, new ArrayList<>());
            this.requestMapB.put(i, new ArrayList<>());
        }
    }

    public void LeaveShadow(
        String type, int nowFloor, int direction, int passengerNum,
        HashMap<Integer, ArrayList<PersonRequest>> passengerMap,
        HashMap<Integer, ArrayList<PersonRequest>> requestMap) {
        if (type.equals("A")) {
            this.nowFloorA = nowFloor;
            this.directionA = direction;
            this.passengerNumA = passengerNum;
            this.passengerMapA = passengerMap;
            this.requestMapA = requestMap;
        } else {
            this.nowFloorB = nowFloor;
            this.directionB = direction;
            this.passengerNumB = passengerNum;
            this.passengerMapB = passengerMap;
            this.requestMapB = requestMap;
        }
    }

    public void SetNormalReset(int velocity, int capacity) {
        this.time = 12;
        this.velocity = velocity;
        this.capacity = capacity;
        this.ClearPassenger();
    }

    public void SetDoubleReset(int velocity, int capacity, int exchangeFloor) {
        this.time = 12;
        this.inDoubleMode = true;
        this.velocity = velocity;
        this.capacity = capacity;
        this.exchangeFloor = exchangeFloor;
        this.ClearPassenger();
    }

    private void ClearPassenger() {
        this.passengerNumA = 0;
        this.passengerNumB = 0;
        for (int i = 0; i < 12; i++) {
            this.passengerMapA.replace(i, new ArrayList<>());
            this.passengerMapB.replace(i, new ArrayList<>());
            this.requestMapA.replace(i, new ArrayList<>());
            this.requestMapB.replace(i, new ArrayList<>());
        }
    }

    public int GetEstimatePerformance(PersonRequest personRequest) {
        this.AddPassengerToRequestQueue(personRequest);

        Status nextStatusA;
        Status nextStatusB;
        while (!this.IfEnd() && time <= 1000) {
            if (this.waitTimeA != 0) {
                this.waitTimeA--;
            } else {
                if (this.stageA == 0) {
                    nextStatusA = this.GetAdvice("A");
                } else {
                    nextStatusA = Status.EXCHANGE;
                }
                this.ShadowElevatorRun("A", nextStatusA);
            }

            if (this.inDoubleMode) {
                if (this.waitTimeB != 0) {
                    this.waitTimeB--;
                } else {
                    if (this.stageB == 0) {
                        nextStatusB = this.GetAdvice("B");
                    } else {
                        nextStatusB = Status.EXCHANGE;
                    }
                    this.ShadowElevatorRun("B", nextStatusB);
                }
            }

            this.time++;
        }

        return (int) (this.time * this.count);
    }

    public void AddPassengerToRequestQueue(PersonRequest passenger) {
        if (this.inDoubleMode) {
            int fromFloor = passenger.getFromFloor();
            int toFloor = passenger.getToFloor();

            if (fromFloor < this.exchangeFloor ||
                (fromFloor == this.exchangeFloor && toFloor < this.exchangeFloor)) {
                this.requestMapA.get(passenger.getFromFloor()).add(passenger);
            } else {
                this.requestMapB.get(passenger.getFromFloor()).add(passenger);
            }
        } else {
            this.requestMapA.get(passenger.getFromFloor()).add(passenger);
        }
    }

    private boolean IfEnd() {
        if (this.inDoubleMode) {
            return this.passengerNumA == 0 && this.passengerNumB == 0 &&
                this.IfIsEmpty("A") && this.IfIsEmpty("B");
        } else {
            return this.passengerNumA == 0 && this.IfIsEmpty("A");
        }
    }

    private void ShadowElevatorRun(String type, Status nextStatus) {
        if (nextStatus == Status.EXCHANGE) {
            this.Exchange(type);
        } else if (nextStatus == Status.OPEN) {
            this.Open(type);
        } else if (nextStatus == Status.MOVE) {
            this.Move(type);
        } else {
            this.Reverse(type);
        }
    }

    private void Exchange(String type) {
        int stage = type.equals("A") ? this.stageA : this.stageB;
        if (stage == 0) {
            this.Move(type);
            stage = 1;
        } else if (stage == 1) {
            this.ExchangePassenger(type);
            this.Reverse(type);
            this.InPassenger(type);
            if (type.equals("A")) {
                this.waitTimeA = 4;
            } else {
                this.waitTimeB = 4;
            }
            stage = 2;
        } else {
            this.Move(type);
            stage = 0;
        }

        if (type.equals("A")) {
            this.stageA = stage;
        } else {
            this.stageB = stage;
        }
    }

    private void Open(String type) {
        this.OutPassenger(type);
        this.InPassenger(type);

        if (type.equals("A")) {
            this.waitTimeA = 4;
        } else {
            this.waitTimeB = 4;
        }

        this.count += this.inDoubleMode ? 0.5 : 2;
    }

    private void Move(String type) {
        if (type.equals("A")) {
            this.nowFloorA += this.directionA;
            this.waitTimeA = this.velocity;
        } else {
            this.nowFloorB += this.directionB;
            this.waitTimeB = this.velocity;
        }

        this.count += this.inDoubleMode ? 1 : 4;
    }

    private void Reverse(String type) {
        if (type.equals("A")) {
            this.directionA = -this.directionA;
        } else {
            this.directionB = -this.directionB;
        }
    }

    private void ExchangePassenger(String type) {
        int lowFloor = type.equals("A") ? this.exchangeFloor : 1;
        int highFloor = type.equals("A") ? 11 : this.exchangeFloor;
        HashMap<Integer, ArrayList<PersonRequest>> passengerMap =
            type.equals("A") ? this.passengerMapA : this.passengerMapB;
        ArrayList<PersonRequest> exchangeList = type.equals("A") ?
            this.requestMapB.get(this.exchangeFloor) :
            this.requestMapA.get(this.exchangeFloor);

        for (int i = lowFloor; i <= highFloor; i++) {
            if (i != this.exchangeFloor) {
                for (PersonRequest passenger : passengerMap.get(i)) {
                    exchangeList.add(new PersonRequest(this.exchangeFloor,
                        passenger.getToFloor(), passenger.getPersonId()));
                }
            }
            if (type.equals("A")) {
                this.passengerNumA -= passengerMap.get(i).size();
            } else {
                this.passengerNumB -= passengerMap.get(i).size();
            }
            passengerMap.get(i).clear();
        }
    }

    private void OutPassenger(String type) {
        HashMap<Integer, ArrayList<PersonRequest>> passengerMap =
            type.equals("A") ? this.passengerMapA : this.passengerMapB;
        int nowFloor = type.equals("A") ? this.nowFloorA : this.nowFloorB;

        ArrayList<PersonRequest> passengersOutThisFloor = passengerMap.get(nowFloor);
        if (type.equals("A")) {
            this.passengerNumA -= passengersOutThisFloor.size();
        } else {
            this.passengerNumB -= passengersOutThisFloor.size();
        }

        passengersOutThisFloor.clear();
    }

    private void InPassenger(String type) {
        HashMap<Integer, ArrayList<PersonRequest>> requestMap =
            type.equals("A") ? requestMapA : requestMapB;
        HashMap<Integer, ArrayList<PersonRequest>> passengerMap =
            type.equals("A") ? this.passengerMapA : this.passengerMapB;
        int nowFloor = type.equals("A") ? this.nowFloorA : this.nowFloorB;
        int direction = type.equals("A") ? this.directionA : this.directionB;
        int passengerNum = type.equals("A") ? this.passengerNumA : this.passengerNumB;

        ArrayList<PersonRequest> passengersWantInNow = requestMap.get(nowFloor);

        Iterator<PersonRequest> iterator = passengersWantInNow.iterator();
        while (iterator.hasNext() && passengerNum < this.capacity) {
            PersonRequest passenger = iterator.next();
            if ((passenger.getToFloor() - nowFloor) * direction > 0) {
                passengerMap.get(passenger.getToFloor()).add(passenger);
                iterator.remove();
                passengerNum++;
            }
        }

        if (type.equals("A")) {
            this.passengerNumA = passengerNum;
        } else {
            this.passengerNumB = passengerNum;
        }
    }

    public Status GetAdvice(String type) {
        if (this.HavePassengerOutNow(type)) {
            return Status.OPEN;
        }

        if ((this.HavePassengerSameDirectionNow(type) && this.GetLeftCapacity(type) > 0)) {
            return Status.OPEN;
        }

        if (this.InExchangeFloorLater(type) &&
            (this.HavePassengerWantExchange(type) || this.HavePassengerInLater(type))) {
            return Status.EXCHANGE;
        }

        if (this.HavePassengerInLater(type)) {
            return Status.MOVE;
        }

        if (this.HavePassengerInsideNow(type)) {
            return Status.MOVE;
        }

        if (this.IfIsEmpty(type)) {
            return Status.WAIT;
        }

        return Status.REVERSE;
    }

    private boolean HavePassengerOutNow(String type) {
        if (type.equals("A")) {
            return !this.passengerMapA.get(this.nowFloorA).isEmpty();
        } else {
            return !this.passengerMapB.get(this.nowFloorB).isEmpty();
        }
    }

    private boolean HavePassengerSameDirectionNow(String type) {
        int nowFloor = type.equals("A") ? this.nowFloorA : this.nowFloorB;
        int direction = type.equals("A") ? this.directionA : this.directionB;
        ArrayList<PersonRequest> passengersThisFloor = type.equals("A") ?
            this.requestMapA.get(nowFloor) : this.requestMapB.get(nowFloor);
        for (PersonRequest passenger : passengersThisFloor) {
            if ((passenger.getToFloor() - nowFloor) * direction > 0) {
                return true;
            }
        }

        return false;
    }

    private int GetLeftCapacity(String type) {
        if (type.equals("A")) {
            return (this.capacity - this.passengerNumA);
        } else {
            return (this.capacity - this.passengerNumB);
        }
    }

    private boolean InExchangeFloorLater(String type) {
        int nowFloor = type.equals("A") ? this.nowFloorA : this.nowFloorB;
        int direction = type.equals("A") ? this.directionA : this.directionB;
        return this.inDoubleMode && (nowFloor + direction == this.exchangeFloor);
    }

    private boolean HavePassengerWantExchange(String type) {
        int direction = type.equals("A") ? this.directionA : this.directionB;
        HashMap<Integer, ArrayList<PersonRequest>> passengerMap =
            type.equals("A") ? this.passengerMapA : this.passengerMapB;
        for (int i = this.exchangeFloor; i >= 1 && i <= 11; i += direction) {
            if (!passengerMap.get(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private boolean HavePassengerInLater(String type) {
        HashMap<Integer, ArrayList<PersonRequest>> requestMap =
            type.equals("A") ? this.requestMapA : this.requestMapB;
        int nowFloor = type.equals("A") ? this.nowFloorA : this.nowFloorB;
        int direction = type.equals("A") ? this.directionA : this.directionB;
        for (int i = nowFloor + direction; i >= 1 && i <= 11; i += direction) {
            if (!requestMap.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean HavePassengerInsideNow(String type) {
        return type.equals("A") ? this.passengerNumA > 0 : this.passengerNumB > 0;
    }

    private boolean IfIsEmpty(String type) {
        boolean isEmpty = true;
        HashMap<Integer, ArrayList<PersonRequest>> requestMap =
            type.equals("A") ? this.requestMapA : this.requestMapB;
        for (int i = 1; i <= 11; i++) {
            isEmpty = isEmpty && requestMap.get(i).isEmpty();
        }
        return isEmpty;
    }

    public ShadowElevator clone() {
        ShadowElevator newShadowElevator = new ShadowElevator();

        newShadowElevator.inDoubleMode = this.inDoubleMode;
        newShadowElevator.exchangeFloor = this.exchangeFloor;
        newShadowElevator.velocity = this.velocity;
        newShadowElevator.capacity = this.capacity;
        newShadowElevator.time = this.time;

        newShadowElevator.nowFloorA = this.nowFloorA;
        newShadowElevator.directionA = this.directionA;
        newShadowElevator.passengerNumA = this.passengerNumA;

        newShadowElevator.nowFloorB = this.nowFloorB;
        newShadowElevator.directionB = this.directionB;
        newShadowElevator.passengerNumB = this.passengerNumB;

        for (int i = 1; i <= 11; i++) {
            newShadowElevator.passengerMapA.put(i, new ArrayList<>(this.passengerMapA.get(i)));
            newShadowElevator.passengerMapB.put(i, new ArrayList<>(this.passengerMapB.get(i)));
            newShadowElevator.requestMapA.put(i, new ArrayList<>(this.requestMapA.get(i)));
            newShadowElevator.requestMapB.put(i, new ArrayList<>(this.requestMapB.get(i)));
        }

        return newShadowElevator;
    }
}
