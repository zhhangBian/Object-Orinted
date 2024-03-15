import java.math.BigInteger;

public class Mono {
    private BigInteger coef;

    private BigInteger index;

    private Poly exponent;

    public Mono(BigInteger coef, BigInteger index, Poly exponent) {
        this.coef = coef;
        this.index = index;
        this.exponent = exponent;
    }

    public Mono createSame() {
        BigInteger newCoef = coef;
        BigInteger newIndex = index;
        if (exponent == null) {
            Mono newMono = new Mono(newCoef, newIndex, null);
            return newMono;
        }
        else {
            Poly newPoly = exponent.createSame();
            Mono newMono = new Mono(newCoef, newIndex, newPoly);
            return newMono;
        }
    }

    public BigInteger getIndex() {
        return index;
    }

    public BigInteger getCoef() {
        return coef;
    }

    public void setCoef(BigInteger coef) {
        this.coef = coef;
    }

    public Poly getExponent() {
        return exponent;
    }

    public boolean canMerge(Mono mono) {
        boolean indexEqual = index.equals(mono.getIndex());
        boolean exponentEqual = false;
        if (exponent == null && mono.getExponent() == null) {
            exponentEqual = true;
        }
        else if (exponent != null && mono.getExponent() != null) {
            exponentEqual = exponent.equals(mono.getExponent());
        }
        else {
            exponentEqual = false;
        }
        return indexEqual & exponentEqual;
    }
}
