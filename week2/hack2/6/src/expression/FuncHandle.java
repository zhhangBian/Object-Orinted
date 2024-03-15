package expression;

import mainhandle.Handle;

import java.util.ArrayList;
import java.util.HashMap;

public class FuncHandle {
    private static HashMap<String,String> funcMap = new HashMap<>(); //存储函数名和函数体
    private static HashMap<String, ArrayList<String>> funcParaMap = new HashMap<>(); //存储函数名和函数参数

    public static void addFunc(String input) {
        String now = input.replaceAll("[ \\t]","");
        StringBuilder sb = new StringBuilder(now);
        for (int i = 0; i < sb.length();i++) {
            if (sb.charAt(i) == 'e') {
                sb.replace(i + 1,i + 2,"r");
            }
        }
        now = sb.toString();
        String[] s = now.split("=");
        String part1 = s[0];
        String part2 = s[1];
        String[] s1 = part1.split("\\(");
        String funcName = s1[0];
        String[] s2 = s1[1].split("\\)");
        String[] s3 = s2[0].split(",");
        ArrayList<String> paraList = new ArrayList<>();
        char a = 'a';
        for (int i = 0; i < s3.length; i++) {
            paraList.add(String.valueOf(a));
            a++;
        }
        funcParaMap.put(funcName,paraList);
        Handle handle = new Handle(part2);
        part2 = handle.secondHandle();
        for (int i = 0; i < paraList.size(); i++) {
            part2 = part2.replaceAll(s3[i],paraList.get(i));
        }
        funcMap.put(funcName,part2);
    }

    public static String realFunc(String funcName, ArrayList<Factor> realParaList) {
        String func = funcMap.get(funcName);
        ArrayList<String> paraList = funcParaMap.get(funcName);
        for (int i = 0; i < paraList.size(); i++) {
            String para = realParaList.get(i).transPoly().toString();
            func = func.replaceAll(paraList.get(i),"(" + para + ")");
        }
        return func;
    }
}
