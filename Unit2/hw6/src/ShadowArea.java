import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

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
        int id, int velocity, int capacity, int nowFloor, int direction, int passengerNum,
        HashMap<Integer, ArrayList<PersonRequest>> passengerMap,
        HashMap<Integer, ArrayList<Request>> requestMap) {
        this.shadowElevatorMap.get(id).LeaveShadow(velocity, capacity, nowFloor, direction,
            passengerNum, passengerMap, requestMap);
        this.notify();
    }

    public synchronized void ShadowElevatorSetReset(int id) {
        this.shadowElevatorMap.get(id).SetReset();
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
