import java.util.ArrayList;

public class Lexer {
    private String input;
    private int pos = 0;
    private int numToken = 0;
    private ArrayList<Token> tokenList;
    private Token curToken;

    public Lexer(String input) {
        this.input = input;
        this.tokenList = new ArrayList<>();
        this.init();
    }

    public void init() {
        while (pos < input.length()) {
            if (input.charAt(pos) == '+') {
                tokenList.add(new Token(Token.Type.Plus, "+"));
                pos++;
            } else if (input.charAt(pos) == '-') {
                tokenList.add(new Token(Token.Type.Minus, "-"));
                pos++;
            } else if (input.charAt(pos) == '*') {
                tokenList.add(new Token(Token.Type.Mul, "*"));
                pos++;
            } else if (input.charAt(pos) == '(') {
                tokenList.add(new Token(Token.Type.Left, "("));
                pos++;
            } else if (input.charAt(pos) == ')') {
                tokenList.add(new Token(Token.Type.Right, ")"));
                pos++;
            } else if (input.charAt(pos) == 'x') {
                tokenList.add(new Token(Token.Type.Power, this.getPower()));
            } else if (input.charAt(pos) == '^') {
                tokenList.add(new Token(Token.Type.Exp, "^"));
                pos++;
            } else if (input.charAt(pos) >= '0' && input.charAt(pos) <= '9') {
                tokenList.add(new Token(Token.Type.Num, this.getNumber()));
            } else if (input.charAt(pos) == 'e') {
                tokenList.add(new Token(Token.Type.E, this.getE()));
            }
        }
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }
        if (pos < input.length() && input.charAt(pos) != '^') {
            return sb.toString();
        } else if (pos < input.length()) {
            sb.append(input.charAt(pos));
            pos++;
        }
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }

        return sb.toString();
    }

    private String getPower() {
        if (pos < input.length() - 1 && input.charAt(pos + 1) == '^') {
            StringBuilder sb = new StringBuilder();
            sb.append("x^");
            pos += 2;
            sb.append(this.getNumber());
            return sb.toString();
        } else {
            pos++;
            return "x^1";
        }
    }

    private String getE() {
        if (pos < input.length() - 1) {
            pos++;
        }
        return "e";
    }

    public void next() {
        numToken++;
    }

    public Token peek() {
        curToken = tokenList.get(numToken);
        return this.curToken;
    }

    public void printToken() {
        System.out.println(tokenList.size());
        for (Token token : tokenList) {
            System.out.println(token.getString());
        }
    }

    public int getTokenListSize() {
        return tokenList.size();
    }

    public Boolean hasNext() {
        return numToken < tokenList.size() - 1;
    }
}
