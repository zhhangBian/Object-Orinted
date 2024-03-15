
import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine(); //非常重要！把\N去掉
        ArrayList<DefFunc> defFuncs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String function = scanner.nextLine();
            DefFunc defFunc = new DefFunc(function);
            defFuncs.add(defFunc);
        }
        String input = scanner.nextLine();
        Processor processor = new Processor(defFuncs);
        String proInput = processor.processFormula(input);

        //System.out.println(proInput);

        Lexer lexer = new Lexer(proInput);//lexer词法分析器，分解原始字符串，得到token流
        Parser parser = new Parser(lexer);//parser语法分析器，构建语法树，也就是token之间的逻辑关系

        Expr expr = parser.parseExpr();
        //System.out.println(expr);

        String output = expr.toPoly().toString();
        //System.out.println("Output is " + output);
        String proOutput = processor.mergeAddAndSub(output);
        proOutput = processor.delForwardZero(proOutput);
        //proOutput = processor.adjust(proOutput);
        proOutput = processor.delForAdd(proOutput);

        System.out.println(proOutput);
    }

}
