import java.math.BigInteger;

public class Term {
    private Expression termExpression;

    public Term() {
        this.termExpression = new Expression(new Unit(BigInteger.ONE));
    }

    public void AddFactor(Factor factor) {
        Expression expression = (factor instanceof Unit) ?
                new Expression((Unit) factor) : (Expression) factor;
        this.termExpression = this.termExpression.MulExpression(expression);
    }

    public Expression GetTermExpression() {
        return this.termExpression;
    }
}
