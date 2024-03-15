import java.util.Scanner;
import formulation.Expr;
import formulation.Funcs;
import parse.Lexer;
import parse.Parser;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < number; i++) {
            Funcs.getInstance().addFunc(scanner.nextLine().replaceAll("[ \t]", ""));
        }
        System.out.println();
        String expression = scanner.nextLine().replaceAll("[ \t]", ""); // get Expression
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        System.out.println(expr.toPoly().toString());
    }
}
