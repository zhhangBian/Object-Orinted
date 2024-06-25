import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

public class RequestQueue {
    private final HashMap<Integer, ArrayList<Passenger>> requestMap;
    private boolean isEnd;

    public RequestQueue() {
        this.requestMap = new HashMap<>();
        this.isEnd = false;
        IntStream.range(1, 12).forEach(i -> this.requestMap.put(i, new ArrayList<>()));
    }

    public synchronized void AddPassenger(Passenger passenger) {
        this.requestMap.get(passenger.GetFromFloor()).add(passenger);
        notify();
    }

    public synchronized ArrayList<Passenger> GetPassengerInList(
            int nowFloor, int direction, int capacity) {
        ArrayList<Passenger> passengerInList = new ArrayList<>();
        int leftCapacity = capacity;

        ArrayList<Passenger> passengersWantInNow = this.requestMap.get(nowFloor);
        Iterator<Passenger> iterator = passengersWantInNow.iterator();
        while (iterator.hasNext() && leftCapacity > 0) {
            Passenger passenger = iterator.next();
            if ((passenger.GetDirectionFloor() - nowFloor) * direction > 0) {
                passengerInList.add(passenger);
                iterator.remove();
                leftCapacity--;
            }
        }

        notify();
        return passengerInList;
    }

    public synchronized boolean HavePassengerSameDirectionNow(int nowFloor, int direction) {
        ArrayList<Passenger> passengersThisFloor = this.requestMap.get(nowFloor);

        for (Passenger passenger : passengersThisFloor) {
            if ((passenger.GetDirectionFloor() - nowFloor) * direction > 0) {
                return true;
            }
        }

        notify();
        return false;
    }

    public synchronized boolean HavePassengerInLater(int nowFloor, int direction) {
        for (int i = nowFloor + direction; i >= 1 && i <= 11; i += direction) {
            if (!this.requestMap.get(i).isEmpty()) {
                notify();
                return true;
            }
        }

        notify();
        return false;
    }

    public synchronized void SetEnd() {
        this.isEnd = true;
        notify();
    }

    public synchronized boolean IfIsEnd() {
        notify();
        return this.isEnd;
    }

    public synchronized boolean IfIsEmpty() {
        boolean isEmpty = true;
        for (Map.Entry<Integer, ArrayList<Passenger>> entry : this.requestMap.entrySet()) {
            isEmpty = isEmpty && entry.getValue().isEmpty();
        }

        notify();
        return isEmpty;
    }
}
