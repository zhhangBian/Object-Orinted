public class Strategy {
    public static Status GetAdvice(Elevator elevator, RequestQueue requestQueue) {
        return GetAdviceLook(elevator, requestQueue);
    }

    public static Status GetAdviceLook(Elevator elevator, RequestQueue requestQueue) {
        int nowFloor = elevator.GetNowFloor();
        int direction = elevator.GetDirection();

        if (elevator.HavePassengerOutNow()) {
            return Status.IN;
        }

        if ((requestQueue.HavePassengerSameDirectionNow(nowFloor, direction)
                && elevator.GetLeftCapacity() > 0)) {
            return Status.IN;
        }

        // 优先响应同方向最远请求
        if (requestQueue.HavePassengerInLater(nowFloor, direction)) {
            return Status.MOVE;
        }

        if (elevator.GetLeftCapacity() < 6) {
            return Status.MOVE;
        }

        if (requestQueue.IfIsEmpty()) {
            if (requestQueue.IfIsEnd()) {
                return Status.END;
            } else {
                return Status.WAIT;
            }
        }

        // 之后有人进则继续运动
        if (requestQueue.HavePassengerInLater(nowFloor, direction)) {
            return Status.MOVE;
        }

        // 否则反向
        return Status.REVERSE;
    }
}
