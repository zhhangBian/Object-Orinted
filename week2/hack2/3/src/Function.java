public class Function implements Factor {
    private final Expr expr;

    public Function(Expr expr) {
        this.expr = expr;
    }

    @Override
    public Polynomial toPoly() {
        return expr.toPoly();
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}
