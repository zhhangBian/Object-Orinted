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

    public Unit CopyUnit() {
        TreeMap<String, Polynome> newPolyList = new TreeMap<>();
        for (Map.Entry<String, Polynome> entry : this.polyList.entrySet()) {
            newPolyList.put(entry.getKey(), entry.getValue().CopyPoly());
        }
        Expression newExpression = (Expression) this.expPower.CopyFactor();

        return new Unit(this.cofficient, newPolyList, newExpression);
    }

    public void UnitInversion() {
        this.cofficient = this.cofficient.negate();
    }

    public boolean IfSameUnit(Unit otherUnit) {
        if (!(this.polyList.size() == otherUnit.polyList.size() &&
                this.expPower.equals(otherUnit.expPower))) {
            return false;
        }

        TreeMap<String, Polynome> otherList = otherUnit.polyList;
        for (Map.Entry<String, Polynome> entry : polyList.entrySet()) {
            if (!otherList.containsKey(entry.getKey()) ||
                    !entry.getValue().IfSamePoly(otherList.get(entry.getKey()))) {
                return false;
            }
        }

        return true;
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
        this.expPower = this.expPower.ExprSimplify();

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
        Unit unit = this.CopyUnit();
        return new Unit(unit.cofficient.divide(num), unit.polyList, unit.expPower);
    }

    @Override
    public Factor CopyFactor() {
        return this.CopyUnit();
    }

    @Override
    public boolean equals(Factor otherFactor) {
        if (!otherFactor.getClass().getSimpleName().equals("Unit")) {
            return false;
        }

        Unit otherUnit = (Unit) otherFactor;
        return this.IfSameUnit(otherUnit) && this.cofficient.equals(otherUnit.cofficient);
    }

    @Override
    public String toString() {
        if (this.cofficient.equals(BigInteger.ZERO)) {
            return "0";
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (this.polyList.isEmpty() && this.expPower.toString().equals("0")) {
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
        this.expPower = this.expPower.ExprSimplify();
        if (!this.expPower.toString().equals("0")) {
            stringBuilder.append(isFirst ? "" : "*");
            stringBuilder.append("exp(");

            //BigInteger gcd = this.expPower.GetExpressionGcd();
            BigInteger bestNum = this.expPower.GetExpressionGcd();

            if (this.expPower.toString().matches("-?\\d+")) {
                bestNum = BigInteger.ONE;
            }

            String expPowerString = this.expPower.GetExpressionDivideNum(bestNum).toString();

            if (this.expPower.GetTermListSize() == 1 &&
                    !expPowerString.contains("*") &&
                    !expPowerString.contains("-")) {
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
        int minLength = this.expPower.toString().length() + gcd.toString().length();
        BigInteger num = BigInteger.ONE;
        BigInteger bestNum = gcd;

        for (; num.compareTo(gcd) <= 0; num = num.add(BigInteger.ONE)) {
            if (!gcd.remainder(num).equals(BigInteger.ZERO)) {
                continue;
            }

            int length = this.expPower.GetExpressionDivideNum(num).toString().length()
                    + num.toString().length();
            if (length < minLength) {
                minLength = length;
                bestNum = num;
            }
        }

        return bestNum;
    }
}
