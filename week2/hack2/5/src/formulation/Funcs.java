package formulation;

import parse.Lexer;
import parse.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Funcs {
    private HashMap<String, Func> funcs;

    private static Funcs samples = new Funcs();

    private Funcs() {
        funcs = new HashMap<>();;
    }

    public static Funcs getInstance() {
        return samples;
    }

    public void addFunc(String input) {
        Func newFunc = new Func(input);
        funcs.put(newFunc.getName(), newFunc);
    }

    public Factor parseFunc(String input) {
        Pattern pattern = Pattern.compile("([fgh])\\(([^ ]+)\\)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String name = matcher.group(1);
            ArrayList<Factor> replacements = new ArrayList<>();
            String reptemp = matcher.group(2);

            int pos = 0;
            while (pos < reptemp.length()) {
                StringBuilder sb = new StringBuilder();
                while (pos < reptemp.length() && reptemp.charAt(pos) != ',') {
                    if (reptemp.charAt(pos) == '(') {
                        while (reptemp.charAt(pos) != ')') {
                            sb.append(reptemp.charAt(pos));
                            ++pos;
                        }
                    }
                    sb.append(reptemp.charAt(pos));
                    ++pos;
                }
                Lexer lexer = new Lexer(sb.toString());
                Parser parser = new Parser(lexer);
                Expr tempexpr = parser.parseExpr();
                replacements.add(tempexpr);
                ++pos;
            }
            Func choosen = funcs.get(name);
            return choosen.replace(replacements);
        }
        return null;
    }

}
