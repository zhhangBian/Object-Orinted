public class Pretreat {
    private String input;

    public Pretreat(String input) {
        this.input = input;
    }

    private void treatSpace() {
        int pos = 0;
        StringBuilder sb = new StringBuilder(input);
        while (pos < sb.length()) {
            if (sb.charAt(pos) == ' ' || sb.charAt(pos) == '\t') {
                sb.deleteCharAt(pos);
            } else {
                pos++;
            }
        }

        input = sb.toString();
    }

    private void treatPlusMinus() {
        int pos = 0;
        StringBuilder sb = new StringBuilder(input);
        while (pos < sb.length() - 1) {
            switch (sb.charAt(pos)) {
                case '+':
                    if (sb.charAt(pos + 1) == '+' || sb.charAt(pos + 1) == '-') {
                        sb.deleteCharAt(pos);
                    } else {
                        pos++;
                    }
                    break;
                case '-':
                    if (sb.charAt(pos + 1) == '+') {
                        sb.deleteCharAt(pos + 1);
                    } else if (sb.charAt(pos + 1) == '-') {
                        sb.deleteCharAt(pos + 1);
                        sb.setCharAt(pos, '+');
                    } else {
                        pos++;
                    }
                    break;
                default:
                    pos++;
            }
        }
        input = sb.toString();
    }

    private void treatExp() {
        int pos = 0;
        StringBuilder sb = new StringBuilder(input);
        while (pos < sb.length() - 1) {
            if (sb.charAt(pos) == '^' && sb.charAt(pos + 1) == '+') {
                sb.deleteCharAt(pos + 1);
            } else {
                pos++;
            }
        }
        input = sb.toString();
    }

    private void treatZero() {
        int pos = 1;
        StringBuilder sb = new StringBuilder(input);
        while (pos < sb.length() - 1) {
            if ((!Character.isDigit(sb.charAt(pos - 1)))
                    && sb.charAt(pos) == '0'
                    && Character.isDigit(sb.charAt(pos + 1))) {
                sb.deleteCharAt(pos);
            } else {
                pos++;
            }
        }
        input = sb.toString();
    }

    private void treatHead() {
        StringBuilder sb = new StringBuilder(input);
        if (sb.charAt(0) == '+' || sb.charAt(0) == '-') {
            sb.insert(0, '0');
        }

        int pos = 0;
        while (pos < input.length() - 1) {
            if (sb.charAt(pos) == '(' && (sb.charAt(pos + 1) == '+' || sb.charAt(pos + 1) == '-')) {
                sb.insert(pos + 1, '0');
            }
            pos++;
        }
        input = sb.toString();
    }

    private void treatE() {
        input = input.replaceAll("exp", "e");
    }

    public String getTreat() {
        this.treatSpace();
        this.treatPlusMinus();
        this.treatExp();
        this.treatZero();
        this.treatHead();
        this.treatE();
        return input;
    }
}
