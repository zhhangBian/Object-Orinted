import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.ArrayList;

public class WaitQueue {
    private static WaitQueue waitQueue = new WaitQueue();
    private final ArrayList<Request> waitList;
    private final ArrayList<Request> resetList;
    private int passengerInServiceNum;
    private boolean finishInput;

    private WaitQueue() {
        this.waitList = new ArrayList<>();
        this.resetList = new ArrayList<>();
        this.passengerInServiceNum = 0;
        this.finishInput = false;
    }

    public static synchronized WaitQueue getInstance() {
        if (waitQueue == null) {
            waitQueue = new WaitQueue();
        }
        return waitQueue;
    }

    public synchronized void SetInputEnd() {
        this.finishInput = true;
        this.notify();
    }

    public synchronized void AddRequest(Request request) {
        if (request instanceof PersonRequest) {
            this.passengerInServiceNum++;
            this.waitList.add(request);
        } else {
            this.resetList.add(request);
        }
        this.notify();
    }

    public synchronized void AddResetPassengerList(ArrayList<PersonRequest> passengerList) {
        this.waitList.addAll(0, passengerList);
        this.notify();
    }

    public synchronized Request GetOneRequest() {
        if (!this.finishInput && this.waitList.isEmpty() && this.resetList.isEmpty()) {
            try {
                this.notifyAll();
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Request request = null;
        if (!this.resetList.isEmpty()) {
            request = this.resetList.get(0);
            this.resetList.remove(0);
        } else if (!this.waitList.isEmpty()) {
            request = this.waitList.get(0);
            this.waitList.remove(0);
        }
        this.notify();
        return request;
    }

    public synchronized void DecreasePassengerOutServiceNum(int num) {
        this.passengerInServiceNum -= num;
        this.notify();
    }

    public synchronized boolean IfIsEmpty() {
        this.notify();
        return this.waitList.isEmpty() && this.resetList.isEmpty();
    }

    public synchronized boolean IfDispatchEnd() {
        this.notify();
        return this.waitList.isEmpty() && this.resetList.isEmpty() &&
            this.finishInput && this.passengerInServiceNum == 0;
    }
}
