import java.math.BigInteger;
import java.util.ArrayList;

public class Expression implements Factor {
    private ArrayList<Unit> unitList;

    public Expression() {
        this.unitList = new ArrayList<>();
    }

    public Expression(Unit unit) {
        this.unitList = new ArrayList<>();
        this.unitList.add(unit);
    }

    public int GetElementSize() {
        return this.unitList.size();
    }

    public boolean isEmpty() {
        return this.unitList.isEmpty();
    }

    @Override
    public Expression derivation(String polyName) {
        Expression finalExpression = new Expression();

        for (Unit unit : this.unitList) {
            finalExpression.AddExpression(unit.derivation(polyName));
        }

        return finalExpression;
    }

    public Expression negate() {
        this.unitList.forEach(Unit::negate);
        return this;
    }

    public void AddUnit(Unit otherUnit) {
        boolean haveAdded = false;
        for (Unit thisUnit : this.unitList) {
            if (thisUnit.IfSimilarUnit(otherUnit)) {
                thisUnit.AddUnit(otherUnit);
                haveAdded = true;

                if (thisUnit.IfIsZero()) {
                    this.unitList.remove(thisUnit);
                }
                break;
            }
        }
        if (!haveAdded && !otherUnit.IfIsZero()) {
            this.unitList.add(otherUnit);
        }
    }

    public void AddExpression(Expression otherExpression) {
        for (Unit otherUnit : otherExpression.unitList) {
            this.AddUnit(otherUnit);
        }
    }

    public Expression MulExpression(Expression otherExpression) {
        Expression finalExpression = new Expression();

        for (Unit thisUnit : this.unitList) {
            if (thisUnit.IfIsOne()) {
                finalExpression.AddExpression(otherExpression);
                continue;
            }
            for (Unit otherUnit : otherExpression.unitList) {
                Unit newUnit = (Unit) thisUnit.clone();
                newUnit.MulUnit(otherUnit);
                finalExpression.AddUnit(newUnit);
            }
        }

        return finalExpression;
    }

    public BigInteger GetExpressionGcd() {
        if (this.unitList.isEmpty()) {
            return BigInteger.ONE;
        }

        BigInteger gcd = BigInteger.ZERO;
        for (Unit unit : this.unitList) {
            gcd = unit.GetUnitGcd(gcd);
        }

        return gcd;
    }

    public Expression GetExpressionDivideNum(BigInteger num) {
        Expression expression = new Expression();
        for (Unit unit : this.unitList) {
            expression.AddUnit(unit.GetUnitDivideNum(num));
        }
        return expression;
    }

    @Override
    public Factor clone() {
        Expression expression = new Expression();
        for (Unit unit : this.unitList) {
            expression.AddUnit((Unit) unit.clone());
        }

        return expression;
    }

    @Override
    public boolean equals(Factor otherFactor) {
        if (!(otherFactor instanceof Expression)) {
            return false;
        }

        ArrayList<Unit> thisUnitList = this.unitList;
        ArrayList<Unit> otherUnitList = ((Expression) otherFactor).unitList;

        if (thisUnitList.size() != otherUnitList.size()) {
            return false;
        }

        for (Unit thisUnit : thisUnitList) {
            boolean isContain = false;
            for (Unit otherUnit : otherUnitList) {
                if (otherUnit.equals(thisUnit)) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int skipPos = GetSkipPos(stringBuilder);
        boolean havePrinted = (skipPos >= 0);
        this.PrintExpression(stringBuilder, skipPos, havePrinted);

        return stringBuilder.toString();
    }

    private int GetSkipPos(StringBuilder stringBuilder) {
        int skipPos = -1;
        if (!this.unitList.isEmpty()) {
            if (this.unitList.get(0).IsNegative()) {
                for (int i = 0; i < this.unitList.size(); i++) {
                    if (!this.unitList.get(i).IsNegative()) {
                        stringBuilder.append(this.unitList.get(i).toString());
                        skipPos = i;
                        break;
                    }
                }
            }
        }

        return skipPos;
    }

    private void PrintExpression(StringBuilder stringBuilder, int skipPos, boolean ifHavePrinted) {
        boolean havePrinted = ifHavePrinted;
        for (int i = 0; i < this.unitList.size(); i++) {
            if (i == skipPos) {
                continue;
            }

            String unitString = this.unitList.get(i).toString();
            if (havePrinted && unitString.charAt(0) != '-') {
                stringBuilder.append("+");
            }
            stringBuilder.append(unitString);
            havePrinted = true;
        }

        if (!havePrinted || this.unitList.isEmpty()) {
            stringBuilder.append("0");
        }
    }
}
