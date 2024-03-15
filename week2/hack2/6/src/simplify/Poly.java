package simplify;

import expression.Number;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Poly {

    private final ArrayList<Mono> monos;

    public Poly() {
        this.monos = new ArrayList<>();
    }

    public void addMono(Mono mono) {
        this.monos.add(mono);
    }

    public Poly clonePoly() {
        ArrayList<Mono> monos1 = new ArrayList<>();
        for (Mono mono : this.monos) {
            monos1.add(mono.cloneMono());
        }
        Poly poly = new Poly();
        poly.monos.addAll(monos1);
        return poly;
    }

    public Poly add(Poly poly2) {
        Poly result = new Poly();
        for (Mono mono : this.monos) {
            result.addMono(mono);
        }
        for (Mono mono2 : poly2.monos) {
            boolean flag = false;
            for (Mono mono1 : result.monos) {
                if (equals(mono1, mono2)) {
                    mono1.addCoefficient(mono2.getCoefficient());
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                result.addMono(mono2);
            }
        }
        return result;
    }

    public Poly mul(Poly poly2) {
        final Poly result = new Poly();
        for (Mono mono1 : this.monos) {
            for (Mono mono2 : poly2.monos) {
                BigInteger newCoefficient = mono1.getCoefficient().multiply(mono2.getCoefficient());
                String newVariable = mono1.getVariable();
                BigInteger newIndex = mono1.getIndex().add(mono2.getIndex());
                HashMap<BigInteger,Poly> newExpExpr = new HashMap<>();
                for (BigInteger index : mono1.getExpExpr().keySet()) {
                    Poly poly = mono1.getExpExpr().get(index);
                    Poly poly1 = poly.clonePoly();
                    newExpExpr.put(index, poly1);
                }
                for (BigInteger index : mono2.getExpExpr().keySet()) {
                    Poly poly = mono2.getExpExpr().get(index);
                    if (newExpExpr.containsKey(index)) {
                        newExpExpr.put(index, newExpExpr.get(index).add(poly));
                    } else {
                        Poly poly1 = poly.clonePoly();
                        newExpExpr.put(index, poly1);
                    }
                }
                Mono mono3 = new Mono(newCoefficient, newVariable, newIndex);
                mono3.setExpExpr(newExpExpr);
                boolean flag = false;
                for (Mono mono : result.monos) {
                    if (equals(mono, mono3)) {
                        flag = true;
                        mono.addCoefficient(newCoefficient);
                        break;
                    }
                }
                if (!flag) {
                    result.addMono(mono3);
                }
            }
        }
        return result;
    }

    public Poly pow(BigInteger n) {
        Poly result;
        if (n.equals(BigInteger.ONE)) {
            return this;
        } else {
            result = this.clonePoly();
            for (BigInteger i = BigInteger.ONE; i.compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
                result = result.mul(this);
            }
            return result;
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        int flag = 0;
        String monoString1 = "";
        for (Mono mono : this.monos) {
            String monoString = mono.toString();
            if (flag == 0 && !monoString.isEmpty() && monoString.charAt(0) != '-') {
                monoString1 = monoString;
                flag = 1;
            } else {
                if (result.length() > 0 && !monoString.isEmpty() && monoString.charAt(0) != '-') {
                    result.append("+");
                }
                result.append(mono.toString());
            }
        }
        if (flag == 1) {
            if (!result.toString().isEmpty() && result.toString().charAt(0) != '-') {
                result.insert(0, monoString1 + "+");
            } else {
                result.insert(0, monoString1);
            }
        }
        if (result.length() == 0) {
            return ("0");
        } else {
            return result.toString();
        }
    }

    public Poly changeSign() {
        Poly result = new Poly();
        for (Mono mono : this.monos) {
            BigInteger newCoefficient = mono.getCoefficient().negate();
            Mono newMono = new Mono(newCoefficient, mono.getVariable(), mono.getIndex());
            newMono.setExpExpr(mono.getExpExpr());
            result.addMono(newMono);
        }
        return result;
    }

    private boolean equals(Mono mono1, Mono mono2) {
        boolean flag = false;
        boolean flag1 = mono1.getIndex().equals(mono2.getIndex());
        boolean flag2 = ExpEqual(mono1, mono2);
        if (flag1 && flag2) {
            flag = true;
        }
        return flag;
    }

    private boolean equals1(Mono mono1, Mono mono2) {
        boolean flag1 = mono1.getCoefficient().equals(mono2.getCoefficient());
        boolean flag2 = mono1.getIndex().equals(mono2.getIndex());
        boolean flag3 = ExpEqual(mono1, mono2);
        return flag1 && flag2 && flag3;
    }

    private boolean ExpEqual(Mono mono1,Mono mono2) {
        if (mono1.getExpExpr().isEmpty() && mono2.getExpExpr().isEmpty()) {
            return true;
        }
        Poly poly1 = new Poly();
        Poly poly2 = new Poly();
        Poly poly;
        for (BigInteger index : mono1.getExpExpr().keySet()) {
            poly = mono1.getExpExpr().get(index).clonePoly();
            Number number = new Number(new BigInteger(index.toString()));
            poly = poly.mul(number.transPoly());
            poly1 = poly1.add(poly);
        }
        for (BigInteger index : mono2.getExpExpr().keySet()) {
            poly = mono2.getExpExpr().get(index).clonePoly();
            Number number = new Number(new BigInteger(index.toString()));
            poly = poly.mul(number.transPoly());
            poly2 = poly2.add(poly);
        }
        return polyEquals(poly1, poly2);
    }

    private boolean polyEquals(Poly poly1, Poly poly2) {
        boolean flag1 = false;
        boolean flag2 = false;
        if (poly1.monos.isEmpty()) {
            flag1 = true;
        }
        if (poly2.monos.isEmpty()) {
            flag2 = true;
        }
        for (Mono mono1 : poly1.monos) {
            flag1 = false;
            if (mono1.getCoefficient().equals(BigInteger.ZERO)) {
                flag1 = true;
                continue;
            }
            for (Mono mono2 : poly2.monos) {
                if (equals1(mono1, mono2)) {
                    flag1 = true;
                    break;
                }
            }
            if (!flag1) {
                break;
            }
        }
        for (Mono mono2 : poly2.monos) {
            flag2 = false;
            if (mono2.getCoefficient().equals(BigInteger.ZERO)) {
                flag2 = true;
                continue;
            }
            for (Mono mono1 : poly1.monos) {
                if (equals1(mono1, mono2)) {
                    flag2 = true;
                    break;
                }
            }
            if (!flag2) {
                break;
            }
        }
        return flag1 && flag2;
    }
}
