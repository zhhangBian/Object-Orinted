package mainhandle;

public class Handle {

    private String input;

    public Handle(String input) {
        this.input = input;
    }

    private String firstHandle() {
        String afterHandle;
        afterHandle = input.replaceAll("[ \\t]","");
        int pos = 0;
        StringBuilder stringBuilder = new StringBuilder(afterHandle);
        while (pos < stringBuilder.length()) {
            if (stringBuilder.charAt(pos) == '+' || stringBuilder.charAt(pos) == '-') {
                int pos1 = pos + 1;
                char flag = stringBuilder.charAt(pos);
                if (pos1 < stringBuilder.length()) {
                    while (stringBuilder.charAt(pos1) == '+' || stringBuilder.charAt(pos1) == '-') {
                        if (flag == '+' && stringBuilder.charAt(pos1) == '-') {
                            flag = '-';
                        } else if (flag == '-' && stringBuilder.charAt(pos1) == '+') {
                            flag = '-';
                        } else {
                            flag = '+';
                        }
                        stringBuilder.replace(pos1, pos1 + 1, " ");
                        pos1++;
                        if (pos1 >= stringBuilder.length()) {
                            break;
                        }
                    }
                }
                stringBuilder.replace(pos, pos + 1, String.valueOf(flag));
                pos = pos1 + 1;
            } else {
                pos++;
            }
        }
        String afterHandle1 = new String(stringBuilder);
        afterHandle1 = afterHandle1.replaceAll("[ \\t]","");
        return afterHandle1;
    }

    public String secondHandle() {
        String s = firstHandle();
        StringBuilder sb = new StringBuilder(s);
        int pos = 0;
        if (sb.charAt(pos) == '+') {
            sb.replace(pos,pos + 1,"");
        } else if (sb.charAt(pos) == '-') {
            sb.insert(pos,'0');
        }
        while (pos < sb.length()) {
            if (sb.charAt(pos) == '(' || sb.charAt(pos) == '^' || sb.charAt(pos) == '*') {
                if (sb.charAt(pos + 1) == '+') {
                    sb.replace(pos + 1,pos + 2,"");
                } else if (sb.charAt(pos) == '(' && sb.charAt(pos + 1) == '-') {
                    if (sb.charAt(pos + 2) >= 'x' && sb.charAt(pos + 2) <= 'z') {
                        sb.insert(pos + 1,"0");
                    }
                }
            }
            pos++;
        }
        return sb.toString();
    }
}
