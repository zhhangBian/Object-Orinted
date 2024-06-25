import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Definer definer = new Definer();

        int funcNum = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < funcNum; i++) {
            String funcForm = scanner.nextLine();
            definer.AddFuncDefine(funcForm.replaceAll("\\s+", ""));
        }

        String expressionIn = scanner.nextLine();

        Lexer lexer = new Lexer(expressionIn);
        Parser parser = new Parser(lexer, definer);

        Expression expression = parser.parseExpression();

        System.out.println(expression);
    }
}
