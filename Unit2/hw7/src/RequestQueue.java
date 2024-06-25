import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ResetRequest;
import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.IntStream;

public class RequestQueue {
    private final int id;
    private final String type;

    private final HashMap<Integer, ArrayList<PersonRequest>> requestMap;
    private final ArrayList<PersonRequest> bufferPassengerList;
    private final ArrayList<ResetRequest> resetRequestList;

    private boolean inDoubleMode;
    private int exchangeFloor;

    private boolean finishRequest;
    private boolean isReset;

    public RequestQueue(int id, String type) {
        this.id = id;
        this.type = type;

        this.inDoubleMode = false;
        this.exchangeFloor = 0;

        this.finishRequest = false;
        this.isReset = false;

        this.requestMap = new HashMap<>();
        this.bufferPassengerList = new ArrayList<>();
        this.resetRequestList = new ArrayList<>();

        IntStream.range(0, 12).forEach(i -> this.requestMap.put(i, new ArrayList<>()));
    }

    public synchronized void SetDoubleMode(int exchangeFloor) {
        this.inDoubleMode = true;
        this.exchangeFloor = exchangeFloor;
    }

    public synchronized boolean IfInDoubleMode() {
        return this.inDoubleMode;
    }

    public synchronized int GetExchangeFloor() {
        return this.exchangeFloor;
    }

    public synchronized void SetResetBegin() {
        this.isReset = true;
        this.notify();
    }

    public synchronized void SetResetEnd() {
        this.isReset = false;
        this.notify();
    }

    public synchronized void AddRequest(NormalResetRequest normalResetRequest) {
        this.resetRequestList.add(normalResetRequest);
        this.notify();
    }

    public synchronized void AddRequest(DoubleCarResetRequest doubleCarResetRequest) {
        this.resetRequestList.add(doubleCarResetRequest);
        this.notify();
    }

    public synchronized void AddRequest(PersonRequest passenger) {
        if (!this.isReset) {
            StringBuilder sb = new
                StringBuilder("RECEIVE-" + passenger.getPersonId() + "-" + this.id);
            TimableOutput.println((this.inDoubleMode ? sb + "-" + this.type : sb));
            this.requestMap.get(passenger.getFromFloor()).add(passenger);
        } else {
            this.bufferPassengerList.add(passenger);
        }

        this.notify();
    }

    public synchronized void AddBufferPassengers() {
        this.bufferPassengerList.forEach(this::AddRequest);
        this.bufferPassengerList.clear();
        this.notify();
    }

    public synchronized void AddExchangePassengerList(
        ArrayList<PersonRequest> exchangePassengerList) {
        exchangePassengerList.forEach(this::AddRequest);
        this.notify();
    }

    public synchronized ResetRequest GetResetRequest() {
        ResetRequest resetRequest = this.resetRequestList.get(0);
        this.resetRequestList.remove(0);

        this.notify();
        return resetRequest;
    }

    public synchronized ArrayList<PersonRequest> GetResetWaitingPassengerList() {
        ArrayList<PersonRequest> resetWaitingPassengerList = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            ArrayList<PersonRequest> passengerList = this.requestMap.get(i);
            resetWaitingPassengerList.addAll(passengerList);
            passengerList.clear();
        }

        this.notify();
        return resetWaitingPassengerList;
    }

    public synchronized ArrayList<PersonRequest> GetPassengerInList(
        String type, int nowFloor, int direction, int capacity) {
        ArrayList<PersonRequest> passengerInList = new ArrayList<>();
        int leftCapacity = capacity;

        ArrayList<PersonRequest> passengersWantInNow = this.requestMap.get(nowFloor);
        Iterator<PersonRequest> iterator = passengersWantInNow.iterator();
        while (iterator.hasNext() && leftCapacity > 0) {
            PersonRequest passenger = iterator.next();
            if ((passenger.getToFloor() - nowFloor) * direction > 0) {
                passengerInList.add(passenger);
                iterator.remove();
                leftCapacity--;
            }
        }

        this.notify();
        return passengerInList;
    }

    public synchronized boolean IsResetNow() {
        this.notify();
        return !this.resetRequestList.isEmpty();
    }

    public synchronized boolean HavePassengerSameDirectionNow(int nowFloor, int direction) {
        ArrayList<PersonRequest> passengersThisFloor = this.requestMap.get(nowFloor);
        for (PersonRequest passenger : passengersThisFloor) {
            if ((passenger.getToFloor() - nowFloor) * direction > 0) {
                this.notify();
                return true;
            }
        }

        this.notify();
        return false;
    }

    public synchronized boolean HavePassengerInLater(int nowFloor, int direction) {
        for (int i = nowFloor + direction; i >= 1 && i <= 11; i += direction) {
            if (!this.requestMap.get(i).isEmpty()) {
                this.notify();
                return true;
            }
        }

        this.notify();
        return false;
    }

    public synchronized void SetRequestEnd() {
        this.finishRequest = true;
        this.notify();
    }

    public synchronized boolean IfRequestEnd() {
        this.notify();
        return this.finishRequest;
    }

    public synchronized boolean IfIsEmpty() {
        boolean isEmpty = true;
        for (int i = 1; i <= 11; i++) {
            isEmpty = isEmpty && this.requestMap.get(i).isEmpty();
        }

        return isEmpty;
    }

    public synchronized PersonRequest GetNotEmpty() {
        for (int i = 1; i <= 11; i++) {
            if (!this.requestMap.get(i).isEmpty()) {
                return this.requestMap.get(i).get(0);
            }
        }
        return null;
    }

    public synchronized HashMap<Integer, ArrayList<PersonRequest>> CloneRequestMap() {
        HashMap<Integer, ArrayList<PersonRequest>> newRequestMap = new HashMap<>();
        for (int i = 1; i <= 11; i++) {
            newRequestMap.put(i, new ArrayList<>(this.requestMap.get(i)));
        }

        this.notify();
        return newRequestMap;
    }
}
