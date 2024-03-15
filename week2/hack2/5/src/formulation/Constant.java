package formulation;

import calc.Poly;
import calc.Mono;
import java.math.BigInteger;

public class Constant implements Factor {
    private BigInteger num;
    private int index;

    public Constant(String num) {
        this.num = new BigInteger(num);
    }

    public void setIndex(int newindex) {
        this.index = newindex;
    }

    public Factor replace(char source, Factor target) {
        return new Constant(num.toString());
    }

    public Factor clonec() {
        return new Constant(num.toString());
    }

    public Poly toPoly() {
        Mono newmono = new Mono(0, num);
        Poly newpoly = new Poly();
        newpoly.addMono(newmono);
        return newpoly;
    }

    @Override
    public String toString() {
        if (num == null) {
            return " ";
        }
        return num.toString();
    }
}
