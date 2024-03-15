import java.util.ArrayList;
import java.util.HashMap;

public class Function {
    private String rowFunction;
    private char funcName;
    private int funcVarNum;
    private ArrayList<Character> funcVars;
    private String funcString;

    public Function(String rowFunction) {
        this.rowFunction = rowFunction;
        this.funcVars = new ArrayList<>();
    }

    public void funcPreTreat() {
        Pretreat pretreat = new Pretreat(rowFunction);
        rowFunction = pretreat.getTreat();
        int pos = 0;
        this.funcName = rowFunction.charAt(pos);
        pos += 2;
        this.funcVars.add(rowFunction.charAt(pos));
        pos++;
        if (rowFunction.charAt(pos) == ',') {
            pos++;
            this.funcVars.add(rowFunction.charAt(pos));
            pos++;
            if (rowFunction.charAt(pos) == ',') {
                pos++;
                this.funcVars.add(rowFunction.charAt(pos));
                pos += 3;
            } else {
                pos += 2;
            }
            funcString = rowFunction.substring(pos);
        } else {
            pos += 2;
            funcString = rowFunction.substring(pos);
        }
        funcVarNum = funcVars.size();
        funcString = String.format("(%s)", funcString);
    }

    public static String funcScan(String input, ArrayList<Function> functions) {
        int pos = 0;
        String temp = input.substring(0);
        HashMap<Character, Function> funcNames = new HashMap<>();
        for (Function function : functions) {
            funcNames.put(function.getFuncName(), function);
        }
        while (pos < temp.length()) {
            if (funcNames.containsKey(temp.charAt(pos))) {
                Function function = funcNames.get(temp.charAt(pos));
                int beginPos = pos;
                int parStack = 0;
                int varNum = 0;
                ArrayList<String> paras = new ArrayList<>();
                pos += 2;
                while (varNum < function.getFuncVarNum()) {
                    int paraBegin = pos;
                    while (parStack > 0 || (temp.charAt(pos) != ',' && temp.charAt(pos) != ')')) {
                        if (temp.charAt(pos) == '(') {
                            parStack++;
                        } else if (temp.charAt(pos) == ')') {
                            parStack--;
                        }
                        pos++;
                    }
                    paras.add(temp.substring(paraBegin, pos));
                    varNum++;
                    pos++;
                }
                String target = function.funcReplace(paras);
                temp = temp.substring(0, beginPos) + target + temp.substring(pos);
                pos = beginPos;
            } else {
                pos++;
            }
        }
        return temp;
    }

    public String funcReplace(ArrayList<String> paras) {
        String funcExpr = funcString;
        int xvarIndex = funcVars.indexOf('x');
        if (xvarIndex != -1) {
            String xvarPara = paras.get(xvarIndex);
            xvarPara = String.format("(%s)", xvarPara);
            funcExpr = funcExpr.replaceAll("x", xvarPara);
        }
        int yvarIndex = funcVars.indexOf('y');
        if (yvarIndex != -1) {
            String yvarPara = paras.get(yvarIndex);
            yvarPara = String.format("(%s)", yvarPara);
            funcExpr = funcExpr.replaceAll("y", yvarPara);
        }
        int zvarIndex = funcVars.indexOf('z');
        if (zvarIndex != -1) {
            String zvarPara = paras.get(zvarIndex);
            zvarPara = String.format("(%s)", zvarPara);
            funcExpr = funcExpr.replaceAll("z", zvarPara);
        }
        return funcExpr;
    }

    public char getFuncName() {
        return funcName;
    }

    public ArrayList<Character> getFuncVars() {
        return funcVars;
    }

    public String getFuncString() {
        return funcString;
    }

    public int getFuncVarNum() {
        return funcVarNum;
    }

    public void printFunction() {
        System.out.println("funcName:" + funcName);
        System.out.println("funcList:" + funcVars);
        System.out.println("funcExpr:" + funcString);
    }
}
