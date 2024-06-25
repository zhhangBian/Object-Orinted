public class Lexer {
    private String exprIn;
    private int pos = 0;
    private String currentToken;

    public Lexer(String exprIn) {
        this.exprIn = exprIn;
        this.PreHandleExpression();
        this.GoToNextToken();
    }

    public String GetCurrentToken() {
        return this.currentToken;
    }

    public void GoToNextToken() {
        if (pos == exprIn.length()) {
            return;
        }

        char c = exprIn.charAt(pos);
        if (Character.isDigit(c)) {
            currentToken = GetNumber();
        } else if (Character.isLetter(c)) {
            currentToken = GetPolyName();
        } else if (c == '*' || c == '+' || c == '-' ||
                c == '(' || c == ')' || c == '^' || c == ',') {
            currentToken = String.valueOf(c);
            pos = pos + 1;
        }
    }

    private String GetNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        while (pos < exprIn.length() && Character.isDigit(exprIn.charAt(pos))) {
            stringBuilder.append(exprIn.charAt(pos));
            pos = pos + 1;
        }

        return stringBuilder.toString();
    }

    private String GetPolyName() {
        StringBuilder stringBuilder = new StringBuilder();
        while (pos < exprIn.length() && Character.isLetter(exprIn.charAt(pos))) {
            stringBuilder.append(exprIn.charAt(pos));
            pos = pos + 1;
        }

        return stringBuilder.toString();
    }

    private void PreHandleExpression() {
        exprIn = exprIn.replaceAll("\\s+", "");

        while (exprIn.contains("--") || exprIn.contains("++") ||
                exprIn.contains("+-") || exprIn.contains("-+")) {
            exprIn = exprIn.replace("--", "+");
            exprIn = exprIn.replace("++", "+");
            exprIn = exprIn.replace("+-", "-");
            exprIn = exprIn.replace("-+", "-");
        }

        exprIn = exprIn.replace("^+", "^");
        exprIn = exprIn.replace("(-", "(0-");
        exprIn = exprIn.replace("(+", "(0+");
        exprIn = exprIn.replace("-", "-1*");
        exprIn = exprIn.replace("*+", "*");
        exprIn = exprIn.replace("*-1", "*(0-1)");
        exprIn = exprIn.replace(",-1", ",(0-1)");
        exprIn = exprIn.replace("+(", "+1*(");
        exprIn = exprIn.replace("-(", "-1*(");
        exprIn = exprIn.replace("f(", "1*f(");
        exprIn = exprIn.replace("g(", "1*g(");
        exprIn = exprIn.replace("h(", "1*h(");
        exprIn = exprIn.replace("dx(", "1*dx(");
        exprIn = exprIn.replace("dy(", "1*dy(");
        exprIn = exprIn.replace("dz(", "1*dz(");
        exprIn = exprIn.replace("dD(", "1*dD(");
        exprIn = exprIn.replace("exp(", "1*exp(");
        exprIn = exprIn.replace(",+", ",");
        exprIn = exprIn.replace(",(", ",1*(");

        while (exprIn.contains("((")) {
            exprIn = exprIn.replace("((", "(1*(");
        }

        if (exprIn.charAt(0) == '-' || exprIn.charAt(0) == '+') {
            exprIn = "0" + exprIn;
        }

        if (exprIn.charAt(0) == '(') {
            exprIn = "1*" + exprIn;
        }
    }
}
