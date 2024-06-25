import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

public class ShadowArea {
    private static ShadowArea shadowArea = new ShadowArea();
    private final HashMap<Integer, ShadowElevator> shadowElevatorMap;

    private ShadowArea() {
        this.shadowElevatorMap = new HashMap<>();
        IntStream.rangeClosed(1, 6).forEach(i -> shadowElevatorMap.put(i, new ShadowElevator()));
    }

    public static synchronized ShadowArea getInstance() {
        if (shadowArea == null) {
            shadowArea = new ShadowArea();
        }
        return shadowArea;
    }

    public synchronized void ElevatorLeaveShadow(
        int id, String type, int nowFloor, int direction, int passengerNum,
        HashMap<Integer, ArrayList<PersonRequest>> passengerMap,
        HashMap<Integer, ArrayList<PersonRequest>> requestMap) {
        this.shadowElevatorMap.get(id).LeaveShadow(type, nowFloor, direction,
            passengerNum, passengerMap, requestMap);
        this.notify();
    }

    public synchronized void ShadowElevatorNormalReset(int id, int velocity, int capacity) {
        this.shadowElevatorMap.get(id).SetNormalReset(velocity, capacity);
        this.notify();
    }

    public synchronized void ShadowElevatorDoubleReset(
        int id, int velocity, int capacity, int exchangeFloor) {
        this.shadowElevatorMap.get(id).SetDoubleReset(velocity, capacity, exchangeFloor);
        this.notify();
    }

    public synchronized int GetShadowEstimateId(PersonRequest personRequest) {
        int id = personRequest.getPersonId() % 6 + 1;
        int minPerformance = Integer.MAX_VALUE;

        for (int i = 1; i <= 6; i++) {
            int estimatePerformance = this.shadowElevatorMap.get(i).clone().
                GetEstimatePerformance(personRequest);
            if (estimatePerformance < minPerformance) {
                minPerformance = estimatePerformance;
                id = i;
            }
        }
        this.shadowElevatorMap.get(id).AddPassengerToRequestQueue(personRequest);

        this.notify();
        return id;
    }
}
