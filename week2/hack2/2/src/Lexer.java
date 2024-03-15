import java.util.ArrayList;

public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    private boolean isDigit;//包含数字和未知数

    private ArrayList<String> tokenList;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos)/* TODO */);//把表达式附加到StringBuilder之后
            ++pos;
        }

        return sb.toString();
    }

    public boolean start() {
        char c = input.charAt(pos);
        if (pos == 0 && c != '(') {
            return true;
        }
        else if (c == '-') {
            return true;
        }
        else {
            return Character.isDigit(c) || c == 'x';
        }
    }

    public String getFactor() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && start()) {
            sb.append(input.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }

        if (start()) {
            curToken = getFactor();
        }
        else {
            curToken = String.valueOf(input.charAt(pos));
            pos++;
        }

        /*char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
            isDigit = true;
        }
        else if (c == '+' || c == '-' || c == '*' || c == '(' || c == ')' || c == '^') {
            pos += 1;
            curToken = String.valueOf(c);//转换为字符串
        }
        else if (c == 'x' || c == 'e')
        {
            curToken = String.valueOf(c);
            ++pos;
        }*/
    }

    public String peek() {
        return this.curToken;
    }

    public static void main(String[] args) {
        String str = "e(-x)";
        Lexer lexer = new Lexer(str);
        while (lexer.pos <= lexer.input.length()) {
            System.out.println(lexer.curToken);
            if (lexer.pos == str.length()) {
                break;
            }
            lexer.next();
        }
    }

}

