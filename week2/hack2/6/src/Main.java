import expression.Expr;
import expression.FuncHandle;
import mainhandle.Handle;
import mainhandle.Lexer;
import mainhandle.Parser;
import simplify.Poly;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String input = scanner.nextLine();
            FuncHandle.addFunc(input);
        }
        String input = scanner.nextLine();
        Handle handle = new Handle(input);
        String afterHandle = handle.secondHandle();
        Lexer lexer = new Lexer(afterHandle);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        Poly poly = expr.transPoly();
        System.out.println(poly);
    }
}
