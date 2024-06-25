import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        Scanner scanner = new Scanner(System.in);
        BasicData basicData = new BasicData();
        IOhandle io = new IOhandle();

        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            io.HandleType(scanner, basicData);
        }
    }
}