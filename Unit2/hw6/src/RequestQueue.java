import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.ResetRequest;
import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.IntStream;

public class RequestQueue {
    private final HashMap<Integer, ArrayList<Request>> requestMap;
    private final int id;
    private boolean finishRequest;
    private boolean isReset;

    public RequestQueue(int id) {
        this.requestMap = new HashMap<>();
        this.id = id;
        this.finishRequest = false;
        this.isReset = false;
        IntStream.range(-1, 12).forEach(i -> this.requestMap.put(i, new ArrayList<>()));
    }

    public synchronized void AddRequest(Request request) {
        if (!this.isReset) {
            if (request instanceof ResetRequest) {
                this.requestMap.get(0).add(request);
            } else {
                PersonRequest passenger = (PersonRequest) request;
                TimableOutput.println("RECEIVE-" + passenger.getPersonId() + "-" + this.id);
                this.requestMap.get(passenger.getFromFloor()).add(passenger);
            }
        } else {
            this.requestMap.get(-1).add(request);
        }

        this.notify();
    }

    public synchronized void SetReset() {
        this.isReset = true;
        this.notify();
    }

    public synchronized void FinishReset() {
        this.isReset = false;
        ArrayList<Request> passengerResetList = this.requestMap.get(-1);
        passengerResetList.forEach(this::AddRequest);
        passengerResetList.clear();
        this.notify();
    }

    public synchronized ResetRequest GetResetRequest() {
        ArrayList<Request> resetList = this.requestMap.get(0);
        ResetRequest resetRequest = (ResetRequest) resetList.get(0);
        resetList.remove(0);
        this.notify();
        return resetRequest;
    }

    public synchronized ArrayList<PersonRequest> GetResetWaitingPassengerList() {
        ArrayList<PersonRequest> resetWaitingPassengerList = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            ArrayList<Request> passengerList = this.requestMap.get(i);
            passengerList.stream().map(passenger -> (PersonRequest) passenger).
                forEach(resetWaitingPassengerList::add);
            passengerList.clear();
        }

        this.notify();
        return resetWaitingPassengerList;
    }

    public synchronized ArrayList<PersonRequest> GetPassengerInList(
        int nowFloor, int direction, int capacity) {
        ArrayList<PersonRequest> passengerInList = new ArrayList<>();
        int leftCapacity = capacity;

        ArrayList<Request> passengersWantInNow = this.requestMap.get(nowFloor);
        Iterator<Request> iterator = passengersWantInNow.iterator();
        while (iterator.hasNext() && leftCapacity > 0) {
            PersonRequest passenger = (PersonRequest) iterator.next();
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
        return !this.requestMap.get(0).isEmpty();
    }

    public synchronized boolean HavePassengerSameDirectionNow(int nowFloor, int direction) {
        ArrayList<Request> passengersThisFloor = this.requestMap.get(nowFloor);
        for (Request passenger : passengersThisFloor) {
            if ((((PersonRequest) passenger).getToFloor() - nowFloor) * direction > 0) {
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

        this.notify();
        return isEmpty;
    }

    public synchronized HashMap<Integer, ArrayList<Request>> CloneRequestMap() {
        HashMap<Integer, ArrayList<Request>> newRequestMap = new HashMap<>();
        for (int i = 1; i < 12; i++) {
            newRequestMap.put(i, new ArrayList<>(this.requestMap.get(i)));
        }
        this.notify();
        return newRequestMap;
    }
}
