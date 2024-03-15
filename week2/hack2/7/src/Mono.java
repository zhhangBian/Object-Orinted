import java.math.BigInteger;
import java.util.Objects;

public class Mono implements Factor, Comparable<Mono> {
    private BigInteger coe;
    private BigInteger exp;
    private Poly expPoly;

    public Mono(BigInteger coe, BigInteger exp, Poly poly) {
        this.coe = coe;
        this.exp = exp;
        this.expPoly = poly;
    }

    public static Boolean monoEqual(Mono mono1, Mono mono2) {
        if (mono1.getCoe().equals(BigInteger.ZERO) && mono2.getCoe().equals(BigInteger.ZERO)) {
            return true;
        } else if (mono1.getCoe().equals(mono2.getCoe()) &&
                Objects.equals(mono1.getExp(), mono2.getExp())) {
            if (mono1.getExpPoly() == null && mono2.getExpPoly() == null) {
                return true;
            } else {
                return Poly.polyEqual(mono1.getExpPoly(), mono2.getExpPoly());
            }
        } else {
            return false;
        }
    }

    public static Boolean monoCanMerge(Mono mono1, Mono mono2) {
        if (mono1.coe.equals(BigInteger.ZERO) && mono2.getCoe().equals(BigInteger.ZERO)) {
            return true;
        }
        if (Objects.equals(mono1.getExp(), mono2.getExp())) {
            if (mono1.getExpPoly() == null && mono2.getExpPoly() == null) {
                return true;
            } else {
                return Poly.polyEqual(mono1.getExpPoly(), mono2.getExpPoly());
            }
        } else {
            return false;
        }
    }

    public static Mono monoTime(int time, Mono mono) {
        return new Mono(mono.getCoe().multiply(BigInteger.valueOf(time)),
                mono.getExp(),
                mono.getExpPoly());
    }

    public static Mono monoMul(Mono mono1, Mono mono2) {
        BigInteger coe = mono1.getCoe().multiply(mono2.getCoe());
        BigInteger exp = mono1.getExp().add(mono2.getExp());
        Poly poly = Poly.polyAdd(mono1.getExpPoly(), mono2.getExpPoly());
        return new Mono(coe, exp, poly);
    }

    public void mergeMono(Mono mono) {
        this.coe = this.coe.add(mono.getCoe());
    }

    public static Mono addMono(Mono mono1, Mono mono2) {
        BigInteger coe = mono1.getCoe().add(mono2.getCoe());
        return new Mono(coe, mono1.getExp(), mono1.getExpPoly());
    }

    public String toString() {  //need to update
        if (coe.compareTo(BigInteger.ZERO) == 0) {
            return "";
        }
        if (exp.equals(BigInteger.ZERO)) {
            return toStringExp0();
        } else if (exp.equals(BigInteger.ONE)) {
            return toStringExp1();
        } else {
            return toStringExpElse();
        }
    }

    private String toStringExp0() {
        String sign;
        if (coe.compareTo(BigInteger.ZERO) > 0) {
            sign = "+";
        } else {
            sign = "";
        }
        if (expPoly == null || Poly.polyEqual(expPoly, Poly.getZero())) {
            return sign + coe;
        } else {
            Boolean flag = Poly.isBaseFactor(expPoly);
            if (coe.compareTo(BigInteger.ONE) == 0) {
                if (flag) {
                    return "+exp(" + expPoly.toString() + ")";
                } else {
                    return "+exp((" + expPoly.toString() + "))";
                }
            } else if (coe.compareTo(new BigInteger("-1")) == 0) {
                if (flag) {
                    return "-exp(" + expPoly.toString() + ")";
                } else {
                    return "-exp((" + expPoly.toString() + "))";
                }
            } else {
                if (flag) {
                    return sign + coe + "*exp(" + expPoly.toString() + ")";
                } else {
                    return sign + coe + "*exp((" + expPoly.toString() + "))";
                }
            }
        }
    }

    private String toStringExp1() {
        String sign;
        if (coe.compareTo(BigInteger.ZERO) > 0) {
            sign = "+";
        } else {
            sign = "";
        }
        if (expPoly == null || Poly.polyEqual(expPoly, Poly.getZero())) {
            if (coe.compareTo(BigInteger.ONE) == 0) {
                return "+x";
            } else if (coe.compareTo(new BigInteger("-1")) == 0) {
                return "-x";
            } else {
                return sign + coe + "*x";
            }
        } else {
            Boolean flag = Poly.isBaseFactor(expPoly);
            if (coe.compareTo(BigInteger.ONE) == 0) {
                if (flag) {
                    return "+x*exp(" + expPoly.toString() + ")";
                } else {
                    return "+x*exp((" + expPoly.toString() + "))";
                }
            } else if (coe.compareTo(new BigInteger("-1")) == 0) {
                if (flag) {
                    return "-x*exp(" + expPoly.toString() + ")";
                } else {
                    return "-x*exp((" + expPoly.toString() + "))";
                }
            } else {
                if (flag) {
                    return sign + coe + "*x*exp(" + expPoly.toString() + ")";
                } else {
                    return sign + coe + "*x*exp((" + expPoly.toString() + "))";
                }
            }
        }
    }

    private String toStringExpElse() {
        String sign;
        if (coe.compareTo(BigInteger.ZERO) > 0) {
            sign = "+";
        } else {
            sign = "";
        }
        if (expPoly == null || Poly.polyEqual(expPoly, Poly.getZero())) {
            if (coe.compareTo(BigInteger.ONE) == 0) {
                return "+x^" + exp;
            } else if (coe.compareTo(new BigInteger("-1")) == 0) {
                return "-x^" + exp;
            } else {
                return sign + coe + "*x^" + exp;
            }
        } else {
            Boolean flag = Poly.isBaseFactor(expPoly);
            if (coe.compareTo(BigInteger.ONE) == 0) {
                if (flag) {
                    return "+x^" + exp + "*exp(" + expPoly.toString() + ")";
                } else {
                    return "+x^" + exp + "*exp((" + expPoly.toString() + "))";
                }
            } else if (coe.compareTo(new BigInteger("-1")) == 0) {
                if (flag) {
                    return "-x^" + exp + "*exp(" + expPoly.toString() + ")";
                } else {
                    return "-x^" + exp + "*exp((" + expPoly.toString() + "))";
                }
            } else {
                if (flag) {
                    return sign + coe + "*x^" + exp + "*exp(" + expPoly.toString() + ")";
                } else {
                    return sign + coe + "*x^" + exp + "*exp((" + expPoly.toString() + "))";
                }
            }
        }
    }

    public boolean isPos() {
        return this.coe.compareTo(BigInteger.ZERO) > 0;
    }

    public void neg() {
        this.coe = this.coe.negate();
    }

    public BigInteger getCoe() {
        return coe;
    }

    public BigInteger getExp() {
        return exp;
    }

    public Poly getExpPoly() {
        return expPoly;
    }

    @Override
    public int compareTo(Mono o) {
        return o.getExp().subtract(this.exp).compareTo(BigInteger.ZERO);
    }
}
