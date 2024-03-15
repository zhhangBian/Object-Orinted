import java.math.BigInteger;

public class Power implements Factor {
    private BigInteger exp;

    public Power(BigInteger exp) {
        this.exp = exp;
    }

    public String toString() {
        if (exp.equals(BigInteger.ZERO)) {
            return "1";
        } else if (exp.equals(BigInteger.ONE)) {
            return "x";
        } else {
            return "x^" + exp;
        }
    }

    public Poly toPoly() {
        Poly powerPoly = new Poly();
        Mono mono = new Mono(BigInteger.ONE, exp, Poly.getZero());
        powerPoly.addMono(mono);
        return powerPoly;
    }
}
