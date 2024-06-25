import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Definer {
    private TreeMap<String, String> funcMap;
    private TreeMap<String, ArrayList<String>> paraMap;

    public Definer() {
        this.funcMap = new TreeMap<>();
        this.paraMap = new TreeMap<>();
    }

    public void AddFuncDefine(String funcExpression) {
        String funcName = funcExpression.substring(0, 1);

        int bracketBegin = -1;
        int bracketEnd = -1;
        for (int i = 0; i < funcExpression.length(); i++) {
            if (funcExpression.charAt(i) == '(') {
                bracketBegin = i;
            } else if (funcExpression.charAt(i) == ')' && bracketBegin >= 0) {
                bracketEnd = i;
                break;
            }
        }
        String paraString = funcExpression.substring(bracketBegin + 1, bracketEnd);
        ArrayList<String> paraList = new ArrayList<>(Arrays.asList(paraString.split(",")));
        this.paraMap.put(funcName, paraList);

        String funcString = GetFuncReplaced(funcExpression.substring(bracketEnd + 2), paraList);
        this.funcMap.put(funcName, funcString);
    }

    private String GetFuncReplaced(String funOriginalExpression, ArrayList<String> paraList) {
        Lexer lexer = new Lexer(funOriginalExpression);
        Parser parser = new Parser(lexer, this);
        String funcExpression = parser.parseExpression().toString();

        String funcString = funcExpression.replace("exp", "EE");
        for (String paraForm : paraList) {
            String paraReplace = paraForm.equals("x") ? "u" : paraForm.equals("y") ? "v" : "w";
            funcString = funcString.replace(paraForm, "(" + paraReplace + ")");
        }
        funcString = funcString.replace("EE", "exp");

        return funcString;
    }

    public boolean IfIsFunction(String funcName) {
        return this.funcMap.containsKey(funcName);
    }

    public int GetParaNum(String funcName) {
        return this.paraMap.get(funcName).size();
    }

    public String GetFunc(String funcName, ArrayList<String> paraList) {
        String funcString = this.funcMap.get(funcName);
        ArrayList<String> paraNameList = this.paraMap.get(funcName);

        for (int i = 0; i < paraNameList.size(); i++) {
            String paraName = paraNameList.get(i);
            String paraToReplace = paraName.equals("x") ? "u" : paraName.equals("y") ? "v" : "w";

            funcString = funcString.replace(paraToReplace, paraList.get(i));
        }

        return funcString;
    }
}
