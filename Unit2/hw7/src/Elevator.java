import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.TimableOutput;

public class Elevator {
    private final int id;
    private long velocity;
    private int capacity;
    private int nowFloor;
    private int direction;
    private int passengerNum;
    private final HashMap<Integer, ArrayList<PersonRequest>> passengerMap;
    private boolean isClosed;

    private final String type;
    private boolean inDoubleMode;
    private int exchangeFloor;

    public Elevator(int id, String type) {
        this.id = id;
        this.velocity = 400;
        this.capacity = 6;
        this.nowFloor = 1;
        this.direction = 1;
        this.passengerNum = 0;
        this.passengerMap = new HashMap<>();
        IntStream.range(1, 12).forEach(i -> this.passengerMap.put(i, new ArrayList<>()));
        this.isClosed = true;

        this.type = type;
        this.inDoubleMode = false;
        this.exchangeFloor = 0;
    }

    public long ResetBegin() {
        return TimableOutput.println("RESET_BEGIN-" + this.id);
    }

    public void ResetDoubleMode(long velocity, int capacity, int exchangeFloor) {
        this.velocity = velocity;
        this.capacity = capacity;
        this.exchangeFloor = exchangeFloor;
        this.inDoubleMode = true;
        this.nowFloor = exchangeFloor - 1;
        this.direction = -1;
    }

    public void ResetValue(long velocity, int capacity) {
        this.velocity = velocity;
        this.capacity = capacity;
    }

    public void ResetEnd() {
        if (this.type.equals("A")) {
            TimableOutput.println("RESET_END-" + this.id);
        }
    }

    public ArrayList<PersonRequest> GetResetPassengerOutList() {
        return this.GetPassengerFloorList(1, 11);
    }

    public ArrayList<PersonRequest> GetExchangePassengerOutList() {
        int lowFloor = type.equals("A") ? this.exchangeFloor : 1;
        int highFloor = type.equals("A") ? 11 : this.exchangeFloor;

        return GetPassengerFloorList(lowFloor, highFloor);
    }

    private ArrayList<PersonRequest> GetPassengerFloorList(int lowFloor, int highFloor) {
        ArrayList<PersonRequest> passengerFloorList = new ArrayList<>();
        for (int i = lowFloor; i <= highFloor; i++) {
            ArrayList<PersonRequest> passengerList = this.passengerMap.get(i);
            for (PersonRequest passenger : passengerList) {
                this.passengerNum--;

                StringBuilder stringBuilder =
                    new StringBuilder("OUT-" + passenger.getPersonId() + "-" + nowFloor + "-" + id);
                if (this.inDoubleMode) {
                    stringBuilder.append("-").append(this.type);
                }
                TimableOutput.println(stringBuilder);

                if (i != this.nowFloor) {
                    passengerFloorList.add(new PersonRequest(nowFloor, i, passenger.getPersonId()));
                }
            }
            if (i == this.nowFloor) {
                WaitQueue.getInstance().DecreasePassengerOutServiceNum(passengerList.size());
            }
            passengerList.clear();
        }

        return passengerFloorList;
    }

    public void InPassengerList(ArrayList<PersonRequest> passengerInList) {
        for (PersonRequest passenger : passengerInList) {
            this.passengerNum++;
            this.passengerMap.get(passenger.getToFloor()).add(passenger);
            StringBuilder stringBuilder =
                new StringBuilder("IN-" + passenger.getPersonId() + "-" + nowFloor + "-" + id);
            if (this.inDoubleMode) {
                stringBuilder.append("-").append(this.type);
            }
            TimableOutput.println(stringBuilder);
        }
    }

    public void OutPassenger() {
        ArrayList<PersonRequest> passengersOutThisFloor = this.passengerMap.get(this.nowFloor);
        int passengerOutNum = passengersOutThisFloor.size();
        WaitQueue.getInstance().DecreasePassengerOutServiceNum(passengerOutNum);
        for (PersonRequest passenger : passengersOutThisFloor) {
            StringBuilder stringBuilder =
                new StringBuilder("OUT-" + passenger.getPersonId() + "-" + nowFloor + "-" + id);
            if (this.inDoubleMode) {
                stringBuilder.append("-").append(this.type);
            }
            TimableOutput.println(stringBuilder);
        }
        passengersOutThisFloor.clear();

        this.passengerNum -= passengerOutNum;
    }

    public long OpenDoor() {
        if (this.isClosed) {
            this.isClosed = false;
            StringBuilder stringBuilder =
                new StringBuilder("OPEN-" + this.nowFloor + "-" + this.id);
            if (inDoubleMode) {
                stringBuilder.append("-").append(this.type);
            }

            return TimableOutput.println(stringBuilder);
        }
        return System.currentTimeMillis();
    }

    public void CloseDoor() {
        this.isClosed = true;
        StringBuilder stringBuilder = new StringBuilder("CLOSE-" + this.nowFloor + "-" + this.id);
        if (this.inDoubleMode) {
            stringBuilder.append("-").append(this.type);
        }
        TimableOutput.println(stringBuilder);
    }

    public void Move() {
        this.nowFloor += this.direction;
        StringBuilder stringBuilder = new StringBuilder("ARRIVE-" + this.nowFloor + "-" + this.id);
        if (this.inDoubleMode) {
            stringBuilder.append("-").append(this.type);
        }
        TimableOutput.println(stringBuilder);
    }

    public void Reverse() {
        this.direction = -this.direction;
    }

    public boolean HavePassengerOutNow() {
        return !this.passengerMap.get(this.nowFloor).isEmpty();
    }

    public boolean InExchangeFloorLater() {
        return this.inDoubleMode && (this.nowFloor + this.direction == this.exchangeFloor);
    }

    public boolean HavePassengerWantExchange() {
        for (int i = this.exchangeFloor; i >= 1 && i <= 11; i += this.direction) {
            if (!this.passengerMap.get(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public boolean HavePassengerInsideNow() {
        return this.passengerNum > 0;
    }

    public boolean IfInDoubleMode() {
        return this.inDoubleMode;
    }

    public int GetId() {
        return this.id;
    }

    public String GetType() {
        return this.type;
    }

    public long GetVelocity() {
        return this.velocity;
    }

    public int GetNowFloor() {
        return this.nowFloor;
    }

    public int GetLeftCapacity() {
        return (this.capacity - this.passengerNum);
    }

    public int GetPassengerNum() {
        return this.passengerNum;
    }

    public int GetDirection() {
        return this.direction;
    }

    public HashMap<Integer, ArrayList<PersonRequest>> ClonePassengerMap() {
        return IntStream.range(1, 12).boxed().collect(Collectors.toMap(i -> i,
            i -> new ArrayList<>(this.passengerMap.get(i)), (a, b) -> b, HashMap::new));
    }

    public Elevator GetDoubleElevator(int exchangeFloor) {
        Elevator newElevator = new Elevator(this.id, "B");
        newElevator.velocity = this.velocity;
        newElevator.capacity = this.capacity;
        newElevator.nowFloor = exchangeFloor + 1;
        newElevator.direction = 1;
        newElevator.passengerNum = 0;
        newElevator.isClosed = true;
        newElevator.inDoubleMode = true;
        newElevator.exchangeFloor = exchangeFloor;

        return newElevator;
    }
}
