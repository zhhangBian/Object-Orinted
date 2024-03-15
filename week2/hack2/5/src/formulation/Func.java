package formulation;

import parse.Lexer;
import parse.Parser;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Func {
    private String name;
    private Expr model;
    private ArrayList<String> vars;

    public String getName() {
        return name;
    }

    public Factor replace(ArrayList<Factor> replacements) {
        Factor result = model;
        if (vars.contains("x")) {
            result = result.replace('x', replacements.get(vars.indexOf("x")));
        }
        if (vars.contains("y")) {
            result = result.replace('y', replacements.get(vars.indexOf("y")));
        }
        if (vars.contains("z")) {
            result = result.replace('z', replacements.get(vars.indexOf("z")));
        }
        return result;
    }

    public Func(String input) {
        vars = new ArrayList<>();
        Pattern pattern = Pattern.compile("([fgh])\\(([xyz,]+)\\)=([^ ]+)");
        Matcher match = pattern.matcher(input);
        if (match.find()) {
            name = match.group(1);
            for (String temp:match.group(2).split(",")) {
                vars.add(temp);
            }
            Lexer newlexer = new Lexer(match.group(3));
            Parser newparser = new Parser(newlexer);
            model = newparser.parseExpr();
        }
    }
}