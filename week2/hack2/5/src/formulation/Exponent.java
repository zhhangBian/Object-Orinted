package formulation;

import calc.Poly;
import calc.Epow;

import java.math.BigInteger;

public class Exponent implements Factor {
    private Factor expo;
    private int index;

    public Exponent(Factor expo) {
        this.expo = expo;
        this.index = 0;
    }

    @Override
    public void setIndex(int input) {
        this.index = input;
    }

    public Factor replace(char source, Factor target) {
        return new Exponent(expo.replace(source, target));
    }

    public Factor clonec() {
        return new Exponent(expo);
    }

    public Poly toPoly() {
        Epow newEpow;
        if (expo instanceof Expr) {
            newEpow = new Epow(index, new BigInteger("1"), expo.toPoly(), true); // 指数， 系数， 幂表达式
        } else {
            newEpow = new Epow(index, new BigInteger("1"), expo.toPoly(), false); // 指数， 系数， 幂表达式
        }
        Poly newpoly = new Poly();
        newpoly.addMono(newEpow);
        return newpoly;
    }

    @Override
    public String toString() {
        return "exp(" + expo.toString() + ")";
    }
}
