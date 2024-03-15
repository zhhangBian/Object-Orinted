package formulation;

import calc.Poly;
import calc.Mono;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;

public class Expr implements Factor {
    private HashMap<Term, Boolean> terms;
    //private boolean negative;
    private int index;

    public Expr() {
        this.terms = new HashMap<>();
        this.index = 1;
    }

    @Override
    public void setIndex(int newindex) {
        this.index = newindex;
    }

    public void addTerm(Term term, boolean negative) {
        terms.put(term, negative);
    }

    public Factor replace(char source, Factor target) {
        Expr result = new Expr();
        result.setIndex(this.index);
        for (Term temp: terms.keySet()) {
            result.addTerm(temp.replace(source, target), terms.get(temp));
        }
        return result;
    }

    public Factor clonec() {
        Expr copy = new Expr();
        copy.setIndex(this.index);
        for (Term temp: terms.keySet()) {
            copy.addTerm(temp.clonec(), terms.get(temp));
        }
        return copy;
    }

    public Poly toPoly() {
        Poly newpoly = new Poly();
        if (index == 0) { // 0次方
            newpoly.addMono(new Mono(0, new BigInteger("1")));
            return newpoly;
        }
        Iterator<Term> it = terms.keySet().iterator();
        Term judhgeNeg = it.next();
        Poly temp = judhgeNeg.toPoly();
        if (terms.get(judhgeNeg)) { // 减号
            temp.inverse();
        }
        newpoly = temp;
        while (it.hasNext()) {
            //newpoly = newpoly.add(it.next().toPoly());
            judhgeNeg = it.next();
            temp = judhgeNeg.toPoly();
            if (terms.get(judhgeNeg)) { // 减号
                temp.inverse();
            }
            newpoly = newpoly.add(temp);
        }
        temp = newpoly;
        for (int i = 1; i < index; i++) { //乘方
            newpoly = newpoly.mult(temp);
        }
        return newpoly;
    }

    @Override
    public String toString() {
        Iterator<Term> iter = terms.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        Term temp = iter.next();
        sb.append("(");
        if (terms.get(temp)) { // if negative
            sb.append("-");
        }
        sb.append(temp.toString());
        while (iter.hasNext()) {
            temp = iter.next();
            if (terms.get(temp)) {  // if negative
                sb.append("-");
            } else {
                sb.append("+");
            }
            sb.append(temp.toString());
        }
        sb.append(")");
        if (index != 1) {
            sb.append("^");
            sb.append(index);
        }
        return sb.toString();
    }
}
