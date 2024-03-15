public interface Factor {
    boolean equals(Factor otherFactor);

    String toString();

    Factor CopyFactor();
}
