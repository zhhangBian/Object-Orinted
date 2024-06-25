public class Strategy {
    public static Status GetAdvice(Elevator elevator,
                                   RequestQueue requestQueue,
                                   RequestQueue otherRequestQueue) {
        final int nowFloor = elevator.GetNowFloor();
        final int direction = elevator.GetDirection();

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

        if (elevator.InExchangeFloorLater() &&
            (elevator.HavePassengerWantExchange() ||
                requestQueue.HavePassengerInLater(nowFloor, direction))) {
            return Status.EXCHANGE;
        }

        if (requestQueue.HavePassengerInLater(nowFloor, direction)) {
            return Status.MOVE;
        }

        if (elevator.HavePassengerInsideNow()) {
            return Status.MOVE;
        }

        if (requestQueue.IfIsEmpty()) {
            if (requestQueue.IfRequestEnd() && otherRequestQueue.IfIsEmpty()) {
                return Status.END;
            } else {
                return Status.WAIT;
            }
        }

        return Status.REVERSE;
    }
}
