import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class Unit implements Factor {
    private BigInteger cofficient;
    private TreeMap<String, Polynome> polyList;
    private Expression expPower;

    public Unit(BigInteger cofficient) {
        this.cofficient = cofficient;
        this.polyList = new TreeMap<>();
        this.expPower = new Expression();
    }

    public Unit(Polynome polynome) {
        this.cofficient = BigInteger.ONE;
        this.polyList = new TreeMap<>();
        this.polyList.put(polynome.GetPolyName(), polynome);
        this.expPower = new Expression();
    }

    public Unit(Expression expPower) {
        this.cofficient = BigInteger.ONE;
        this.polyList = new TreeMap<>();
        this.expPower = expPower;
    }

    public Unit(BigInteger cofficient, TreeMap<String, Polynome> polyList, Expression expPower) {
        this.cofficient = cofficient;
        this.polyList = polyList;
        this.expPower = expPower;
    }

    public boolean IfIsZero() {
        return this.cofficient.equals(BigInteger.ZERO);
    }

    public boolean IfIsOne() {
        return this.cofficient.equals(BigInteger.ONE) &&
                this.polyList.isEmpty() && this.expPower.isEmpty();
    }

    public boolean IsNegative() {
        return this.cofficient.compareTo(BigInteger.ZERO) < 0;
    }

    public void negate() {
        this.cofficient = this.cofficient.negate();
    }

    @Override
    public Expression derivation(String polyName) {
        Expression resultExpression = new Expression();

        Expression expResult = expPower.derivation(polyName).
                MulExpression(new Expression((Unit) this.clone()));
        resultExpression.AddExpression(expResult);

        if (polyName.equals("D")) {
            for (Map.Entry<String, Polynome> entry : this.polyList.entrySet()) {
                Expression polyResult = GetPolyDerivation(entry.getKey());
                resultExpression.AddExpression(polyResult);
            }
        } else {
            Expression polyResult = GetPolyDerivation(polyName);
            resultExpression.AddExpression(polyResult);
        }

        return resultExpression;
    }

    private Expression GetPolyDerivation(String polyName) {
        if (!this.polyList.containsKey(polyName)) {
            return new Expression();
        } else {
            Unit resultUnit = (Unit) this.clone();
            Polynome polynome = resultUnit.polyList.get(polyName);
            if (polynome.GetPolyPower().equals(BigInteger.ONE)) {
                resultUnit.polyList.remove(polyName);
            } else {
                resultUnit.cofficient = resultUnit.cofficient.multiply(polynome.GetPolyPower());
                polynome.derivation();
            }

            return new Expression(resultUnit);
        }
    }

    public void AddUnit(Unit otherUnit) {
        this.cofficient = this.cofficient.add(otherUnit.cofficient);

        if (this.cofficient.equals(BigInteger.ZERO)) {
            this.polyList.clear();
            this.expPower = new Expression();
        }
    }

    public void MulUnit(Unit otherUnit) {
        this.cofficient = this.cofficient.multiply(otherUnit.cofficient);

        if (this.cofficient.equals(BigInteger.ZERO)) {
            this.polyList.clear();
            this.expPower = new Expression();
            return;
        }

        this.expPower.AddExpression(otherUnit.expPower);

        TreeMap<String, Polynome> otherList = otherUnit.polyList;
        for (Map.Entry<String, Polynome> entry : otherList.entrySet()) {
            Polynome newPoly = entry.getValue();
            if (polyList.containsKey(newPoly.GetPolyName())) {
                polyList.get(newPoly.GetPolyName()).MulPoly(newPoly.GetPolyPower());
            } else {
                polyList.put(newPoly.GetPolyName(), newPoly);
            }
        }
    }

    public BigInteger GetUnitGcd(BigInteger oldGcd) {
        if (oldGcd.equals(BigInteger.ZERO)) {
            return this.cofficient.compareTo(BigInteger.ZERO) > 0 ?
                    this.cofficient : this.cofficient.negate();
        } else {
            return this.cofficient.gcd(oldGcd);
        }
    }

    public Unit GetUnitDivideNum(BigInteger num) {
        Unit unitClone = (Unit) this.clone();
        return new Unit(unitClone.cofficient.divide(num), unitClone.polyList, unitClone.expPower);
    }

    @Override
    public Factor clone() {
        TreeMap<String, Polynome> newPolyList = new TreeMap<>();
        for (Map.Entry<String, Polynome> entry : this.polyList.entrySet()) {
            newPolyList.put(entry.getKey(), entry.getValue().clone());
        }
        Expression newExpression = (Expression) this.expPower.clone();

        return new Unit(this.cofficient, newPolyList, newExpression);
    }

    @Override
    public boolean equals(Factor otherFactor) {
        if (!(otherFactor instanceof Unit)) {
            return false;
        }

        Unit otherUnit = (Unit) otherFactor;
        return this.IfSimilarUnit(otherUnit) && this.cofficient.equals(otherUnit.cofficient);
    }

    public boolean IfSimilarUnit(Unit otherUnit) {
        if (!(this.polyList.size() == otherUnit.polyList.size() &&
                this.expPower.equals(otherUnit.expPower))) {
            return false;
        }

        TreeMap<String, Polynome> otherList = otherUnit.polyList;
        for (Map.Entry<String, Polynome> entry : polyList.entrySet()) {
            if (!otherList.containsKey(entry.getKey()) ||
                    !entry.getValue().equals(otherList.get(entry.getKey()))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        if (this.cofficient.equals(BigInteger.ZERO)) {
            return "0";
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (this.polyList.isEmpty() && this.expPower.isEmpty()) {
            stringBuilder.append(cofficient);
        } else {
            boolean isFirst = this.PrintCofficient(stringBuilder);
            isFirst = this.PrintPoly(isFirst, stringBuilder);
            this.PrintExpFunc(isFirst, stringBuilder);
        }
        return stringBuilder.toString();
    }

    private boolean PrintCofficient(StringBuilder stringBuilder) {
        boolean isFirst = true;
        if (this.cofficient.equals(BigInteger.ONE) ||
                this.cofficient.equals(BigInteger.valueOf(-1))) {
            stringBuilder.append(this.cofficient.equals(BigInteger.valueOf(-1)) ? "-" : "");
        } else {
            stringBuilder.append(this.cofficient);
            isFirst = false;
        }

        return isFirst;
    }

    private boolean PrintPoly(boolean isFirst, StringBuilder stringBuilder) {
        boolean isFirstNow = isFirst;
        for (Map.Entry<String, Polynome> entry : this.polyList.entrySet()) {
            if (!entry.getValue().GetPolyPower().equals(BigInteger.ZERO)) {
                stringBuilder.append(isFirstNow ? "" : "*");
                stringBuilder.append(entry.getValue().toString());
                isFirstNow = false;
            }
        }

        return isFirstNow;
    }

    private void PrintExpFunc(boolean isFirst, StringBuilder stringBuilder) {
        if (!this.expPower.isEmpty()) {
            stringBuilder.append(isFirst ? "" : "*");
            stringBuilder.append("exp(");

            BigInteger gcd = this.expPower.GetExpressionGcd();
            BigInteger bestNum = GetBestNum(gcd);

            bestNum = bestNum.compareTo(BigInteger.ZERO) < 0 ? BigInteger.ONE : bestNum;

            String expPowerString = this.expPower.GetExpressionDivideNum(bestNum).toString();

            if (this.expPower.GetElementSize() == 1 &&
                    (!expPowerString.contains("*") || expPowerString.startsWith("exp(")) &&
                    !(expPowerString.startsWith("-exp(")) &&
                    !(expPowerString.startsWith("-x"))) {
                stringBuilder.append(expPowerString);
            } else {
                stringBuilder.append("(").append(expPowerString).append(")");
            }
            stringBuilder.append(")");
            if (!bestNum.equals(BigInteger.ONE)) {
                stringBuilder.append("^").append(bestNum);
            }
        }
    }

    private BigInteger GetBestNum(BigInteger gcd) {
        int minLength = this.expPower.toString().length();
        BigInteger num = BigInteger.ONE;
        BigInteger bestNum = gcd;

        for (; num.compareTo(BigInteger.valueOf(10)) < 0; num = num.add(BigInteger.ONE)) {
            if (!gcd.remainder(num).equals(BigInteger.ZERO)) {
                continue;
            }

            int length = num.equals(gcd) ? minLength :
                    this.expPower.GetExpressionDivideNum(gcd.divide(num)).toString().length()
                    + gcd.divide(num).toString().length() + 1;
            if (length < minLength) {
                minLength = length;
                bestNum = gcd.divide(num);
            }
        }

        return bestNum;
    }
}
