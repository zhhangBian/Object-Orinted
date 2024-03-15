package parse;

import formulation.Constant;
import formulation.Expr;
import formulation.Factor;
import formulation.Term;
import formulation.Varible;
import formulation.Exponent;
import formulation.Funcs;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {   // Expr -> expr + term | term
        Expr expr = new Expr();
        String symbol = lexer.peek();
        if (symbol.equals("-")) {
            lexer.next();
            expr.addTerm(parseTerm(), true);
        } else if (symbol.equals("+")) {
            lexer.next();
            expr.addTerm(parseTerm(), false);
        } else {
            expr.addTerm(parseTerm(), false);
        }

        symbol = lexer.peek();
        while (symbol.equals("+") | symbol.equals("-")) {
            lexer.next();
            expr.addTerm(parseTerm(), symbol.equals("-"));
            symbol = lexer.peek();
        }
        return expr;
    }

    public Term parseTerm() {   // term -> term * factors | factors
        Term term = new Term();
        String symbol = lexer.peek();
        if (symbol.equals("-")) {
            lexer.next();
            term.addFactor(parseFactor(), true);
        } else if (symbol.equals("+")) {
            lexer.next();
            term.addFactor(parseFactor(), false);
        } else {
            term.addFactor(parseFactor(), false);
        }

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor(), false);
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) { // if expression factor
            lexer.next();
            Factor expr = parseExpr();
            if (lexer.peek().equals(")")) {
                lexer.next();
            }
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                expr.setIndex(Integer.parseInt(lexer.peek()));
                lexer.next();
            }
            return expr;
        } else {    // if constant factor
            String constant = lexer.peek();
            lexer.next();
            if (constant.equals("-")) {
                constant = lexer.peek();
                lexer.next();
                return new Constant("-" + constant);
            } else if (constant.equals("+")) {
                constant = lexer.peek();
                lexer.next();
                return new Constant(constant);
            } else if (Character.isDigit(constant.charAt(0))) {
                return new Constant(constant);
            } else { // if varible factor
                return parseVarible(constant);
            }
        }
    }

    public Factor parseVarible(String constant) {
        if (constant.equals("x")) { // if varible factor
            int index = 1;
            //lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                index = Integer.parseInt(lexer.peek());
                lexer.next();
            }
            return new Varible(index, 'x');
        } else if (constant.equals("y")) { // if varible factor
            int index = 1;
            //lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                index = Integer.parseInt(lexer.peek());
                lexer.next();
            }
            return new Varible(index, 'y');
        } else if (constant.equals("z")) { // if varible factor
            int index = 1;
            //lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                index = Integer.parseInt(lexer.peek());
                lexer.next();
            }
            return new Varible(index, 'z');
        } else if (constant.equals("exp(")) { // if exponent factor
            int index = 1;
            //lexer.next();
            Expr var =  parseExpr();
            if (lexer.peek().equals(")")) { lexer.next(); }
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) { lexer.next(); }
                index = Integer.parseInt(lexer.peek());
                lexer.next();
            }
            Expr newcoffemult;
            if (index != 1) {
                newcoffemult = new Expr();
                Term coffemult = new Term();
                coffemult.addFactor(var, false);
                coffemult.addFactor(new Constant(Integer.toString(index)),false);
                newcoffemult.addTerm(coffemult, false);
            } else {
                newcoffemult = var;
            }
            return new Exponent((Factor)newcoffemult);
        } else { // fx
            return Funcs.getInstance().parseFunc(constant);
        }
    }
}
