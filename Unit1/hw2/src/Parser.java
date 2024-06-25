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
        expression.AddTerm(parseTerm(), true);

        while (lexer.GetCurrentToken().equals("+") || lexer.GetCurrentToken().equals("-")) {
            expression = expression.ExprSimplify();
            boolean isAdd = lexer.GetCurrentToken().equals("+");
            lexer.GoToNextToken();
            expression.AddTerm(parseTerm(), isAdd);
        }

        return expression.ExprSimplify();
    }

    private Term parseTerm() {
        Term term = new Term();

        Factor prevFactor = parseFactor();
        this.HandlePower(term, prevFactor);

        while (lexer.GetCurrentToken().equals("*")) {
            lexer.GoToNextToken();
            prevFactor = parseFactor();
            this.HandlePower(term, prevFactor);
        }

        return term;
    }

    private void HandlePower(Term term, Factor prevFactor) {
        if (lexer.GetCurrentToken().equals("^")) {
            lexer.GoToNextToken();
            int power = Integer.parseInt(parseFactor().toString());

            if (power == 0) {
                term.AddFactor(new Unit(BigInteger.ONE));
            } else {
                for (int i = 0; i < power; i++) {
                    term.AddFactor(prevFactor.CopyFactor());
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

        return funcParser.parseExpression().ExprSimplify();
    }

    private Unit parseExpFunc() {
        lexer.GoToNextToken();
        Unit expUnit = new Unit(parseExpression().ExprSimplify());
        lexer.GoToNextToken();

        return expUnit;
    }
}