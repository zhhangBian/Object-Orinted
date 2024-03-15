import java.math.BigInteger;

public class EeeFactor implements Factor {
    private Factor expFactor;

    private BigInteger index;

    public EeeFactor(Factor expFactor, BigInteger index) {
        this.expFactor = expFactor;
        this.index = index;
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        Poly proExpFactor = expFactor.toPoly().multNum(index); //把exp(factor)^b变成exp(factor*b)
        Mono mono = new Mono(BigInteger.ONE, BigInteger.ZERO, proExpFactor);
        poly.addMono(mono);
        return poly;
    }

    public String toString() {
        return "e(" + expFactor + "*" + index + ")";
    }
}
