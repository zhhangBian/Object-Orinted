import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.TimableOutput;

public class Elevator {
    private final int id;
    private long velocity;
    private int capacity;
    private int nowFloor;
    private int direction;
    private int passengerNum;
    private final HashMap<Integer, ArrayList<PersonRequest>> passengerMap;
    private boolean isClosed;

    public Elevator(int id) {
        this.id = id;
        this.velocity = 400;
        this.capacity = 6;
        this.nowFloor = 1;
        this.direction = 1;
        this.passengerNum = 0;
        this.passengerMap = new HashMap<>();
        IntStream.range(1, 12).forEach(i -> this.passengerMap.put(i, new ArrayList<>()));
        this.isClosed = true;
    }

    public long ResetBegin() {
        return TimableOutput.println("RESET_BEGIN-" + this.id);
    }

    public void ResetValue(long velocity, int capacity) {
        this.velocity = velocity;
        this.capacity = capacity;
    }

    public void ResetEnd() {
        TimableOutput.println("RESET_END-" + this.id);
    }

    public ArrayList<PersonRequest> ResetPassengerOutList() {
        ArrayList<PersonRequest> passengerOutList = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            ArrayList<PersonRequest> passengerList = this.passengerMap.get(i);
            for (PersonRequest passenger : passengerList) {
                TimableOutput.println("OUT-" + passenger.getPersonId() + "-" + nowFloor + "-" + id);

                if (i != this.nowFloor) {
                    passengerOutList.add(new PersonRequest(nowFloor, i, passenger.getPersonId()));
                }
            }
            if (i == this.nowFloor) {
                WaitQueue.getInstance().DecreasePassengerOutServiceNum(passengerList.size());
            }
            passengerList.clear();
        }
        this.passengerNum = 0;

        return passengerOutList;
    }

    public void InPassengerList(ArrayList<PersonRequest> passengerInList) {
        for (PersonRequest passenger : passengerInList) {
            this.passengerNum++;
            this.passengerMap.get(passenger.getToFloor()).add(passenger);
            TimableOutput.println("IN-" + passenger.getPersonId() + "-" + nowFloor + "-" + id);
        }
    }

    public void OutPassenger() {
        ArrayList<PersonRequest> passengersOutThisFloor = this.passengerMap.get(this.nowFloor);
        int passengerOutNum = passengersOutThisFloor.size();
        WaitQueue.getInstance().DecreasePassengerOutServiceNum(passengerOutNum);
        for (PersonRequest passenger : passengersOutThisFloor) {
            TimableOutput.println("OUT-" + passenger.getPersonId() + "-" + nowFloor + "-" + id);
        }
        passengersOutThisFloor.clear();

        this.passengerNum -= passengerOutNum;
    }

    public long OpenDoor() {
        if (this.isClosed) {
            this.isClosed = false;
            return TimableOutput.println("OPEN-" + this.nowFloor + "-" + this.id);
        }
        return System.currentTimeMillis();
    }

    public void CloseDoor() {
        this.isClosed = true;
        TimableOutput.println("CLOSE-" + this.nowFloor + "-" + this.id);
    }

    public void Move() {
        this.nowFloor += this.direction;
        TimableOutput.println("ARRIVE-" + this.nowFloor + "-" + this.id);
    }

    public void Reverse() {
        this.direction = -this.direction;
    }

    public boolean HavePassengerOutNow() {
        return !this.passengerMap.get(this.nowFloor).isEmpty();
    }

    public boolean HavePassengerInsideNow() {
        return this.passengerNum > 0;
    }

    public int GetId() {
        return this.id;
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

    public int GetCapacity() {
        return this.capacity;
    }

    public int GetDirection() {
        return this.direction;
    }

    public HashMap<Integer, ArrayList<PersonRequest>> ClonePassengerMap() {
        return IntStream.range(1, 12).boxed().collect(Collectors.toMap(i -> i,
            i -> new ArrayList<>(this.passengerMap.get(i)), (a, b) -> b, HashMap::new));
    }
}
