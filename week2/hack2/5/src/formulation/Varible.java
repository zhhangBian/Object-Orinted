package formulation;

import calc.Poly;
import calc.Mono;

import java.math.BigInteger;

public class  Varible implements Factor {
    private int index;

    private char base;

    public Varible(int index, char base) {
        this.index = index;
        this.base = base;
    }

    @Override
    public void setIndex(int newindex) {
        this.index = newindex;
    }

    public Factor replace(char source, Factor target) {
        if (base == source) {
            Expr result = (Expr) target.clonec();
            result.setIndex(index);
            return result;
        } else {
            return this.clonec();
        }
    }

    public Factor clonec() {
        return new Varible(index, base);
    }

    public Poly toPoly() {
        Mono newmono = new Mono(index, new BigInteger("1"));
        Poly newpoly = new Poly();
        newpoly.addMono(newmono);
        return newpoly;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(base);
        if (index != 1) {
            sb.append("^");
            sb.append(index);
        }
        sb.append(")");
        return sb.toString();
    }
}
