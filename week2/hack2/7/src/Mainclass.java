import java.util.ArrayList;
import java.util.Scanner;

public class Mainclass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int funcNum = Integer.parseInt(scanner.nextLine());
        ArrayList<Function> functions = new ArrayList<>();
        for (int i = 0; i < funcNum; i++) {
            Function function = new Function(scanner.nextLine());
            function.funcPreTreat();
            functions.add(function);
        }
        String input = scanner.nextLine();
        //x*exp(x)

        Pretreat pretreat = new Pretreat(input);
        String output = pretreat.getTreat();
        output = Function.funcScan(output, functions);
        Lexer lexer = new Lexer(output);

        Parser parser = new Parser(lexer);
        Poly poly = parser.parseExpr(1);
        poly.polyCal();
        String show = poly.toString();
        System.out.println(show);
    }
}
