public final class Passenger {
    private final int id;
    private final int fromFloor;
    private final int directionFloor;
    private final int elevatorId;

    public Passenger(int id, int fromFloor, int directionFloor, int elevatorId) {
        this.id = id;
        this.fromFloor = fromFloor;
        this.directionFloor = directionFloor;
        this.elevatorId = elevatorId;
    }

    public int GetId() {
        return this.id;
    }

    public int GetElevatorId() {
        return this.elevatorId;
    }

    public int GetFromFloor() {
        return this.fromFloor;
    }

    public int GetDirectionFloor() {
        return this.directionFloor;
    }
}
