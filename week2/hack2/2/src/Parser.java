
import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm("+"));

        while (lexer.peek().equals("+") || lexer.peek().equals("-")/* TODO */) {
            String op = lexer.peek();
            lexer.next();
            expr.addTerm(parseTerm(op));
        }
        //System.out.println(expr);
        return expr;
    }

    public Term parseTerm(String op) {
        int sign = (op.equals("+")) ? 1 : -1;
        Term term = new Term(sign);
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        //System.out.println(term);
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) { //(expr)^b
            lexer.next();
            final Expr expr = parseExpr();//提取一个表达式
            lexer.next();//?跳过后面的）
            //lexer.next();
            BigInteger num = BigInteger.ONE;
            if (lexer.peek().equals("^")) {
                lexer.next();
                num = new BigInteger(lexer.peek()); //指数应该都是常数？
                lexer.next();
            }
            return new ExprFactor(expr, num);
        }
        else if (lexer.peek().equals("x")) { //x^b
            String var = lexer.peek();
            lexer.next();
            BigInteger num = BigInteger.ONE;
            if (lexer.peek().equals("^")) {
                lexer.next();
                num = new BigInteger(lexer.peek()); //指数应该都是常数？
                lexer.next();
            }
            return new Variable(var, num);
        }
        else if (lexer.peek().equals("e")) { //e(factor)^b
            lexer.next();
            lexer.next();
            final Factor factor = parseFactor();
            lexer.next();
            //lexer.next();
            BigInteger num = BigInteger.ONE;
            if (lexer.peek().equals("^")) {
                lexer.next();
                num = new BigInteger(lexer.peek()); //指数应该都是常数？
                lexer.next();
            }
            return new EeeFactor(factor, num);
        }
        else {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            //System.out.println(num);
            return new Number(num);
        }
    }
}
