import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BasicData data = new BasicData();
        IOhandle io = new IOhandle();

        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            io.HandleType(scanner, data, scanner.nextInt());
        }
    }
}
