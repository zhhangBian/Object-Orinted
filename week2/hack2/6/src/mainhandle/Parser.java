package mainhandle;

import expression.Expr;
import expression.Factor;
import expression.Term;
import expression.FucFactor;
import expression.ExpFactor;
import expression.Power;
import expression.Number;

import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    private BigInteger power = BigInteger.ONE;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        if (lexer.peek().equals("-")) {
            lexer.next();
            expr.addTerm(parseTerm(-1));
        } else {
            expr.addTerm(parseTerm(1));
        }
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.peek().equals("+")) {
                lexer.next();
                expr.addTerm(parseTerm(1));
            } else {
                lexer.next();
                expr.addTerm(parseTerm(-1));
            }
        }
        power = BigInteger.ONE;
        return expr;
    }

    private Term parseTerm(int sign) {
        Term term = new Term(sign);
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    private Factor parsePowerFactor() {
        String nowPower = lexer.peek();
        lexer.next();
        if ("^".contains(lexer.peek())) {
            lexer.next();
            BigInteger a = new BigInteger(lexer.peek());
            lexer.next();
            return new Power(nowPower, a);
        } else {
            return new Power(nowPower, BigInteger.ONE);
        }
    }

    private Factor parseExprFactor() {
        lexer.next();
        Factor expr = parseExpr();
        while (!lexer.peek().equals(")")) {
            lexer.next();
        }
        lexer.next();
        if ("^".contains(lexer.peek())) {
            lexer.next();
            power = new BigInteger(lexer.peek());
            lexer.next();
            expr.changeIndex(power);
            return expr;
        } else {
            return expr;
        }
    }

    private Factor parseNumFactor() {
        if (lexer.peek().equals("-")) {
            lexer.next();
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num.negate());
        } else if (lexer.peek().equals("+")) {
            lexer.next();
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num);
        } else {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num);
        }
    }

    private Factor parseExpFactor() {
        lexer.next(); // to x
        lexer.next(); // to p
        lexer.next(); // to (
        lexer.next();
        Factor factor = parseFactor();
        while (!lexer.peek().equals(")")) {
            lexer.next();
        }
        lexer.next();
        if ("^".contains(lexer.peek())) {
            lexer.next();
            power = new BigInteger(lexer.peek());
            lexer.next();
            ExpFactor expFactor = new ExpFactor(factor);
            expFactor.changeIndex(power);
            return expFactor;
        } else {
            return new ExpFactor(factor);
        }
    }

    private Factor parseFuncFactor(String funcName) {
        lexer.next(); // to (
        lexer.next(); // to factor
        ArrayList<Factor> factorList = new ArrayList<>();
        factorList.add(parseFactor());
        while (!lexer.peek().equals(")")) {
            lexer.next();
            factorList.add(parseFactor());
        }
        lexer.next();
        return new FucFactor(funcName, factorList);
    }

    public Factor parseFactor() {
        if ("xyz".contains(lexer.peek())) {
            return parsePowerFactor();
        } else if (lexer.peek().equals("(")) {
            return parseExprFactor();
        } else if (lexer.peek().equals("e")) {
            return parseExpFactor();
        } else if ("fgh".contains(lexer.peek())) {
            return parseFuncFactor(lexer.peek());
        } else {
            return parseNumFactor();
        }
    }
}
