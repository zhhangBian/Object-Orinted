import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 创建并在LogCenter中注册一个世界
        LogCenter.registerWorld(World.getInstance());
        // 操作数满足1≤n≤2000
        int n = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < n; ++i) {
            CommandCenter.inputCommand(World.getInstance(), scanner.nextLine());
            // 如果进入战斗状态，读取战斗日志
            int k = LogCenter.getWorldLogCount(World.getInstance());
            for (int j = 0; j < k; ++j) {
                LogCenter.inputLog(World.getInstance(), scanner.nextLine());
            }
            LogCenter.endWorldLog(World.getInstance());
        }
    }
}
