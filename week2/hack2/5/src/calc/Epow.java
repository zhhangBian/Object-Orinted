package calc;

import java.math.BigInteger;

public class Epow extends Mono {

    private Poly pow;
    private boolean parenthesis;

    public Epow(int index, BigInteger coffe, Poly pow, boolean necessary) {
        super(index, coffe);
        this.pow = pow;
        this.parenthesis = necessary;
    }

    @Override
    public boolean likeTerm(Mono check) {
        if (check instanceof Epow) {
            return super.likeTerm(check) && this.pow.equals(((Epow) check).pow);
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Mono compare) {
        return false;
    }

    @Override
    public Mono clone() {
        BigInteger replace = new BigInteger(this.getCoffe().toString());
        return new Epow(super.getIndex(), replace, this.pow.add(null), parenthesis);
    }

    @Override
    public Mono mult(Mono mono2) {
        if (mono2 instanceof Epow) {
            int newIndex = super.getIndex() + mono2.getIndex();
            BigInteger newCoffe = super.getCoffe().multiply(mono2.getCoffe());
            Poly newPow = this.pow.add(((Epow)mono2).pow);
            boolean newPare = this.parenthesis | ((Epow) mono2).parenthesis;
            return new Epow(newIndex, newCoffe, newPow, newPare);
        } else {
            int newIndex = super.getIndex() + mono2.getIndex();
            BigInteger newCoffe = super.getCoffe().multiply(mono2.getCoffe());
            Poly newPow = this.pow.add(null);
            boolean newPare = this.parenthesis;
            return new Epow(newIndex, newCoffe, newPow, newPare);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String monopart = super.toString();
        if (monopart.equals("-1")) {
            sb.append("-");
        } else if (!monopart.equals("1")) {
            sb.append(monopart);
            sb.append("*");
        }

        if (monopart.equals("0")) {
            return "0";
        } else {
            if (parenthesis) { // if expr with parenthesis
                sb.append("exp((").append(pow.toString()).append("))");
            } else {
                sb.append("exp(").append(pow.toString()).append(")");
            }
        }
        return sb.toString();
    }
}
