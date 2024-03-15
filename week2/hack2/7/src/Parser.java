import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;
    private Poly lastPoly;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.lastPoly = new Poly();
    }

    public Poly parseExpr(int sign) {
        Expr expr = new Expr();
        expr.addPoly(parseTerm(1));

        while (lexer.hasNext() && (lexer.peek().getType() == Token.Type.Plus
                || lexer.peek().getType() == Token.Type.Minus)) {
            if (lexer.peek().getType() == Token.Type.Plus) {
                lexer.next();
                expr.addPoly(parseTerm(1));
            } else {
                lexer.next();
                expr.addPoly(parseTerm(-1));
            }
        }

        Poly exprPoly = expr.toPoly();
        if (sign == -1) {
            exprPoly.neg();
        }
        return exprPoly;
    }

    public Poly parseTerm(int sign) {
        Term term = new Term(sign);
        lastPoly = parseFactor();
        term.addPoly(lastPoly);

        while (lexer.hasNext() && lexer.peek().getType() == Token.Type.Mul) {
            lexer.next();
            lastPoly = parseFactor();
            term.addPoly(lastPoly);
        }

        return term.toPoly();
    }

    public Poly parseFactor() {
        int sign = 1;
        if (lexer.peek().getType() == Token.Type.Plus) {
            lexer.next();
        } else if (lexer.peek().getType() == Token.Type.Minus) {
            sign = -1;
            lexer.next();
        }
        if (lexer.peek().getType() == Token.Type.Left) {
            return parserFactorExpr(sign);
        } else if (lexer.peek().getType() == Token.Type.Power) {
            return parserFactorPower(sign);
        } else if (lexer.peek().getType() == Token.Type.Num) {
            return parserFactorNum(sign);
        } else if (lexer.peek().getType() == Token.Type.E) {
            return parserFactorExp(sign);
        }
        return null;
    }

    public Poly parserFactorExpr(int sign) {
        lexer.next();
        Poly exprPoly = parseExpr(sign);
        lexer.next();
        if (lexer.hasNext() && lexer.peek().getType() == Token.Type.Exp) {
            lexer.next();
            int exp = Integer.parseInt(lexer.peek().getString());
            exprPoly = Poly.polyPower(exp, exprPoly);
            lexer.next();
        }
        return exprPoly;
    }

    public Poly parserFactorExp(int sign) {
        lexer.next();
        lexer.next();
        Poly expPoly = parseExpr(sign);
        lexer.next();
        if (lexer.hasNext() && lexer.peek().getType() == Token.Type.Exp) {
            lexer.next();
            int time = Integer.parseInt(lexer.peek().getString());
            expPoly = Poly.polyTime(time, expPoly);
            lexer.next();
        }

        Poly poly = new Poly();
        poly.addMono(new Mono(BigInteger.ONE, BigInteger.ZERO, expPoly));
        return poly;
    }

    public Poly parserFactorPower(int sign) {
        BigInteger exp = new BigInteger(lexer.peek().getString().replace("x^", ""));
        Power power = new Power(exp);
        lexer.next();

        Poly powerPoly = power.toPoly();
        if (sign == -1) {
            powerPoly.neg();
        }
        return powerPoly;
    }

    public Poly parserFactorNum(int sign) {
        String numString = lexer.peek().getString();
        Number number;
        if (numString.contains("^")) {
            String[] numStrings = numString.split("\\^");
            BigInteger base = new BigInteger(numStrings[0]);
            int exp = Integer.parseInt(numStrings[1]);
            base = base.pow(exp);
            number = new Number(base);
        } else {
            number = new Number(new BigInteger(lexer.peek().getString()));
        }
        lexer.next();

        Poly numPoly = number.toPoly();
        if (sign == -1) {
            numPoly.neg();
        }
        return numPoly;
    }

}
