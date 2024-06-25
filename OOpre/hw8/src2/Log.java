public class Log {
    private final int type;
    private final int year;
    private final int month;
    private final String advName1;
    private final String advName2;
    private final String name;

    public Log(int type, int year, int month, String advName1, String advName2, String name) {
        this.type = type;
        this.year = year;
        this.month = month;
        this.advName1 = advName1;
        this.advName2 = advName2;
        this.name = name;
    }

    @Override
    public String toString() {
        switch (type) {
            case 1:
                return String.format("%04d/%02d %s used %s",
                        year, month, advName1, name);
            case 2:
                return String.format("%04d/%02d %s attacked %s with %s",
                        year, month, advName1, advName2, name);
            case 3:
                return String.format("%04d/%02d %s AOE-attacked with %s",
                        year, month, advName1, name);
            default:
                // 出现极端情况，返回一个错误
                return "Fight log error";
        }
    }
}
