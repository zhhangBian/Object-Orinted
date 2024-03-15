package simplify;

import java.math.BigInteger;
import java.util.HashMap;

public class Mono {

    private  BigInteger coefficient;
    private  final String variable;
    private  BigInteger index;
    private HashMap<BigInteger, Poly> expExpr;

    public Mono(BigInteger coefficient, String variable, BigInteger index) {
        this.coefficient = coefficient;
        this.variable = variable;
        this.index = index;
        this.expExpr = new HashMap<>();
    }

    public HashMap<BigInteger, Poly> getExpExpr() {
        return this.expExpr;
    }

    public void addExpExpr(BigInteger index, Poly poly) {
        if (this.expExpr.containsKey(index)) {
            this.expExpr.get(index).add(poly);
        } else {
            this.expExpr.put(index, poly);
        }
    }

    public Mono cloneMono() {
        HashMap<BigInteger, Poly> expExpr1 = new HashMap<>();
        for (BigInteger index : this.expExpr.keySet()) {
            Poly poly = this.expExpr.get(index);
            expExpr1.put(index, poly.clonePoly());
        }
        Mono mono = new Mono(this.coefficient, this.variable, this.index);
        mono.setExpExpr(expExpr1);
        return mono;
    }

    public void setExpExpr(HashMap<BigInteger, Poly> expExpr) {
        this.expExpr.clear();
        for (BigInteger index : expExpr.keySet()) {
            Poly poly = expExpr.get(index).clonePoly();
            this.expExpr.put(index, poly);
        }
    }

    public BigInteger getCoefficient() {
        return this.coefficient;
    }

    public String getVariable() {
        return this.variable;
    }

    public BigInteger getIndex() {
        return this.index;
    }

    public void addCoefficient(BigInteger coefficient) {
        this.coefficient = this.coefficient.add(coefficient);
    }

    public String toString() {
        if (this.coefficient.equals(new BigInteger("0"))) {
            return "";
        } else if (this.index.equals(BigInteger.ZERO)) {
            if (this.coefficient.equals(new BigInteger("1"))) {
                if (this.expExpr.isEmpty()) {
                    return this.coefficient.toString();
                } else {
                    StringBuilder exp = new StringBuilder();
                    exp.append(expToString());
                    if (!exp.toString().isEmpty() && exp.toString().charAt(0) == '*') {
                        exp.deleteCharAt(0);
                        return exp.toString();
                    } else {
                        return this.coefficient.toString();
                    }
                }
            } else if (this.coefficient.equals(new BigInteger("-1"))) {
                if (this.expExpr.isEmpty()) {
                    return this.coefficient.toString();
                } else {
                    StringBuilder exp = new StringBuilder();
                    exp.append(expToString());
                    if (!exp.toString().isEmpty() && exp.toString().charAt(0) == '*') {
                        exp.deleteCharAt(0);
                        return "-" + exp.toString();
                    } else {
                        return this.coefficient.toString();
                    }
                }
            } else {
                return this.coefficient.toString() + expToString();
            }
        } else if (this.coefficient.equals(new BigInteger("1"))) {
            if (this.index.equals(BigInteger.ONE)) {
                return this.variable + expToString();
            } else {
                return this.variable + "^" + this.index.toString() + expToString();
            }
        } else if (this.coefficient.equals(new BigInteger("-1"))) {
            if (this.index.equals(BigInteger.ONE)) {
                return "-" + this.variable + expToString();
            } else {
                return "-" + this.variable + "^" + this.index.toString() + expToString();
            }
        } else if (this.index.equals(BigInteger.ONE)) {
            return this.coefficient.toString() + "*" + this.variable + expToString();
        } else {
            String s = this.index.toString();
            return this.coefficient.toString() + "*" + this.variable + "^" + s + expToString();
        }
    }

    private String expToString() {
        StringBuilder result = new StringBuilder();
        for (BigInteger index : this.expExpr.keySet()) {
            Poly poly = this.expExpr.get(index);
            if (index.equals(BigInteger.ZERO)) {
                result.append("");
            } else if (index.equals(BigInteger.ONE)) {
                if (poly.toString().equals("0")) {
                    result.append("");
                } else {
                    Boolean flag1 = poly.toString().contains("+");
                    Boolean flag2 = poly.toString().contains("-");
                    Boolean flag3 = poly.toString().contains("*");
                    if (flag1 || flag2 || flag3) {
                        result.append("*exp((" + poly.toString() + "))");
                    } else {
                        result.append("*exp(" + poly.toString() + ")");
                    }
                }
            } else {
                if (poly.toString().equals("0")) {
                    result.append("");
                } else {
                    Boolean flag1 = poly.toString().contains("+");
                    Boolean flag2 = poly.toString().contains("-");
                    Boolean flag3 = poly.toString().contains("*");
                    if (flag1 || flag2 || flag3) {
                        result.append("*exp((" + poly.toString() + "))^" + index.toString());
                    } else {
                        result.append("*exp(" + poly.toString() + ")^" + index.toString());
                    }
                }
            }
        }
        return result.toString();
    }
}


