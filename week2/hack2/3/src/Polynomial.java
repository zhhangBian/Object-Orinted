import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Polynomial {
    private final HashMap<PolyKey, BigInteger> coefMap = new HashMap<>();

    public Polynomial() {
    }

    public Polynomial(PolyKey polyKey, BigInteger coef) {
        this.addItem(polyKey, coef);
    }

    public void addItem(PolyKey polyKey, BigInteger coef) {
        this.coefMap.put(polyKey, coef);
    }

    public void addPoly(Polynomial right) {
        for (Map.Entry<PolyKey, BigInteger> entry : right.coefMap.entrySet()) {
            PolyKey keyR = entry.getKey();
            BigInteger coefR = entry.getValue();
            if (this.coefMap.containsKey(keyR)) {
                addItem(keyR, coefR.add(this.coefMap.get(keyR)));
            } else {
                addItem(keyR, coefR);
            }
        }
    }

    public void subPoly(Polynomial right) {
        for (Map.Entry<PolyKey, BigInteger> entry : right.coefMap.entrySet()) {
            PolyKey keyR = entry.getKey();
            BigInteger coefR = entry.getValue();
            if (this.coefMap.containsKey(keyR)) {
                addItem(keyR, this.coefMap.get(keyR).subtract(coefR));
            } else {
                addItem(keyR, coefR.negate());
            }
        }
    }

    public void multPoly(Polynomial right) {
        Polynomial res = new Polynomial();
        for (Map.Entry<PolyKey, BigInteger> entryL : this.coefMap.entrySet()) {
            for (Map.Entry<PolyKey, BigInteger> entryR : right.coefMap.entrySet()) {
                PolyKey polyKey = entryL.getKey().multiplyKey(entryR.getKey());
                BigInteger coef = entryL.getValue().multiply(entryR.getValue());
                res.addPoly(new Polynomial(polyKey, coef));
            }
        }
        this.coefMap.clear();
        this.coefMap.putAll(res.coefMap);
    }

    public void power(int exp) {
        if (exp == 0) {
            this.coefMap.clear();
            this.addItem(new PolyKey(), BigInteger.ONE);
            return;
        }
        Polynomial newPoly = new Polynomial();
        newPoly.coefMap.putAll(this.coefMap);
        for (int i = 1; i < exp; i++) {
            this.multPoly(newPoly);     //  this.multPoly(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean hasValue = false;
        for (Map.Entry<PolyKey, BigInteger> entry : this.coefMap.entrySet()) {
            final PolyKey polyKey = entry.getKey();
            final BigInteger coef = entry.getValue();
            final String polyKeyStr = polyKey.toString();

            if (coef.compareTo(BigInteger.ZERO) == 0) {
                continue;  // coef == 0
            }

            if (hasValue && coef.compareTo(BigInteger.ZERO) > 0) {
                sb.append("+"); // coef > 0
            }
            hasValue = true;

            if (coef.compareTo(BigInteger.ONE) == 0) { // coef == 1
                sb.append(polyKeyStr);
            } else if (coef.compareTo(BigInteger.ONE.negate()) == 0) {
                sb.append("-").append(polyKeyStr);
            } else {
                sb.append(coef);
                if (!polyKeyStr.equals("1")) {
                    sb.append("*").append(polyKeyStr);
                }
            }
        }
        if (!hasValue) {
            return "0";
        }
        return sb.toString();
    }
}
