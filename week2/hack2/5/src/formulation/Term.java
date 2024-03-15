package formulation;

import calc.Poly;
import java.util.HashMap;
import java.util.Iterator;

public class Term {
    private HashMap<Factor, Boolean> factors;  // Constant refers to index

    private Constant index;

    public Term() {
        this.factors = new HashMap<>();
    }

    public void addFactor(Factor factor, boolean negative) {
        factors.put(factor, negative);
    }

    public Term replace(char source, Factor target) {
        Term result = new Term();
        for (Factor temp: factors.keySet()) {
            result.addFactor(temp.replace(source, target), factors.get(temp));
        }
        return result;
    }

    public Term clonec() {
        Term copy = new Term();
        for (Factor temp: factors.keySet()) {
            copy.addFactor(temp.clonec(), factors.get(temp));
        }
        return copy;
    }

    public Poly toPoly() {
        Poly newpoly;
        Iterator<Factor> it = factors.keySet().iterator();
        Factor judge = it.next();
        Poly temp = judge.toPoly();
        if (factors.get(judge)) { //减号
            temp.inverse();
        }
        newpoly = temp;
        while (it.hasNext()) {
            judge = it.next();
            temp = judge.toPoly();
            if (factors.get(judge)) {
                temp.inverse();
            }
            newpoly = newpoly.mult(temp);
        }
        return newpoly;
    }

    @Override
    public String toString() {
        Iterator<Factor> iter = factors.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append(" *");
            sb.append(iter.next().toString());
        }
        sb.append(")");
        return sb.toString();
    }
}
