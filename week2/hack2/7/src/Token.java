public class Token {
    public enum Type {
        Plus, Minus, Mul, Left, Right, Num, Power, Exp, E
    }

    private Type type;
    private String string;

    public Token(Type type, String string) {
        this.type = type;
        this.string = string;
    }

    public String getString() {
        return this.string;
    }

    public Type getType() {
        return this.type;
    }
}
