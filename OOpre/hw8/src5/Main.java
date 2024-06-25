import config.GlobalScanner;
import util.OperatorUtil;

public class Main {

    public static void main(String[] args) {
        int opCount = Integer.parseInt(GlobalScanner.SCANNER.nextLine());
        for (int i = 0; i < opCount; i++) {
            String op = GlobalScanner.SCANNER.nextLine();
            OperatorUtil.matchChoice(op);
        }
    }

}