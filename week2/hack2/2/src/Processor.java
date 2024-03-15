import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processor {
    private ArrayList<DefFunc> defFuncs;

    public Processor(ArrayList<DefFunc> defFuncs)
    {
        this.defFuncs = defFuncs;
    }

    public Processor(){}

    public String processFunc(String str) {
        String proStr = mergeAddAndSub(str);
        proStr = substitueExp(proStr);
        proStr = delForwardZero(proStr);
        proStr = dealWithPrefix(proStr);
        proStr = mergeAddAndSub(proStr);
        proStr = delAdd(proStr);
        //proStr = addZero(proStr);

        return proStr;
    }

    public String processFormula(String str) {
        String proStr = substituteFunc(str);
        proStr = mergeAddAndSub(proStr);
        proStr = substitueExp(proStr);
        proStr = delForwardZero(proStr);
        proStr = dealWithPrefix(proStr);
        proStr = mergeAddAndSub(proStr);
        proStr = delAdd(proStr);
        proStr = addExpQua(proStr);
        //proStr = addZero(proStr);

        return proStr;
    }

    public String substitueExp(String str) {
        return str.replaceAll("exp", "e");
    }

    public String substituteFunc(String str) {
        String s = str;
        while (s.contains("f") | s.contains("g") | s.contains("h")) {
            for (DefFunc defFunc : defFuncs) {
                s = defFunc.substitute(s);
            }
        }
        return s;
    }

    public String mergeAddAndSub(String originPoly)
    {
        String processedPoly = originPoly.replaceAll("\\s", "");
        char[] processedChars = processedPoly.toCharArray();

        for (int i = 0; i < processedPoly.length() - 1; i++) {
            char ch1 = processedChars[i];
            char ch2 = processedChars[i + 1];
            if (ch1 == '+' || ch1 == '-') {
                if (ch2 == '+' || ch2 == '-') {
                    if (ch1 == ch2) {
                        processedChars[i] = ' ';
                        processedChars[i + 1] = '+';
                    } else {
                        processedChars[i] = ' ';
                        processedChars[i + 1] = '-';
                    }
                }
            }
        }
        processedPoly = new String(processedChars);
        processedPoly = processedPoly.replaceAll("\\s", "");


        //processedPoly = addZero(processedPoly);
        return processedPoly;
    }

    public String dealWithPrefix(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '-' && (str.charAt(i + 1) == '(' || str.charAt(i + 1) == 'x'
                                            || str.charAt(i + 1) == 'e')) {
                sb.append('-');
                sb.append('1');
                sb.append('*');
            }
            else if  (str.charAt(i) == '+' && (str.charAt(i + 1) == '(' || str.charAt(i + 1) == 'x'
                                                || str.charAt(i + 1) == 'e')) {
                sb.append('+');
                sb.append('1');
                sb.append('*');
            }
            else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    public String addExpQua(String str) {
        return str.replaceAll("\\(", "\\(\\(").replaceAll("\\)", "\\)\\)");
    }

    public String delForwardZero(String str) {
        String proString = str.replaceAll("0*(\\d+)", "$1");
        /*str = str.replaceAll("-0+", "-");
        str = str.replaceAll("\\*0+", "\\*");
        str = str.replaceAll("\\^0+", "\\^");*/

        return proString;
    }

    public String delAdd(String str) { //(+
        String proStr = str.replaceAll("\\(\\+", "\\(").replaceAll("\\*\\+", "\\*")
                .replaceAll("\\^\\+", "\\^");

        String pattern = "(\\d+|x|\\))-(\\d+|x|\\()"; // 匹配 "数字orx-数字orx"
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(proStr);
        while (matcher.find())  {
            String replacement = "$1\\+\\-$2"; // 在 "-" 前添加 "+"
            proStr = proStr.replaceAll(pattern, replacement);
        }
        return proStr;
    }

    public String addZero(String str) { //假如expr的首项是符号，添加0
        String string = str;
        if (string.startsWith("+") || string.startsWith("-")) {
            string = "0" + string;
        }

        string = string.replaceAll("\\(\\+", "\\("); //(+2
        string = string.replaceAll("\\(\\-", "\\(0\\-"); //(-2
        string = string.replaceAll("\\*\\+", "\\*"); //*+2
        string = string.replaceAll("\\*-", "\\*\\(0-1)\\*"); //*-2
        string = string.replaceAll("\\^\\+", "\\^"); //^+2
        //string = string.addParExp(string); //e(0-x)->e((0-x))

        return string;
    }

    public String adjust(String str) {
        String s = str;
        if (s.charAt(0) != '-') {
            return s;
        }
        else {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '+') {
                    String front = str.substring(0, i);
                    String back = str.substring(i);
                    return back + front;
                }
            }
            return s;
        }
    }

    public String delForAdd(String str) {
        String s = str;
        if (s.startsWith("+")) {
            s = s.substring(1);
        }
        return s;
    }

    /*
    //验证程序！
    public static void main(String[] args) {
        DefFunc e1 = new DefFunc("f(x, y)=2 * x +-exp(y)");
        DefFunc e2 = new DefFunc("g(z) = exp(exp(z))^2");
        ArrayList<DefFunc> defFuncs = new ArrayList<>();
        defFuncs.add(e1);
        defFuncs.add(e2);
        String str = new String("x+f(x,x)-g(x)");
        Processor processor = new Processor(defFuncs);
        str = processor.delAdd("e(x)*e(x)-(1)");
        System.out.println(str);
    }
     */
}
