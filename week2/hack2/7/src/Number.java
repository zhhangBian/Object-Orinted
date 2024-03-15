import java.math.BigInteger;

public class Number implements Factor {
    private BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public String toString() {
        return String.valueOf(num);
    }

    public Poly toPoly() {
        Poly numPoly = new Poly();
        Mono mono = new Mono(num, BigInteger.ZERO, Poly.getZero());
        numPoly.addMono(mono);
        return numPoly;
    }
}
