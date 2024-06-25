import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import com.oocourse.elevator1.TimableOutput;

public class Elevator {
    private final int id;
    private int nowFloor;
    private int direction;
    private int passengerNum;
    private final HashMap<Integer, ArrayList<Passenger>> passengerMap;
    private boolean isClosed;

    public Elevator(int id) {
        this.id = id;
        this.nowFloor = 1;
        this.direction = 1;
        this.passengerNum = 0;
        this.passengerMap = new HashMap<>();
        IntStream.range(1, 12).forEach(i -> this.passengerMap.put(i, new ArrayList<>()));
        this.isClosed = true;
    }

    public void Move() {
        this.nowFloor += this.direction;
        TimableOutput.println("ARRIVE-" + this.nowFloor + "-" + this.id);
    }

    public void Reverse() {
        this.direction = -this.direction;
    }

    public void InPassengerList(ArrayList<Passenger> passengerInList) {
        for (Passenger passenger : passengerInList) {
            this.passengerNum++;
            this.passengerMap.get(passenger.GetDirectionFloor()).add(passenger);
            TimableOutput.println("IN-" + passenger.GetId() + "-" + nowFloor + "-" + id);
        }
    }

    public void OutPassenger() {
        ArrayList<Passenger> passengersOutThisFloor = this.passengerMap.get(this.nowFloor);
        for (Passenger passenger : passengersOutThisFloor) {
            this.passengerNum--;
            TimableOutput.println("OUT-" + passenger.GetId() + "-" + nowFloor + "-" + id);
        }
        passengersOutThisFloor.clear();
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

    public boolean HavePassengerOutNow() {
        return !this.passengerMap.get(this.nowFloor).isEmpty();
    }

    public int GetNowFloor() {
        return this.nowFloor;
    }

    public int GetLeftCapacity() {
        return (6 - this.passengerNum);
    }

    public int GetDirection() {
        return this.direction;
    }
}
