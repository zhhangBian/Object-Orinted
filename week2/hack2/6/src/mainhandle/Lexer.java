package mainhandle;

public class Lexer {

    private final String input;
    private Integer pos = 0;
    private Integer flag = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (pos == 0 && input.charAt(pos) == '-') {
            pos += 1;
            curToken = '-' + this.getNumber();
        } else if (input.charAt(pos) == '*' && input.charAt(pos + 1) == '-') {
            flag = 1;
            pos += 2;
            curToken = String.valueOf(c);
        } else if (Character.isDigit(c)) {
            if (flag == 1) {
                curToken = '-' + this.getNumber();
                flag = 0;
            } else {
                curToken = this.getNumber();
            }
        } else {
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public String peek() {
        return this.curToken;
    }
}
