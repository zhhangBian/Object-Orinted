import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    private final Definer definer;

    public Parser(Lexer lexer, Definer definer) {
        this.lexer = lexer;
        this.definer = definer;
    }

    public Expression parseExpression() {
        Expression expression = new Expression();
        expression.AddExpression(parseTerm());

        while (lexer.GetCurrentToken().equals("+") || lexer.GetCurrentToken().equals("-")) {
            boolean isAdd = lexer.GetCurrentToken().equals("+");
            lexer.GoToNextToken();
            Expression termExpression = parseTerm();
            termExpression = isAdd ? termExpression : termExpression.negate();
            expression.AddExpression(termExpression);
        }

        return expression;
    }

    private Expression parseTerm() {
        Term term = new Term();

        Factor prevFactor = parseFactor();
        this.HandlePower(term, prevFactor);

        while (lexer.GetCurrentToken().equals("*")) {
            lexer.GoToNextToken();
            prevFactor = parseFactor();
            this.HandlePower(term, prevFactor);
        }

        return term.GetTermExpression();
    }

    private void HandlePower(Term term, Factor prevFactor) {
        if (lexer.GetCurrentToken().equals("^")) {
            lexer.GoToNextToken();
            BigInteger power = new BigInteger(parseFactor().toString());

            if (power.equals(BigInteger.ZERO)) {
                term.AddFactor(new Unit(BigInteger.ONE));
            } else {
                for (BigInteger i = BigInteger.ZERO;
                     i.compareTo(power) < 0; i = i.add(BigInteger.ONE)) {
                    term.AddFactor(prevFactor.clone());
                }
            }
        } else {
            term.AddFactor(prevFactor);
        }
    }

    private Factor parseFactor() {
        if (lexer.GetCurrentToken().equals("(")) {
            lexer.GoToNextToken();
            Factor expression = parseExpression();
            lexer.GoToNextToken();

            return expression;
        } else {
            String currentToken = lexer.GetCurrentToken();

            if (Character.isDigit(currentToken.charAt(0))) {
                lexer.GoToNextToken();
                return new Unit(new BigInteger(currentToken));
            } else if (currentToken.charAt(0) == 'd' && currentToken.length() == 2) {
                lexer.GoToNextToken();
                return parseDerivation(String.valueOf(currentToken.charAt(1)));
            } else if (currentToken.equals("exp")) {
                lexer.GoToNextToken();
                return parseExpFunc();
            } else if (definer.IfIsFunction(currentToken)) {
                lexer.GoToNextToken();
                return parseFunction(currentToken);
            } else {
                lexer.GoToNextToken();
                return new Unit(new Polynome(currentToken, BigInteger.ONE));
            }
        }
    }

    private Expression parseDerivation(String polyName) {
        lexer.GoToNextToken();
        Expression expression = parseExpression();
        lexer.GoToNextToken();
        return expression.derivation(polyName);
    }

    private Unit parseExpFunc() {
        lexer.GoToNextToken();
        Unit expUnit = new Unit(parseExpression());
        lexer.GoToNextToken();

        return expUnit;
    }

    private Expression parseFunction(String funcName) {
        int paraNum = definer.GetParaNum(funcName);
        ArrayList<String> paraList = new ArrayList<>();

        for (int i = 0; i < paraNum; i++) {
            lexer.GoToNextToken();
            paraList.add(parseExpression().toString());
        }
        lexer.GoToNextToken();

        Lexer funcLexer = new Lexer(definer.GetFunc(funcName, paraList));
        Parser funcParser = new Parser(funcLexer, definer);

        return funcParser.parseExpression();
    }
}