import java.util.ArrayList;

public class WaitQueue {
    private final ArrayList<Passenger> waitList;
    private boolean isEnd;

    public WaitQueue() {
        this.waitList = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void SetEnd() {
        this.isEnd = true;
        notify();
    }

    public synchronized void AddPassenger(Passenger passenger) {
        this.waitList.add(passenger);
        notify();
    }

    public synchronized Passenger GetOnePassenger() {
        if (!this.isEnd && this.waitList.isEmpty()) {
            try {
                notify();
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (this.waitList.isEmpty()) {
            return null;
        }

        Passenger passenger = this.waitList.get(0);
        this.waitList.remove(0);
        notify();
        return passenger;
    }

    public synchronized boolean IfIsEmpty() {
        notify();
        return this.waitList.isEmpty();
    }

    public synchronized boolean IfIsEnd() {
        notify();
        return this.isEnd;
    }
}
