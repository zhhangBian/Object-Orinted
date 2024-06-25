public class Strategy {
    public static Status GetAdvice(Elevator elevator, RequestQueue requestQueue) {
        int nowFloor = elevator.GetNowFloor();
        int direction = elevator.GetDirection();

        if (requestQueue.IsResetNow()) {
            return Status.RESET;
        }

        if (elevator.HavePassengerOutNow()) {
            return Status.OPEN;
        }

        if ((requestQueue.HavePassengerSameDirectionNow(nowFloor, direction)
            && elevator.GetLeftCapacity() > 0)) {
            return Status.OPEN;
        }

        if (requestQueue.HavePassengerInLater(nowFloor, direction)) {
            return Status.MOVE;
        }

        if (elevator.HavePassengerInsideNow()) {
            return Status.MOVE;
        }

        if (requestQueue.IfIsEmpty()) {
            if (requestQueue.IfRequestEnd()) {
                return Status.END;
            } else {
                return Status.WAIT;
            }
        }

        return Status.REVERSE;
    }
}
