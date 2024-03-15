public class DefFunc {
    private char name; //函数名

    private int num; //参数数量

    private String[] varString; //参数列，如“XYZ”

    private String funcContent; //函数表达式

    public DefFunc(String str) {
        String s = str;
        Processor processor = new Processor();
        s = processor.processFunc(s);
        s = s.replaceAll("x", "a").replaceAll("y", "b")
                .replaceAll("z", "c");


        String[] parts = s.split("=");

        name = parts[0].charAt(0);
        String vars = parts[0].substring(parts[0].indexOf("(") + 1, parts[0].indexOf(")"));
        varString = vars.split(",");
        funcContent = parts[1].trim();

        num = varString.length;
    }

    public String substitute(String str) {
        String s = str;
        int funcPos = str.indexOf(name); //找到函数名的位置,找不到返回-1
        if (funcPos == -1) {
            return s;
        }
        else {
            int count = 0;
            String frontString = s.substring(0, funcPos); //假如pos=0，返回空字符串
            String[] actVarList = new String[num];
            int pos = funcPos + 2;
            for (int i = funcPos + 1, j = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(') {
                    count++;
                }
                else if (s.charAt(i) == ')') {
                    count--;
                }

                if (s.charAt(i) == ',' && count == 1) {
                    actVarList[j] = s.substring(pos, i);
                    j++;
                    pos = i + 1;
                }

                if (s.charAt(i) == ')' && count == 0) {
                    actVarList[j] = s.substring(pos, i);
                    String middleString = s.substring(funcPos + 2, i);
                    middleString = replaceFunc(actVarList);
                    String lastString = s.substring(i + 1, s.length());
                    return frontString  + "(" + middleString + ")" + lastString;
                }
            }
            return null;
        }
    }

    public String replaceFunc(String[] actList) {
        String s = funcContent;
        for (int j = 0; j < num; j++) {
            s = s.replaceAll(varString[j], "(" + actList[j] + ")");
        }
        return s;
    }

    /*
    public static void main(String[] args) {
        String str = "f(x,y,z)=x^2+y^2+exp(exp(z+x))";
        DefFunc a = new DefFunc(str);
        System.out.println(a.name);
        System.out.println(a.funcContent);
        for (int i = 0; i < a.varString.length; i++) {
            System.out.println(a.varString[i]);
        }
        System.out.println(a.num);
    }*/
}
