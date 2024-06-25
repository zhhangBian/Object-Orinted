public interface Factor {
    Expression derivation(String polyName);

    boolean equals(Factor otherFactor);

    String toString();

    Factor clone();
}
