package parse;

public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        /*if (input.charAt(pos) == '-') {
            sb.append('-');
            ++pos;
        } else if (input.charAt(pos) == '+') {
            ++pos;
        }*/
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        return sb.toString();
    }

    private String getFunction() {
        StringBuilder sb = new StringBuilder();
        sb.append(input.charAt(pos));
        pos++;
        sb.append(input.charAt(pos));
        pos++;
        int endcount = 1;
        while (endcount > 0 && pos < input.length()) {
            if (input.charAt(pos) == '(') {
                endcount++;
            }
            if (input.charAt(pos) == ')') {
                endcount--;
            }
            sb.append(input.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) { // end
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) { //number
            curToken = this.getNumber();
        } else if (c == 'f' || c == 'g' || c == 'h') {      // function
            curToken = this.getFunction();
        } else if (c == 'e') {      // exponent
            pos += 4;
            curToken = "exp(";
        } else { // other operator
            pos += 1;
            curToken = String.valueOf(c);
        }
        System.out.print(curToken);
    }

    public String peek() {
        return this.curToken;
    }
}
